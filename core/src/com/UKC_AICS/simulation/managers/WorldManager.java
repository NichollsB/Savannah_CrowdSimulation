package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.UKC_AICS.simulation.entity.Object;
import java.util.HashMap;

/**
 * @author Emily
 */
public class WorldManager extends Manager {

    private static final int TILE_SIZE = 16;
    private Vector3 size;
    private HashMap<String, Byte[][]> information_layers = new HashMap<String, Byte[][]>();

    private QuadTree objects;

    public WorldManager(int width, int height) {
        size = new Vector3(width/TILE_SIZE, height/TILE_SIZE, 1);

        objects = new QuadTree(0, new Rectangle(0,0,width,height));

        Byte [][] mapInfo = new Byte[width/TILE_SIZE][height/TILE_SIZE];

        for(int i = 0; i < mapInfo.length; i++ ) {
            for(int j = 0; j < mapInfo[i].length; j++ ) {
                mapInfo[i][j] = 0;
            }
        }


        information_layers.put("map_tiles", mapInfo);
    }


    @Override
    public void update() {

    }

    public void putObject(Object object, int x, int y) {
        putObject(object, new Vector3(x,y,0));
    }
    public void putObject(Object object, Vector3 position) {
        if(object.getPosition().x != position.x || object.getPosition().y != position.y){
            object.setPosition(position);
        }
        objects.insert(object);
    }

    public byte getTileAt(int x, int y) {
        return information_layers.get("map_tiles")[x][y];
    }

    public void createMap(int width, int height) {
        size = new Vector3(width, height, 1);
    }

    public Vector3 getSize() {
        return size;
    }
}
