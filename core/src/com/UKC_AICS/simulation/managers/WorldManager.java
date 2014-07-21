package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.utils.ObjectGrid;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.UKC_AICS.simulation.world.LandMap;
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
    private static LandMap map;
    private static Vector3 size;
//    private HashMap<String, byte[][]> information_layers = new HashMap<String, byte[][]>();


    private Array<Entity> objects = new Array<Entity>();
    private QuadTree objects_map;
    private ObjectGrid objectGrid;

    public WorldManager(int width, int height) {
        size = new Vector3(width/TILE_SIZE, height/TILE_SIZE, 1);

        objects_map = new QuadTree(0, new Rectangle(0,0,width,height));
        objectGrid = new ObjectGrid(60, Constants.screenWidth, Constants.screenHeight);

        map = new LandMap(width, height);
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
        return map.information_layers.get("terrain")[x][y];
    }

//    public void createMap(int width, int height) {
//        size = new Vector3(width, height, 1);
//    }

    public Vector3 getSize() {
        return size;
    }

    public byte[][] getTiles() {
        return map.information_layers.get("terrain");
    }

    public Array<Entity> getObjectsNearby(int x, int y) {
        return getObjectsNearby(new Vector2(x,y));
    }
    public Array<Entity> getObjectsNearby(Vector2 point) {
        Array<Entity> returnObjects = new Array<Entity>();
        objects_map.retrieveObjects(returnObjects, point);
        return returnObjects;
    }

    public static HashMap<String,Byte> getTileInfoAt(int x, int y) {
        HashMap<String, Byte> layers = new HashMap<String, Byte>();
        int mapX = x/TILE_SIZE;
        int mapY = y/TILE_SIZE;
        if (mapX >= 0 && mapX < size.x &&
                mapY >= 0 && mapY < size.y) {


            for (String layer : map.information_layers.keySet()) {
                layers.put(layer, map.information_layers.get(layer)[mapX][mapY]);
            }
        }
        return layers;
    }

    public static void changeTileOnLayer(float x, float y, String layer, byte newValue) {
        map.changeTileOnLayer((int)x,(int)y,layer, newValue);
    }
}
