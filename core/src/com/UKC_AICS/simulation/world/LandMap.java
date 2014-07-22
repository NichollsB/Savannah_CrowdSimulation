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


        byte[][] mapInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];

        // randomly assigns grass to tiles
        Random rand = new Random();
        for (int i = 0; i < mapInfo.length; i++) {
            for (int j = 0; j < mapInfo[i].length; j++) {
                mapInfo[i][j] = (byte) rand.nextInt(100);
            }
        }
        information_layers.put("grass", mapInfo);

        // randomly assigns water to tiles
        mapInfo = new byte[width / TILE_SIZE][height / TILE_SIZE];
        for (int i = 0; i < mapInfo.length; i++) {
            for (int j = 0; j < mapInfo[i].length; j++) {
                mapInfo[i][j] = (byte) rand.nextInt(100);
                if (mapInfo[i][j] > 50) {
                    terrain[i][j] = 1; //change terrain type to water.
                }
            }
        }


        information_layers.put("water", mapInfo);
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
        information_layers.get(layer)[x][y] = newValue;
    }

}