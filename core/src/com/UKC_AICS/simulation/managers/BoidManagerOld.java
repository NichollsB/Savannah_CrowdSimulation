package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.UKC_AICS.simulation.utils.MathsUtils;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;


/**
 * @author Emily
 */
public class BoidManagerOld extends Manager {



    public static Array<Boid> boids = new Array<Boid>();
    private static BoidGrid boidGrid;
    public final SimulationManager parent;
    private QuadTree quadtree;
    private Vector3 steering = new Vector3();
    private Random rand = new Random();
    public HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();

    public BoidManagerOld(SimulationManager parent) {

        this.parent = parent;
        setBoidGrid(new BoidGrid(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());
        
        behaviours.put("attractor", new Attractor());
        behaviours.put("repeller", new Repeller());

    }
    
    
    public static void createBoid(byte species, int age ,int bDay ,float pX ,float pY ,float pZ ,float vX ,float vY ,float vZ) {
    	Boid boid = new Boid(species);
    	
    	boid.setAge(age);
    	boid.setBirthDay(bDay);
    	boid.setPosition(pX,pY,pZ);
    	boid.setVelocity(vX,vY,vZ);
    	
    	boids.add(boid);
//      quadtree.insert(boid);
     	getBoidGrid().addBoid(boid);
    }

    public static BoidGrid getBoidGrid() {
        return boidGrid;
    }

    public static void setBoidGrid(BoidGrid boidGrid) {
        BoidManagerOld.boidGrid = boidGrid;
    }


//    public void createBoid(byte species) {
//        Boid boid = new Boid(species);
//
//        int maxXPos = 1180;
//        int minXPos = 100;
//
//        int maxYPos = 620;
//        int minYPos = 100;
//
//        int maxXOrient = 10;
//        int maxYOrient = 10;
//
//
//        int maxXVel = 1;
//        int maxYVel = 1;
//
//
//        int xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
//        int yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
//
//        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);
//
//        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);
//
//
//        boid.setBirthDay(SimulationManager.getDay());
//
//        boid.setPosition(xPos, yPos, 0);
////        boid.setOrientation(xOrient, yOrient, 0);
//        boid.setVelocity(xVel, yVel, 0);
//
//        boids.add(boid);
////        quadtree.insert(boid);
//        boidGrid.addBoid(boid);
//    }

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


//        boid.setBirthDay(SimulationManager.getDay());
//        boid.setOrientation(xOrient, yOrient, 0);

        boid.setPosition(xPos, yPos, 0);
        boid.setVelocity(xVel, yVel, 0);

        boid.hunger = rand.nextInt(120) + 20;
        boid.thirst = rand.nextInt(150) + 50;
        //random start age
        boid.age = rand.nextInt((int)species.getLifespan());

        boids.add(boid);
//        quadtree.insert(boid);
        getBoidGrid().addBoid(boid);
    }

    /**
     * create a copy of a boid.
     * @param oldBoid
     * @return
     */
    public Boid createBoid(Boid oldBoid) {
        return new Boid(oldBoid);
    }

    /**
     * removes all the boids from the boidGrid one-by-one TODO: make a method in boidGrid to do this better.
     *
     * and clears the boids list.
     */
    public void clearBoidList() {

        for(Boid boid : boids){
            getBoidGrid().removeBoid(boid);
        }
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
     * @param dayIncrement
     */
    public void update(boolean dayIncrement) {
//        rebuildTree(boids);
        //loop through boids and ask them to do their thing.

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

            nearBoids = getBoidGrid().findNearby(boid.getPosition());
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
                if (steering.len2() > boid.sightRadius * boid.sightRadius) {
                    dummyObjects.removeValue(ent, false);
                }
            }

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


            steering.add(behaviours.get("repeller").act(nearBoids, dummyObjects, boid).scl(0.2f));
            steering.add(behaviours.get("attractor").act(nearBoids, dummyObjects, boid).scl(0.2f));

            // NaN check
//            if (steering.x != steering.x) {
//                System.out.println("blerpy");
//            }


            //store the steering movement
            boid.setAcceleration(steering);
            //apply it.
            boid.move();

            if(checkForDeath(boid)) {
                continue;
            } else {

                //tell the grid to update its position.
                getBoidGrid().update(boid);


            }
        }
        //do aging here, purely on a day increment basis, so we'll see swathes of death everytime the day increments.
        if (dayIncrement) {
            updateAges();
        }
    }

    public boolean checkForDeath(final Boid boid) {
        if( boid.hunger <= -20) {
            boids.removeValue(boid, false);
            getBoidGrid().removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of hunger :( ");
            return true;
        }
        else if( boid.thirst <= -10) {
            boids.removeValue(boid, false);
            getBoidGrid().removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of thirst :( ");
            return true;
        }
        float lifespan = SimulationManager.speciesData.get(boid.getSpecies()).getLifespan() + MathsUtils.randomNumber(-10, 10);
        if( boid.age > lifespan)  {
            boids.removeValue(boid, false);
            getBoidGrid().removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of age related issues :( ");
            return true;
        }
        return false;
    }


    public void updateAges(){
    	for(Boid b : boids){
            b.age++;
//            int bday = b.getBirthDay();
//            int day = SimulationManager.getDay();
//            int newAge = bday + (day - bday);
//            b.setAge(newAge);
    	}
    }
    
    
    

    public void rebuildTree(Array<Boid> boids) {
        quadtree = new QuadTree(0, new Rectangle(0, 0, Constants.screenWidth, Constants.screenHeight));
        for (Boid boid : boids)
            quadtree.insert(boid);
    }

    public Array<Boid> getBoids() {
        return boids;
    }
}
