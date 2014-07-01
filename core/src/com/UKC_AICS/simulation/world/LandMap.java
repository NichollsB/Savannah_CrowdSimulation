package com.UKC_AICS.simulation.world;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Emily
 */
public class LandMap {

    private Vector2 size = new Vector2();
    
    /**
     *  This class is a data structure to hold the map information - could be done in layers.
     * 
     *  Info layer ideas - Height, Moisture/Humidity, Grass(Herbivore Food) levels
     */
    public LandMap(Vector2 size) {
        this.size.set(size);
    }
    
    
    public LandMap(int xSize, int ySize) {
        size.set(xSize,ySize);
        
        
    }
    
    /**
     *  create the map.
     */
    public void create() {
        
    }

    public Vector2 getSize() {
        return size.cpy();
    }
    
}