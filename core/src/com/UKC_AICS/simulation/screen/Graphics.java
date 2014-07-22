package com.UKC_AICS.simulation.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;

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
	
	private byte[][] tileMap;
	
	private int renderWidth;
	private int renderHeight;
	
	private OrthographicCamera camera = new OrthographicCamera();
	
	private TileGraphics dynamicTiles;
	private Texture simBase;
	
	ShapeRenderer back = new ShapeRenderer();
	private HashMap<Byte, float[]> boidColours = new HashMap<Byte, float[]>();
	
	public Graphics(int width, int height){
		renderWidth = width;
		renderHeight = height;
	}
	

	/**
	 * Update and render the sprites representing the boids. Renders via the SpriteBatch passed in.
	 * @param batch is the SpriteBatch to render the boid sprites in
	 */
	public void update(SpriteBatch batch, Rectangle scissor){
			
			
			if(spriteManager.update()){
//				ScissorStack.pushScissors(scissor);
//				spriteManager.drawTileCache();
//				back.begin(ShapeType.Filled);
//		    	back.setColor(0.8f,0.7f,0.5f,1f);
//		    	back.rect(0, 0, renderWidth, renderHeight);
//		    	back.end();
//				batch.begin();
				if(dynamicTiles != null)
			        
			    	
					dynamicTiles.updateTiles(batch);
				
				//drawGrass
				//int x=0, y=0;
	//			Texture tex;
	//			if(tileMap.length > 0){
	//				for(int y = 0; y < renderHeight; y+=16){
	//					for(int x = 0; x < renderWidth; x+=16){
	//						//tex = spriteManager.getGrassTexture();
	//						sprite = spriteManager.getGrassTexture();
	//						sprite.setPosition(x, y);
	//						sprite.draw(batch);
	////						batch.draw(sprite, x, y);
	//						//x+=16;
	//						//System.out.println("x " + x + " y " + y);
	//					}
	//					//y+=16;
	//				}
	//			}
				
				byte b = 0;
				if(entityArray.size>0){
					for(Entity entity : entityArray){
						sprite = spriteManager.getObjectSprite(entity.getType());
						Vector3 pos = entity.getPosition();
						sprite.setPosition(pos.x, pos.y);
						sprite.draw(batch);
					}
					
				}
				if(boidsArray.size>0){
					for(Boid boid : boidsArray){
						sprite = spriteManager.getSprite(b, boid.getSpecies());
						if(sprite == null){
//							sprite = (!boid.tracked ? spriteManager.getDefaults()[0] : spriteManager.getDefaults()[1]);
							if(boid.tracked){
								sprite = spriteManager.getDefaults()[1];
								updateSpritePosition(boid, sprite);
								sprite.draw(batch);
							}
							sprite = spriteManager.getDefaults()[0];
							if(boidColours.containsKey(boid.getSpecies())){
								float colour[] = boidColours.get(boid.getSpecies());
								sprite.setColor(colour[0], colour[1], colour[2], 1f);
								
							}
							else{
								sprite.setColor(Color.WHITE);
							}
						}
						updateSpritePosition(boid, sprite);

						sprite.draw(batch);
						
//						sprite.draw(batch);
						/*if(boid.species == 1){
							altSprite.draw(batch);
						}
						else
							boidSprite.draw(batch);*/
					}
				}
//				batch.end();
//				ScissorStack.popScissors();
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
//		boidSprite = new Sprite(defaultBoidTexture);
//		boidSprite.setOrigin((defaultBoidTexture.getWidth()/2), defaultBoidTexture.getHeight()/2);
//		
//		altSprite = new Sprite(altTexture);
//		boidSprite.setOrigin((altTexture.getWidth()/2), altTexture.getHeight()/2);

	}
	public void setBoidSprite_Colours(HashMap<Byte, float[]> rgbValues) {
		boidColours = rgbValues;
	}
	
	public void setBoids(Array<Boid> boidArray){
		this.boidsArray = boidArray;
	}
	
	public void initObjSprites (Array<Entity> entityArray){
		this.entityArray = new Array<Entity>(entityArray);
		Array<Byte> types = new Array<Byte>();
		
		for(Entity entity : entityArray){
			types.add(entity.getType());
		}
//		System.out.println(types.size);
//		types.add((byte)1);
//		types.add((byte)2);
		spriteManager.loadAssets_Objects(types);
	}
	
	public void initTileSprites(HashMap<String, byte[][]> tileLayers){
		/*spriteManager.loadAssets_Tiles();
		tileMap = map;
		spriteManager.createTileCache(map);*/
		
//		tileMap = map;
////		System.out.println(map.length);
//		spriteManager.loadAssets_Tiles();
		dynamicTiles = new TileGraphics(tileLayers, spriteManager);
		
	}
	
	/**
	 * sets the boidSprite to the Boid perameters current position and finds the equivalent rotation of the sprite from
	 * the boid velcoity vector
	 * @param boid The Boid that the boidSprite will be postioned to
	 */
	public void updateSpritePosition(Boid boid, Sprite sprite){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = boid.getPosition();
			Vector3 velocity = boid.getVelocity();
			double rot = Math.toDegrees(Math.atan2( - velocity.x, velocity.y)); //made x negative.
			sprite.setPosition(boid.getPosition().x - sprite.getWidth()/2, 
					boid.getPosition().y - sprite.getHeight()/2);
			sprite.setRotation((float) rot);

	}
	
	public Camera getCamera(){
		return camera;
	}


	
	

}
