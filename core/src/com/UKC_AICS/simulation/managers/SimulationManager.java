package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.UKC_AICS.simulation.utils.StaXParser;
import com.UKC_AICS.simulation.utils.StaXParserLoad;
import com.UKC_AICS.simulation.utils.StaxWriter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Iterator;


/**
 * @author Emily
 */
public class SimulationManager extends Manager {

    final SimulationScreen parent;
    //    static final BoidManagerThreaded boidManager = new BoidManagerThreaded();
//    static final BoidManagerThreadedTwo boidManager = new BoidManagerThreadedTwo();
//    BoidManagerThreadedThree boidManager = new BoidManagerThreadedThree(this);
//    BoidManagerOld boidManager = new BoidManagerOld(this);
    BoidManager boidManager = new BoidManager(this);
    WorldManager worldManager = new WorldManager(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    static public int minutes = 0;
    static public int hours = 0;
    static public int days = 0;
    static public int weeks = 0;
     public static int currentDay = 0;
    
    //monstrous things.
    static final HashMap<String, HashMap<String, Float>> tempSpeciesData = new HashMap<String, HashMap<String, Float>>();
    static final HashMap<Byte, String> speciesByte = new HashMap<Byte, String>();

    StaXParser staXParser = new StaXParser();
    public static HashMap<Byte, Species> speciesData;
    
    HashMap<Byte, String> fileLocations;
    HashMap<Byte, float[]> speciesRGB = new HashMap<Byte, float[]>(); 
    

    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * <p/>
     * <p/>
     * Possibly store the data lookup tables here? like subType data for example
     */
    public SimulationManager(SimulationScreen parent) {
        this.parent = parent;
        speciesData = staXParser.readConfig("../core/assets/data/species.xml");

        generateBoids();

        Array<Byte> objTypes = new Array<Byte>();
        Object obj = new Object((byte)2,(byte)1,355,450);
        objTypes.add(obj.getType());
        worldManager.putObject(obj);

        obj = new Object((byte)2,(byte)1,500,200);
////        obj = new Object((byte)2,(byte)1,900,300);
        worldManager.putObject(obj);

        obj = new Object((byte)3,(byte)1,755,450);
        objTypes.add(obj.getType());
        worldManager.putObject(obj);

        obj = new Object((byte)2,(byte)1,400,600);
        worldManager.putObject(obj);
        obj = new Object((byte)2,(byte)1,160,200);
        worldManager.putObject(obj);
        obj = new Object((byte)2,(byte)1,160,400);
        worldManager.putObject(obj);
        obj = new Object((byte)2,(byte)1,180,600);
        worldManager.putObject(obj);
        obj = new Object((byte)2,(byte)1,1100,200);
        worldManager.putObject(obj);
        obj = new Object((byte)2,(byte)1,1100,400);
        worldManager.putObject(obj);
        obj = new Object((byte)2,(byte)1,1100,600);
        worldManager.putObject(obj);

    }

    public void reset(){
    	boidManager.clearBoidList();
    	generateBoids();
    	resetTime();
    }

    public void save() {
    	System.out.println("Called in SM");
    	StaxWriter configFile = new StaxWriter();
        configFile.setFile("config2.xml");
        try {
          configFile.saveConfig();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    //Change this see tutorial
    public void load() {
    	System.out.println("Called in SM");
    	StaXParserLoad load = new StaXParserLoad();
        load.readConfig("config2.xml");
      }

    public void generateBoids(){
    	//Create a map of species bytes to filenames for the boid sprite textures
    	fileLocations = new HashMap<Byte, String>();
        // Looks through tempSpeciesData Hashmap for each species hashmap.  extracts number for that species and byte reference.
        Iterator it = speciesData.keySet().iterator();
        while (it.hasNext()) {
            Byte spByte = (Byte)it.next();
            Species species = speciesData.get(spByte);
            int number = species.getNumber();

            for (int i = 0; i < number; i++) {
                boidManager.createBoid(species);
            }
            //Find the species texture file location
            if(species.hasSpriteLocation())
            	fileLocations.put(spByte, species.getSpriteLocation());
            if(species.hasRGB())
            	speciesRGB.put(spByte, species.getRGB());
            
        }
    }

    public void clear(){
    	boidManager.clearBoidList();
    }
    
//    public void update() {
//        boidManager.update();
//        worldManager.update();
//        tick();

    public void update(boolean dayIncrement) {
        dayIncrement = tick();
        boidManager.update(dayIncrement);
        worldManager.update(dayIncrement);
    }

    /**
     *
     * @return whether the time has "ticked" over a dya or not.
     */
    private boolean tick() {
        boolean increment = false;
        if (minutes < 59) {
            minutes += 1;
        } else if (hours < 23) {
            minutes = 0;
            hours += 1;
        } else if (days < 6) {
            hours = 0;
            days += 1;
            setDay();
            increment = true;
        } else {
            days = 0;
            weeks += 1;
            setDay();
            increment = true;
        }
        return increment;
//        System.out.println(minutes + " mins; " + hours + " hrs; " + days + " days; " + weeks + " wks.");
    }

    public String getTime() {
        return " Time " + hours + " hrs; "
                + days + " days; " + weeks + " wks.";
    }

    public void setDay() {
    	currentDay++;
//    	boidManager.updateAge(); //moved this to boidmanager
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

	public HashMap<Byte, String> getTextureLocations() {
		// TODO Auto-generated method stub
		return fileLocations;
	}
	public HashMap<Byte, float[]> getRGBValues(){
		return speciesRGB;
	}

    public HashMap<String, Byte> getTileInfo(int screenX, int screenY) {
        return worldManager.getTileInfoAt(screenX,screenY);
    }

    public Boid getBoidAt(int screenX, int screenY) {
        return boidManager.getBoidAt(screenX,screenY);
    }
    
    //Added by Ben Nicholls for graphics purposes
    public HashMap<String, byte[][]> getFullInfo(){
    	return worldManager.getMapInfo();
    }
}
