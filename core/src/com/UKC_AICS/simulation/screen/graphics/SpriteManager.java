package com.UKC_AICS.simulation.screen.graphics;

import java.util.HashMap;

import com.UKC_AICS.simulation.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
	private final static String defaultBoid_path = "data/EntitySprites/newTriangle.png";
	private static Sprite defaultBoid_selected;
	private final static String defaultBoid_selected_path = "data/EntitySprites/newTriangle_highlight.png";
	
	private final static HashMap<Byte, float[]> boidColors = new HashMap<Byte, float[]>();
	private final static ObjectMap<Byte, Sprite> boidSprites = new ObjectMap<Byte, Sprite>();
    private final static ObjectMap<Byte, Sprite> corpseSprites = new ObjectMap<Byte, Sprite>();

    private final static String entitySheet_Path = "data/EntitySprites/EntitySheet.pack";
    private TextureAtlas entitySheet;

	//World Tiling:
	private ObjectMap<String, String> tileFiles = new ObjectMap<String, String>(){{
		put("grass", "data/EntitySprites/grass_tile_x16.png");
		put("terrain", "data/EntitySprites/water_tile_x16.png");
	}}
	;
	
	private HashMap<Byte, String> boidsFiles = new HashMap<Byte, String>();//{{
//		put(0, "data/triangle2.png");
//		put(1, "data/triangle3.png");
//        put(2, "data/triangle3.png");
//	}};
	private String defaultBoidTextureFile = "data/EntitySprites/triangle2.png";
	private ObjectMap<Byte, String> objectsFiles = new ObjectMap<Byte, String>(){{
				put((byte)0, "data/EntitySprites/corpse_object_x16.png");
				put((byte)1, "data/EntitySprites/corpse_object_x16.png");
				put((byte)2, "data/EntitySprites/Attractor.png");
				put((byte)3, "data/EntitySprites/Repellor.png");
			}};
    private final ObjectMap<Integer, String> objectNames = new ObjectMap<Integer, String>(){{
        put(0, "corpse");
        put(1, "corpse");
        put(2, "A");
        put(3, "R");
        put(4, "tree");
        put(5, "rock");
    }};
	

	private ObjectMap<Byte, Sprite> objectSprites = new ObjectMap<Byte, Sprite>();
    private ObjectMap<Byte, Sprite> objectSprites_Selected = new ObjectMap<Byte, Sprite>();
	private Array<Array<Sprite>> entitySprites;
	
	private ObjectMap<String, Sprite> tileTextures;
	private SpriteCache tileCache = new SpriteCache();
	private int tile;
	private boolean tilesLoaded = false, tilesCreated = false;
	private byte[][] tilesMap;
	
	private AssetManager assetManager = new AssetManager();
	
	private boolean created = true;
	
	private static final TextureAtlas blankAtlas = new TextureAtlas();
	
	public SpriteManager(){
		//loadAssets();
		assetManager.load(defaultBoid_path, Texture.class);
		assetManager.load(defaultBoid_selected_path, Texture.class);
		
		Pixmap blankImage = new Pixmap(16, 16, Format.Alpha);
		blankImage.setColor(0, 0, 0, 0);
		blankImage.fillRectangle(0, 0, 16, 16);
		blankAtlas.addRegion("0", new Texture(blankImage), 0, 0, 0, 0);
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
		objectSprites = new ObjectMap<Byte, Sprite>(objectsFiles.size);
		for(Byte entity: objectsFiles.keys()){
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
			defaultBoid_selected.setScale(0.5f);//.setSize(spriteTexture.getWidth(), spriteTexture.getHeight());
		}
        //USING A SINGLE ENTITY SHEET
        if(assetManager.isLoaded(entitySheet_Path)){
            entitySheet = assetManager.get(entitySheet_Path, TextureAtlas.class);
            Sprite s;
            for(Integer i : objectNames.keys()){
                s = entitySheet.createSprite(objectNames.get(i));
                s.setScale(0.5f);//.setSize(s.getWidth()/2, s.getWidth()/2);
                if(s!=null){
                    objectSprites.put(i.byteValue(), s);
                }
                s = entitySheet.createSprite((objectNames.get(i)+"_highlight"));
                s.setScale(0.5f);//.setSize(s.getWidth()/2, s.getWidth()/2);
                if(s!=null){
                    objectSprites_Selected.put(i.byteValue(), s);
                }
            }

            s = entitySheet.createSprite("boid");
            s.setScale(0.5f);//.setSize(s.getWidth()/2, s.getWidth()/2);
            defaultBoid = s;
            s = entitySheet.createSprite("boid_highlight");
            s.setScale(0.5f);//.setSize(s.getWidth()/2, s.getWidth()/2);
            defaultBoid_selected = s;
            for(byte b: boidColors.keySet()){
                if(!boidSprites.containsKey(b)){
                    s = entitySheet.createSprite("boid");
                    s.setScale(0.5f);//.setSize(s.getWidth()/2, s.getWidth()/2);
                    float[] color = boidColors.get(b);
                    s.setColor(color[0], color[1], color[2], 1);
                    boidSprites.put(b, s);
                }
                if(!corpseSprites.containsKey(b)){
                    s = entitySheet.createSprite("corpse");
                    s.setScale(0.5f);//.setSize(s.getWidth()/2, s.getWidth()/2);
                    float[] color = boidColors.get(b);
                    s.setColor(color[0], color[1], color[2], 1);
                    corpseSprites.put(b, s);
                }
            }

        }
		
		
		//Environment sprites
		if(assetManager.isLoaded(environmentTiles_path)){
			environmentTiles_Atlas = assetManager.get(environmentTiles_path, TextureAtlas.class);
			TextureAtlas atlas = environmentTiles_Atlas;
			Pixmap blankImage = new Pixmap(16, 16, Format.Alpha);
			blankImage.setColor(0, 0, 0, 0);
			blankImage.fillRectangle(0, 0, 16, 16);
			environmentTiles_Atlas.addRegion("0", new Texture(blankImage), 0, 0, 0, 0);
            ObjectMap<Float, AtlasSprite> m = new ObjectMap<Float, AtlasSprite>();
            m.put(0f, new AtlasSprite(environmentTiles_Atlas.findRegion("0")));
            environmentTiles_sprites.put("0", m);
			String[] parts;
			AtlasSprite atSprite;
			for(AtlasRegion region : atlas.getRegions()){
				atSprite = new AtlasSprite(region);
				atSprite.setSize(region.originalWidth, region.originalHeight);
//				if(region.name == "0" || !region.name.contains("#")){
//					if(!environmentTiles_sprites.containsKey("0")){
//						ObjectMap atlasMap = new ObjectMap<Float, AtlasSprite>();
//						atlasMap.put(0, atSprite);
//						environmentTiles_sprites.put("0", atlasMap);
//					}
//					continue;
//				}
//				else
                if (region.name.contains("#")){
					parts = region.name.split("#");
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
		}
		
		
		created = true;
	}
	

	public Sprite getObjectSprite(Byte subtype){
//        System.out.println("Getting sprite " + subtype + " " + objectNames.get((int)subtype));
		if(objectSprites.containsKey(subtype)){

			return objectSprites.get(subtype);
		}
		return null;
	}

	private boolean loadAsset(String fileLocation, boolean atlas){
		try {
            if(atlas) {
                assetManager.load(fileLocation, TextureAtlas.class);
            }
            else {
                assetManager.load(fileLocation, Texture.class);
            }
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
			if(loadAsset(fileStr, false)){
				if(continuousEntities){
					boidsFiles.put(entity, fileStr);
				}
	//			else {
	//				objectsFiles.put(entity.intValue(), fileStr);
	//			}
			}
			
		}
	}

    public void loadAssets_Entities(){
        loadAsset(entitySheet_Path, true);
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
		for(byte type : objectsFiles.keys()){
			filename = objectsFiles.get(type);
			//System.out.println(filename);
			loadAsset(filename, false);

		}
	}
	
	public void loadAssets_Tiles(String path){
		if(path != null)
			environmentTiles_path = path;
		assetManager.load(environmentTiles_path, TextureAtlas.class);
	}

	public AtlasRegion getTileRegion(String layer, int amount){
		
		try{
			
//			if(layer == "terrain")
//				layer = "water";
			if(layer != null && amount > 0){
//                return environmentTiles_regions.get(layer).get((float)amount);
				return environmentTiles_Atlas.findRegion(layer + "#" + amount);
			}
			else
				return null;
			
		} catch (NullPointerException e){
			
			return null;
		}
	}
	public AtlasRegion getTileRegion(String layer){
		return environmentTiles_Atlas.findRegion(layer);
	}
	
	public AtlasRegion getEmptyRegion(){
		return environmentTiles_Atlas.findRegion("0"); 
	}
    public AtlasSprite getEmptySprite(){ return  environmentTiles_sprites.get("0").get(0f); }
	public int getNumTileRegions(){
		
		return environmentTiles_Atlas.getRegions().size;
	}
    public AtlasSprite getTileSprite(String layer){ return getTileSprite(layer, 100);}
	public AtlasSprite getTileSprite(String layer, float amount){
		String l = layer;
//		if(l == "water")return null;
//		if(l == "terrain")
//			l = "water";
		
		if(!environmentTiles_sprites.containsKey(l)) return null;
		if(!environmentTiles_sprites.get(l).containsKey(amount)) return null;
			
//			
			return environmentTiles_sprites.get(l).get(amount);
		
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

    public Sprite getCorpse_Sprite(byte species){ return corpseSprites.get(species); }

    public Sprite getObject_highlight(byte type){
        return objectSprites_Selected.get(type);
    }
	
}
