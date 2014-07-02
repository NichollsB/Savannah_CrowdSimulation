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
 * @author Emily
 */
public class BoidManager extends Manager {


    private static final float FLOCK_RADIUS = 100f;
    private static final float SEP_RADIUS = 20f;
    private Array<Boid> boids = new Array<Boid>();
    private QuadTree quadtree;


    //TEMP: each boid type will have its own sight radius.
    private Vector3 steering = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();


    public BoidManager() {

        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());

    }


    public void createBoid() {
        Boid boid = new Boid();

        int maxXPos = 1180;
        int minXPos = 100;

        int maxYPos = 620;
        int minYPos = 100;

        int maxXOrient = 10;


        int maxYOrient = 10;


        int maxXVel = 1;


        int maxYVel = 1;


        int xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        int yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;

        int xOrient = (rand.nextInt(2 * maxXOrient) - maxXOrient);

        int yOrient = (rand.nextInt(2 * maxYOrient) - maxYOrient);

        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);

        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);


        boid.setPosition(xPos, yPos, 0);
        boid.setOrientation(xOrient, yOrient, 0);
        boid.setVelocity(xVel, yVel, 0);

        boids.add(boid);
        quadtree.insert(boid);

    }
    
    public void clearBoidList() {
    
    	boids.clear();
    
    	
    }

    private Vector3 randomVel() {
        Vector3 vel = new Vector3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        return vel;
    }


    /**
     * called by the update in SimulationManager
     * <p/>
     * this will loop through the boids and update them
     */
    public void update() {
        rebuildTree(boids);
        //loop through boids and ask them to do their thing.
        Boid boid;
        for (int i = 0; i < boids.size; i++) {
            boid = boids.get(i);
//        for(Boid boid : boids) {
            // find relevant boids
/*
 SIMPLE LOOPING. HORRIBLE! :(
 */
            Array<Boid> nearBoids = new Array<Boid>();
            Array<Boid> closeBoids = new Array<Boid>();
            for (Boid b : boids) {
                if (boid != b) {
                    steering.set(boid.getPosition());
                    steering.sub(b.getPosition());
                    if (steering.len() < FLOCK_RADIUS) {
                        if (!nearBoids.contains(b, true)) {
                            nearBoids.add(b);
                        }
                    } else {
                        if (nearBoids.contains(b, true)) {
                            nearBoids.removeValue(b, true);
                        }
                    }
                    if (steering.len() < SEP_RADIUS) {
                        closeBoids.add(b);
                    }
                }
            }

            /*
             QUADTREE ATTEMPTS.
             */
//            Array<Boid> nearBoids = quadtree.retrieveBoidsInRadius(boid.getPosition(), FLOCK_RADIUS);
            //For use with the quadtree lookup
//            for(Boid b : nearBoids) {
//                steering.set(boid.getPosition());
//                steering.sub(b.getPosition());
//                if (steering.len() > FLOCK_RADIUS) {
//                    nearBoids.removeValue(b, true);
//                }
//                //if the boid is outside the flock radius it CANT be in the "too close" range
//                else if (steering.len() < SEP_RADIUS) {
//                    closeBoids.add(b);
//                }
//
//            }

            //crudely ask each one if it's inside the radius
            float coh = SimulationManager.tempSpeciesData.get("zebra").get("cohesion");
            float ali = SimulationManager.tempSpeciesData.get("zebra").get("alignment");
            float sep = SimulationManager.tempSpeciesData.get("zebra").get("separation");
            float wan = SimulationManager.tempSpeciesData.get("zebra").get("wander");
            //do stuff
            steering.set(0f, 0f, 0f);

            Array<WorldObject> dummyObjects = new Array<WorldObject>();
            steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
            steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
            steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
            steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

            Seek s = new Seek();
            s.act(nearBoids,dummyObjects, boid);

            boid.move(steering);

        }
    }

    public void rebuildTree(Array<Boid> boids) {
        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        for (Boid boid : boids)
            quadtree.insert(boid);
    }

    public Array<Boid> getBoids() {
        return (Array<Boid>) boids;
    }
}
