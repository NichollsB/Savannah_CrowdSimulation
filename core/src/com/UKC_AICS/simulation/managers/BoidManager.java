package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.UKC_AICS.simulation.entity.behaviours.Alignment;
import com.UKC_AICS.simulation.entity.behaviours.Behaviour;
import com.UKC_AICS.simulation.entity.behaviours.Cohesion;
import com.UKC_AICS.simulation.entity.behaviours.Separation;
import com.badlogic.gdx.math.Vector3;


/**
 *
 * @author Emily
 */
public class BoidManager extends Manager {


    private ArrayList<Boid> boids = new ArrayList<Boid>();
    
    //TEMP: each boid type will have its own sight radius.
    static final float radius = 10f;
    private Vector3 tmpVec = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String,Behaviour>();

    public BoidManager () {
        createBoid();

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
    }
    
    
    public void createBoid(){
        Boid boid = new Boid();
        
        boid.position.set(rand.nextInt(30),rand.nextInt(30),rand.nextInt(30));
        boid.orientation.set(10,10,0);
        boid.velocity.set(10,10,0);
        
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
    	for(Boid boid : getBoids()) {
    		// find relevant boids
    		//crudely ask each one if it's inside the radius
    		float coh = SimulationManager.tempSpeciesData.get("zebra").get("cohesion");
            float ali = SimulationManager.tempSpeciesData.get("zebra").get("alignment");
            float sep = SimulationManager.tempSpeciesData.get("zebra").get("separation");
    		//do stuff
            tmpVec.set(0f,0f,0f);

            tmpVec.add(behaviours.get("cohesion").act(getBoids(), new ArrayList<WorldObject>(), boid).scl(coh));
            tmpVec.add(behaviours.get("alignment").act(getBoids(), new ArrayList<WorldObject>(), boid).scl(ali));
            tmpVec.add(behaviours.get("separation").act(getBoids(), new ArrayList<WorldObject>(), boid).scl(sep));

            boid.move(tmpVec);

    	}
    }

    public ArrayList<Boid> getBoids() {
        return (ArrayList<Boid>) boids.clone();
    }
}
