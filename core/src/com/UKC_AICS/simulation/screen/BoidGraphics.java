package com.UKC_AICS.simulation.screen;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.UKC_AICS.simulation.entity.*;

public class BoidGraphics {
	
	private SpriteBatch boidBatch;
	private ObjectMap<Boid, Sprite> boidMap = new ObjectMap<Boid, Sprite>();
	private Texture defaultTexture = new Texture(Gdx.files.internal("square.png"));
	
	public BoidGraphics() {
		boidBatch = new SpriteBatch();
	}
	
	public void update(){
		Boid boid;
		boidBatch.begin();
		for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			boid = boids.next();
			updateBoidPosition(boid);
			boidMap.get(boid).draw(boidBatch);
		}
		boidBatch.end();
	}
	
	public void initBoidSprites(Array<Boid> boids){
		for(Boid boid : boids){
			if(!boidMap.containsKey(boid)){
				boidMap.put(boid, new Sprite(defaultTexture));
			}
		}
		//updateBoidPositions();
	}
	
	public void updateBoidPosition(Boid boid){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = boid.getPosition();
			boidMap.get(boid).setPosition(position.x, position.y);
		//}
	}
	

}
