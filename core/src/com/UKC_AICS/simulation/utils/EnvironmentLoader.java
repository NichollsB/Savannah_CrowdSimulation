package com.UKC_AICS.simulation.utils;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class EnvironmentLoader {
	
	private static final String defaultEnvAtlas_path = "data/Maps/MapSheet.txt";
	private static String atlasPath = defaultEnvAtlas_path;
	
	private static TextureAtlas environmentAtlas;
	private static Pixmap environmentAtlas_pixmap;
	private static HashMap<String, TextureAtlas> environments = new HashMap<String, TextureAtlas>();
	
	public void EnvironmentLoader(){
		
	}
	public void loadMap(){
		loadMap(atlasPath);
	}
	public void loadMap(String filepath){
//		environments.put(name, 
//				new TextureAtlas(defaultEnvAtlas_path));
		FileHandle handle = new FileHandle(filepath);
		environmentAtlas = new TextureAtlas(handle);
		environmentAtlas_pixmap = new Pixmap(handle);
	}
	
	public float[][] interpretRegion(String region_name){
		return null;
		
	}
	public float[][] interpretRegion(byte region_id){
		return null;
		
	}
	
	
}
