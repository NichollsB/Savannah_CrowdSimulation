package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class WorldObject extends Object {

    private Vector3 position; // explicitly 3d vectors for extensibility


    public WorldObject(byte type, Vector3 position) {
        this.type = type;
        this.position = position;
    }

    /**
     * @return the type of the object as its byte type
     */
    public byte getType() {
        return type;

    }

    public void setType(byte type) {
        this.type = type;
    }

}
