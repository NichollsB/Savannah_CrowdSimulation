package com.UKC_AICS.simulation.entity;

import com.UKC_AICS.simulation.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Boid extends Entity {
    public enum State {
        DEFAULT,
        HUNGRY,
        THIRSTY,
        EVADE;

        public int getStateID() {
            return ordinal();
        }
    }
    //boids own specific variants on the species.
    public float maxSpeed = 2f;
    public float maxForce = 0.03f; //
    private Vector3 acceleration = new Vector3();

    public float sightRadius = 200f;
    public float flockRadius = 100f;
    public float nearRadius = 20f;

    public Rectangle bounds = new Rectangle();

    public float hunger = 0;
    public float thirst = 0;
    public float panic = 0;

    public State state = State.DEFAULT;

    public int age = 0;
    public int birthDay = 0;

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

        bounds.set(position.x, position.y, 10, 10);
    }

    /**
     *
     * Copy constructor
     *
     * @param boid the boid to make a copy of.
     */
    public Boid(Boid boid) {
        type = 1;
        subType =  boid.getSubType();
        nearRadius = boid.nearRadius;
        sightRadius = boid.sightRadius;
        flockRadius = boid.flockRadius;

        maxSpeed = boid.maxSpeed;
        maxForce = boid.maxForce;

        position = new Vector3(boid.getPosition());
        velocity = new Vector3(boid.getVelocity());

        hunger = boid.hunger;
        thirst = boid.thirst;
        age = boid.age;

        bounds = boid.bounds;
    }


    public void setAcceleration(Vector3 acceleration) {
        this.acceleration.set(acceleration);
    }

    public void move() {
        //TODO: Add in better limiter for speed. Possibly??
        //move
        velocity.add(acceleration).limit(maxSpeed);

        velocity.sub(acceleration.set(velocity).scl(0.05f)); //drag attempt; using the acceleration vector (not actually anything to do with acceleration)

        position.add(velocity);
        bounds.setPosition(position.x, position.y);
        //check for out of bounds
        checkInBounds();


        //TODO: potentially have different species "degrade" at different rates
        hunger -= (float) 0.5 /60;
        thirst -= (float) 2 /60;
    }



    private void checkInBounds() {
        //TODO make this access the simulation map size, as this will be different from screen size eventually.
        if(position.x > Constants.screenWidth) {
            position.x -= Constants.screenWidth;
        } else if(position.x < 0) {
            position.x += Constants.screenWidth;
        }

        if(position.y > Constants.screenHeight) {
            position.y -= Constants.screenHeight;
        } else if(position.y < 0) {
            position.y += Constants.screenHeight;
        }
    }



    public Vector3 getPosition() {
        return position;//.cpy();
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
    public void setPosition( float x, float y, float z) {
        setPosition(new Vector3(x, y, z));
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setBirthDay(int birthDay) {
       this.birthDay = birthDay;     
    }   
    
    public int getBirthDay() {
    	return birthDay;    	
    }

    public void setAge(int newAge) {
        age = newAge;
    }
    public void age() {
        age++;
    }

    public int getAge() {
    	return age;
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

    public String toString() {
        String string = "";

        string += "BOID: " + "\n" + "\t position:" + position.toString() + "\n";
        string += "\t hunger:" + hunger + "\n";
        string += "\t thirst:" + thirst + "\n";
        string += "\t age:" + age + "\n";

        return string;
    }

}
