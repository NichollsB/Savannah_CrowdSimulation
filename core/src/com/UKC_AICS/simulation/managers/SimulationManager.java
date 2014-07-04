package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.utils.Species;
import com.UKC_AICS.simulation.utils.StaXParser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;


/**
 * @author Emily
 */
public class SimulationManager extends Manager {

//    static final BoidManagerThreaded boidManager = new BoidManagerThreaded();
    static final BoidManager boidManager = new BoidManager();
    static final WorldManager worldManager = new WorldManager(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    static public int minutes = 0;
    static public int hours = 0;
    static public int days = 0;
    static public int weeks = 0;
     public static int currentDay = 0;
    
    //monstrous things.
    static final HashMap<String, HashMap<String, Float>> tempSpeciesData = new HashMap<String, HashMap<String, Float>>();
    static final HashMap<Byte, String> speciesByte = new HashMap<Byte, String>();

    StaXParser staXParser = new StaXParser();
    static HashMap<Byte, Species> speciesData;


    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * <p/>
     * <p/>
     * Possibly store the data lookup tables here? like subType data for example
     */
    public SimulationManager() {
        speciesData = staXParser.readConfig("../Project-Savannah/core/assets/settings.xml");

        generateBoids();

        worldManager.putObject(new Object((byte)2,(byte)1,100,100), new Vector3(100,100,0));
        worldManager.putObject(new Object((byte)2,(byte)1,200,200), new Vector3(200,200,0));
    }

    public void reset(){
    	boidManager.clearBoidList();
    	generateBoids();
    	resetTime();
    }


    public void generateBoids(){
        // Looks through tempSpeciesData Hashmap for each species hashmap.  extracts number for that species and byte reference.
        Iterator it = speciesData.keySet().iterator();
        while (it.hasNext()) {
            Byte spByte = (Byte)it.next();
            Species species = speciesData.get(spByte);
            int number = species.getNumber();

            for (int i = 0; i < number; i++) {
                boidManager.createBoid(spByte.byteValue());  //TODO get the subType int from xml file
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
            setDay();
        } else {
            days = 0;
            weeks += 1;
            setDay();
        }
//        System.out.println(minutes + " mins; " + hours + " hrs; " + days + " days; " + weeks + " wks.");
    }

    public String getTime() {
        return " Time " + minutes + " mins; " + hours + " hrs; "
                + days + " days; " + weeks + " wks.";
    }

    public void setDay() {
    	currentDay++;
    	System.out.println(currentDay);
    	boidManager.updateAge();
    }
    
   public static int getDay() {
	   return currentDay;
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
    public Array<Entity> getObjects() {
        return worldManager.getObjects();
    }

    public Array<Entity> getObjectsNearby(Vector2 point) {
        return worldManager.getObjectsNearby(point);
    }

    public byte[][] getMapTiles() {
        return worldManager.getTiles();
    }

    public Vector3 getMapSize() {
        return worldManager.getSize();
    }
}
