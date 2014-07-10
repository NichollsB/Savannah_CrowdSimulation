package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * @author Emily
 */
public class WorldManager extends Manager {

    private static final int TILE_SIZE = 16;
    private Vector3 size;
    private HashMap<String, byte[][]> information_layers = new HashMap<String, byte[][]>();


    private Array<Entity> objects = new Array<Entity>();
    private QuadTree objects_map;

    public WorldManager(int width, int height) {
        size = new Vector3(width/TILE_SIZE, height/TILE_SIZE, 1);

        objects_map = new QuadTree(0, new Rectangle(0,0,width,height));

        byte [][] mapInfo = new byte[width/TILE_SIZE][height/TILE_SIZE];

        for(int i = 0; i < mapInfo.length; i++ ) {
            for(int j = 0; j < mapInfo[i].length; j++ ) {
                mapInfo[i][j] = 0;
            }
        }


        information_layers.put("map_tiles", mapInfo);
    }


    @Override
    public void update(boolean dayIncrement) {

    }

    public void putObject(Entity entity, int x, int y) {
        putObject(entity, new Vector3(x,y,0));
    }
    public void putObject(Entity entity) {
        putObject(entity, entity.getPosition());
    }
    public void putObject(Entity entity, Vector3 position) {
        if(entity.getPosition().x != position.x || entity.getPosition().y != position.y){
            entity.setPosition(position);
        }
        objects_map.insert(entity);
        objects.add(entity);
    }

    public void removeObject(Entity entity) {
        objects.removeValue(entity, true);
        rebuildTree(objects);
    }

    public void rebuildTree(Array<Entity> objects) {
        objects_map = new QuadTree(0, new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        for (Entity boid : objects)
            objects_map.insert(boid);
    }

    public Array<Entity> getObjects() {
        return objects;
    }

    public byte getTileAt(int x, int y) {
        return information_layers.get("map_tiles")[x][y];
    }

//    public void createMap(int width, int height) {
//        size = new Vector3(width, height, 1);
//    }

    public Vector3 getSize() {
        return size;
    }

    public byte[][] getTiles() {
        return information_layers.get("map_tiles");
    }

    public Array<Entity> getObjectsNearby(int x, int y) {
        return getObjectsNearby(new Vector2(x,y));
    }
    public Array<Entity> getObjectsNearby(Vector2 point) {
        Array<Entity> returnObjects = new Array<Entity>();
        objects_map.retrieveObjects(returnObjects, point);
        return returnObjects;
    }
}
