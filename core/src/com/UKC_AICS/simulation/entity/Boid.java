package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Emily
 */
public class Boid {
    
    public Vector3 position;
    public Vector3 velocity;
    public Vector3 orientation;


    
    public byte species;

    
    public Boid() {
        species = 0;
        position = new Vector3();
        velocity = new Vector3();
        orientation = new Vector3();
    }
    
       public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public Vector3 getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector3 orientation) {
        this.orientation = orientation;
    }
    
    public byte getSpecies() {
        return species;
    }
    
}
