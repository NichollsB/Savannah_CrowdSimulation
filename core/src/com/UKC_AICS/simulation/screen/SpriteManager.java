package com.UKC_AICS.simulation.screen;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteManager {
	
	
	private ObjectMap<Integer, String> speciesFiles = new ObjectMap(){{
		put(0, "data/triangle2.png");
		put(1, "data/triangle3.png");
	}};
	private ObjectMap<Integer, String> objects = new ObjectMap<Integer, String>();
	
	
	private ObjectMap<Integer, Sprite> speciesSprites;
//	private ObjectMap<Integer, Sprite> objectSprites ;
	private Array<Array<Sprite>> entitySprites;
	
	private AssetManager assetManager = new AssetManager();
	
	private boolean created = true;
	
	public SpriteManager(){
		loadAssets();
	}
	
	public boolean update(){
		if(assetManager.update() && !created){
			createSprites();
		}
		return created;
	}
	
	private void createSprites(){
		
		created = true;
		speciesSprites = new ObjectMap<Integer, Sprite>(speciesFiles.size);
		for(Integer species: speciesFiles.keys()){
			if(assetManager.isLoaded(speciesFiles.get(species))){
				speciesSprites.put(species, new Sprite(
						assetManager.get(speciesFiles.get(species), Texture.class)));
			}
			else
				created = false;
		}
//		for(Integer object : objects.keys()){
//			if(assetManager.isLoaded(objects.get(object))){
//				objectSprites.put(object, new Sprite(
//						assetManager.get(objects.get(object), Texture.class)));
//			}
//			else
//				created = false;
//		}
	}
	
	public Sprite getSprite(Byte type, Byte subtype){
		if(type == 0 && subtype !=  null && subtype < speciesSprites.size){
			return speciesSprites.get((int)subtype);
		}
//		else if(type < objectSprites.size){
//			return objectSprites.get((int)type);
//		}
		return null;
	}
	public Sprite getSprite(Byte type){
		return getSprite(type, null);
	}
	
	public void loadAssets(){
		created = false;

		for(Iterator<Integer> species = speciesFiles.keys(); species.hasNext();){
			assetManager.load(speciesFiles.get(species.next()), Texture.class);
		}
		for(Iterator<Integer> obj = objects.keys(); obj.hasNext();){
			assetManager.load(objects.get(obj.next()), Texture.class);
		}
	}
}
