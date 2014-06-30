package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;
import java.util.Random;

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
        
    	for(int i = 0 ; i<101 ;i++){
    	createBoid();
    	}
    }
    
    
    public void createBoid(){
        Boid boid = new Boid();
        
        Random rand = new Random();
        
        
        int maxXPos = 1280;
        int minXPos = 0;
        
        int maxYPos = 720;
        int minYPos = 0;
        
        int maxXOrient = 1280;
        int minXOrient = 0;
        
        int maxYOrient = 720;
        int minYOrient = 0;
        
        int maxXVel = 10;
        int minXVel = 0;
        
        int maxYVel = 10;
        int minYVel = 0;
        
        int xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        int yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        
        int xOrient = rand.nextInt((maxXOrient - minXOrient) + 1) + minXOrient;
        int yOrient = rand.nextInt((maxYOrient - minYOrient) + 1) + minYOrient;
        
        int xVel = rand.nextInt((maxXVel - minXVel) + 1) + minXVel;
        int yVel = rand.nextInt((maxYVel - minYVel) + 1) + minYVel;
        
        
        boid.position.set(xPos,yPos,0);
        boid.orientation.set(xOrient,yOrient,0);
        boid.velocity.set(xVel,yVel,0);
        
        boids.add(boid);
        
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
