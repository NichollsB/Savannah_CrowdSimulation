package com.UKC_AICS.simulation.managers;

import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class WorldManager extends Manager {

    private Vector3 size;

    public WorldManager() {

    }


    @Override
    public void update() {

    }


    public void createMap(int width, int height) {
        size = new Vector3(width, height, 1);
    }

    public Vector3 getSize() {
        return size;
    }
}
