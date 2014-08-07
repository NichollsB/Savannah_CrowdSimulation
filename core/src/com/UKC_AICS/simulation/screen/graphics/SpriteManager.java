package com.UKC_AICS.simulation.screen.graphics;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteManager {
	
	//Environment
	private final static String environmentTiles_defaultPath = "data/EnvironmentTiles/EnvironmentTiles.txt";
	private static String environmentTiles_path = environmentTiles_defaultPath;
	private static TextureAtlas environmentTiles_Atlas;
	private final static ObjectMap<String, ObjectMap<Float, AtlasSprite>> environmentTiles_sprites = new ObjectMap<String, ObjectMap<Float, AtlasSprite>>();
	private final static ObjectMap<String, ObjectMap<Float, AtlasRegion>> environmentTiles_regions = new ObjectMap<String, ObjectMap<Float, AtlasRegion>>();
	
	private static Sprite defaultBoid;
	private final static String defaultBoid_path = "data/newTriangle.png";
	private static Sprite defaultBoid_selected;
	private final static String defaultBoid_selected_path = "data/newTriangle_highlight.png";
	
	private final static HashMap<Byte, float[]> boidColors = new HashMap<Byte, float[]>();
	private final static ObjectMap<Byte, Sprite> boidSprites = new ObjectMap<Byte, Sprite>();

	//World Tiling:
	private ObjectMap<String, String> tileFiles = new ObjectMap<String, String>(){{
		put("grass", "data/grass_tile_x16.png");
		put("terrain", "data/water_tile_x16.png");
	}}
	;
	
	private HashMap<Byte, String> boidsFiles = new HashMap<Byte, String>();//{{
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
		
		for(String tile : tileFiles.keys()){
			if(assetManager.isLoaded(tileFiles.get(tile))){
				spriteTexture = assetManager.get(tileFiles.get(tile), Texture.class);
				tileTextures.put(tile, new Sprite(spriteTexture));
				tilesLoaded = true;
			}
		}
			
		for(byte entity: boidsFiles.keySet()){
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
			
		}
		if(assetManager.isLoaded(defaultBoid_path)){
			spriteTexture = assetManager.get(defaultBoid_path, Texture.class);
			sprite = new Sprite(spriteTexture);
			defaultBoid = sprite;
			for(byte b : boidColors.keySet()){
				if(!boidSprites.containsKey(b)){
//					defaultBoid = new Sprite(spriteTexture);
					float[] color = boidColors.get(b);
					sprite.setColor(color[0], color[1], color[2], 1);
					boidSprites.put(b, sprite);
				}
			}
		}
		if(assetManager.isLoaded(defaultBoid_selected_path)){
			spriteTexture = assetManager.get(defaultBoid_selected_path, Texture.class);
			defaultBoid_selected = new Sprite(spriteTexture);
			defaultBoid_selected.setSize(spriteTexture.getWidth(), spriteTexture.getHeight());
		}
		
		
		//Environment sprites
		if(assetManager.isLoaded(environmentTiles_path)){
			environmentTiles_Atlas = assetManager.get(environmentTiles_path, TextureAtlas.class);
			TextureAtlas atlas = environmentTiles_Atlas;
			
			String[] parts;
			AtlasSprite atSprite;
			for(AtlasRegion region : atlas.getRegions()){
				parts = region.name.split("#");
//				System.out.println(parts[0] + " | " + parts[1]);
				atSprite = new AtlasSprite(region);
				atSprite.setSize(region.originalWidth, region.originalHeight);
				if(!environmentTiles_sprites.containsKey(parts[0])){
					ObjectMap atlasMap = new ObjectMap<Float, AtlasSprite>();
					atlasMap.put(Float.parseFloat(parts[1]), atSprite);
					environmentTiles_sprites.put(parts[0], atlasMap);
				}
				else {
					environmentTiles_sprites.get(parts[0]).put(Float.parseFloat(parts[1]), 
							atSprite);
				}
			}
		}
		
		created = true;
	}
	

	public Sprite getObjectSprite(Byte subtype){
		if(objectSprites.containsKey((int)subtype)){
			return objectSprites.get(subtype.intValue());
		}
		return null;
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
					boidsFiles.put(entity, fileStr);
				}
	//			else {
	//				objectsFiles.put(entity.intValue(), fileStr);
	//			}
			}
			
		}
	}
	public void loadAssets_Boids(HashMap<Byte, float[]> spriteColors){
		created = false;
		String fileStr;
		for(byte b : spriteColors.keySet()){
			boidColors.put(b, spriteColors.get(b));
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
	
	public void loadAssets_Tiles(String path){
		if(path != null)
			environmentTiles_path = path;
		assetManager.load(environmentTiles_path, TextureAtlas.class);
	}

	public AtlasRegion getTileRegion(String layer, int amount){
//		String regionName = layer + "#" + amount;
//		System.out.println(environmentTiles_Atlas.findRegion(regionName));
		return environmentTiles_Atlas.findRegion(layer + "#" + amount);
	}
	public int getNumTileRegions(){
		
		return environmentTiles_Atlas.getRegions().size;
	}
		
	public AtlasSprite getTileSprite(String layer, float amount){
//		String name = (layer + "_" + amount);
//		if(layer == "water"){
//			System.out.println(layer + amount);
//			System.out.print(environmentTiles_sprites.containsKey(layer));
//			for(Float s : environmentTiles_sprites.get(layer).keys()){
//				System.out.println("sprite " + s);
//			}
//		}
		if(!environmentTiles_sprites.containsKey(layer)) return null;
		if(!environmentTiles_sprites.get(layer).containsKey(amount)) return null;
			
//			System.out.println("layer exists " + environmentTiles_sprites.get(name).keys());
//			Array<Float> keys = environmentTiles_sprites.get(layer).keys().toArray();
//			float threshold;
////			if(amount < keys.get(0))
////				return null;
//			int nullcount=0;
//			for(int i = 0; i < keys.size; i ++){
//				threshold = keys.get(i);
//				if(amount >= keys.get(i) && amount < keys.get(i+1)){
//					if(environmentTiles_sprites.get(layer).get(keys.get(i)) == null) nullcount++;
//					return environmentTiles_sprites.get(layer).get(keys.get(i));
//				}
//			
			return environmentTiles_sprites.get(layer).get(amount);
		
	}
	public Sprite getBoid_HighlightSprite(){
		return defaultBoid_selected;
	}
	public Sprite getBoid_Sprite(byte species){
		return boidSprites.get(species);
	}
	
	public Sprite getBoid_DefaultSprite(){
		return defaultBoid;
	}
	
}
