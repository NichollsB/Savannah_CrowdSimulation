package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Boid extends Object {

    public static float MAX_SPEED = 2f;

   //public Vector3 position; //now held in Super class.

    private Vector3 velocity;
//    private Vector3 orientation;
//    private float f_orientation;


    public byte species;
    public float maxSpeed = 2f;
    public float maxForce = 0.03f; //30 degree?


    public Boid() {
        type = 1; // this is for categorising it as a "boid" object.
        species = 0;
        position = new Vector3();
        velocity = new Vector3();
//        orientation = new Vector3();
    }

    public Boid( Vector3 pos, Vector3 vel) {
        this.type = 1; // this is for categorising it as a "boid" object.
        species = 0;
        position = pos.cpy();
        velocity = vel.cpy();
//        orientation = new Vector3();
    }
    public Boid(byte spec) {
        this.type = 1; // this is for categorising it as a "boid" object.
        species = spec;
        position = new Vector3();
        velocity = new Vector3();
//        orientation = new Vector3();
    }

    public Boid(byte spec, Vector3 pos, Vector3 vel) {
        this.type = 1; // this is for categorising it as a "boid" object.
        species = spec;
        position = pos.cpy();
        velocity = vel.cpy();
//        orientation = new Vector3();
    }

    public void move(Vector3 acceleration) {
        //TODO: Add in better limiter for speed.
//        limitVelocityChange(velocityChange);

        velocity.add(acceleration).limit(MAX_SPEED);

        //move
        position.add(velocity);

        //check for out of bounds
        checkInBounds();
    }

//    /**
//     * This will do checks to make sure the boid cannot turn too sharply or increase its speed too heavily.
//     *
//     * @param change the velocity that the boid wants to change by.
//     */
//    public void limitVelocityChange(Vector3 change) {
//        Vector3 oldVelTarget = new Vector3().set(position).add(velocity);
//        Vector3 newVelTarget = new Vector3().set(position).add(change);
//
//        float dotProd = oldVelTarget.dot(newVelTarget);
//
//        float cosA =  dotProd/ (newVelTarget.len() + oldVelTarget.len() );
//
//        float angle = MathUtils.cos(cosA);
//
//        System.out.println(angle);
//    }



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


    public Vector3 getPosition() {
        return position;//.cpy();
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
        return species;
    }

}
