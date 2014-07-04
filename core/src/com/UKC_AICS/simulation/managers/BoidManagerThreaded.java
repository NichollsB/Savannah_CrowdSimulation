package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.UKC_AICS.simulation.utils.Species;
import com.UKC_AICS.simulation.world.BoidGrid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Emily
 */
public class BoidManagerThreaded extends Manager {


    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    //    private static ExecutorService executorServiceTwo = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    private boolean isRecalculating = true;
    final static java.lang.Object lock = new java.lang.Object();
    private java.lang.Object lockUpdate = new java.lang.Object();
    private java.lang.Object lockMove = new java.lang.Object();

    private static final float FLOCK_RADIUS = 100f;
    private static final float SEP_RADIUS = 20f;
    private Array<Boid> boids = new Array<Boid>();
    private QuadTree quadtree;


    //TEMP: each boid type will have its own sight radius.
    private Vector3 steering = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();


    private BoidGrid boidGrid;

    private Set<Boid> updatedBoids = new java.util.concurrent.CopyOnWriteArraySet();
    private Set<Boid> movedBoids = new java.util.concurrent.CopyOnWriteArraySet();

    public BoidManagerThreaded() {

        boidGrid = new BoidGrid(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        behaviours.put("separation", new Separation());
        behaviours.put("alignment", new Alignment());
        behaviours.put("cohesion", new Cohesion());
        behaviours.put("wander", new Wander());

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

        int xOrient = (rand.nextInt(2 * maxXOrient) - maxXOrient);

        int yOrient = (rand.nextInt(2 * maxYOrient) - maxYOrient);

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

    public void resetThreadLists() {
        updatedBoids.clear();
        movedBoids.clear();
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
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

        resetThreadLists();

        Boid boid;
        float coh;
        float ali;
        float sep;
        float wan;

//        Thread calculationThread = new Thread("calculations");

        synchronized (lock) {

            for (int i = 0; i < boids.size; i++) {
                boid = boids.get(i);
                byte species = boid.getSpecies();

                Species speciesData = SimulationManager.newSpecieData.get(species);

                coh = speciesData.getCohesion();
                ali = speciesData.getAlignment();
                sep = speciesData.getSeparation();
                wan = speciesData.getWander();


                Runnable runnableCalculate = createCalcRunnable(boid, species, coh, sep, ali, wan);
                executorService.submit(runnableCalculate);

                Runnable runnableMove = createMoveRunnable(boid);
                executorService.submit(runnableMove);
            }
        }
        synchronized (lockUpdate) {
            if (!allBoidsUpdated()) {
                isRecalculating = true;
            } else {
                isRecalculating = false;
            }
        }

        synchronized (lockUpdate) {
            if (!allBoidsMoved()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                executorService.shutdown();
                System.out.println("UPDATE CYCLE COMPLETE");
            }
        }
    }

    private boolean allBoidsUpdated() {
        return updatedBoids.size() == boids.size;
    }

    private boolean updated(Boid boid) {
        return updatedBoids.contains(boid);
    }

    private boolean allBoidsMoved() {
        return movedBoids.size() == boids.size;
    }

    private boolean moved(Boid boid) {
        return movedBoids.contains(boid);
    }


    private Runnable createCalcRunnable(final Boid boid, final byte species, final float coh, final float sep, final float ali, final float wan) {

        Runnable runnableCalculate = new Runnable() {

            @Override
            public void run() {
                if (!isRecalculating && updated(boid)) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Array<Boid> nearBoids;
                        Array<Boid> closeBoids = new Array<Boid>();
                    /*
                            CELL LOOKUP.
                     */
                        nearBoids = boidGrid.findNearby(boid.getPosition());
                        Boid anotherBoid;
                        for (int i = 0; i < nearBoids.size; i++) {
                            anotherBoid = nearBoids.get(i);
                            steering.set(boid.getPosition());
                            steering.sub(anotherBoid.getPosition());
                            if (steering.len() > FLOCK_RADIUS) {
                                if (nearBoids == null || nearBoids.size <= 0) {
                                    System.out.println("near boids is none existent!");
                                }
                                nearBoids.removeValue(anotherBoid, true);
                            }
                            //if the boid is outside the flock radius it CANT also be in the "too close" range
                            else if (steering.len() < SEP_RADIUS) {
                                closeBoids.add(anotherBoid);
                            }
                        }


                        //do stuff
                        steering.set(0f, 0f, 0f);

                        Array<Object> dummyObjects = new Array<Object>();
                        steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
                        steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
                        steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
                        steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

                        boid.setAcceleration(steering);
                        updatedBoids.add(boid);
                        System.out.println(boid.toString() + " has calculated");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        return runnableCalculate;

    }

    private Runnable createMoveRunnable(final Boid boid) {

        Runnable runnableMove = new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    if (allBoidsUpdated() && !moved(boid)) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        boid.move();
                        //tell the grid to update its position.
                        boidGrid.update(boid);
                        movedBoids.add(boid);
                        System.out.println(boid.toString() + " has moved");
                    }
                }
            }
        };

        return runnableMove;
    }


    public void updateAge() {
        for (Boid b : boids) {
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
