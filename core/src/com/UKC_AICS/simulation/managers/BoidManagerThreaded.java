package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
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
public class BoidManagerThreaded extends Manager {


    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    //    private static ExecutorService executorServiceTwo = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    private boolean isRecalculating = false;
    final static java.lang.Object lock = new java.lang.Object();
    final static java.lang.Object lockUpdate = new java.lang.Object();
    final static java.lang.Object lockMove = new java.lang.Object();

    private HashMap<Boid, Runnable> moveRunnables = new HashMap<Boid, Runnable>();

    private CopyOnWriteArraySet<Boid> updatedBoids = new CopyOnWriteArraySet<Boid>();
    private CopyOnWriteArraySet<Boid> movedBoids = new CopyOnWriteArraySet<Boid>();


    private static final float FLOCK_RADIUS = 100f;
    private static final float SEP_RADIUS = 20f;
    private Array<Boid> boids = new Array<Boid>();
    private QuadTree quadtree;


    //TEMP: each boid type will have its own sight radius.
    private Vector3 steering = new Vector3();
    private Random rand = new Random();

    private HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();


    private BoidGrid boidGrid;


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

                Species speciesData = SimulationManager.speciesData.get(species);

                coh = speciesData.getCohesion();
                ali = speciesData.getAlignment();
                sep = speciesData.getSeparation();
                wan = speciesData.getWander();

                //post boids calculation task
                Runnable runnableCalculate = createCalcRunnable(boid, species, coh, sep, ali, wan);
                executorService.submit(runnableCalculate);

                //post boids move task.
                Runnable runnableMove = createMoveRunnable(boid);

                moveRunnables.put(boid, runnableMove);
            }

//            System.out.println("All runnables created!");
            isRecalculating = true;
            lock.notifyAll();
        }
        synchronized (lockUpdate) {
            while (!allBoidsUpdated()) {
                try {
                    isRecalculating = true;
//                    System.out.println("All boids have calculated is waiting");
                    lockUpdate.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            isRecalculating = false;
//            System.out.println("All boids have calculated has notified");
            lockUpdate.notifyAll();

        }

        synchronized (lockMove) {
            while (!allBoidsMoved()) {
                try {
//                    System.out.println("All boids have moved is waiting");
                    lockMove.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
//            System.out.println("UPDATE CYCLE COMPLETE");
//            notifyAll();
        }
    }

    private boolean allBoidsUpdated() {
        boolean bool = updatedBoids.size() == boids.size;
        if (bool == true) {
            lockUpdate.notifyAll();
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


    private Runnable createCalcRunnable(final Boid boid, final byte species, final float coh, final float sep, final float ali, final float wan) {

        Runnable runnableCalculate;
        runnableCalculate = new Runnable() {

            @Override
            public void run() {
                synchronized (lock) {
                    while (!isRecalculating && updated(boid)) {
                        try {
//                            System.out.println(boid + " calc is waiting");
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (lockUpdate) {
                        while (updated(boid)) {
                            try {
                               lockUpdate.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Array<Boid> nearBoids = new Array<Boid>();
                            Array<Boid> closeBoids = new Array<Boid>();
                            /*
                                    CELL LOOKUP.
                             */
                            nearBoids = boidGrid.findNearby(boid.getPosition());
                            Boid anotherBoid;
                            if (nearBoids.size > 0) {
                                for (int i = 0; i < nearBoids.size; i++) {
                                    anotherBoid = nearBoids.get(i);
                                    steering.set(boid.getPosition());
                                    steering.sub(anotherBoid.getPosition());
                                    if (steering.len() > FLOCK_RADIUS) {
                                        if (nearBoids.size <= 0) {
                                            System.out.println("there are no near boids! ~> " + nearBoids.size);
                                        } else {
                                            nearBoids.removeValue(anotherBoid, true);
                                        }
                                    }
                                    //if the boid is outside the flock radius it CANT also be in the "too close" range
                                    else if (steering.len() < SEP_RADIUS) {
                                        closeBoids.add(anotherBoid);
                                    }
                                }
                            } else {
                                System.out.println("there are no near boids!");
                            }


                            //do stuff
                            steering.set(0f, 0f, 0f);

                            Array<Entity> dummyObjects = new Array<Entity>();

                            if (boid.equals(null) || nearBoids.size <= 0 || closeBoids.size <= 0) {
                                System.out.println("SOmething is null!!!");
                            }

                            steering.add(behaviours.get("cohesion").act(nearBoids, dummyObjects, boid).scl(coh));
                            steering.add(behaviours.get("alignment").act(nearBoids, dummyObjects, boid).scl(ali));
                            steering.add(behaviours.get("separation").act(closeBoids, dummyObjects, boid).scl(sep));
                            steering.add(behaviours.get("wander").act(nearBoids, dummyObjects, boid).scl(wan));

                            boid.setAcceleration(steering);
                            updatedBoids.add(boid);
//                            System.out.println(boid.toString() + " has calculated");

                            executorService.submit(moveRunnables.remove(boid));


                            lockUpdate.notifyAll();
                            //                        lockMove.notifyAll();
                        } catch (Exception ex) {
                            ex.printStackTrace();

                        } finally {
                            lockUpdate.notifyAll();
                        }
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
                synchronized (lockUpdate) {
                    while (isRecalculating) { //&& !allBoidsUpdated() && moved(boid)
                        try {
//                            System.out.println(boid + " move is waiting");
                            lockUpdate.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    synchronized (lockMove) {
                        while (moved(boid)) {
                            try {
                                lockMove.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        boid.move();
                        //tell the grid to update its position.
                        boidGrid.update(boid);
                        movedBoids.add(boid);

//                        System.out.println(boid.toString() + " has moved");

                        lockMove.notifyAll();
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
