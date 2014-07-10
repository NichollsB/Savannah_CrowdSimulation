package com.UKC_AICS.simulation.world;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Random;

import static com.UKC_AICS.simulation.Constants.TILE_SIZE;

/**
 *
 * @author Emily
 */
public class LandMap {

    private Vector3 size;
    public HashMap<String, byte[][]> information_layers = new HashMap<String, byte[][]>();


    
    /**
     *  This class is a data structure to hold the map information - could be done in layers.
     * 
     *  Info layer ideas - Height, Moisture/Humidity, Grass(Herbivore Food) levels
     */
    public LandMap(int width, int height) {
        size = new Vector3(width/TILE_SIZE, height/TILE_SIZE, 1);

        byte [][] mapInfo = new byte[width/TILE_SIZE][height/TILE_SIZE];

        for(int i = 0; i < mapInfo.length; i++ ) {
            for(int j = 0; j < mapInfo[i].length; j++ ) {
                mapInfo[i][j] = 0;
            }
        }

        information_layers.put("terrain", mapInfo);

        mapInfo = new byte[width/TILE_SIZE][height/TILE_SIZE];
        Random rand = new Random();
        for(int i = 0; i < mapInfo.length; i++ ) {
            for(int j = 0; j < mapInfo[i].length; j++ ) {
                mapInfo[i][j] = (byte)rand.nextInt(100);
            }
        }


        information_layers.put("grass", mapInfo);
    }
    

    
    /**
     *  create the map.
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
     *
     * @param x SCREEN POSITION
     * @param y will be conveerted to tile.
     * @param layer
     * @param newValue
     */
    public void changeTileOnLayer(int x, int y, String layer, byte newValue) {
        x /= TILE_SIZE;
        y /= TILE_SIZE;
        information_layers.get(layer)[x][y] = newValue;
    }
    
}