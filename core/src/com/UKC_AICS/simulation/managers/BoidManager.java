package com.UKC_AICS.simulation.managers;

import java.util.HashMap;
import java.util.Random;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.WorldObject;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 *
 * @author Emily
 */
public class BoidManager extends Manager {


    private Array<Boid> boids = new Array<Boid>();
    private QuadTree quadtree;

    
    //TEMP: each boid type will have its own sight radius.
    static final float radius = 50f;
    private Vector3 tmpVec = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String,Behaviour>();

    public BoidManager () {

        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());
    }
    
    
    public void createBoid(){
        Boid boid = new Boid();
        
        boid.setPosition(rand.nextInt(555), rand.nextInt(555), 0);
        boid.setOrientation(10, 10, 0);
        boid.setVelocity(randomVel());
        
        boids.add(boid);
        quadtree.insert(boid);
        
    }
    private Vector3 randomVel() {
        Vector3 vel = new Vector3(rand.nextFloat(),rand.nextFloat(), rand.nextFloat());
        return vel;
    }

    
    /**
     * called by the update in SimulationManager
     * 
     * this will loop through the boids and update them
     * 
     */
    public void update() {
    	rebuildTree(boids);
        //loop through boids and ask them to do their thing.
        Boid boid;
        for(int i = 0; i < boids.size; i++) {
            boid = boids.get(i);
//        for(Boid boid : boids) {
    		// find relevant boids
            Array<Boid> nearBoids = quadtree.retrieveBoidsInRadius(boid.getPosition(), radius);


            for(Boid b : nearBoids) {
            }

    		//crudely ask each one if it's inside the radius
    		float coh = SimulationManager.tempSpeciesData.get("zebra").get("cohesion");
            float ali = SimulationManager.tempSpeciesData.get("zebra").get("alignment");
            float sep = SimulationManager.tempSpeciesData.get("zebra").get("separation");
            float wan = SimulationManager.tempSpeciesData.get("zebra").get("wander");
    		//do stuff
            tmpVec.set(0f,0f,0f);

            tmpVec.add(behaviours.get("cohesion").act(boids, new Array<WorldObject>(), boid).scl(coh));
            tmpVec.add(behaviours.get("alignment").act(boids, new Array<WorldObject>(), boid).scl(ali));
            tmpVec.add(behaviours.get("separation").act(boids, new Array<WorldObject>(), boid).scl(sep));
            tmpVec.add(behaviours.get("wander").act(boids, new Array<WorldObject>(), boid).scl(wan));

            boid.move(tmpVec);

    	}
    }

    public void rebuildTree(Array<Boid> boids) {
        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        for(Boid boid : boids)
            quadtree.insert(boid);
    }
    public Array<Boid> getBoids() {
        return (Array<Boid>) boids;
    }
}
