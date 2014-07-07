package com.UKC_AICS.simulation.managers;

import java.util.HashMap;
import java.util.Random;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 * @author Emily
 */
public class BoidManager extends Manager {



    private final SimulationManager parent;

    private Array<Boid> boids = new Array<Boid>();
    private QuadTree quadtree;
    

    //TEMP: each boid type will have its own sight radius.
    private Vector3 steering = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();


    private BoidGrid boidGrid;

    public BoidManager(SimulationManager parent) {

        this.parent = parent;
        boidGrid = new BoidGrid(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());
        behaviours.put("attractor", new Attractor());
        behaviours.put("repeller", new Repeller());

    }


    public void createBoid(byte species) {
        Boid boid = new Boid(species);

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

        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);

        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);


        boid.setBirthDay(SimulationManager.getDay());

        boid.setPosition(xPos, yPos, 0);
//        boid.setOrientation(xOrient, yOrient, 0);
        boid.setVelocity(xVel, yVel, 0);

        boids.add(boid);
//        quadtree.insert(boid);
        boidGrid.addBoid(boid);
    }

    /**
     * create boid from the species file.
     * @param species
     */
    public void createBoid(Species species) {
        Boid boid = new Boid(species);

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

        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);

        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);


        boid.setBirthDay(SimulationManager.getDay());

        boid.setPosition(xPos, yPos, 0);
//        boid.setOrientation(xOrient, yOrient, 0);
        boid.setVelocity(xVel, yVel, 0);

        boids.add(boid);
//        quadtree.insert(boid);
        boidGrid.addBoid(boid);
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
//        rebuildTree(boids);
        //loop through boids and ask them to do their thing.

        //TODO: Look into making this so it can be threaded.

        Boid boid;
        for (int i = 0; i < boids.size; i++) {
            boid = boids.get(i);
            byte species = boid.getSpecies();

            // find relevant boids

            Array<Boid> nearBoids = new Array<Boid>();
            Array<Boid> closeBoids = new Array<Boid>();
/*
 SIMPLE LOOPING. HORRIBLE! :(
 */
//            for (Boid b : boids) {
//                if (boid != b) {
//                    steering.set(boid.getPosition());
//                    steering.sub(b.getPosition());
//                    if (steering.len() < FLOCK_RADIUS) {
//                        if (!nearBoids.contains(b, true)) {
//                            nearBoids.add(b);
//                        }
//                    } else {
//                        if (nearBoids.contains(b, true)) {
//                            nearBoids.removeValue(b, true);
//                        }
//                    }
//                    if (steering.len() < SEP_RADIUS) {
//                        closeBoids.add(b);
//                    }
//                }
//            }

            /*
             QUADTREE ATTEMPTS.
             */
//            nearBoids = quadtree.retrieveBoidsInRadius(boid.getPosition(), FLOCK_RADIUS);
////            For use with the quadtree lookup
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


            /*
            * CELL  ATTEMPTS.
            */

            nearBoids = boidGrid.findNearby(boid.getPosition());
            for (Boid b : nearBoids) {
                steering.set(boid.getPosition());
                steering.sub(b.getPosition());
                if (steering.len() > boid.flockRadius) {
                    nearBoids.removeValue(b, true);
                }
                //if the boid is outside the flock radius it CANT also be in the "too close" range
                else if (steering.len() < boid.nearRadius) {
                    closeBoids.add(b);
                }
            }

            //find objects nearby
            Array<Entity> dummyObjects = parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));
            for (Entity ent : dummyObjects) {
//                if (ent.getPosition().dst2(boid.getPosition()) > boid.sightRadius) {
                steering.set(boid.position);
                steering.sub(ent.position);
                if (steering.len() > boid.sightRadius) {
                    dummyObjects.removeValue(ent, false);
                }
            }

//            float coh = SimulationManager.tempSpeciesData.get(SimulationManager.speciesByte.get(species)).get("cohesion");
//            float ali = SimulationManager.tempSpeciesData.get(SimulationManager.speciesByte.get(species)).get("alignment");
//            float sep = SimulationManager.tempSpeciesData.get(SimulationManager.speciesByte.get(species)).get("separation");
//            float wan = SimulationManager.tempSpeciesData.get(SimulationManager.speciesByte.get(species)).get("wander");

            float coh = SimulationManager.speciesData.get(boid.getSpecies()).getCohesion();
            float sep = SimulationManager.speciesData.get(boid.getSpecies()).getSeparation();
            float ali = SimulationManager.speciesData.get(boid.getSpecies()).getAlignment();
            float wan = SimulationManager.speciesData.get(boid.getSpecies()).getWander();

            //do stuff
            steering.set(0f, 0f, 0f);

            steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
            steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
            steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
            steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

            //TODO: add these behaviours in properly.
            steering.add(behaviours.get("repeller").act(nearBoids, dummyObjects, boid).scl(0.5f));
            steering.add(behaviours.get("attractor").act(nearBoids, dummyObjects, boid).scl(0.5f));

            // NaN check
//            if (steering.x != steering.x) {
//                System.out.println("blerpy");
//            }


            //store the steering movement
            boid.setAcceleration(steering);
            //apply it.
            boid.move();
            //tell the grid to update its position.
            boidGrid.update(boid);
        }
    }
    
    
    	
    	
    public void updateAge(){
    	for(Boid b : boids){
            int bday = Boid.getBirthDay();
            int day = SimulationManager.getDay();
            int newAge = bday + (day - bday);
            Boid.setAge(newAge);
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
