package com.UKC_AICS.simulation.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.UKC_AICS.simulation.entity.Boid;
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
        tempSpeciesData.put("zebra", zebra);
        for (int i = 0; i < 100; i++) {
            boidManager.createBoid();
        }
        worldManager.createMap(20, 20);
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


    public ArrayList<Boid> getBoids() {
        return boidManager.getBoids();
    }

    public Vector3 getMapSize() {
        return worldManager.getSize();
    }
}
