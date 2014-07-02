package com.UKC_AICS.simulation.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.UKC_AICS.simulation.entity.Boid;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 * @author Emily
 */
public class SimulationManager extends Manager {

    static final BoidManager boidManager = new BoidManager();
    static final WorldManager worldManager = new WorldManager();
    static public int minutes = 0;
    static public int hours = 0;
    static public int days = 0;
    static public int weeks = 0;

    //monstrous things.
    static final HashMap<String, HashMap<String, Float>> tempSpeciesData = new HashMap<String, HashMap<String, Float>>();

    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * <p/>
     * <p/>
     * Possibly store the data lookup tables here? like species data for example
     */
    public SimulationManager() {
        HashMap<String, Float> zebra = new HashMap<String, Float>();
        zebra.put("cohesion", 0.5f);
        zebra.put("alignment", 0.5f);
        zebra.put("separation", 0.5f);
        zebra.put("wander", 0.2f);
        zebra.put("species", 1f);
        tempSpeciesData.put("zebra", zebra);

        HashMap<String, Float> Bison = new HashMap<String, Float>();
        zebra.put("cohesion", 0.5f);
        zebra.put("alignment", 0.5f);
        zebra.put("separation", 0.5f);
        zebra.put("wander", 0.2f);
        zebra.put("species", 2f);
        tempSpeciesData.put("bison", Bison);

        generateBoids();
        worldManager.createMap(20, 20);
    }

    public void reset(){
    	boidManager.clearBoidList();
    	generateBoids();
    	resetTime();
    }
    
    public void generateBoids(){
        Iterator iterator = tempSpeciesData.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pairs = (Map.Entry) iterator.next();  //TODO trying to make it iterate through the species hashmap to get species int
//            float species = tempSpeciesData.get(pairs).get("species");

//            for (int i = 0; i < 100; i++) {
//                boidManager.createBoid((byte)species);  //TODO get the species int from xml file
//            }
        }
    }
    
    public void update() {
        boidManager.update();
        worldManager.update();
        tick();
    }

    private void tick() {
        if (minutes < 59) {
            minutes += 1;
        } else if (hours < 23) {
            minutes = 0;
            hours += 1;
        } else if (days < 6) {
            hours = 0;
            days += 1;
        } else {
            days = 0;
            weeks += 1;
        }
//        System.out.println(minutes + " mins; " + hours + " hrs; " + days + " days; " + weeks + " wks.");
    }

    public String getTime() {
        return " Time " + minutes + " mins; " + hours + " hrs; "
                + days + " days; " + weeks + " wks.";
    }

    public void resetTime() {
        minutes = 0;
        hours = 0;
        days = 0;
        weeks = 0;
    }


    public Array<Boid> getBoids() {
        return boidManager.getBoids();
    }

    public Vector3 getMapSize() {
        return worldManager.getSize();
    }
}
