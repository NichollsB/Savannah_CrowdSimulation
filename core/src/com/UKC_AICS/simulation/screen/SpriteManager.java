package com.UKC_AICS.simulation.screen;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteManager {
	
	private static Sprite defaultBoid;
	private final static String defaultBoid_path = "data/newTriangle.png";
	private static Sprite defaultBoid_selected;
	private final static String defaultBoid_selected_path = "data/newTriangle_highlight.png";
	
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
	private ObjectMap<String, String> tileFiles = new ObjectMap<String, String>(){{
		put("grass", "data/grass_tile_x16.png");
		put("terrain", "data/water_tile_x16.png");
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
	
	private ObjectMap<String, Sprite> tileTextures;
	private SpriteCache tileCache = new SpriteCache();
	private int tile;
	private boolean tilesLoaded = false, tilesCreated = false;
	private byte[][] tilesMap;
	
	private AssetManager assetManager = new AssetManager();
	
	private boolean created = true;
	
	public SpriteManager(){
		//loadAssets();
		assetManager.load(defaultBoid_path, Texture.class);
		assetManager.load(defaultBoid_selected_path, Texture.class);
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
		tileTextures = new ObjectMap<String, Sprite>(tileFiles.size);
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
		
		for(String tile : tileFiles.keys()){
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
			
		}
//		for(Integer object : objects.keys()){
//			if(assetManager.isLoaded(objects.get(object))){
//				objectSprites.put(object, new Sprite(
//						assetManager.get(objects.get(object), Texture.class)));
//			}
//			else
//				created = false;
//		}
		
		//Create defaults
		if(assetManager.isLoaded(defaultBoid_path)){
			spriteTexture = assetManager.get(defaultBoid_path, Texture.class);
			defaultBoid = new Sprite(spriteTexture);
			defaultBoid.setOrigin(spriteTexture.getWidth()/2, spriteTexture.getHeight()/2);
			defaultBoid.setSize(spriteTexture.getWidth(), spriteTexture.getHeight());
//			defaultBoid.setCenter(spriteTexture.getWidth()/2, spriteTexture.getHeight()/2);
//			defaultBoid.setOriginCenter();
		}
		if(assetManager.isLoaded(defaultBoid_selected_path)){
			spriteTexture = assetManager.get(defaultBoid_selected_path, Texture.class);
			defaultBoid_selected = new Sprite(spriteTexture);
			defaultBoid_selected.setSize(spriteTexture.getWidth(), spriteTexture.getHeight());
////			defaultBoid.setOrigin(spriteTexture.getWidth()/2, spriteTexture.getHeight()/2);
//			
//			defaultBoid.setCenter(spriteTexture.getWidth()/2, spriteTexture.getHeight()/2);
//			defaultBoid_selected.setOriginCenter();
		}
		
		created = true;
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
	
	public Sprite getGrassTexture(String name){
		return tileTextures.get(name);
	}
	
	public Sprite getSprite(Byte type, Byte subType){
		if(type <= 0 || type == null){
			return getContinuousSprite(subType);
		}
		else {
			return getObjectSprite(subType);
		}
	}

	private boolean loadAsset(String fileLocation){
		try {
			assetManager.load(fileLocation, Texture.class);
			return true;
		} 
		catch(NullPointerException e) {
			System.out.println("Texture file, or file location " + fileLocation + " is missing");
			return false;
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
			if(loadAsset(fileStr)){
				if(continuousEntities){
					boidsFiles.put(entity.intValue(), fileStr);
				}
	//			else {
	//				objectsFiles.put(entity.intValue(), fileStr);
	//			}
			}
			
		}
	}
	
	public void loadAssets_Objects(Array<Byte> objs){
		created = false;
		String filename = "data/corpse_object_x16.png";
		for(int type : objectsFiles.keys()){
			filename = objectsFiles.get(type);
			//System.out.println(filename);
			loadAsset(filename);

		}
	}
	
	public void loadAssets_Tiles(){
		tilesLoaded = false;
		String fileLocation;
		for(String tile : tileFiles.keys()){
			fileLocation = tileFiles.get(tile);
			loadAsset(fileLocation);
		}
	}

	public boolean loadAssets_Tile(String tile) {
		//Need to change this, as with others to retrieving region of texture atlas, but this will do for now
		if(loadAsset(tileFiles.get(tile))){
			return true;
		}
		return false;
	}

	public Sprite getTileSprite(String layer) {
//		System.out.println("Getting layer " + layer + " " + tileTextures.get(layer));
		return tileTextures.get(layer);
	}
	
	public Sprite[] getDefaults(){
		Sprite[] defaultArray = {defaultBoid, defaultBoid_selected};
		return defaultArray;
	}
}
