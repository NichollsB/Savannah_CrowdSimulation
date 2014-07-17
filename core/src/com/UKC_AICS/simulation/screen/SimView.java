package com.UKC_AICS.simulation.screen;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public interface SimView {
	
	/**
	 * Pass the arrays of boids and objects for use in the simulation view
	 * @param boidArray - Array<Boid> to be used in the simulation view
	 * @param objectArray - Array<Object> to be used in the simulation view
	 */
	public void setEntityArrays(Array<Boid> boidArray, Array<Object> objectArray);
	
	
	/**
	 * Pass map of file locations associated with the boid sprites and pass to asset manager to load and create sprites
	 * @param fileLocations - HashMap<Byte, String> indicating different texture file location strings and associated type
	 */
	public void initBoidSprites(HashMap<Byte, String> fileLocations);
	/**
	 * Pas map of file locations associated with explicitly placed objects and pass to asset manager to laod and create sprites
	 * @param fileLocations - HashMap<Byte, String> indicating different texture file location strings and associated type
	 */
	public void initObjectSprites(HashMap<Byte, String> fileLocations);
	
}
