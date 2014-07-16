package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Boid extends Entity {

    //boids own specific variants on the species.
    public float maxSpeed = 2f;
    public float maxForce = 0.03f; //
    
    //Present for each species
     // - individula weightings will go here.

    public float sightRadius = 200f;
    public float flockRadius = 100f;
    public float nearRadius = 20f;

    private Vector3 acceleration = new Vector3();



    public static int Age = 0;
    public static int birthDay = 0;

    public Boid( Vector3 pos, Vector3 vel) {
        this.type = 1; // this is for categorising it as a "boid" object.
        subType = 1;
        position = pos.cpy();
        velocity = vel.cpy();
//        orientation = new Vector3();
        initCircle();
    }
    public Boid(byte spec) {
        this.type = 1; // this is for categorising it as a "boid" object.
        subType = spec;
        position = new Vector3();
        velocity = new Vector3();
//        orientation = new Vector3();
        initCircle();
    }

    public Boid(byte spec, Vector3 pos, Vector3 vel) {
        this.type = 1; // this is for categorising it as a "boid" object.
        subType = spec;
        position = pos.cpy();
        velocity = vel.cpy();
//        orientation = new Vector3();
        initCircle();
    }


    public Boid(Species species) {
        type = 1;
        subType =  species.getSpbyte();
        nearRadius = species.getNearRadius();
        sightRadius = species.getSightRadius();
        flockRadius = species.getFlockRadius();

        maxSpeed = species.getMaxSpeed();
        maxForce = species.getMaxForce();

        position = new Vector3();
        velocity = new Vector3();
        initCircle();
    }


    public void setAcceleration(Vector3 acceleration) {

        this.acceleration.set(acceleration);
    }


    public void move() {
        //TODO: Add in better limiter for speed. Possibly??
        //move
//        velocity.sub(acceleration.set(velocity).scl(0.08f));  //drag??
        velocity.add(acceleration).limit(maxSpeed);
        velocity.sub(acceleration.set(velocity).scl(0.04f)); //drag
        position.add(velocity);
        updateCircle();
        //check for out of bounds
        checkInBounds();
    }

    public void setNewVelocity(Vector3 newVel){
        velocity.set(newVel);
    }

    public void move2() {
        position.add(velocity);
        updateCircle();
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

    public void setBirthDay(int birthDay) {
       this.birthDay = birthDay;     
    }   
    
    public static int getBirthDay() {
    	return birthDay;    	
    }
    
    public static void setAge(int newAge) {
    	Age = newAge;	 
    }
    public static int getAge() {
    	return Age;	 
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
        return subType;
    }

}
