package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.entity.behaviours.*;
import com.UKC_AICS.simulation.utils.BoidGrid;
import com.UKC_AICS.simulation.utils.MathsUtils;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;


/**
 * @author Emily
 */
public class BoidManager extends Manager {



    public static Array<Boid> boids = new Array<Boid>();
    private static BoidGrid boidGrid;
    public final SimulationManager parent;
    public HashMap<String, Behaviour> behaviours = new HashMap<String, Behaviour>();
    private QuadTree quadtree;
//    private Vector3 steering = new Vector3();
    private Random rand = new Random();
    private StateMachine stateMachine;

    public BoidManager(SimulationManager parent) {

        this.parent = parent;
        setBoidGrid(new BoidGrid(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        quadtree = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


        stateMachine = new StateMachine(this);
    }
    
    
    public void createBoid(byte species, int age, int bDay, float pX, float pY, float pZ, float vX, float vY, float vZ) {
    	Boid boid = new Boid(species);
    	
    	boid.setAge(age);
    	boid.setBirthDay(bDay);
    	boid.setPosition(pX,pY,pZ);
    	boid.setVelocity(vX,vY,vZ);
    	
        addToLists(boid);
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


//        boid.setBirthDay(SimulationManager.getDay());
//        boid.setOrientation(xOrient, yOrient, 0);

        boid.setPosition(xPos, yPos, 0);
        boid.setVelocity(xVel, yVel, 0);

        boid.hunger = rand.nextInt(120) + 20;
        boid.thirst = rand.nextInt(150) + 50;
        //random start age
        boid.age = rand.nextInt((int)species.getLifespan());

        addToLists(boid);
    }

    /**
     * create a copy of a boid.
     * @param oldBoid
     * @return
     */
    public Boid createBoid(Boid oldBoid) {
        Boid boid = new Boid(oldBoid);
        addToLists(boid);
        return boid;

    }

    private void addToLists(Boid boid) {
        boids.add(boid);
//        quadtree.insert(boid);
        getBoidGrid().addBoid(boid);
        stateMachine.addBoid(boid);
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

        stateMachine.update();

        Boid boid;
        for (int i = 0; i < boids.size; i++) {
            boid = boids.get(i);
            //apply movement to it.
            boid.move();

//            if(checkForDeath(boid)) {
//                continue;
//            } else {

                //tell the grid to update its position.
                getBoidGrid().update(boid);
//            }
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

    public static BoidGrid getBoidGrid() {
        return boidGrid;
    }

    public static void setBoidGrid(BoidGrid boidGrid) {
        BoidManager.boidGrid = boidGrid;
    }

}
