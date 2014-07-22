package com.UKC_AICS.simulation.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.HashMap;

public class SpriteManager {
	
//	//Full filemap
//	private ObjectMap<String, ObjectMap<Integer, String>> filesMap = 
//			new ObjectMap<String, ObjectMap<Integer, String>>(){{
//				put("Tiles", new ObjectMap<Integer, String>(){{
//					put(0, "data/grass_tile_x16.png");
//				}});
//				put("Objects", new ObjectMap<Integer, String>(){{
//					put(0, "data/corpse_object_x16.png");
//					put(1, "data/corpse_object_x16.png");
//					put(2, "data/Attractor.png");
//					put(2, "data/Repellor.png");
//				}});
//				put("Boids", new ObjectMap<Integer, String>());
//			}};
	
	//World Tiling:
	private ObjectMap<Integer, String> tileFiles = new ObjectMap<Integer, String>(){{
		put(0, "data/grass_tile_x16.png");
	}}
	;
	
	private ObjectMap<Integer, String> boidsFiles = new ObjectMap<Integer, String>();//{{
//		put(0, "data/triangle2.png");
//		put(1, "data/triangle3.png");
//        put(2, "data/triangle3.png");
//	}};
	private String defaultBoidTextureFile = "data/triangle2.png";
	private ObjectMap<Integer, String> objectsFiles = new ObjectMap<Integer, String>(){{
				put(0, "data/corpse_object_x16.png");
				put(1, "data/corpse_object_x16.png");
				put(2, "data/Attractor.png");
				put(3, "data/Repellor.png");
			}};
	
	private ObjectMap<Integer, Sprite> boidSprites;
	private ObjectMap<Integer, Sprite> objectSprites;
	private Array<Array<Sprite>> entitySprites;
	
	private ObjectMap<Integer, Sprite> tileTextures;
	private SpriteCache tileCache = new SpriteCache();
	private int tile;
	private boolean tilesLoaded = false, tilesCreated = false;
	private byte[][] tilesMap;
	
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
		created = false;
		Sprite sprite;
		Texture spriteTexture;
		tileTextures = new ObjectMap<Integer, Sprite>(tileFiles.size);
		/*for(Integer tile: tileFiles.keys()){
			if(assetManager.isLoaded(tileFiles.get(tile))){
				spriteTexture = assetManager.get(tileFiles.get(tile), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				tileTextures.put(tile, sprite);
				tilesLoaded = true;
			}
			else
				tilesLoaded = false;
		}*/
		
		for(Integer tile : tileFiles.keys()){
			if(assetManager.isLoaded(tileFiles.get(tile))){
				spriteTexture = assetManager.get(tileFiles.get(tile), Texture.class);
				tileTextures.put(tile, new Sprite(spriteTexture));
				tilesLoaded = true;
			}
		}
			
		boidSprites = new ObjectMap<Integer, Sprite>(boidsFiles.size);
		for(Integer entity: boidsFiles.keys()){
			if(assetManager.isLoaded(boidsFiles.get(entity))){
				spriteTexture = assetManager.get(boidsFiles.get(entity), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				boidSprites.put(entity, sprite);
			}
//			else
//				created = false;
		}
		objectSprites = new ObjectMap<Integer, Sprite>(objectsFiles.size);
		for(Integer entity: objectsFiles.keys()){
			if(assetManager.isLoaded(objectsFiles.get(entity))){
				spriteTexture = assetManager.get(objectsFiles.get(entity), Texture.class);
				sprite = new Sprite(spriteTexture);
				sprite.setOrigin((spriteTexture.getWidth()/2), spriteTexture.getHeight()/2);
				objectSprites.put(entity, sprite);
			}
//			else {
//				System.out.println("Not loaded");
////				created = false;
//			}
			created = true;
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
	
	
//	/**
//	 * Initialise the tileCache
//	 * @param tiles The map of tile types in the environment
//	 */
//	public void createTileCache(byte[][] tiles) {
//		//while(!tilesLoaded){}
//		if(tilesLoaded){
//			tileCache = new SpriteCache();
//			tileCache.beginCache();
//			int x = 0;
//			Sprite sprite;
//			int y = 0;
//			int count = 0;
//			for(int i = 0; i < tiles.length; i++){
//				for(int j = 0; j < tiles[i].length; j++){
//					if(i == 0) x = 0; if(j == 0) y = 0;
//					count++;
////					System.out.println(tiles[i][j]);
////					System.out.println("Adding texture " + tileTextures.get((int)tiles[i][j]) +
////							" cache " + tileCache + " num tiles " + count);
//					sprite = tileTextures.get((int)tiles[i][j]);
//					sprite.setPosition(x, y);
//					tileCache.add(sprite);
//					y+= 16;
//				}
//				x+= 16;
//			}
////			System.out.println("Cache index " + tileCache.endCache());
//			tile = tileCache.endCache();
//			tilesCreated = true;
//		}
//		else {
//			tilesMap = tiles;
//		}
//		//created = true;
//	}
//	
//	public void drawTileCache(){
//		if(tilesCreated){
//			tileCache.begin();		
//			tileCache.draw(tile);
//			tileCache.end();
//		}
//		else
//			createTileCache(tilesMap);
//	}

	public Sprite getContinuousSprite(Byte subType){
		if(boidSprites.containsKey((int)subType)){
			return boidSprites.get((int)subType);
		}
		return null;
	}
	public Sprite getObjectSprite(Byte subtype){
		if(objectSprites.containsKey((int)subtype)){
			return objectSprites.get(subtype.intValue());
		}
		return null;
	}
	
	public Sprite getGrassTexture(){
		return tileTextures.get(0);
	}
	
	public Sprite getSprite(Byte type, Byte subType){
		if(type <= 0 || type == null){
			return getContinuousSprite(subType);
		}
		else {
			return getObjectSprite(subType);
		}
	}

	/**
	 * Set the AssetManager to loading a set of entities textures (for creation of sprites), and add
	 * the string location to an associated map for processing once loaded
	 * @param entityTextureLocation : String. Location of the texture that should be loaded
	 * @param continuousEntities : boolean. If the entity associated with the texture will be continuously updated or not...will 
	 * probably not include this
	 */
	public void loadAssets_Boids(HashMap<Byte, String> entityTextureLocation, boolean continuousEntities){
		created = false;
		String fileStr;
		for(Byte entity : entityTextureLocation.keySet()){
			fileStr = entityTextureLocation.get(entity);
			assetManager.load(fileStr, Texture.class);
			if(continuousEntities){
				boidsFiles.put(entity.intValue(), fileStr);
			}
//			else {
//				objectsFiles.put(entity.intValue(), fileStr);
//			}
			
		}
	}
	
	public void loadAssets_Objects(Array<Byte> objs){
		created = false;
		String filename = "data/corpse_object_x16.png";
//		for(Byte type : objs){
//			filename = objectsFiles.get((int)type);
//			//System.out.println(filename);
//			assetManager.load(filename, Texture.class);
//		}
		for(int type : objectsFiles.keys()){
			filename = objectsFiles.get(type);
			assetManager.load(filename, Texture.class);
		}
	}
	
	public void loadAssets_Tiles(){
		tilesLoaded = false;
		String fileLocation;
		for(Integer type : tileFiles.keys()){
			fileLocation = tileFiles.get(type);
			assetManager.load(fileLocation, Texture.class);
		}
	}
}
