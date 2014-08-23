package com.UKC_AICS.simulation.screen.graphics;

import java.util.Arrays;
import java.util.HashMap;

import com.UKC_AICS.simulation.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.screen.controlutils.RenderState;

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

	public SpriteManager spriteManager = new SpriteManager();

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

	private TileMesh grassMesh = new TileMesh(0f, 1f, 0f, 1f, 128);
    private TileMesh waterMesh = new TileMesh(0.3f, 0.8f, 0f, 0.8f, 128);
    private TileMesh groundMesh = new TileMesh(0f, 1f, 0f, 1f, 128);
    private TileMesh meshes[] = new TileMesh[]{groundMesh, grassMesh, waterMesh};
	private static enum DynamicRenderOption{
		TILED, MESH;
	}
	private DynamicRenderOption renderMode = DynamicRenderOption.TILED;
	private final MeshRenderer meshRenderer = new MeshRenderer();


    TileMesh gridMesh;
    OrthographicCamera cam;
    ShaderProgram shader;
    Rectangle glViewport;
    FileHandle imageFileHandle = Gdx.files.internal("data/EnvironmentTiles/grassSeamless.jpg");
    Texture texture = new Texture(imageFileHandle){{
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }};
    //This is how we control the size of the input grid (and the meshgrid)


	public Graphics(int width, int height){
		renderWidth = width;
		renderHeight = height;

//        }
	}


//	private Texture test = new Texture(Gdx.files.internal("/data/grass_tile_x16.png"));
    boolean created = false;
	/**
	 * Update and render the entity sprites and background graphics, depending on the
     * {@link com.UKC_AICS.simulation.screen.controlutils.RenderState RenderState}. If in TILESTATE,
	 * @param batch is the SpriteBatch to render the boid sprites in
	 * @param viewRect
	 */
	public boolean update(SpriteBatch batch, Rectangle viewRect){
			if(spriteManager.update() && !RenderState.TILESTATE.equals(RenderState.State.OFF)){
                Gdx.gl.glDepthMask(false);
                //enable blending, for alpha
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                if(tileMap == null || tileMap.size()<1 || !created){
//                    System.out.println("Trying to create mesh");
                    if(!created){

                        if(texture!=null) {
                            grassMesh.createMesh(tileMap.get("grass"), 0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE,
                                    texture, true, 10, 10, new Color(0.55f, 0.7f, 0.41f, 1f));
                            waterMesh.createMesh(tileMap.get("water"), 0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE, Color.TEAL);
                            byte g[][] = new byte[tileMap.get("water").length][tileMap.get("water")[0].length];
                            for(byte b[] : g){
                                Arrays.fill(b, (byte)100);
                            }
//                            groundMesh.createMesh(tileMap.get("water"), 0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE, new Color(0.5f, 0.4f, 0.36f, 1f));
                            groundMesh.createMesh(g, 0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE, new Color(0.28f, 0.18f, 0.08f,1f));
                            created = true;
                        }
                    }
                }
				AtlasRegion region;
                AtlasSprite aSprite;
				ScissorStack.pushScissors(viewRect);

                if(RenderState.TILESTATE.equals(RenderState.State.MESH)){
                    if(created){
                        //update the mesh grid (setVertices again with the new verts)
                        grassMesh.update(tileMap.get("grass"));
//                        waterMesh.update(tileMap.get("water"));
//                        meshRenderer.renderMesh(groundMesh, camera);
                        meshRenderer.renderMeshes(meshes, camera);
                    }

                }

                if(!RenderState.TILESTATE.equals(RenderState.State.MESH)) {
                    batch.begin();
                    Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
                    if (background == null) {
                        background = spriteManager.getTileRegion("ground");
                    }
                    try {
                        int iNum = renderWidth / background.originalWidth;
                        int jNum = renderHeight / background.originalHeight;

                        for (int i = 0; i < iNum; i++) {
                            for (int j = 0; j < jNum; j++) {
//                			batch.draw(background, i, j);
                                batch.draw(background, i * background.originalWidth, j * background.originalHeight,
                                        background.originalWidth + 1, background.originalHeight + 1);
                            }
                        }
//                    background.draw(batch);
                    } catch (NullPointerException e) {
                        System.out.println("Missing GROUND environment layer");
                    }
                    batch.end();
                }

				if(RenderState.TILESTATE.equals(RenderState.State.TILED) && dynamicTiles != null){
                    Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
					dynamicTiles.updateTiles(batch, true, tileMap);
				}

		    	batch.begin();
                Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
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
				float optimumSize = 0.5f;
				float size = 0f;
				if(boidsArray.size>0){
					Byte boidSelection = null;
					for(Boid boid : boidsArray){

                        size = boid.getSize()/30;
                        if(size>0.5)
                            size = (0.5f + (size/5f));
                        else if(size<0.5)
                            size = (0.25f + (size/5f));
						if(boid.tracked){
							altSprite = spriteManager.getBoid_HighlightSprite();
							updateSpritePosition(boid, altSprite);
                            altSprite.setScale(size);
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

							sprite.setScale(size);
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
//				//DEBUGGING BOID POSITIION
//				camera.update();
//			 	r.setProjectionMatrix(camera.combined);
//			 	r.setColor(Color.RED);
//				r.begin(ShapeType.Filled);
//				for(Boid boid : boidsArray){
//					r.circle(boid.position.x, boid.position.y, 2);
////					r.rect(boid.position.x, boid.position.y, 16, 16);
//				}
//				r.end();
//                //////////////////////////////////

				ScissorStack.popScissors();
                return true;
			}
             Gdx.gl.glFinish();
            return false;



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
	
	public void initEnvironmentMeshes(byte[][] bs, int tileSize){
        grassMesh = new TileMesh(0f, 1f, 0f, 1f, 128);
        waterMesh = new TileMesh(0.3f, 0.8f, 0f, 0.8f, 128);
        groundMesh = new TileMesh(0f, 1f, 0f, 1f, 128);
        meshes = new TileMesh[]{groundMesh, grassMesh, waterMesh};
        created = false;

	}
	public void initEnvironmentTiling(HashMap<String, byte[][]> tileLayers){
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
