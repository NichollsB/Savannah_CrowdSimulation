package com.UKC_AICS.simulation.utils;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import static com.UKC_AICS.simulation.Constants.*;

/**
 * Class for the loading of external texture sheets that may be used as large graphics layers or for initialising simulation world values
 * Any packfile for environment layers must be packed such that no rotation has been performed to the original textures
 * 
 * @author Ben Nicholls bn65@kent.ac.uk
 *
 */
public abstract class EnvironmentLoader {
	
	private static final String defaultPackFile_path = "data/Maps/newMap.txt";
	private static final String defaultMapSheet_path = "data/Maps/newMap.png";
	private static String atlasPath = defaultPackFile_path;
	private static String mapPath = defaultMapSheet_path;
	
	private static TextureAtlas environmentAtlas;
	private static Pixmap environmentAtlas_pixmap;
    private static final HashMap<String, EnvironmentLayer> environmentLayers = new HashMap<String, EnvironmentLayer>(){{
        for(EnvironmentLayer layer : EnvironmentLayer.values()){
            put(layer.toString(), layer);
        }
    }};
	private static final HashMap<String, Pixmap> environmentLayers_Pixmap = new HashMap<String, Pixmap>();
	private static final HashMap<String, AtlasRegion> environmentLayers_atlasRegion = new HashMap<String, AtlasRegion>();
	
	/**
	 * Enum for specifying the various environment layers to load, the associated String region names that the loader should look for
	 * in the packfile containing the various layers, and the layer String name
	 * @author Benjamin Nicholls
	 *
	 */
	public enum EnvironmentLayer{
		
		TERRAIN("terrain", "terrain"),
		WATER("water", "water"),
		GRASS("grass", "grass");

		private final String name;
		private final String layerMap;

        private int width;
        private int height;
		
		EnvironmentLayer(String name, String regionName){
			this.name = name;
			this.layerMap = regionName;
		}
		public String toString(){
			return this.name;
		}
		public String mapName(){
			return this.layerMap;
		}
	}
	
	/**
	 * Load the default maps texture sheet file and pack file
	 */
	public static void loadMaps(){
		loadMaps(Gdx.files.internal(atlasPath), Gdx.files.internal(mapPath));
	}

    public static void loadMap(File filePath, String layer){
        loadMap(new FileHandle(filePath), layer);
    }
    public static void loadMap(FileHandle filePath, String layer){
        String ext = filePath.extension();
        System.out.println("File path " + filePath.path() + " ext " + filePath.extension());
        if(!ext.contains("txt") && !ext.contains("png") && !ext.contains("bmp") ) return;
        System.out.println(layer + " contained "+ environmentLayers.containsKey(layer));
        if(environmentLayers.containsKey(layer)){
            Pixmap pixLayer = new Pixmap(filePath);
            environmentLayers_Pixmap.put(layer, pixLayer);
        }
    }

