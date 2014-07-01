package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Boid extends Object {

    public static float MAX_SPEED = 2f;
   //public Vector3 position;
    private Vector3 velocity;
    private Vector3 orientation;


    public static byte species;
    public float maxSpeed = 2f;


    public Boid() {
        type = 1; // this is for categorising it as a "boid" object.
        species = 0;
        position = new Vector3();
        velocity = new Vector3();
        orientation = new Vector3();
    }



    public void move(Vector3 velocityChange) {
        //TODO: Add in better limiter for speed.
        velocity.add(velocityChange).limit(MAX_SPEED);
        position.add(velocity);

        if(position.x > Gdx.graphics.getWidth()) {
            position.x -= Gdx.graphics.getWidth();
        } else if(position.x < 0) {
            position.x += Gdx.graphics.getWidth();
        }

        if(position.y > Gdx.graphics.getHeight()) {
            position.y -= Gdx.graphics.getHeight();
        } else if(position.y < 0) {
            position.y += Gdx.graphics.getHeight();
        }
    }


    public Vector3 getPosition() {
        return position.cpy();
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
    public void setPosition( float x, float y, float z) {
        this.position = new Vector3(x, y, z);
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    /**
     * explicit setting to a defined velocity.
     *
     * @param velocity the speed the boid's velocity will be set to.
     */
    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }
    public void setVelocity(float x, float y, float z) {
        this.velocity = new Vector3(x, y, z);
    }

    /**
     * This will be added to the boids velocity, but there will be checks to make sure of max acceleration etc.
     *
     * @param change the velocity to add
     */
    public void changeVelocity(Vector3 change) {
        //TODO: implement the change.
    }

    public Vector3 getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector3 orientation) {
        this.orientation = orientation;
    }
    public void setOrientation(float x, float y, float z) {
        this.orientation = new Vector3(x,y,z);
    }

    public byte getSpecies() {
        return species;
    }

}
