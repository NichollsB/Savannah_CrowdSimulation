package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.UKC_AICS.simulation.entity.Boid;


/**
 *
 * @author Emily
 */
public class SimulationManager extends Manager{
    
    static final BoidManager boidManager = new BoidManager();
    static final WorldManager worldManager = new WorldManager();
    
    //monstrous things.
    static final HashMap<String, HashMap<String, Float>> tempSpeciesData = new HashMap<String, HashMap<String, Float>>();
    
    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * 
     * 
     * Possibly store the data lookup tables here? like species data for example
     * 
     * 
     */
    public SimulationManager() {
    	HashMap<String,Float> zebra = new HashMap<String,Float>();
    	zebra.put("cohesion", 0.5f);
    	zebra.put("alignment", 0.5f);
    	zebra.put("seperation", 0.5f);
        tempSpeciesData.put("zebra", zebra);
    }
    
    public void update() {
        boidManager.update();
        worldManager.update();
    }
    
    
    public ArrayList<Boid> getBoids() {
    	return (ArrayList<Boid>) boidManager.boids.clone();
    }
}
