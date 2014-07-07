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
	
	
	private ObjectMap<Integer, String> dynamicsFiles = new ObjectMap<Integer, String>();//{{
//		put(0, "data/triangle2.png");
//		put(1, "data/triangle3.png");
//        put(2, "data/triangle3.png");
//	}};
	private String defaultTextureFile = "data/triangle2.png";
	private ObjectMap<Integer, String> staticsFiles = new ObjectMap<Integer, String>();
	
	
	private ObjectMap<Integer, Sprite> continuousEntitySprites;
	private ObjectMap<Integer, Sprite> staticEntitySprites;
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
		continuousEntitySprites = new ObjectMap<Integer, Sprite>(dynamicsFiles.size);
		for(Integer entity: dynamicsFiles.keys()){
			if(assetManager.isLoaded(dynamicsFiles.get(entity))){
				spriteTexture = assetManager.get(dynamicsFiles.get(entity), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				continuousEntitySprites.put(entity, sprite);
			}
			else
				created = false;
		}
		staticEntitySprites = new ObjectMap<Integer, Sprite>(staticsFiles.size);
		for(Integer entity: staticsFiles.keys()){
			if(assetManager.isLoaded(staticsFiles.get(entity))){
				spriteTexture = assetManager.get(staticsFiles.get(entity), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				staticEntitySprites.put(entity, sprite);
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
		if(subType < continuousEntitySprites.size){
			return continuousEntitySprites.get((int)subType);
		}
		return null;
	}
	public Sprite getStaticSprite(Byte subtype){
		if(subtype < staticEntitySprites.size){
			return staticEntitySprites.get((int)subtype);
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
				dynamicsFiles.put(entity.intValue(), fileStr);
			}
			else {
				staticsFiles.put(entity.intValue(), fileStr);
			}
			
		}
	}
	
	public void loadAssetsTemp(Array<Byte> objs){
		created = false;
		String filename = "corpse_object_x16.png";
		for(Byte type : objs){
			staticsFiles.put((int)type, filename);
			assetManager.load(filename, Texture.class);
		}
		//System.out.println("Loading static sprites:" + staticsFiles.size);
	}
}
