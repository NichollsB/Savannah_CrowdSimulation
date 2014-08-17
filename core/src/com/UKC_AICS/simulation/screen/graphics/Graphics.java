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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
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
	
	private AtlasSprite background;
	
	
	SpriteCache backgroundCache = new SpriteCache(20000, false);
	private TileGraphics dynamicTiles;
	
	private final TileMesh grassMesh = new TileMesh();
	
	private static enum DynamicRenderOption{
		TILED, MESH;
	}
	private DynamicRenderOption renderMode = DynamicRenderOption.TILED;
	private final MeshRenderer meshRenderer = new MeshRenderer();
	
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
				if(RenderState.TILESTATE.equals(RenderState.State.MESH)){
					if(grassMesh.update(tileMap.get("grass"))){
						meshRenderer.renderAll(camera);
					}
					else{
//						grassMesh.createMesh(tileMap.get("grass"), 0, 0, 16, 16, test, false, 0, 0);
//						grassMesh.createMesh(tileMap.get("grass"), 0, 0, 16, 16, Color.WHITE);
						meshRenderer.addMesh(grassMesh);
					}
//					if(grassMesh.update(tileMap.get("grass"))){
//						meshRenderer.renderAll(camera);
//					}
//					else{
//						region = spriteManager.getTileRegion("grass", 100);
//						System.out.println();
//						if(region != null){
//							grassMesh.createMesh(tileMap.get("grass"), 0, 0, 16, 16, region.originalWidth, region.originalHeight, false, 0, 0);
//							meshRenderer.addMesh(grassMesh);
//						}
//						
//					}
					
				}
				batch.begin();
//				ScissorStack.pushScissors(scissor);
//				spriteManager.drawTileCache();
//				back.begin(ShapeType.Filled);
//		    	back.setColor(0.8f,0.7f,0.5f,1f);
//		    	back.rect(0, 0, renderWidth, renderHeight);
//		    	back.end();
//				batch.begin();
				
				try{
					background.draw(batch);
				}
				catch(NullPointerException e){
					System.out.println("Missing GROUND environment layer");
				}
				batch.end();
				if(RenderState.TILESTATE.equals(RenderState.State.TILED) && dynamicTiles != null){
					Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
					dynamicTiles.updateTiles(batch, true, tileMap);
				}
				
		    	batch.begin();
				byte b = 0;
				if(entityArray.size>0){
					for(Entity entity : entityArray){
						sprite = spriteManager.getObjectSprite(entity.getType());
                        if(sprite!=null){
                            updateSpritePosition(entity, sprite);
                            sprite.draw(batch);
                        }

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
				ScissorStack.popScissors();
			}
			
		}
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
	 * @param boidArray the Array of boids to store
	 */
	public void initBoidSprites(HashMap<Byte, String> fileLocations){

		spriteManager.loadAssets_Boids(fileLocations, true);


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
		background = EnvironmentLoader.getLayer_sprite(EnvironmentLayer.GROUND);
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
