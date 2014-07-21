package com.UKC_AICS.simulation.screen;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

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
	//private ObjectMap<Boid, Sprite> boidMap = new ObjectMap<Boid, Sprite>();
	private Array<Boid> boidsArray;
	private Array<Entity> entityArray;
	private Texture defaultTexture = new Texture(Gdx.files.internal("data/triangle2.png"));
	//TEMPORARY
	private Texture altTexture = new Texture(Gdx.files.internal("data/triangle3.png"));
	private Sprite altSprite;
	
	private SpriteManager spriteManager = new SpriteManager();
	
	private byte[][] tileMap;
	
	private int renderWidth;
	private int renderHeight;
	
	private OrthographicCamera camera = new OrthographicCamera();
	
	public Graphics(int width, int height){
		renderWidth = width;
		renderHeight = height;
		
	}
	

	/**
	 * Update and render the sprites representing the boids. Renders via the SpriteBatch passed in.
	 * @param batch is the SpriteBatch to render the boid sprites in
	 */
	public void update(SpriteBatch batch){
		if(spriteManager.update()){
			
			
			//spriteManager.drawTileCache();
			batch.enableBlending();
			batch.begin();
			//drawGrass
			//int x=0, y=0;
//			Texture tex;
			if(tileMap.length > 0){
				for(int y = 0; y < renderHeight; y+=16){
					for(int x = 0; x < renderWidth; x+=16){
						//tex = spriteManager.getGrassTexture();
						sprite = spriteManager.getGrassTexture();
						sprite.setPosition(x, y);
						sprite.draw(batch);
//						batch.draw(sprite, x, y);
						//x+=16;
						//System.out.println("x " + x + " y " + y);
					}
					//y+=16;
				}
			}
			
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
					updateSpritePosition(boid, sprite);
					sprite.draw(batch);
					/*if(boid.species == 1){
						altSprite.draw(batch);
					}
					else
						boidSprite.draw(batch);*/
				}
			}
			batch.end();
			batch.enableBlending();
			
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
		
	}
	
	/**
	 * Pass in and store the boids and initialise the boidSprite to a sprite with the default texture
	 * @param boidArray the Array of boids to store
	 */
	public void initBoidSprites(Array<Boid> boidArray, HashMap<Byte, String> fileLocations){

		spriteManager.loadAssets_Boids(fileLocations, true);

		boidsArray = boidArray;
		boidSprite = new Sprite(defaultTexture);
		boidSprite.setOrigin((defaultTexture.getWidth()/2), defaultTexture.getHeight()/2);
		
		altSprite = new Sprite(altTexture);
		boidSprite.setOrigin((altTexture.getWidth()/2), altTexture.getHeight()/2);

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
	
	public void initTileSprites(byte[][] map){
		/*spriteManager.loadAssets_Tiles();
		tileMap = map;
		spriteManager.createTileCache(map);*/
		
		tileMap = map;
//		System.out.println(map.length);
		spriteManager.loadAssets_Tiles();
	}
	
	/**
	 * sets the boidSprite to the Boid perameters current position and finds the equivalent rotation of the sprite from
	 * the boid velcoity vector
	 * @param boid The Boid that the boidSprite will be postioned to
	 */
	public void updateSpritePosition(Boid boid, Sprite sprite){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = boid.getPosition();
			//Vector3 velocity = boid.getVelocity();
			double rot = boid.getOrientation();
			sprite.setPosition(position.x, position.y);
			sprite.setRotation((float) rot);

	}
	
	public Camera getCamera(){
		return camera;
	}
	

}
