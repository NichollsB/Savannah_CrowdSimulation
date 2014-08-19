package com.UKC_AICS.simulation.utils;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;

import static com.UKC_AICS.simulation.Constants.*;

/**
 * Class for the loading of external texture sheets that may be used as large graphics layers or for initialising simulation world values
 * Any packfile for environment layers must be packed such that no rotation has been performed to the original textures
 * 
 * @author Ben Nicholls
 *
 */
public abstract class EnvironmentLoader {
	
	private static final String defaultPackFile_path = "data/Maps/EnvSettings.txt";
	private static final String defaultMapSheet_path = "data/Maps/EnvSettings.png";
	private static String atlasPath = defaultPackFile_path;
	private static String mapPath = defaultMapSheet_path;
	
	private static TextureAtlas environmentAtlas;
	private static Pixmap environmentAtlas_pixmap;
	private static final HashMap<String, Pixmap> environmentLayers_pixmap = new HashMap<String, Pixmap>();
	private static final HashMap<String, AtlasRegion> environmentLayers_atlasRegion = new HashMap<String, AtlasRegion>();
	
	/**
	 * Enum for specifying the various environment layers to load, the associated String region names that the loader should look for
	 * in the packfile containing the various layers, and the layer String name
	 * @author Benjamin Nicholls
	 *
	 */
	public enum EnvironmentLayer{
		
		TERRAIN("terrain", "water"),
		WATER("water", "grass"),
		GRASS("grass", "terrain");

		private final String name;
		private final String layerMap;
		
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
		loadMaps(atlasPath, mapPath);
	}
	
	/**
	 * Load maps into a TextureAtlas via the pack file and the packed map sheet as a Pixmap. Retrieve regions from the 
	 * pack file that match the EnvironmentLayer layerMap Strings and store Pixmaps for each region.
	 * @param packfile_path String path for the pack file of the texture sheet to load
	 * @param packsheet_path String path for the texture sheet to load
	 */
	public static void loadMaps(String packfile_path, String packsheet_path){
//		environments.put(name, 
//				new TextureAtlas(defaultEnvAtlas_path));
		FileHandle handle = Gdx.files.internal(packfile_path);
		environmentAtlas = new TextureAtlas(handle);
		handle = Gdx.files.internal(packsheet_path);
		environmentAtlas_pixmap = new Pixmap(handle);
		
		for(EnvironmentLayer layer : EnvironmentLayer.values()){
			AtlasRegion region = environmentAtlas.findRegion(layer.mapName());
			try{
				Pixmap pixLayer = new Pixmap(region.originalWidth, region.originalHeight, environmentAtlas_pixmap.getFormat());
				pixLayer.drawPixmap(environmentAtlas_pixmap, (int)region.offsetX, (int)region.offsetY, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
				environmentLayers_pixmap.put(layer.toString(), pixLayer);
				environmentLayers_atlasRegion.put(layer.toString(), region);
			}
			catch(NullPointerException e){
				System.out.println("Cannot find environment layer named " + layer.toString() + " in Map packfile");
			}
		}
//		for(AtlasRegion region : environmentAtlas.getRegions()){
//			Pixmap layer = new Pixmap(region.originalWidth, region.originalHeight, environmentAtlas_pixmap.getFormat());
//			layer.drawPixmap(environmentAtlas_pixmap, (int)region.offsetX, (int)region.offsetY, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
//			environmentLayers.put(region.name, layer);
//		}
	}
	
	public void loadLayer(String packfile_path, String packsheet_path){
		try{
			
		}
		catch(NullPointerException e){
			
		}
	}
	
	/**
	 * Return the specified environment layer as an AtlasSprite
	 * @param name String name of the layer to be retrieved (set by the name of the original image packed into the environment pack file
	 * @return Texture form of the image layer
	 */
	public static AtlasSprite getLayer_sprite(String name){
		try{
			return new AtlasSprite(environmentLayers_atlasRegion.get(name));
		} catch(NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Return the specified environment layer as an AtlasSprite
	 * @param name The EnvironmentLayer enum associated with the map layer (region) to load
	 * @return AtlasSprite of the map layer region retrieved
	 */
	public static AtlasSprite getLayer_sprite(EnvironmentLayer name){
		return getLayer_sprite(name.toString());
	}
	
	/**
	 * Retrieve the specified environment layer as a Pixmap
	 * @param name String name of the layer to be retrieved (set by the name of the original image packed into the environment pack file
	 * @return Pixmap of the map layer region retrieved
	 */
	public static Pixmap getLayer_pixmap(String name){
		if(environmentLayers_pixmap.containsKey(name))
			return environmentLayers_pixmap.get(name);
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
		byte[][] layerVals = {{0},{0}};
		if(environmentLayers_pixmap.containsKey(name)){
			Pixmap layer = environmentLayers_pixmap.get(name);
			layerVals = new byte[layer.getHeight()/TILE_SIZE][layer.getWidth()/TILE_SIZE];
			float color=0f;
			Color c = new Color();
			for(int xGrid = 0, j = 0, i = 0; xGrid < layer.getWidth(); xGrid+=TILE_SIZE, j++, i = 0){
				for( int yGrid = 0; yGrid < layer.getHeight(); yGrid+=TILE_SIZE, i++){
					for(int pixX = xGrid; pixX<(xGrid+TILE_SIZE); pixX++){
						for(int pixY = yGrid; pixY<(yGrid+TILE_SIZE); pixY++){
							Color.rgb888ToColor(c, layer.getPixel(pixX, pixY));
//							color += (layer.getPixel(pixX, pixY) & 0x000000ff) / 255f;
							if(alphaChannel)
								color+= c.a;
							else
								color+= c.r;
							
						}
					}
					color = (color/(TILE_SIZE*TILE_SIZE))*100;
					layerVals[i][j] = (byte)color;
					color = 0;
				}
			}
		}
		return layerVals;
	}
	

	
	
}
