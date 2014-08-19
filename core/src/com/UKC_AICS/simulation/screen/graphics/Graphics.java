package com.UKC_AICS.simulation.screen.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.gui.controlutils.RenderState;
import com.UKC_AICS.simulation.utils.EnvironmentLoader;
import com.UKC_AICS.simulation.utils.EnvironmentLoader.EnvironmentLayer;

/**
 * 
 * Simple 2D rendering of boids within the simulation
 * @author Ben Nicholls bn65@kent.ac.uk
 *
 */
public class Graphics {
	//generic sprite for all entities
	private Sprite sprite;
	
	//private SpriteBatch boidBatch;
	private Sprite boidSprite;
	private Sprite selectedBoidSprite;
	//private ObjectMap<Boid, Sprite> boidMap = new ObjectMap<Boid, Sprite>();
	private Array<Boid> boidsArray;
	private Array<Entity> entityArray;
	private Texture defaultBoidTexture;
	//TEMPORARY
//	private Texture altTexture = new Texture(Gdx.files.internal("triangle3.png"));
	private Sprite altSprite;
	
	private SpriteManager spriteManager = new SpriteManager();
	
	private HashMap<String, byte[][]> tileMap;
	
	private int renderWidth;
	private int renderHeight;
	
	private OrthographicCamera camera = new OrthographicCamera();
	

	private Texture simBase;
	
	ShapeRenderer back = new ShapeRenderer();
	private HashMap<Byte, float[]> boidColours = new HashMap<Byte, float[]>();
	
	private AtlasRegion background;
	
	
	SpriteCache backgroundCache = new SpriteCache(20000, false);
	private TileGraphics dynamicTiles;
	
	private final TileMesh grassMesh = new TileMesh();
	
	private static enum DynamicRenderOption{
		TILED, MESH;
	}
	private DynamicRenderOption renderMode = DynamicRenderOption.TILED;
	private final MeshRenderer meshRenderer = new MeshRenderer();

    public static final String VERT_SHADER =
            "attribute vec2 a_position;\n" +
                    "attribute vec4 a_color;\n" +
                    "attribute vec2 a_texCoords;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoords;\n" +
                    "void main() {\n" +
                    " vColor = a_color;\n" +
                    " vTexCoords = a_texCoords;\n" +
                    " gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" +
                    "}";
    public static final String FRAG_SHADER =
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "void main() {\n" +
                    "// gl_FragColor = vColor;\n" +
                    "gl_FragColor = vColor * texture2D(u_texture, vTexCoords);\n" +
                    "}";
    private static final ShaderProgram shader = createMeshShader();
    private final Array<TileMesh> meshes = new Array<TileMesh>();
    protected static ShaderProgram createMeshShader() {
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
        String log = shader.getLog();
        if (!shader.isCompiled())
            throw new GdxRuntimeException(log);
        if (log!=null && log.length()!=0)
            System.out.println("Shader Log: "+log);
        return shader;
    }

	public Graphics(int width, int height){
		renderWidth = width;
		renderHeight = height;
	}

	
//	private Texture test = new Texture(Gdx.files.internal("/data/grass_tile_x16.png"));

