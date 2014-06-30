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
 * @author Ben Nicholls
 *
 */
public class BoidGraphics {
	
	private SpriteBatch boidBatch;
	private ObjectMap<Boid, Sprite> boidMap = new ObjectMap<Boid, Sprite>();
	private Texture defaultTexture = new Texture(Gdx.files.internal("square.png"));
	

	
	public void update(SpriteBatch batch){
		Boid boid;
		if(boidMap.size>0){
			batch.begin();
			for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
				boid = boids.next();
				updateSpritePosition(boid);
				boidMap.get(boid).draw(batch);
			}
			batch.end();
		}
	}
	
	public void initBoidSprites(ArrayList<Boid> arrayList){
		for(Boid boid : arrayList){
			if(!boidMap.containsKey(boid)){
				boidMap.put(boid, new Sprite(defaultTexture));
				updateSpritePosition(boid);
			}
		}
	}
	
	public void updateSpritePosition(Boid boid){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = boid.getPosition();
			boidMap.get(boid).setPosition(position.x, position.y);
		//}
	}
	

}
