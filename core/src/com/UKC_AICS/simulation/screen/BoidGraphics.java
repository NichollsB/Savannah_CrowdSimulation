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

public class BoidGraphics {
	
	private SpriteBatch boidBatch;
	private Sprite boidSprite;
	private ObjectMap<Boid, Sprite> boidMap = new ObjectMap<Boid, Sprite>();
	private ArrayList<Boid> boidsArray;
	private Texture defaultTexture = new Texture(Gdx.files.internal("square.png"));
	

	
	public void update(SpriteBatch batch){
		
		if(boidsArray.size()>0){
			batch.begin();
			for(Boid boid : boidsArray){
				updateSpritePosition(boid);
				boidSprite.draw(batch);
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
		batch.end();
	}
	
	public void initBoidSprites(ArrayList<Boid> arrayList){
		boidsArray = new ArrayList<Boid>(arrayList);
		boidSprite = new Sprite(defaultTexture);
		for(Boid boid : arrayList){
			//boidsArray.add(boid);
			updateSpritePosition(boid);
		}
		/*System.out.println("boid array size: " + boidsArray.size());
		boidSprite = new Sprite(defaultTexture);
		for(Boid boid : arrayList){
			if(!boidMap.containsKey(boid)){
				boidMap.put(boid, new Sprite(defaultTexture));
				//updateSpritePosition(boid);
			}
		}*/
	}
	
	public void updateSpritePosition(Boid boid){
		//for(Iterator<Boid> boids = boidMap.keys(); boids.hasNext();){
			Vector3 position = boid.getPosition();
			boidSprite.setPosition(position.x, position.y);
			//boidMap.get(boid).setPosition(position.x, position.y);
		//}
	}
	

}
