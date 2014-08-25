package com.UKC_AICS.simulation.managers;

import EvolutionaryAlgorithm.EA2;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.states.State;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.UKC_AICS.simulation.utils.MathsUtils;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;
import java.util.Stack;


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
    static StateMachine stateMachine;
    public EA2 ea;


//    private boolean removeBoids = false;
    

    public BoidManager(SimulationManager parent, EA2 ea) {
    	this.ea = ea;
        this.parent = parent;
        setBoidGrid(new BoidGrid(80, Constants.mapWidth, Constants.mapHeight));
//        quadtree = new QuadTree(0, new Rectangle(0, 0, Constants.mapWidth, Constants.mapHeight));
        stateMachine = new StateMachine(this,ea);
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
    public void createBoid(byte spec, byte group, int age, int bDay, float pX, float pY, float pZ, float vX, float vY, float vZ,float cohesion, float separation, float alignment, float wander,
    		float sightRadius, float nearRadius, float flockRadius, float size, float hunger, float thirst, float panic, float stamina, float maxStamina, float hungerLevel, float thirstLevel, float panicLevel, String currentState, float fertility, String states, float offspring){
      
    	Boid boid = new Boid(spec);
        boid.setGroup(group);
        boid.setAge(age);
  
        boid.setPosition(pX, pY, pZ); 
        boid.setVelocity(vX, vY, vZ);
        
        boid.setCohesion(cohesion);
        boid.setAlignment(alignment);
        boid.setSpearation(separation);
        boid.setWander(wander);
       
        boid.setFlockRadius(flockRadius);
        boid.setSightRadius(sightRadius);
        boid.setNearRadius(nearRadius);
     
        boid.setSize(size);
        
        boid.setHunger(hunger);
        boid.setPanic(panic);
        boid.setThirst(thirst);
        
        boid.setStamina(stamina);
        boid.setMaxStamina(maxStamina);

        boid.setHungerLevel(hungerLevel);
        boid.setPanicLevel(panicLevel);
        boid.setThirstLevel(thirstLevel);
        
        boid.setGene(cohesion, separation, alignment, wander,flockRadius, nearRadius, sightRadius, maxStamina, hungerLevel, thirstLevel, panicLevel);
        
        boid.setState(currentState);
        boid.setFertility(fertility);
        boid.setNumberOfOffspring(offspring);
        
        System.out.println("Statestring" +states);
        states.replaceAll("[^a-zA-Z0-9]","");
        states=states.substring(1,states.length()-1);
        String[] strArray = (states.split(", "));
        for( String s : strArray){
        	System.out.println("bm"+s);
        }
        
        stateMachine.retriveStates(boid, strArray);
        
       
       // System.out.println("stamina " + stamina);
       // System.out.println("maxStamina " + maxStamina);
      //  System.out.println("states " + states);
        
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

        int maxXPos = Constants.mapWidth-100;
        int minXPos = 100;

        int maxYPos = Constants.mapHeight-100;
        int minYPos = 100;

        int maxXVel = 1;
        int maxYVel = 1;


        int xPos;
        int yPos;
        xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        while(WorldManager.getTileInfoAt(xPos, yPos).get("terrain") == 1){
            xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
            yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        }

        int xVel = (rand.nextInt(2 * maxXVel) - maxXVel);

        int yVel = (rand.nextInt(2 * maxYVel) - maxYVel);

        boid.setPosition(xPos, yPos, 0);
        boid.setVelocity(xVel, yVel, 0);

        boid.hunger = rand.nextInt((int)boid.hungerLevel);
        boid.thirst = rand.nextInt((int)boid.thirstLevel);
        
        
        float min = 0f;
        float maxStartingHungerLevel = boid.hungerLevel;
        float maxStartingThirstLevel = boid.thirstLevel;
        System.out.println("maxStartingHungerLevel " + maxStartingHungerLevel);
        System.out.println("maxStartingThirstLevel " + maxStartingThirstLevel);
        float startHunger = rand.nextFloat() * (maxStartingHungerLevel - min) + min;
        float startThirst = rand.nextFloat() * (maxStartingThirstLevel - min) + min;
        System.out.println("startHunger " +startHunger);
        System.out.println("startThirst " +startThirst);
        boid.hunger = startHunger;
        boid.thirst = startThirst;
        System.out.println( "boid.hunger "+boid.hunger);
        System.out.println( "boid.thirst" + boid.thirst);
        
        

        boid.fertility = rand.nextInt(100);
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

    private static void addToLists(Boid boid, Stack<State> states) {
        boids.add(boid);
        getBoidGrid().addBoid(boid);
        stateMachine.addBoid(boid, states);
    }

    /**
     * removes all the boids from the boidGrid one-by-one TODO: make a method in boidGrid to do this better.
     * <p/>
     * and clears the boids list.
     */
    public void clearBoidList() {

  //    for(Boid b : boids){
  //      stateMachine.removeBoid(b);
    //    boidGrid.removeBoid(b);
   //   }
        

        setBoidGrid(new BoidGrid(80, Constants.mapWidth, Constants.mapHeight));
        System.out.println("clearing list. ea " + ea);
        stateMachine = new StateMachine(this, ea);

        boids = new Array<Boid>();
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
            boid.update();

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
		if(boid.toDelete){
    		removeBoid(boid);
    		return true;
		}
        float lifespan = SimulationManager.speciesData.get(boid.getSpecies()).getLifespan() + MathsUtils.randomNumber(-10, 10);
        if (boid.hunger >= boid.hungerLevel*2) {
            Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f), boid.size*4);
            WorldManager.putObject(food);
            removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of hunger :( ");
            return true;
        }
        else if( boid.thirst >= boid.thirstLevel*2) {
            Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f), boid.size*4);
            WorldManager.putObject(food);
            boids.removeValue(boid, false);
            getBoidGrid().removeBoid(boid);
            parent.parent.gui.setConsole(" A boid just died of thirst :( ");
            return true;
        }
        else if (boid.age > lifespan) {
            Object food = new Object((byte) 0, (byte) 0, new Vector3(boid.position.x, boid.position.y, 0f), boid.size);
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
