package com.UKC_AICS.simulation.entity;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.entity.states.herbivore.Reproduce;
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

    public float hunger = 40;
    public float thirst = 40;
    public float panic = 0;

    public String state = "default";

    public int age = 0;
    public int birthDay = 0;
    

    //Added to check if boid info is being displayed or not - for highlighting in graphics
	public boolean tracked = false;

    public float cohesion = 0;
    public float separation = 0;
    public float alignment = 0;
    public float wander = 0;
    
    public int geneSize=4;		
    public Float[] gene= new Float[geneSize];


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

        position = new Vector3(500f,500f,0f);
        velocity = new Vector3();
        bounds.set(position.x, position.y, 16, 16);
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
        velocity = new Vector3(); //boid.getVelocity());

        hunger = boid.hunger;
        thirst = boid.thirst;
        age = boid.age;

        bounds = new Rectangle(boid.bounds);
    }


    public void setAcceleration(Vector3 acceleration) {
        this.acceleration.set(acceleration);
    }


    public void move() {
        //TODO: Add in better limiter for speed. Possibly??
        //move
//        velocity.sub(acceleration.set(velocity).scl(0.08f));  //drag??
        velocity.add(acceleration).limit(maxSpeed);
        bounds.setPosition(position.x, position.y);
        velocity.sub(acceleration.set(velocity).scl(0.04f)); //drag
        position.add(velocity);
        //check for out of bounds
        checkInBounds();


        //TODO: potentially have different species "degrade" at different rates
        hunger -= (float) 0.5 /60;
        thirst -= (float) 2 /60;
    }

    public void setNewVelocity(Vector3 newVel){
        velocity.set(newVel);
    }

    private void checkInBounds() {
        //TODO make this access the simulation map size, as this will be different from screen size eventually.
        if(position.x > Constants.screenWidth - bounds.width/2) {
//            System.out.print("out X " + position.x);
            position.x = position.x - Constants.screenWidth + bounds.height;
//            System.out.println(" adjusted to " + position.x);
        } else if(position.x <  bounds.width/2) {
//            System.out.print("out X " + position.x);
            position.x = position.x + Constants.screenWidth - bounds.height;
//            System.out.println(" adjusted to " + position.x);
        }

        if(position.y > Constants.screenHeight - bounds.height/2) {
//            System.out.print("out Y " + position.y);
            position.y = position.y - Constants.screenHeight + bounds.width;
//            System.out.println(" adjusted to " + position.y);
        } else if(position.y < bounds.height/2) {
//            System.out.print("out Y " + position.y);
            position.y = position.y + Constants.screenHeight - bounds.width;
//            System.out.println(" adjusted to " + position.y);
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
    public void setState(String state) {
        this.state = state;
    }

    public byte getSpecies() {
        return subType;
    }
    @Override
    public String toString() {
        String string = "";

        string += "BOID: " + "\t" + "\t position: \n \t" + position.toString() ;
        string += "\n\t hunger:" + hunger;
        string += "\n\t thirst:" + thirst;
        string += "\n\t age:" + age ;
        string += "\n\t state:" + state;

        return string;
    }

    public void setTracked(boolean tracked){
    	this.tracked = tracked;
    }

    public void setGene(float cohesion, float separation, float alignment, float wander ) {
    	gene[0] = cohesion;
    	gene[1] = separation;
    	gene[2] = alignment;
    	gene[3] = wander;
    	
    }      
    
    public Float[] getGene() {
    	return gene;
    }
      
    public void setCohesion( float cohesion) {
        this.cohesion = cohesion; 
    }
    public void setSpearation( float separation) {
    	this.separation = separation;
    }
    public void setAlignment( float alignment) {
    	this.alignment = alignment;
    }
    public void setWander( float wander) {
        this.wander = wander;
    }
    public float getCohesion() {
    	return cohesion;
    }
    public float getSeparation() {
    	return separation;
    }
    public float getAlignment() {
    	return alignment;
    }
    public float getWander() {
        return wander;
    }

}
