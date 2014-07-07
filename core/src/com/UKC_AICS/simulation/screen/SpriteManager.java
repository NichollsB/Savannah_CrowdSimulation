package com.UKC_AICS.simulation.screen;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.xml.internal.stream.Entity;

public class SpriteManager {
	
	//Full filemap
	private ObjectMap<String, ObjectMap<Integer, String>> filesMap = 
			new ObjectMap<String, ObjectMap<Integer, String>>(){{
				put("Tiles", new ObjectMap<Integer, String>(){{
					put(0, "grass_tile_x16.png");
				}});
				put("Objects", new ObjectMap<Integer, String>(){{
					put(0, "corpse_object_x16.png");
					put(1, "corpse_object_x16.png");
					put(2, "corpse_object_x16.png");
				}});
				put("Boids", new ObjectMap<Integer, String>());
			}};
	
	//World Tiling:
	private ObjectMap<Integer, String> tileFiles = new ObjectMap<Integer, String>();
	
	private ObjectMap<Integer, String> boidsFiles = new ObjectMap<Integer, String>();//{{
//		put(0, "data/triangle2.png");
//		put(1, "data/triangle3.png");
//        put(2, "data/triangle3.png");
//	}};
	private String defaultBoidTextureFile = "data/triangle2.png";
	private ObjectMap<Integer, String> objectsFiles = new ObjectMap<Integer, String>();
	
	
	private ObjectMap<Integer, Sprite> boidSprites;
	private ObjectMap<Integer, Sprite> objectSprites;
	private Array<Array<Sprite>> entitySprites;
	
	private AssetManager assetManager = new AssetManager();
	
	private boolean created = true;
	
	public SpriteManager(){
		//loadAssets();
	}
	
	public boolean update(){
		if(assetManager.update() && !created){
			createSprites();
		}
		return created;
	}
	
	private void createSprites(){
		Sprite sprite;
		Texture spriteTexture;
		created = true;
		boidSprites = new ObjectMap<Integer, Sprite>(boidsFiles.size);
		for(Integer entity: boidsFiles.keys()){
			if(assetManager.isLoaded(boidsFiles.get(entity))){
				spriteTexture = assetManager.get(boidsFiles.get(entity), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				boidSprites.put(entity, sprite);
			}
			else
				created = false;
		}
		objectSprites = new ObjectMap<Integer, Sprite>(objectsFiles.size);
		for(Integer entity: objectsFiles.keys()){
			if(assetManager.isLoaded(objectsFiles.get(entity))){
				spriteTexture = assetManager.get(objectsFiles.get(entity), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				objectSprites.put(entity, sprite);
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
	
	public Sprite getContinuousSprite(Byte subType){
		if(subType < boidSprites.size){
			return boidSprites.get((int)subType);
		}
		return null;
	}
	public Sprite getStaticSprite(Byte subtype){
		if(subtype < objectSprites.size){
			return objectSprites.get((int)subtype);
		}
		return null;
	}
	public Sprite getSprite(Byte type, Byte subType){
		if(type <= 0 || type == null){
			return getContinuousSprite(subType);
		}
		else {
			return getStaticSprite(subType);
		}
	}

	/**
	 * Set the AssetManager to loading a set of entities textures (for creation of sprites), and add
	 * the string location to an associated map for processing once loaded
	 * @param entityTextureLocation : String. Location of the texture that should be loaded
	 * @param continuousEntities : boolean. If the entity associated with the texture will be continuously updated or not...will 
	 * probably not include this
	 */
	public void loadAssets(HashMap<Byte, String> entityTextureLocation, boolean continuousEntities){
		created = false;
		String fileStr;
		for(Byte entity : entityTextureLocation.keySet()){
			fileStr = entityTextureLocation.get(entity);
			assetManager.load(fileStr, Texture.class);
			if(continuousEntities){
				boidsFiles.put(entity.intValue(), fileStr);
			}
			else {
				objectsFiles.put(entity.intValue(), fileStr);
			}
			
		}
	}
	
	public void loadAssetsTemp(Array<Byte> objs){
		created = false;
		String filename = "corpse_object_x16.png";
		for(Byte type : objs){
			objectsFiles.put((int)type, filename);
			assetManager.load(filename, Texture.class);
		}
		//System.out.println("Loading static sprites:" + staticsFiles.size);
	}
}
