package com.UKC_AICS.simulation.world;

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
        size = new Vector3(width / TILE_SIZE, height / TILE_SIZE, 1);

        byte[][] terrain = new byte[width / TILE_SIZE][height / TILE_SIZE];

        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[i].length; j++) {
                terrain[i][j] = 0; //terrain type is land.
            }
        }


        byte[][] grassInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];

        // randomly assigns grass to tiles
        Random rand = new Random();

        for (int i = 0; i < grassInfo.length; i++) {
            for (int j = 0; j < grassInfo[i].length; j++) {
                grassInfo[i][j] = (byte) rand.nextInt(100);
            }
        }

        // randomly assigns water to tiles
        byte[][] waterInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
        for (int i = 0; i < waterInfo.length; i++) {
            for (int j = 0; j < waterInfo[i].length; j++) {
                waterInfo[i][j] = (byte) rand.nextInt(100);
                if (waterInfo[i][j] > 95) {
                    terrain[i][j] = 1; //change terrain type to water.
                    grassInfo[i][j] = 0; // no grass/vegetation on a water tile.
                }
            }
        }

        information_layers.put("grass", grassInfo);

        information_layers.put("water", waterInfo);

        information_layers.put("terrain", terrain);
    }


    /**
     * create the map.
     */
    public void create() {

    }

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