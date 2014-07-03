package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 30/06/2014.
 */
public class Object {
    protected Vector3 position; // explicitly 3d vectors for extensibility
    protected static byte type = 0; //what type of object is it? // 1 == boid(any boid),


    /**
     *
     * @return the position of the object as a Vector3 for its map location.
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     *
     * @param position sets the position of the object to this map location.
     */

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public byte getType(){
        return type;
    }

}
