package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;

import com.UKC_AICS.simulation.entity.Boid;


/**
 *
 * @author Emily
 */
public class BoidManager extends Manager {


    static final ArrayList<Boid> boids = new ArrayList<Boid>();
    
    
    public BoidManager () {
        
        
        
    }
    /**
     * called by the update in SimulationManager
     */
    public void update() {
        //loop through boids and ask them to do their thing.
    }
}