    /**
     * Load a given file using the {@link #loadMap(com.badlogic.gdx.files.FileHandle, String) loadMap} method for
     * File types
     * @param packfile texture sheet pack File to load
     * @param packsheet texture sheet image File to load
     */
    public static void loadMaps(File packfile, File packsheet){
        loadMaps(new FileHandle(packfile), new FileHandle(packsheet));
    }
	/**
	 * Load maps into a TextureAtlas via the pack file and the packed map sheet as a Pixmap. Retrieve regions from the 
	 * pack file that match the EnvironmentLayer layerMap Strings and store Pixmaps for each region.
	 * @param packFile String path for the pack file of the texture sheet to load
	 * @param packsheetFile String path for the texture sheet to load
	 */
	public static void loadMaps(FileHandle packFile, FileHandle packsheetFile){
//		environments.put(name,
//				new TextureAtlas(defaultEnvAtlas_path));
        try {
        	environmentLayers_Pixmap.clear();
        	environmentLayers_atlasRegion.clear();
            FileHandle handle = packFile;
            environmentAtlas = new TextureAtlas(handle);
            handle = packsheetFile;
            environmentAtlas_pixmap = new Pixmap(handle);
            for (EnvironmentLayer layer : EnvironmentLayer.values()) {
                AtlasRegion region = environmentAtlas.findRegion(layer.mapName());
                try {
                    System.out.println("region name " + region.name + " layer to string " + layer.toString());
                    Pixmap pixLayer = new Pixmap(region.originalWidth, region.originalHeight, environmentAtlas_pixmap.getFormat());
                    pixLayer.drawPixmap(environmentAtlas_pixmap, (int) region.offsetX, (int) region.offsetY, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
                    environmentLayers_Pixmap.put(layer.toString(), pixLayer);
                    System.out.println(" added as " + layer.toString() + environmentLayers_Pixmap.containsKey(layer.toString()));
                    environmentLayers_atlasRegion.put(layer.toString(), region);
                } catch (NullPointerException e) {
                    System.out.println("Cannot find environment layer named " + layer.toString() + " in Map packfile");
                }
            }
        }
        catch(GdxRuntimeException e){
            System.out.println("Files not suitable to be loaded");
        }
	}

	/**
	 * Retrieve the specified environment layer as a Pixmap
	 * @param name String name of the layer to be retrieved (set by the name of the original image packed into the environment pack file
	 * @return Pixmap of the map layer region retrieved
	 */
	public static Pixmap getLayer_pixmap(String name){
		if(environmentLayers_Pixmap.containsKey(name))
			return environmentLayers_Pixmap.get(name);
		return null;
	}
	
	/**
	 * /**
	 * Return the specified environment layer as an byte[][] array corresponding to amounts indicated by the 
	 * average greyscale pixel colour (alpha or red channel) within each cell area
	 * @param name String name of the layer to be retrieved (set by the name of the original image packed into the environment pack file
	 * @param alphaChannel Boolean indicating whether or not to formulate values based on the alpha channel, alternativey will generate from red channel
	 * @return
	 */
	public static byte[][] getLayer_values(String name, boolean alphaChannel){
		byte[][] layerVals = null;
		
		if(environmentLayers_Pixmap.containsKey(name)){
//			System.out.println("Layer loading " + name);
			Pixmap layer = environmentLayers_Pixmap.get(name);
            int gridWidth = layer.getWidth()/TILE_SIZE;
            int gridHeight = layer.getHeight()/TILE_SIZE;
            int height = TILE_SIZE*gridHeight;
			layerVals = new byte[gridWidth][gridHeight];
			float color=0f;
			Color c = new Color();
//			int yMax = 
            int x = 0, y = 0;
            for(int gridX = 0; gridX < gridWidth; gridX++){
                for(int gridY = 0; gridY < gridHeight; gridY++){
//                    int xMax =
                    for(x = gridX*TILE_SIZE; x<((gridX*TILE_SIZE)+TILE_SIZE); x++){
                        if(x >= layer.getWidth()) break;
                        for(y = gridY*TILE_SIZE; y<((gridY*TILE_SIZE)+TILE_SIZE); y++){
                            if(y >= layer.getHeight()) break;
                            Color.rgb888ToColor(c, layer.getPixel(x, height-y));
                            if(alphaChannel)
                                color+= c.a;
                            else
                                color+= c.r;

                        }
                    }
//                    if(name.equals("water"))
//                    	System.out.println("High value " + name + " " + color + " divide by " + (TILE_SIZE*TILE_SIZE));
                    color = (color/((float)TILE_SIZE*(float)TILE_SIZE));
                    
                    
                    color *= 100;
                    	
//                    if(name.equals("water"))
                    layerVals[gridX][gridY] = (byte)color;
//                    if(layerVals[gridX][gridY] > 30)
//                    	System.out.println(name + " " + layerVals[gridX][gridY]);
//                    if(name.equals("water"))
//                        System.out.println("in grid " + layerVals[gridX][gridY]);
                    color = 0;
                }
            }
		}
		return layerVals;
	}

    /**
     * Get the dimensions of the maps loaded. Finds the smallest map loaded.
     * @return 2 value array with {width, height}.
     */
    public static int[] getDimensions(){

        if(environmentLayers_Pixmap.size()>0){
            int size[] = new int[2];
            boolean first = true;
            for(Pixmap s : environmentLayers_Pixmap.values()){
                if(s.getWidth() < size[0] || first);
                    size[0]=s.getWidth();
                if(s.getHeight()<size[1] || first)
                    size[1]=s.getHeight();
                first = false;
            }
            return size;
        }
        return null;
    }

    /**
     * return the dimension of the grid of values found from the loaded map files
     * @return 2 value array of the grid size with {width, height}.
     */
    public static int[] getGridDimensions(){
        if(environmentLayers_Pixmap.size()>0){
            int size[] = new int[]{0,0};
            boolean first = true;
            for(String s : environmentLayers.keySet()){
                byte a[][] = getLayer_values(s, false);
                if(a.length<size[0])
                    size[0]=a.length;
                if(a[0].length<size[1])
                    size[1]=a[1].length;
                first = false;
            }
        }
        return null;
    }
	

	
	
}
