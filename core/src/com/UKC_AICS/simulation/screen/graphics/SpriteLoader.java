package com.UKC_AICS.simulation.screen.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Array;

public abstract class SpriteLoader {
	
	protected final static AssetManager manager = new AssetManager();
	
	private final static Array<String> fileLoading = new Array<String>();
	
	
	/**
	 * Create AtlasSprites from regions of a loaded TextureAtlas
	 * @param fileLocation String file path of the TextureAtlas (sprite sheet pack file) to create AtlasSprites from
	 * @return Array<AtlasSprite> Sprites created from the regions of the loaded TetureAtlas
	 */
	public static Array<AtlasSprite> createAtlasSprites(String fileLocation){
		if(isLoaded(fileLocation)){
			fileLoading.removeValue(fileLocation, true);
			TextureAtlas atlas = manager.get(fileLocation, TextureAtlas.class);
			Array<AtlasSprite> sprites = new Array<AtlasSprite>();
			for(AtlasRegion region : atlas.getRegions()){
				sprites.add(new AtlasSprite(region));
			}
			return sprites;
		}
		return null;
	}
	
	/**
	 * Create a sprite from a loaded asset
	 * @param fileLocation String file path of texture that should be used to create the sprite
	 * @return Sprite created from the loaded texture
	 */
	public static Sprite createSprite(String fileLocation){
		if(isLoaded(fileLocation)){
			fileLoading.removeValue(fileLocation, true);
			return new Sprite(manager.get(fileLocation, Texture.class));
		}
		return null;
	}
	
	public static boolean isLoaded(String fileLocation){
		if(fileLoading.contains(fileLocation, true) && manager.isLoaded(fileLocation))
			return true;
		return false;
	}
	
	
	
	/**
	 * Begin loading a file as a Texture
	 * @param fileLocation String location of file to load
	 * @return boolean whether or not the file is loading
	 */
	public static boolean loadAsset_Texture(String fileLocation){
		try {
			manager.load(fileLocation, Texture.class);
			fileLoading.add(fileLocation);
			return true;
		} 
		catch(NullPointerException e) {
			System.out.println("Texture file, or file location " + fileLocation + " is missing");
			return false;
		}
	}
	/**
	 * Begin loading a file as a TextureAtlas
	 * @param fileLocation String location of file to load
	 * @return boolean whether or not the file is loading
	 */
	public static boolean loadAsset_Atlas(String fileLocation){
		try {
			manager.load(fileLocation, TextureAtlas.class);
			fileLoading.add(fileLocation);
			return true;
		} 
		catch(NullPointerException e) {
			System.out.println("Texture file, or file location " + fileLocation + " is missing");
			return false;
		}
	}
}