	/**
	 * Update and render the sprites representing the boids. Renders via the SpriteBatch passed in.
	 * @param batch is the SpriteBatch to render the boid sprites in
	 * @param viewRect 
	 */
	public void update(SpriteBatch batch, Rectangle viewRect){
			if(spriteManager.update()){
				AtlasRegion region;
				ScissorStack.pushScissors(viewRect);
                batch.begin();
//				ScissorStack.pushScissors(scissor);
//				spriteManager.drawTileCache();
//				back.begin(ShapeType.Filled);
//		    	back.setColor(0.8f,0.7f,0.5f,1f);
//		    	back.rect(0, 0, renderWidth, renderHeight);
//		    	back.end();
//				batch.begin();
                
                if(background == null){
                	background = spriteManager.getTileRegion("ground");
                }
                try{
                    int iNum = renderWidth/background.originalWidth;
                    int jNum = renderHeight/background.originalHeight;
                	for(int i = 0; i < iNum; i ++){
                		for(int j = 0; j < jNum; j ++){
//                			batch.draw(background, i, j);
                			batch.draw(background, i*background.originalWidth, j*background.originalHeight,
                                    background.originalWidth+1, background.originalHeight+1);
                		}
                	}
//                    background.draw(batch);
                }
                catch(NullPointerException e){
                    System.out.println("Missing GROUND environment layer");
                }
                batch.end();
				if(RenderState.TILESTATE.equals(RenderState.State.MESH)){
//
					if(grassMesh.update(tileMap.get("grass"))){
						meshRenderer.renderAll(camera);
					}
					else{
						region = spriteManager.getTileRegion("grass", 100);
						System.out.println();
						if(region != null){
							grassMesh.createMesh(tileMap.get("grass"), 0, 0, 16, 16, region.getTexture(), false, 0, 0);
							meshRenderer.addMesh(grassMesh);
						}

					}
					
				}

				if(RenderState.TILESTATE.equals(RenderState.State.TILED) && dynamicTiles != null){
					Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
					dynamicTiles.updateTiles(batch, true, tileMap);
				}

		    	batch.begin();
				byte b = 0;
				if(entityArray.size>0){
					for(Entity entity : entityArray){
                        if(entity.tracked){
                            sprite = spriteManager.getObject_highlight(entity.getType());
                            updateSpritePosition(entity, sprite);
                            sprite.draw(batch);
                        }
                        if(entity.getType()==0){
                            sprite = spriteManager.getCorpse_Sprite(entity.getSubType());
                        }
                        else
						    sprite = spriteManager.getObjectSprite(entity.getType());

                        if(sprite!=null){
//                            System
                            updateSpritePosition(entity, sprite);
                            sprite.draw(batch);
                        }
//                        else {
//                            System.out.println("Null sprite " + entity.getType());
//                        }
					}

				}
				if(boidsArray.size>0){
					Byte boidSelection = null;
					for(Boid boid : boidsArray){

						if(boid.tracked){
							altSprite = spriteManager.getBoid_HighlightSprite();
							updateSpritePosition(boid, altSprite);
							altSprite.draw(batch);
						}

						//SETTING COLOUR METHOD
						sprite = spriteManager.getBoid_DefaultSprite();
						if(boidColours.containsKey(boid.getSpecies())){
							float colour[] = boidColours.get(boid.getSpecies()).clone();

                            colour[0] = (colour[0] > 0f) ? (colour[0] + ((float) boid.tertiaryType * 0.03f)) : colour[0];
                            colour[1] = (colour[1] > 0f) ? (colour[1] + ((float) boid.tertiaryType * 0.03f)) : colour[1];
                            colour[2] = (colour[2] > 0f) ? (colour[2] + ((float) boid.tertiaryType * 0.03f)) : colour[2];

							sprite.setColor(colour[0], colour[1], colour[2], 1f);
						}
						else{
							sprite.setColor(Color.WHITE);
						}

						//USING PRE-DEFINED SPRITES METHOD
//						sprite = spriteManager.getBoid_Sprite(boid.getSpecies());
						updateSpritePosition(boid, sprite);

						sprite.draw(batch);

					}
				}
				batch.end();
				//DEBUGGING BOID POSITIION
				camera.update();
			 	r.setProjectionMatrix(camera.combined);
			 	r.setColor(Color.RED);
				r.begin(ShapeType.Filled);
				for(Boid boid : boidsArray){
					r.circle(boid.position.x, boid.position.y, 2);
//					r.rect(boid.position.x, boid.position.y, 16, 16);
				}
				r.end();
				ScissorStack.popScissors();
			}
			
		}
	ShapeRenderer r = new ShapeRenderer();
		/*if(boidMap.size>0){
			 Boid boid;
			 for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
				boid = boids.next();
				updateSpritePosition(boid);
				boidSprite.draw(batch);
				//boidMap.get(boid).draw(batch);
			}
		}*/
		
	
	
	/**
	 * Pass in and store the boids and initialise the boidSprite to a sprite with the default texture
	 */
	public void initBoidSprites(HashMap<Byte, String> fileLocations){

		spriteManager.loadAssets_Boids(fileLocations, true);
        spriteManager.loadAssets_Entities();

	}
	public void setBoidSprite_Colours(HashMap<Byte, float[]> rgbValues) {
		boidColours = rgbValues;
		spriteManager.loadAssets_Boids(rgbValues);
	}
	
	public void setBoids(Array<Boid> boidArray){
		this.boidsArray = boidArray;
	}
	
	public void initObjSprites (Array<Entity> entityArray){
		this.entityArray = entityArray;
		Array<Byte> types = new Array<Byte>();
		
		for(Entity entity : entityArray){
			types.add(entity.getType());
		}
		spriteManager.loadAssets_Objects(types);
	}
	
	public void initGrassMesh(byte[][] bs, int tileSize){
//        grassMesh.createMesh(tileMap.get("grass"), 0, 0, 16, 16, Color.WHITE);
//        meshRenderer.addMesh(grassMesh);
		AtlasRegion region = spriteManager.getTileRegion("grass", 100);
		if(region != null){
			grassMesh.createMesh(bs, 0, 0, tileSize, tileSize, region.originalWidth, region.originalHeight, false, 0, 0);
			meshRenderer.addMesh(grassMesh);
		}
	}
	public void initTileSprites(HashMap<String, byte[][]> tileLayers){
		this.tileMap = tileLayers;
		spriteManager.loadAssets_Tiles(null);
		dynamicTiles = new TileGraphics(tileLayers, spriteManager, backgroundCache);
	}
	public void initBackground(){
//		background = EnvironmentLoader.getLayer_sprite(EnvironmentLayer.GROUND);
//		spriteManager.lo
	}
	
	/**
	 * sets the boidSprite to the Boid perameters current position and finds the equivalent rotation of the sprite from
	 * the boid velcoity vector
	 * @param entity The Boid that the boidSprite will be postioned to
	 */
	public void updateSpritePosition(Entity entity, Sprite sprite){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = entity.getPosition();
			Vector3 velocity = entity.getVelocity();
			
			sprite.setPosition(entity.getPosition().x - sprite.getWidth()/2, 
					entity.getPosition().y - sprite.getHeight()/2);
			if(velocity != null){
				double rot = entity.getOrientation();
				sprite.setRotation((float) rot);
			}

	}
	
	public Camera getCamera(){
		return camera;
	}


	
	

}
