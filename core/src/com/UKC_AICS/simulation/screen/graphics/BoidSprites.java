package com.UKC_AICS.simulation.screen.graphics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;

public class BoidSprites extends SpriteLoader {
	private final ObjectMap<Byte, Sprite> boidSprites = new ObjectMap<Byte, Sprite>();
	private static Sprite defaultBoid;
	private final static String defaultBoid_path = "data/newTriangle.png";
	private String boid_path = defaultBoid_path;
	private static Sprite defaultBoid_selected;
	private final static String defaultBoid_selected_path = "data/newTriangle_highlight.png";
	private String selectedBoid_Path = defaultBoid_selected_path;
	
	
	private HashMap<Byte, float[]> boidColors = new HashMap<Byte, float[]>();
	
	public BoidSprites(String boid_Path, String selectedBoid_Path){
		this.boid_path = boid_Path;
		this.selectedBoid_Path = selectedBoid_Path;
		loadBoids(boid_Path, selectedBoid_Path);
	}
	public BoidSprites(){
		boid_path = defaultBoid_path;
		selectedBoid_Path = defaultBoid_selected_path;
		loadBoids(boid_path, selectedBoid_Path);
	}
	
	public void loadBoids(String boid_Path, String selectedBoid_Path){
		super.loadAsset_Texture(boid_Path);
		super.loadAsset_Texture(selectedBoid_Path);
	}
	
	public void addBoids(HashMap<Byte, float[]> boidColors){
		this.boidColors = boidColors;
		update();
	}
	
	public boolean update(){
		if(super.isLoaded(boid_path) && super.isLoaded(selectedBoid_Path)){
			defaultBoid = super.createSprite(boid_path);
			for(Byte b : boidColors.keySet()){
				if(!boidSprites.containsKey(b)){
					Sprite coloredSprite = new Sprite(defaultBoid);
					coloredSprite.setColor(boidColors.get(b)[0], boidColors.get(b)[0], boidColors.get(b)[1], boidColors.get(b)[2]);
					boidSprites.put(b, coloredSprite);
				}
			}
			return true;
		}
		return false;
	}
	
}
