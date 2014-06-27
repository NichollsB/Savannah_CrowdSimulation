package com.UKC_AICS.simulation.managers;


/**
 *
 * @author Emily
 */
public class SimulationManager extends Manager{
    
    static final BoidManager boidManager = new BoidManager();
    static final WorldManager worldManager = new WorldManager();
    
    
    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * 
     * 
     * Possibly store the data lookup tables here? like species data for example
     * 
     * 
     */
    public void SimulationManager() {
        
    }
    
    public void update() {
        boidManager.update();
        worldManager.update();
    }
    
}
