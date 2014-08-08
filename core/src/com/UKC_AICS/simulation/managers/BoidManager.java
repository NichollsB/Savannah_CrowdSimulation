package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.UKC_AICS.simulation.utils.BoidGridOld;
import com.UKC_AICS.simulation.utils.MathsUtils;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;


/**
 * @author Emily
 */
public class BoidManager extends Manager {


    public static Array<Boid> boids = new Array<Boid>();
    public static Array<Boid> removalBoids = new Array<Boid>();
    public static Array<Boid> additionBoids = new Array<Boid>();
    private static BoidGrid boidGrid;

    public final SimulationManager parent;

    private QuadTree quadtree;
    private Random rand = new Random();
    private static StateMachine stateMachine;


    public BoidManager(SimulationManager parent) {

        this.parent = parent;
        setBoidGrid(new BoidGrid(60, Constants.mapWidth, Constants.mapHeight));

        quadtree = new QuadTree(0, new Rectangle(0, 0, Constants.mapWidth, Constants.mapHeight));

        stateMachine = new StateMachine(this);
    }

    /**
     * This createBoid is for StaXParseLoader
     * @param species
     * @param age
     * @param bDay
     * @param pX
     * @param pY
     * @param pZ
     * @param vX
     * @param vY
     * @param vZ
     * @param wander 
     * @param alignment 
     * @param separation 
     * @param cohesion 
     */
    public static void createBoid(byte species, byte group, int age, int bDay, float pX, float pY, float pZ, float vX, float vY, float vZ,
    		float cohesion, float separation, float alignment, float wander, float flockRadius, float sightRadius, float nearRadius, float hunger, float thirst, float panic) {
        Boid boid = new Boid(species);

        boid.setAge(age);

        boid.setPosition(pX, pY, pZ);
        boid.setVelocity(vX, vY, vZ);
        boid.setCohesion(cohesion);
        boid.setAlignment(alignment);
        boid.setSpearation(separation);
        boid.setWander(wander);
        boid.setGene(cohesion, separation, alignment, wander);
        boid.setGroup(group);
        boid.setFlockRadius(flockRadius);
        boid.setSightRadius(sightRadius);
        boid.setNearRadius(nearRadius);
        boid.setHunger(hunger);
        boid.setPanic(panic);
        boid.setThirst(thirst);
        addToLists(boid);
    }
    
