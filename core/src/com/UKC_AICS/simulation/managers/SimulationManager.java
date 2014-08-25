package com.UKC_AICS.simulation.managers;

import EvolutionaryAlgorithm.EA2;





import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.entity.ObjectData;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.UKC_AICS.simulation.utils.EnvironmentLoader;
import com.UKC_AICS.simulation.utils.Outputter;
import com.UKC_AICS.simulation.utils.StaXParser;
import com.UKC_AICS.simulation.utils.StaXParserLoad;
import com.UKC_AICS.simulation.utils.StaxWriter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.sun.corba.se.impl.orbutil.closure.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


/**
 * @author Emily
 */
public class SimulationManager extends Manager {

    final SimulationScreen parent;
    //    static final BoidManagerThreaded boidManager = new BoidManagerThreaded();
//    static final BoidManagerThreadedTwo boidManager = new BoidManagerThreadedTwo();
//    BoidManagerThreadedThree boidManager = new BoidManagerThreadedThree(this);
//    BoidManagerOld boidManager = new BoidManagerOld(this);
    public EA2 ea = new EA2();
    private File eaFile  = new File("config2.xml");
    
    BoidManager boidManager; //= new BoidManager(this, ea);
    WorldManager worldManager;// = new WorldManager(Constants.mapWidth, Constants.mapHeight);

   

    static public int minutes = 0;
    static public int hours = 0;
    static public int days = 0;
    static public int weeks = 0;
    static public int frames = 0;
    StateMachine stateMachine;
    //monstrous things.
    static final HashMap<String, HashMap<String, Float>> tempSpeciesData = new HashMap<String, HashMap<String, Float>>();
    static final HashMap<Byte, String> speciesByte = new HashMap<Byte, String>();

    StaXParser staXParser = new StaXParser();
    public static HashMap<Byte, Species> speciesData;
    
    HashMap<Byte, String> fileLocations;
    HashMap<Byte, float[]> speciesRGB = new HashMap<Byte, float[]>(); 
    
    private Outputter recorder = new Outputter();
    
    /**
     * Added by ben nicholls - for the creation of objects from ObjectData type/ creation of ObjectData types...
     */
    public static  HashMap<Byte, ObjectData> objectData = new HashMap<Byte, ObjectData>();
    

