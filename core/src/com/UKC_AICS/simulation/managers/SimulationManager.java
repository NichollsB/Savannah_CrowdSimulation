package com.UKC_AICS.simulation.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    static final HashMap<Byte, String> speciesByte = new HashMap<Byte, String>();

    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * <p/>
     * <p/>
     * Possibly store the data lookup tables here? like species data for example
     */
    public SimulationManager() {

        // Hard coded zebra species
        HashMap<String, Float> zebra = new HashMap<String, Float>();
        zebra.put("cohesion", 0.3f);
        zebra.put("alignment", 0.5f);
        zebra.put("separation", 0.9f);
        zebra.put("wander", 0.2f);
        zebra.put("byte", 1f);
        zebra.put("number", 100f);
        speciesByte.put((byte) 1, "zebra");
        tempSpeciesData.put("zebra", zebra);

        // Hard coded bison species
        HashMap<String, Float> bison = new HashMap<String, Float>();
        bison.put("cohesion", 0.7f);
        bison.put("alignment", 0.5f);
        bison.put("separation", 0.3f);
        bison.put("wander", 0.2f);
        bison.put("byte", 2f);
        bison.put("number", 100f);
        speciesByte.put((byte) 2, "bison");
        tempSpeciesData.put("bison", bison);

        generateBoids();
        worldManager.createMap(20, 20);
    }

    public void reset(){
    	boidManager.clearBoidList();
    	generateBoids();
    	resetTime();
    }


    public void generateBoids(){
        // Looks through tempSpeciesData Hashmap for each species hashmap.  extracts number for that species and byte reference.
        Set nameSet = tempSpeciesData.keySet();
        Iterator it = nameSet.iterator();
        while (it.hasNext()) {
            String name = (String)it.next();
            HashMap<String, Float> tmpSp = tempSpeciesData.get(name);
            int number = tmpSp.get("number").intValue();
            byte spByte = tmpSp.get("byte").byteValue();

            for (int i = 0; i < number; i++) {
                boidManager.createBoid(spByte);  //TODO get the species int from xml file
            }
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
