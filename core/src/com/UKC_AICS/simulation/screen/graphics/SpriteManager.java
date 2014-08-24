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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Class for loading graphical assets via an {@link com.badlogic.gdx.assets.AssetManager AssetManager} object.
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
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
	private String defaultBoidTextureFile = "data/EntitySprites/triangle2.png";
	private ObjectMap<Byte, String> objectsFiles = new ObjectMap<Byte, String>(){{
				put((byte)0, "data/EntitySprites/corpse_object_x16.png");
				put((byte)1, "data/EntitySprites/corpse_object_x16.png");
				put((byte)2, "data/EntitySprites/Attractor.png");
				put((byte)3, "data/EntitySprites/Repellor.png");
			}};
    private final ObjectMap<Integer, String> objectNames = new ObjectMap<Integer, String>(){{
        put(0, "corpse");
        put(2, "A");
        put(3, "R");
        put(4, "tree");
        put(5, "rock");
    }};
	

	private ObjectMap<Byte, Sprite> objectSprites = new ObjectMap<Byte, Sprite>();
    private ObjectMap<Byte, Sprite> objectSprites_Selected = new ObjectMap<Byte, Sprite>();
	private Array<Array<Sprite>> entitySprites;
	
	private ObjectMap<String, Sprite> tileTextures;

	private AssetManager assetManager = new AssetManager();
	
	private boolean created = true;
	
	private static final TextureAtlas blankAtlas = new TextureAtlas();

    /**
     * Initialise some default textures
     */
	public SpriteManager(){
		//loadAssets();
		assetManager.load(defaultBoid_path, Texture.class);
		assetManager.load(defaultBoid_selected_path, Texture.class);
		
		Pixmap blankImage = new Pixmap(16, 16, Format.Alpha);
		blankImage.setColor(0, 0, 0, 0);
		blankImage.fillRectangle(0, 0, 16, 16);
		blankAtlas.addRegion("0", new Texture(blankImage), 0, 0, 0, 0);
	}

    /**
     * Called every update, attempts to finish creation of sprites, etc
     * @return True if creation was completed
     */
	public boolean update(){
		if(assetManager.update() && !created){
			createSprites();
		}
		return created;
	}

    /**
     * Once the assetManager has finished loading assets, attempts to finish creation of sprites and other assets
     * used in graphics
     */
	private void createSprites(){
		created = false;
		Sprite sprite;
		Texture spriteTexture;
		tileTextures = new ObjectMap<String, Sprite>(tileFiles.size);
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
                //Create a highligght sprite for each objects selected mode
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
                //Generate corpse for each boid
                if(!corpseSprites.containsKey(b)){
                    s = entitySheet.createSprite("corpse");
                    s.setScale(0.4f);//.setSize(s.getWidth()/2, s.getWidth()/2);
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

    /**
     * Retrieve a sprite for an object with particular type byte
     * @param subtype Identifier type byte of the object to retrieve
     * @return The Sprite retrieved
     */
	public Sprite getObjectSprite(Byte subtype){
//        System.out.println("Getting sprite " + subtype + " " + objectNames.get((int)subtype));
		if(objectSprites.containsKey(subtype)){

			return objectSprites.get(subtype);
		}
		return null;
	}

    /**
     * Attempt to load a particular file
     * @param fileLocation File path of the file to load
     * @param atlas True if this file should be loaded as an atlas, false if a texture
     * @return True if AssetManager has started loading the file
     */
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

    /**
     * Set the AssetManager to loading the default set of entities textures (for creation of sprites)
     */
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

    /**
     * Load assets for particular object identifier type bytes
     * @param objs Array of identifier type bytes to load
     */
	public void loadAssets_Objects(Array<Byte> objs){
		created = false;
		String filename = "data/corpse_object_x16.png";
		for(byte type : objectsFiles.keys()){
			filename = objectsFiles.get(type);
			//System.out.println(filename);
			loadAsset(filename, false);

		}
	}

    /**
     * Load an tile atlas, from a given file path
     * @param path String file path of the TextureAtlas to load
     */
	public void loadAssets_Tiles(String path){
		if(path != null)
			environmentTiles_path = path;
		assetManager.load(environmentTiles_path, TextureAtlas.class);
	}

    /**
     * Retrieve an {@link com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion AtlasRegion}
     * with a given String layer name and a specific opacity value from those that have been loaded via a TextureAtlas file.
     * The texture sheet must contain a region with a name in the format layer+"#"+amount in order to load
     * @param layer Name of the tile layer to load. Texture sheet must contain a region containing
     *              this String in order to load
     * @param amount The opacity value of the tile layer AtlasRegion to load. The texture sheet must
     *               contain a region containing ths amount in its name in order to retrieve
     * @return AtlasRegion retrieved, or null. The {@link #environmentTiles_Atlas atlas map} must contain a key matching
     * layer, and then its map value must contain a key matching amount, in order to retrieve.
     */
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

    /**
     * Return a region with the given layer name from the {@link #environmentTiles_Atlas tile atlas}.
     * @param layer Name of the region to find
     * @return The AtlasRegion if found, otherwise null
     */
	public AtlasRegion getTileRegion(String layer){
		return environmentTiles_Atlas.findRegion(layer);
	}


	public AtlasRegion getEmptyRegion(){
		return environmentTiles_Atlas.findRegion("0");
	}

    /**
     * Retrieve an empty sprite from the {@link #environmentTiles_sprites sprite map}.
     * @return An empty or transparent atlas sprite
     */
    public AtlasSprite getEmptySprite(){ return  environmentTiles_sprites.get("0").get(0f); }

    /**
     * Retrieve an {@link com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite AtlasSprite} from the
     * {@link #environmentTiles_sprites tile sprite map}.
     * @param layer The layer String key to search for in the map
     * @param amount The float amount to find corresponding to the layer
     * @return AtlasSprite, or null if no value is found.
     */
	public AtlasSprite getTileSprite(String layer, float amount){
		String l = layer;
		if(!environmentTiles_sprites.containsKey(l)) return null;
		if(!environmentTiles_sprites.get(l).containsKey(amount)) return null;
        return environmentTiles_sprites.get(l).get(amount);
		
	}

    /**
     * Get the boid highlight {@link #defaultBoid_selected sprite}.
     * @return Sprite
     */
	public Sprite getBoid_HighlightSprite(){
		return defaultBoid_selected;
	}

    /**
     * Get the boid sprite associated with the given species sprite from {@link #boidSprites boidSprites}.
     * @param species byte type of the boid sprite to retrieve
     * @return Sprite of the boid
     */
	public Sprite getBoid_Sprite(byte species){
		return boidSprites.get(species);
	}

    /**
     * Get the default (uncolored) generic boid sprite
     * @return {@link #defaultBoid default boid};
     */
	public Sprite getBoid_DefaultSprite(){
		return defaultBoid;
	}

    /**
     * Get a corpse type object associated with a particular type. This will correspond to a species type
     * @param species byte type of the corpse object
     * @return sprite of species type retrieved from the corpse sprite {@link #corpseSprites map}.
     */
    public Sprite getCorpse_Sprite(byte species){ return corpseSprites.get(species); }

    /**
     * Get the selected highlight sprite for a given object type
     * @param type byte type to retrieve the highlight for
     * @return Sprite from the highlighted object sprites {@link #objectSprites_Selected map}.
     */
    public Sprite getObject_highlight(byte type){
        return objectSprites_Selected.get(type);
    }


    /**
     * Retrieve a HashMap of all boid sprites as images.
     * @return Map of boid species types to {@link com.badlogic.gdx.scenes.scene2d.ui.Image images} of their associated sprites
     */
    public HashMap<Byte, Image> getBoidImages(){
        HashMap<Byte, Image> boidImages = new HashMap<Byte, Image>();
        for(byte b : boidSprites.keys()){
            Image i = new Image(boidSprites.get(b));
            i.setScale(0.8f, 0.8f);
            i.setColor(boidSprites.get(b).getColor());
            boidImages.put(b, i);
        }
        return boidImages;
    }

    /**
     * Retrieve a HashMap of all object sprites as images.
     * @return Map of object types to {@link com.badlogic.gdx.scenes.scene2d.ui.Image images} of their associated sprites
     */
    public HashMap<Byte, Image> getObjectImages(){
        HashMap<Byte, Image> objImages = new HashMap<Byte, Image>();
        for(byte b : objectSprites.keys()){
            Image i = new Image(objectSprites.get(b));
            i.setScale(0.8f, 0.8f);
            objImages.put(b, i);
        }
        return objImages;
    }
}
