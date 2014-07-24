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
	
	private static final String defaultPackFile_path = "data/Maps/default/EnvSheet.txt";
	private static final String defaultMapSheet_path = "data/Maps/default/EnvSheet.png";
	private static String atlasPath = defaultPackFile_path;
	private static String mapPath = defaultMapSheet_path;
	
	private static TextureAtlas environmentAtlas;
	private static Pixmap environmentAtlas_pixmap;
	private static final HashMap<String, Pixmap> environmentLayers_pixmap = new HashMap<String, Pixmap>();
	private static final HashMap<String, AtlasRegion> environmentLayers_atlasRegion = new HashMap<String, AtlasRegion>();
	
	public enum EnvironmentLayer{
		GROUND("ground", "Ground_Map"),
		TERRAIN("terrain", "Movable_Map"),
		WATER("water", "Water_Map"),
		GRASS("grass", "InitialGrass_Map");

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
	
	public static void loadMaps(int width, int height){
		loadMaps(atlasPath, width, height);
	}
	public static void loadMaps(String filepath, int width, int height){
//		environments.put(name, 
//				new TextureAtlas(defaultEnvAtlas_path));
		FileHandle handle = Gdx.files.internal(atlasPath);
		environmentAtlas = new TextureAtlas(handle);
		System.out.println("atlas " + environmentAtlas.toString());
		handle = Gdx.files.internal(mapPath);
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
	
	/**
	 * Return the specified environment layer as a Texture
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
	
	public static AtlasSprite getLayer_sprite(EnvironmentLayer name){
		return getLayer_sprite(name.toString());
	}
	
	public static Pixmap getLayer_pixmap(String name){
		if(environmentLayers_pixmap.containsKey(name))
			return environmentLayers_pixmap.get(name);
		return null;
	}
	
	/**
	 * Return the specified environment layer as an byte[][] array corresponding to amounts indicated by the 
	 * average pixel colour (alpha) within each cell area
	 * @param name String name of the layer to be retrieved (set by the name of the original image packed into the environment pack file
	 * @return byte[][] array containing the average alpha value of each pixel within each TILE_SIZE square array.
	 * This indicates the set 'amount' for each cell.
	 */
	public static byte[][] getLayer_values(String name){
		byte[][] layerVals = {{0},{0}};
		if(environmentLayers_pixmap.containsKey(name)){
			Pixmap layer = environmentLayers_pixmap.get(name);
			layerVals = new byte[layer.getHeight()/TILE_SIZE][layer.getWidth()/TILE_SIZE];
			float color=0f;
			for(int xGrid = 0, j = 0, i = 0; xGrid < layer.getWidth(); xGrid+=TILE_SIZE, j++, i = 0){
				for( int yGrid = 0; yGrid < layer.getHeight(); yGrid+=TILE_SIZE, i++){
					for(int pixX = xGrid; pixX<(xGrid+TILE_SIZE); pixX++){
						for(int pixY = yGrid; pixY<(yGrid+TILE_SIZE); pixY++){
//							color += new Color(layer.getPixel(pixX, pixY)).a;
//							System.out.println(color);
							color += (layer.getPixel(pixX, pixY) & 0x000000ff) / 255f;
							
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