    /**
     * Created by Ben Nicholls
     * custom constructor for the creation of a new boid from a Species type, but with custom position and group
     * @param species
     */
    public void createBoid(Species species, byte group, int x, int y){
    	Boid boid = new Boid(species);
    	boid.setPosition(new Vector3(x, y, 0));
    	
        int maxXVel = 1;
        int maxYVel = 1;
    	
        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);

        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);
        boid.setVelocity(xVel, yVel, 0);
        //random start age
        boid.age = rand.nextInt((int) species.getLifespan()/2); //dont want the starting population to be too old.

        boid.tertiaryType = group;
        boid.setTracked(true);
        
        addToLists(boid);
    }

    /**
     * create boid from the species file and add straight into the lists
     *
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

        boid.setPosition(xPos, yPos, 0);
        boid.setVelocity(xVel, yVel, 0);

        boid.hunger = rand.nextInt(80);
        boid.thirst = rand.nextInt(80);

        //random start age
        boid.age = rand.nextInt((int) species.getLifespan()/2); //dont want the starting population to be too old.


        boid.setGroup((byte)rand.nextInt(10));

        if(boid.age> species.getMaturity()) {
            boid.size = species.getMaxSize() + rand.nextInt(10) - 5;
        } else {
            //TODO: add in the size for an age 3 boid over age 0, as right now the size is set to newborn size even if they're only a day away from maturity
            float growthPerDay = (species.getMaxSize() - species.getNewbornSize()) / species.getMaturity();
            boid.size = species.getNewbornSize() + boid.age * growthPerDay;
        }


        //moved this to boid constructor.
//        boid.setCohesion(species.getCohesion());
//        boid.setAlignment(species.getAlignment());
//        boid.setSpearation(species.getSeparation());
//        boid.setWander(species.getWander());
//        boid.setGene(species.getCohesion(), species.getAlignment(), species.getSeparation(), species.getWander());
        addToLists(boid);
    }

    /**
     * create a copy of a boid and add it straight to the lists..
     *
     * @param oldBoid
     * @return
     */
    public Boid createBoid(Boid oldBoid) {
        Boid boid = new Boid(oldBoid);
        addToLists(boid);
        return boid;

    }

    private static void addToLists(Boid boid) {
        boids.add(boid);
        getBoidGrid().addBoid(boid);
        stateMachine.addBoid(boid);
    }

    /**
     * removes all the boids from the boidGrid one-by-one TODO: make a method in boidGrid to do this better.
     * <p/>
     * and clears the boids list.
     */
    public void clearBoidList() {

        for (Boid boid : boids) {
            getBoidGrid().removeBoid(boid);
            stateMachine.removeBoid(boid);
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
     *
     * @param dayIncrement
     */
    public void update(boolean dayIncrement) {
//        rebuildTree(boids);

        //do all of the boids updates.
        stateMachine.update();

        //then actually move the boids.
        Boid boid;
        for (int i = 0; i < boids.size; i++) {
            boid = boids.get(i);
            //apply movement to it.
            boid.move();

            if (checkForDeath(boid)) {
                continue;
            } else {
                getBoidGrid().update(boid);
            }
        }

        //do aging here, purely on a day increment basis, so we'll see swathes of death everytime the day increments.
        //boids born an hour before midnight will still age a day.
        if (dayIncrement) {
            updateAges();
        }

        //handle the addition and removal of boids here, this does mean that the boid will have potentially been interacted
        // with in the current update tick.
        while (removalBoids.size > 0) {
            removeBoid(removalBoids.pop());
        }
        while(additionBoids.size > 0){
            addToLists(additionBoids.pop());
        }
    }

    public boolean checkForDeath(final Boid boid) {
        float lifespan = SimulationManager.speciesData.get(boid.getSpecies()).getLifespan() + MathsUtils.randomNumber(-10, 10);
        if (boid.hunger >= 120) {
            Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f));
            WorldManager.putObject(food);
            removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of hunger :( ");
            return true;
        }
        else if( boid.thirst >= 110) {
            Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f));
            WorldManager.putObject(food);
            boids.removeValue(boid, false);
            getBoidGrid().removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of thirst :( ");
            return true;
        }
        else if (boid.age > lifespan) {
            Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f));
            WorldManager.putObject(food);
            removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of age related issues :( ");
            return true;
        }
        return false;
    }


    public void updateAges() {
        for (Boid b : boids) {
            b.age();
        }
    }
    public void storeBoidForRemoval(Boid boid) {
        if(!removalBoids.contains(boid, false)) {
            removalBoids.add(boid);
        }
    }

    public void storeBoidForRemoval(Boid boid, Object food) {
        if(!removalBoids.contains(boid, false)) {
            WorldManager.putObject(food);
            removalBoids.add(boid);
        }
    }

    public void storeBoidForAddition(Boid boid) {
        if(!additionBoids.contains(boid, false)) {
            additionBoids.add(boid);
        }
    }
    public void removeBoid(Boid boid) {

        boids.removeValue(boid, false);
        getBoidGrid().removeBoid(boid);
        stateMachine.removeBoid(boid);
    }

    public Array<Boid> getBoids() {
        return boids;
    }

    public static BoidGrid getBoidGrid() {
        return boidGrid;
    }

    public static void setBoidGrid(BoidGrid boidGrid) {
        BoidManager.boidGrid = boidGrid;
    }

    public Boid getBoidAt(int screenX, int screenY) {
        return boidGrid.findBoidAt(screenX, screenY);
    }
    //TODO - Work in progress - Matt
    public int trackPop(byte current) {
    	int population = 0;
    	for(Boid b : boids){
    		if(b.getSpecies()==current){
    			population++;
    		}
    	}
		return population;
    }
}
