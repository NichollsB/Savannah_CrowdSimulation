package com.UKC_AICS.simulation.managers;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.entity.Object;
import com.UKC_AICS.simulation.utils.ObjectGrid;
import com.UKC_AICS.simulation.utils.QuadTree;
import com.UKC_AICS.simulation.world.LandMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;

/**
 * @author Emily
 */
public class WorldManager extends Manager {

    private static final int TILE_SIZE = 16;
    private static LandMap map;
    private static Vector3 tileSize;
    private static Vector3 size;
    private static Random rand = new Random();
//    private HashMap<String, byte[][]> information_layers = new HashMap<String, byte[][]>();


    private static Array<Entity> objects = new Array<Entity>();
    private static QuadTree objects_map;

    public WorldManager(int width, int height) {
        tileSize = new Vector3(width/TILE_SIZE, height/TILE_SIZE, 1);
        size = new Vector3(width, height, 1);

        objects_map = new QuadTree(0, new Rectangle(0,0,width,height));

        map = new LandMap(width, height);
    }


    @Override
    public void update(boolean dayIncrement) {
        if (dayIncrement)
            decayCorpses();

//        if(rand.nextInt(100) > 50) {
//            byte[][] grassGrowth = map.information_layers.get("grassGrowth");
//            byte[][] grass = map.information_layers.get("grass");
//
//            for (int i = 0; i < grassGrowth.length; i++) {
//                for (int j = 0; j < grassGrowth[i].length; j++) {
//                    if(rand.nextInt(100) > 50) {
//                        grassGrowth[i][j] += 1;
//                        if (grassGrowth[i][j] == 127 && grass[i][j] < 127) {
//                            grass[i][j] += 1;
//                            if (grass[i][j] < 0) {
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    public static void putObject(Entity entity, int x, int y) {
        putObject(entity, new Vector3(x,y,0));
    }
    public static void putObject(Entity entity) {
        putObject(entity, entity.getPosition());
    }
    public  static void putObject(Entity entity, Vector3 position) {
        if(entity.getPosition().x != position.x || entity.getPosition().y != position.y){
            entity.setPosition(position);
        }
        objects_map.insert(entity);
        objects.add(entity);
    }

    public static void clearObjects() {
        objects.clear();
    }

    public static void removeObject(Entity entity) {
        objects.removeValue(entity, true);
        rebuildTree(objects);
    }
    public static void rebuildTree(Array<Entity> objects) {
        objects_map = new QuadTree(0, new Rectangle(0, 0,  size.x, size.x));
        for (Entity boid : objects)
            objects_map.insert(boid);
    }

    public  Array<Entity> getObjects() {
        return objects;
    }

    public byte getTileAt(int x, int y) {
        return map.information_layers.get("terrain")[x][y];
    }

//    public void createMap(int width, int height) {
//        tileSize = new Vector3(width, height, 1);
//    }

    public Vector3 getSize() {
        return tileSize;
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
        if (mapX >= 0 && mapX < tileSize.x &&
                mapY >= 0 && mapY < tileSize.y) {

            for (String layer : map.information_layers.keySet()) {
                layers.put(layer, map.information_layers.get(layer)[mapX][mapY]);
            }
        }
        else {
            System.out.println("I'm mysteriously out of bounds");
        }
        return layers;
    }

    public static void changeTileOnLayer(float x, float y, String layer, byte newValue) {
        map.changeTileOnLayer((int)x,(int)y,layer, newValue);
    }
    
  //Added by Ben Nicholls for graphics purposes - very probably temporary
    public HashMap<String, byte[][]> getMapInfo(){
    	return map.information_layers;
    }

    private void decayCorpses() {
        Entity corpse;
        for (int i = 0; i < objects.size; i++) {
            corpse = objects.get(i);
            if( corpse.getType()==0) {
                ((Object)corpse).reduceMass(1f);
                if(((Object)corpse).getMass()<0.5f) {
                    removeObject(corpse);
                }
            }
        }
    }
    
    
    public void createObject(ObjectData objData, byte subtype, int x, int y){
    	com.UKC_AICS.simulation.entity.Object obj = new com.UKC_AICS.simulation.entity.Object(objData, x, y);
    	obj.setSubType(subtype);
    	putObject(obj);
    }

    public static boolean checkObject(Entity object) {
        return objects.contains(object, false);
    }
  
}
