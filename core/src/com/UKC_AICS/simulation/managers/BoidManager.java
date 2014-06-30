package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector3;


/**
 * @author Emily
 */
public class BoidManager extends Manager {


    static final ArrayList<Boid> boids = new ArrayList<Boid>();

    //TEMP: each boid type will have its own sight radius
    static final float radius = 10f;
    private Vector3 tempVec = new Vector3();

    public BoidManager() {

    }


    public void createBoid() {
        Boid boid = new Boid();

        boid.position.set(10, 10, 0);
        boid.orientation.set(10, 10, 0);
        boid.velocity.set(10, 10, 0);

        boids.add(boid);
    }


    /**
     * called by the update in SimulationManager
     * this will loop through the boids and update them
     */
    public void update() {

        //loop through boids and ask them to do their thing.
        for (Boid boid : boids) {
            // find relevant boids
            //crudely ask each one if it's inside the radius


            //get weights
            float coh = SimulationManager.tempSpeciesData.get("zebra").get("cohesion");
            float ali = SimulationManager.tempSpeciesData.get("zebra").get("alignment");
            float sep = SimulationManager.tempSpeciesData.get("zebra").get("separation");

            //do stuff with weights
            tempVec.add(new Vector3()); //TODO: work out how to store the behaviours
            // EG-> tempVec.add(behaviour.update(boids, boid).scl(behWeight));
        }
    }


}
