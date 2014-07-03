package com.UKC_AICS.simulation.screen;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.UKC_AICS.simulation.entity.*;

/**
 * 
 * Simple 2D rendering of boids within the simulation
 * @author Ben Nicholls bn65@kent.ac.uk
 *
 */
public class BoidGraphics {
	
	//private SpriteBatch boidBatch;
	private Sprite boidSprite;
	//private ObjectMap<Boid, Sprite> boidMap = new ObjectMap<Boid, Sprite>();
	private Array<Boid> boidsArray;
	private Texture defaultTexture = new Texture(Gdx.files.internal("triangle2.png"));
	//TEMPORARY
	private Texture altTexture = new Texture(Gdx.files.internal("triangle3.png"));
	private Sprite altSprite;

	/**
	 * Update and render the sprites representing the boids. Renders via the SpriteBatch passed in.
	 * @param batch is the SpriteBatch to render the boid sprites in
	 */
	public void update(SpriteBatch batch){
		
		if(boidsArray.size>0){
			batch.disableBlending();
			batch.begin();
			for(Boid boid : boidsArray){
				updateSpritePosition(boid);
				if(boid.species == 1){
					altSprite.draw(batch);
				}
				else
					boidSprite.draw(batch);
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
	public void initBoidSprites(Array<Boid> boidArray){
		boidsArray = new Array<Boid>(boidArray);
		boidSprite = new Sprite(defaultTexture);
		boidSprite.setOrigin((defaultTexture.getWidth()/2), defaultTexture.getHeight()/2);
		
		altSprite = new Sprite(altTexture);
		boidSprite.setOrigin((altTexture.getWidth()/2), altTexture.getHeight()/2);
		
		/*for(Boid boid : array){
			//boidsArray.add(boid);
			updateSpritePosition(boid);
		}*/
		/*System.out.println("boid array size: " + boidsArray.size());
		boidSprite = new Sprite(defaultTexture);
		for(Boid boid : arrayList){
			if(!boidMap.containsKey(boid)){
				boidMap.put(boid, new Sprite(defaultTexture));
				//updateSpritePosition(boid);
			}
		}*/
	}
	
	/**
	 * sets the boidSprite to the Boid perameters current position and finds the equivalent rotation of the sprite from
	 * the boid velcoity vector
	 * @param boid The Boid that the boidSprite will be postioned to
	 */
	public void updateSpritePosition(Boid boid){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = boid.getPosition();
			Vector3 velocity = boid.getVelocity();
			double rot = Math.toDegrees(Math.atan2( - velocity.x, velocity.y)); //made x negative.
			if(boid.species == 1){
				altSprite.setPosition(position.x, position.y);
				altSprite.setRotation((float) rot);
			}
			else {
				boidSprite.setPosition( position.x, position.y);
				boidSprite.setRotation((float) rot);
			}
			

			
			//boidSprite.rotate((float) rot);
//			boidMap.get(boid).setPosition(position.x, position.y);
			/*if (boid.equals(boidsArray.get(0))){
				System.out.println("Boid pos: " + boid.getPosition().toString() + " Boid Velocity: " + boid.getVelocity().toString() +
						" boid rotation: " + rot);
			}*/
		//}
	}
	

}
