package com.UKC_AICS.simulation.screen.graphics;

import com.UKC_AICS.simulation.entity.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityRenderer {
	
	private boolean spritesLoaded = false;
	
	private SpriteManager manager;
	private ObjectMap<Byte, Sprite> boidSprites = new ObjectMap<Byte, Sprite>();
//	private ObjectMap<Byte, Sprite>
	
	public EntityRenderer(SpriteManager manager){
		this.manager = manager;
	}
	
	public void update(){
//		spritesLoaded = manager.update();
		if(!manager.update()) return;
		
	}
	
	public void update(SpriteBatch batch, byte species, Vector3 position){
		if(!spritesLoaded){
			update();
			return;
		}
		
		
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
				double rot = Math.toDegrees(Math.atan2( - velocity.x, velocity.y)); //made x negative.
				sprite.setRotation((float) rot);
			}

	}
}
