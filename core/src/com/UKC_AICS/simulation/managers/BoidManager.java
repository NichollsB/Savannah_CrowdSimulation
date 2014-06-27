package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;

import com.UKC_AICS.simulation.entity.Boid;


/**
 *
 * @author Emily
 */
public class BoidManager extends Manager {


    static final ArrayList<Boid> boids = new ArrayList<Boid>();
    
    //TEMP: each boid type will have its own.
    static final float radius = 10f;
    
    public BoidManager () {
        createBoid();
    }
    
    
    public void createBoid(){
        Boid boid = new Boid();
        
        boid.position.set(10,10,0);
        boid.orientation.set(10,10,0);
        boid.velocity.set(10,10,0);
    }
    
    
    
    
    /**
     * called by the update in SimulationManager
     * 
     * this will loop through the boids and update them
     * 
     */
    public void update() {
    	
        //loop through boids and ask them to do their thing.
    	for(Boid boid : boids) {
    		// find relevant boids
    		//crudely ask each one if it's inside the radius
    		SimulationManager.tempSpeciesData.get("zebra").get("cohesion");
    		//do stuff
    	}
    }
}