    /**
     * Sends appropriate calls to the world and boid manager to update for this frame.
     * <p/>
     * <p/>
     * Possibly store the data lookup tables here? like subType data for example
     */
    public SimulationManager(SimulationScreen parent) {
    	
        this.parent = parent;

        try {
			eaFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

        speciesData = staXParser.readConfig("data/species.xml");
        objectData = staXParser.readObjectFile("data/objects.xml");
        for (Species species : speciesData.values()) {
            species.setGrowthPerDay((species.getMaxSize() - species.getNewbornSize()) / species.getMaturity());
        }

        EnvironmentLoader.loadMaps();
        int size[] = EnvironmentLoader.getDimensions();
        if(size[0] > 1 && size[1] > 1) {
            Constants.mapWidth = size[0];
            Constants.mapHeight = size[1];
        }
        boidManager = new BoidManager(this, ea);
        worldManager = new WorldManager(Constants.mapWidth, Constants.mapHeight);
        this.stateMachine= boidManager.stateMachine;
        generateBoids();

         ea.setup(this);

//        addTestObjects();

        save();
    }

        


    private void addTestObjects() {
        Random rand = new Random();
        int maxXPos = Constants.mapWidth-100;
        int minXPos = 100;

        int maxYPos = Constants.mapHeight-100;
        int minYPos = 100;
        int xPos;
        int yPos;
        xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        while(WorldManager.getTileInfoAt(xPos, yPos).get("terrain") == 1){
            xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
            yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        }
//        Array<Byte> objTypes = new Array<Byte>();
        Object obj = new Object(objectData.get((byte)2),xPos,yPos);
//        objTypes.add(obj.getType());
        WorldManager.putObject(obj);
        xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        while(WorldManager.getTileInfoAt(xPos, yPos).get("terrain") == 1){
            xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
            yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        }
        obj = new Object(objectData.get((byte)2),xPos,yPos);
////        obj = new Object((byte)2,(byte)1,900,300);
        WorldManager.putObject(obj);
        xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
        yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        while(WorldManager.getTileInfoAt(xPos, yPos).get("terrain") == 1){
            xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
            yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
        }
        obj = new Object(objectData.get((byte)3),xPos,yPos);
//        objTypes.add(obj.getType());
        WorldManager.putObject(obj);
//        objTypes.add((byte)0);
        for(int i = 0; i < 8; i ++){
            xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
            yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
            while(WorldManager.getTileInfoAt(xPos, yPos).get("terrain") == 1){
                xPos = rand.nextInt((maxXPos - minXPos) + 1) + minXPos;
                yPos = rand.nextInt((maxYPos - minYPos) + 1) + minYPos;
            }
            obj = new Object(objectData.get((byte)1),xPos,yPos);
            WorldManager.putObject(obj);
        }
    }

    public void reset(){
    	boidManager.clearBoidList();
        WorldManager.clearObjects();
        addTestObjects();
    	generateBoids();
    	resetTime();
    }

    public void save() {
    	StaxWriter configFile = new StaxWriter();
        configFile.setFile(eaFile);
        try {
          
		configFile.saveConfig(boidManager.stateMachine);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    //Change this see tutorial
    public void load() {
    	StaXParserLoad load = new StaXParserLoad();
        load.readConfig(eaFile,boidManager);
        parent.resetGraphics();
      }

    public void generateBoids(){
    	//Create a map of species bytes to filenames for the boid sprite textures
    	fileLocations = new HashMap<Byte, String>();

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
    
    /**
     * Created by Ben Nicholls - explicit placement of new boids based on species
     */
    public void generateBoid(byte spByte, byte group, int x, int y){
    	Species species = speciesData.get(spByte);
    	boidManager.createBoid(species, group, x, y);
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

        if(recordSimulation ){
            try{
//                simRecorder.recordSim(frames, stateMachine);
            	System.out.println("Appending record");
            	recorder.appendOutput(frames, new File("testxml.xml"));
            }
            catch (Exception e){
                System.out.println("Could not record simulation" + e.toString() );
                e.printStackTrace();
                stopSimRecording();
            }

        }
    }

    /**
     *
     * @return whether the time has "ticked" over a dya or not.
     */
    private boolean tick() {
        boolean increment = false;
        frames++;
        if (minutes < 59) {
            minutes += 1;
        } else if (hours < 23) {
            minutes = 0;
            hours += 1;
            //TODO work in progress - Matt
           // boidManager.trackPop();
             
        } else if (days < 6) {
            minutes = 0;
            hours = 0;
            days += 1;
            
           if(ea.getEaOn()){
            	ea.Evolve();  //causes behaviour to go crazy so turned off for now.
            	
            }
            increment = true;
        } else {
            minutes = 0;
            hours = 0;
            days = 0;
            weeks += 1;
            increment = true;
        }
        return increment;
//        System.out.println(minutes + " mins; " + hours + " hrs; " + days + " days; " + weeks + " wks.");
    }

    public String getTime() {
        return " Time " + hours + " hrs; "
                + days + " days; " + weeks + " wks.";
    }


    public static int getDays() {
           return weeks * 7 + days;
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
        return worldManager.getTileInfoAt(screenX, screenY);
    }

    public Boid getBoidAt(int screenX, int screenY) {
        return boidManager.getBoidAt(screenX, screenY);
    }
    
    //Added by Ben Nicholls for graphics purposes
//    public Entity getEntityAt
    public HashMap<String, byte[][]> getFullInfo(){
    	return worldManager.getMapInfo();
    }
    
    public HashMap<Byte, Species> getSpeciesInfo(){
//    	Array<Species> species = new Array<Species>();
//    	for(Byte s : speciesData.keySet()){
//    		species.add(speciesData.get(s));
//    	}
    	return speciesData;
    }
    public void generateObject(byte type, byte subType, int x, int y){
    	ObjectData obj;
//    	worldManager.getObjects()
    	System.out.println("Placing " + subType);
    	if(objectData.containsKey(type)){
    		obj = objectData.get(type);
			if(type == 0){
				worldManager.createObject(obj, subType, x, y);
        	}
			else{
				worldManager.createObject(obj, x, y);
			}
    		
    		return;
    	}
    }
    public HashMap<Byte, ObjectData> getObjectDataInfo(){
    	return objectData;
    }
    
    public void hardReset(){
    	Constants.mapWidth = EnvironmentLoader.getDimensions()[0];
    	Constants.mapHeight = EnvironmentLoader.getDimensions()[1];
        worldManager = new WorldManager(Constants.mapWidth, Constants.mapHeight);
        boidManager = new BoidManager(this, ea);
        reset();
        parent.resetGraphics();
    }

    public void loadSaveCall(String command, String identifier, HashMap<String, File> file){
        System.out.println("Load call ");
//        for(String s : file.keySet()){
//            System.out.println("name " + s + " file " + file.get(s).getPath());
//        }
        if(command.equalsIgnoreCase("load")){
            if(identifier.equalsIgnoreCase("envpack")){
                if(!file.containsKey("packfile") || !file.containsKey("packatlas")) {
                    System.out.println("missing pack file path or pack atlas file path");
                    return;
                }
                EnvironmentLoader.loadMaps(file.get("packfile"), file.get("packatlas"));
                hardReset();
                return;
            }
            if(identifier.equalsIgnoreCase("envatlas")){
                System.out.println("Num files " + file.size());
                for(String s : file.keySet()){
                    System.out.println(file.get(s));
                    if(file.get(s) != null)
                        EnvironmentLoader.loadMap(file.get(s), s);
                }
                hardReset();
                return;
            }
//            if(file.containsKey("water")) {
//                EnvironmentLoader.loadMap(file.get("water"), "water");
//            }
//            if(file.containsKey("grass")) {
//                EnvironmentLoader.loadMap(file.get("grass"), "grass");
//            }
//            if(file.containsKey("terrain")) {
//                EnvironmentLoader.loadMap(file.get("terrain"), "terrain");
//            }
            hardReset();
        }
    }
    public void loadSaveCall(String command, String identifier, File file){
		if(command.equalsIgnoreCase("load")){

    		
    		if(identifier.equalsIgnoreCase("eafile")){
    				eaFile = file;
    				try {
						eaFile.createNewFile();
						System.out.println("EA exists " + eaFile.exists() + " is file " + eaFile.isFile() + " path " + eaFile.getPath()
								+" abs " + eaFile.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    		}
            if(identifier.equalsIgnoreCase("record")){
					simRecordFile = file;
					try {
						simRecordFile.createNewFile();
						System.out.println("Record exists " + simRecordFile.exists() + " is file " + simRecordFile.isFile() + " path " + simRecordFile.getPath()
								+" abs " + simRecordFile.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
		}
    }
    public void commandCall(String command, String identifier){
        if(command.equalsIgnoreCase("record")){
            if(identifier.equalsIgnoreCase("start")){
                recordSimulation();
            }
            if(identifier.equalsIgnoreCase("stop")){
                stopSimRecording();
            }
        }
    }
    private boolean recordSimulation = false;
    private File simRecordFile;
    public void recordSimulation(){
        try {
//        	simRecorder.setRecordFile(simRecordFile);
//            if (simRecorder.startRecordingSim(stateMachine)) {
//                recordSimulation = true;
//            }
//        	recorder.setFile(simRecordFile);
        	recordSimulation = true;
        }
        catch (Exception e){
            System.out.println("Sim recording exception");
        }
    }
    public void stopSimRecording(){
        try {
//            simRecorder.endRecordingSim();
        	recordSimulation = false;
        }
        catch (Exception e){
            System.out.println("Could not stop recording...");
        }
    }
}
