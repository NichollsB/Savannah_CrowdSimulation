package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Emily
 */
public class BoidManagerThreadedThree extends Manager {



    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    private HashMap<Boid, Runnable> moveRunnables = new HashMap<Boid, Runnable>();

    private CopyOnWriteArraySet<Boid> updatedBoids = new CopyOnWriteArraySet<Boid>();
    private CopyOnWriteArraySet<Boid> movedBoids = new CopyOnWriteArraySet<Boid>();

    private Object lock = new Object();


    private final SimulationManager parent;

    private Array<Boid> boids = new Array<Boid>();
    private QuadTree quadtree;


    //TEMP: each boid type will have its own sight radius.
    private Vector3 steering = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();


    private BoidGrid boidGrid;

    public BoidManagerThreadedThree(SimulationManager parent) {
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

        int maxXVel = 1;
        int maxYVel = 1;


        int xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        int yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;

        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);
        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);

        boid.setBirthDay(SimulationManager.getDay());

        boid.setPosition(xPos, yPos, 0);
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

    private boolean allBoidsUpdated() {
        boolean bool = updatedBoids.size() == boids.size;
        if (bool == true) {
            lock.notifyAll();
        }
        return bool;
    }

    private boolean updated(Boid boid) {
        boolean bool = updatedBoids.contains(boid);
        return bool;
    }

    private boolean allBoidsMoved() {
        boolean bool = movedBoids.size() == boids.size;
        return bool;
    }

    private boolean moved(Boid boid) {
        boolean bool = movedBoids.contains(boid);
        return bool;
    }

    public void resetThreadLists() {
        updatedBoids.clear();
        movedBoids.clear();
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
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

        //TODO: Look into making this so it can be threaded.
        resetThreadLists();

        calculateAllBoids();



        synchronized (lock) {
            while(!allBoidsUpdated()) {
                try{
                    lock.wait(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            moveAllBoids();
            lock.notifyAll();
        }

        synchronized (lock){
            while (!allBoidsMoved()){
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notify(); //not needed??
            System.out.println("DONNNEEE!!!");
        }
    }

    private void calculateAllBoids() {
        synchronized (lock) {
            float coh;
            float ali;
            float sep;
            float wan;
            Boid boid;
            for (int i = 0; i < boids.size; i++) {
                boid = boids.get(i);
                byte species = boid.getSpecies();

                Species speciesData = SimulationManager.speciesData.get(species);

                coh = speciesData.getCohesion();
                ali = speciesData.getAlignment();
                sep = speciesData.getSeparation();
                wan = speciesData.getWander();

                //post boids calculation task
                Runnable runnableCalculate = createCalcRunnable(boid, species, coh, sep, ali, wan);
                executorService.submit(runnableCalculate);
            }
            System.out.println("Posted all boids' calc runnables ");
            lock.notifyAll();
        }
    }

    private void moveAllBoids() {
        for (Boid boid : boids) {
            Runnable r = moveRunnables.get(boid);
            if(r == null) {
                System.out.println("STUPID NO MOVE RUNNABLE");
                if(updated(boid)) {
                    System.out.println("BUT I UPDATED");
                }
            } else {
                executorService.submit(r);
            }
        }
        System.out.println("Posted all boids' move runnables ");
    }

    private Runnable createCalcRunnable(final Boid boid, final byte species, final float coh, final float sep, final float ali, final float wan) {

        Runnable runnableCalculate;
        runnableCalculate = new Runnable() {

            @Override
            public void run() {
                while (updated(boid)) {
                    try {
                        System.out.println(boid + " calc is waiting");
//                            lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {

                    // find relevant boids

            /*
            * CELL  ATTEMPTS.
            */

                    Array<Boid> nearBoids = boidGrid.findNearby(boid.getPosition());
                    Array<Boid> closeBoids = new Array<Boid>();

                    Boid b;
                    for (int i = 0; i < boids.size; i++) {
                        b = boids.get(i);
                        steering.set(boid.getPosition());
                        steering.sub(b.getPosition());
                        if (steering.len() > boid.flockRadius) {
                            nearBoids.removeValue(b, false);
                        }
                        //if the boid is outside the flock radius it CANT also be in the "too close" range
                        else if (steering.len() < boid.nearRadius) {
                            closeBoids.add(b);
                        }
                    }

                    //find objects nearby
                    Array<Entity> dummyObjects = parent.getObjectsNearby(new Vector2(boid.getPosition().x, boid.getPosition().y));
                    for (Entity ent : dummyObjects) {
//                if (ent.getPosition().dst2(boid.getPosition()) > boid.sightRadius) { //old attempt
                        steering.set(boid.position);
                        steering.sub(ent.position);
                        if (steering.len() > boid.sightRadius) {
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

                    //TODO: add these behaviours in properly.
                    steering.add(behaviours.get("repeller").act(nearBoids, dummyObjects, boid).scl(0.5f));
                    steering.add(behaviours.get("attractor").act(nearBoids, dummyObjects, boid).scl(0.5f));

                    boid.setAcceleration(steering);
                    updatedBoids.add(boid);
                    System.out.println(boid.toString() + " has calculated");

                    Runnable r = createMoveRunnable(boid);
                    moveRunnables.put(boid, r);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        return runnableCalculate;

    }

    private Runnable createMoveRunnable(final Boid boid) {

        Runnable runnableMove = new Runnable() {
            @Override
            public void run() {
                    boid.move();
                    //tell the grid to update its position.
                    boidGrid.update(boid);
                    movedBoids.add(boid);
                    System.out.println(boid.toString() + " has moved");
            }
        };
        return runnableMove;
    }
    	
    public void updateAge(){
    	for(Boid b : boids){
            int bday = b.getBirthDay();
            int day = SimulationManager.getDay();
            int newAge = bday + (day - bday);
            b.setAge(newAge);
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
