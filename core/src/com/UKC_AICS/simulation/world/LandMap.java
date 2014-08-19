package com.UKC_AICS.simulation.world;

import com.UKC_AICS.simulation.utils.EnvironmentLoader;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Random;

import static com.UKC_AICS.simulation.Constants.TILE_SIZE;

/**
 * @author Emily
 */
public class LandMap {
	
	

    private Vector3 size;
    public HashMap<String, byte[][]> information_layers = new HashMap<String, byte[][]>();


    /**
     * This class is a data structure to hold the map information - could be done in layers.
     * <p/>
     * Info layer ideas - Height, Moisture/Humidity, Grass(Herbivore Food) levels
     */
    public LandMap(int width, int height) {
        create(width, height);
    }

    public void create(int width, int height){
        size = new Vector3(width / TILE_SIZE, height / TILE_SIZE, 1);
        Random rand = new Random();//for randomly assigning values to tiles

        byte[][] terrain = null; //
//        byte[][] border = EnvironmentLoader.getLayer_values("terrain", false);
        System.out.println("set up terrain layer " + terrain);
        if(terrain == null) {
            terrain = new byte[width / TILE_SIZE][height / TILE_SIZE];
            for (int i = 0; i < terrain.length; i++) {
                for (int j = 0; j < terrain[i].length; j++) {
                    terrain[i][j] = 0; //terrain type is land.
                }
            }
        }
//
        byte[][] grassInfo = EnvironmentLoader.getLayer_values("grass", false);//new byte[width / TILE_SIZE][height / TILE_SIZE];
        System.out.println("set up grass layer " + grassInfo);
        if(grassInfo == null){
            grassInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];


            for (int i = 0; i < grassInfo.length; i++) {
                for (int j = 0; j < grassInfo[i].length; j++) {
                    grassInfo[i][j] = (byte) rand.nextInt(128);
                }
            }
        }


        // randomly assigns water to tiles
        byte[][] waterInfo = EnvironmentLoader.getLayer_values("water", false);
        float value;
        System.out.println("set up water layer " + waterInfo);
        boolean loadedLayer = true;
        if(waterInfo == null) {
            waterInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
            loadedLayer = false;
        }
        for (int i = 0; i < waterInfo.length; i++) {
            for (int j = 0; j < waterInfo[i].length; j++) {
                if(loadedLayer){
                    value = ((((float)waterInfo[i][j])/100)*128);
                    waterInfo[i][j] = (byte)Math.round(value);
                }
                else
                    waterInfo[i][j] = (byte) rand.nextInt(128);
//                System.out.println(waterInfo[i][j] + " loaded " + loadedLayer);
//                if((i == 0 || i == waterInfo.length-1)||
//                        (j == 0 || j == waterInfo[i].length-1)){
//                    terrain[i][j]=1;
//                    waterInfo[i][j] = 0;
//                }
//                else
                if (waterInfo[i][j] > 70) {
                    terrain[i][j] = 1; //change terrain type to water.
                    grassInfo[i][j] = 0; // no grass/vegetation on a water tile.
                }
            }
        }


        byte[][] blockedInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
        for (int i = 0; i < blockedInfo.length; i++) {
            for (int j = 0; j < blockedInfo[i].length; j++) {
                blockedInfo[i][j] = (byte) 0;  // not blocked

                if (terrain[i][j]  == 1) {
                    blockedInfo[i][j] = (byte) 1;  // blocked terrain
                }
            }
        }

        byte[][] grassGrowth = new byte[width / TILE_SIZE][height / TILE_SIZE];
        for (int i = 0; i < grassGrowth.length; i++) {
            for (int j = 0; j < grassGrowth[i].length; j++) {
                grassGrowth[i][j] = (byte) 0;
            }
        }

        information_layers.put("grass", grassInfo);

        information_layers.put("grassGrowth", grassInfo);

        information_layers.put("water", waterInfo);

        information_layers.put("terrain", terrain);

        information_layers.put("blocked", blockedInfo);
    }


//    /**
//     * create the map.
//     */
//    public void create(int width, int height) {
//        size = new Vector3(width / TILE_SIZE, height / TILE_SIZE, 1);
//
//        byte[][] terrain = new byte[width / TILE_SIZE][height / TILE_SIZE];
//
//        for (int i = 0; i < terrain.length; i++) {
//            for (int j = 0; j < terrain[i].length; j++) {
//                terrain[i][j] = 0; //terrain type is land.
//            }
//        }
//
//
//        byte[][] grassInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
//
//        // randomly assigns grass to tiles
//        Random rand = new Random();
//
//        for (int i = 0; i < grassInfo.length; i++) {
//            for (int j = 0; j < grassInfo[i].length; j++) {
//                grassInfo[i][j] = (byte) rand.nextInt(128);
//            }
//        }
//
//        // randomly assigns water to tiles
//        byte[][] waterInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
//        for (int i = 0; i < waterInfo.length; i++) {
//            for (int j = 0; j < waterInfo[i].length; j++) {
//                waterInfo[i][j] = (byte) rand.nextInt(128);
//                if (waterInfo[i][j] > 126) {
//                    terrain[i][j] = 1; //change terrain type to water.
//                    grassInfo[i][j] = 0; // no grass/vegetation on a water tile.
//                }
//            }
//        }
//
//        byte[][] blockedInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
//        for (int i = 0; i < blockedInfo.length; i++) {
//            for (int j = 0; j < blockedInfo[i].length; j++) {
//                blockedInfo[i][j] = (byte) 0;  // not blocked
//
//                if (terrain[i][j]  == 1) {
//                    blockedInfo[i][j] = (byte) 1;  // blocked terrain
//                }
//            }
//        }
//
//        byte[][] grassGrowth = new byte[width / TILE_SIZE][height / TILE_SIZE];
//        for (int i = 0; i < grassGrowth.length; i++) {
//            for (int j = 0; j < grassGrowth[i].length; j++) {
//                grassGrowth[i][j] = (byte) 0;
//            }
//        }
//
//        information_layers.put("grass", grassInfo);
//
//        information_layers.put("grassGrowth", grassInfo);
//
//        information_layers.put("water", waterInfo);
//
//        information_layers.put("terrain", terrain);
//
//        information_layers.put("blocked", blockedInfo);
//    }

    public Vector3 getSize() {
        return size.cpy();
    }

    public byte[][] getLayer(String name) {
        return information_layers.get(name);
    }

    /**
     * @param x        SCREEN POSITION
     * @param y        will be converted to tile.
     * @param layer
     * @param newValue
     */
    public void changeTileOnLayer(int x, int y, String layer, byte newValue) {
    	
        x /= TILE_SIZE;
        y /= TILE_SIZE;
//        System.out.println("TileChanging. Old val " + information_layers.get(layer)[x][y] +
//    			" new val " + newValue);
        information_layers.get(layer)[x][y] = newValue;
    }

}