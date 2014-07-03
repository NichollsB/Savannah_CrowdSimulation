package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Boid extends Entity {

    //boids own specific variants on the species.
    public float maxSpeed = 2f;
    public float maxForce = 0.03f; //30 degree?


    public Boid() {
        type = 1; // this is for categorising it as a "boid" object.
        subType = 1;
        position = new Vector3();
        velocity = new Vector3();
//        orientation = new Vector3();
    }

    public Boid( Vector3 pos, Vector3 vel) {
        this.type = 1; // this is for categorising it as a "boid" object.
        subType = 1;
        position = pos.cpy();
        velocity = vel.cpy();
//        orientation = new Vector3();
    }
    public Boid(byte spec) {
        this.type = 1; // this is for categorising it as a "boid" object.
        subType = spec;
        position = new Vector3();
        velocity = new Vector3();
//        orientation = new Vector3();
    }

    public Boid(byte spec, Vector3 pos, Vector3 vel) {
        this.type = 1; // this is for categorising it as a "boid" object.
        subType = spec;
        position = pos.cpy();
        velocity = vel.cpy();
//        orientation = new Vector3();
    }

    public void move(Vector3 acceleration) {
        //TODO: Add in better limiter for speed.
//        limitVelocityChange(velocityChange);

        velocity.add(acceleration).limit(maxSpeed);

        //move
        position.add(velocity);

        //check for out of bounds
        checkInBounds();
    }



    private void checkInBounds() {
        //TODO make this access the simulation map size, as this will be different from screen size eventually.
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


//    public Vector3 getOrientation() {
//        return orientation;
//    }
//
//    public void setOrientation(Vector3 orientation) {
//        this.orientation = orientation;
//    }
//    public void setOrientation(float x, float y, float z) {
//        this.orientation = new Vector3(x,y,z);
//    }

    public byte getSpecies() {
        return subType;
    }

}
