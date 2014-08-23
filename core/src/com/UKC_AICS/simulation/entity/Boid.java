package com.UKC_AICS.simulation.entity;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Boid extends Entity {

    //boids own specific variants on the species.
    public float maxSpeed = 2f;
    public float maxForce = 0.03f; //
    private Vector3 acceleration = new Vector3();
    private float sprintThreshold = maxSpeed*0.7f;

    public float maxStamina = 60;
    public float stamina = maxStamina;

    public float sightRadius = 200f;
    public float flockRadius = 100f;
    public float nearRadius = 20f;

    public float hunger = 40;
    public float thirst = 40;
    public float panic = 0;
    public float fertility = 0;

    public float hungerLevel=70;
    public float thirstLevel=70;
    public float panicLevel=30;

    public String state = "default";

    public int age = 0;
    public float numberOfOffspring = 0;

//    public byte tertiaryType = 0; //family group of boid.

    // Weights for the flocking behaviours.
    public float cohesion = 0;
    public float separation = 0;
    public float alignment = 0;
    public float wander = 0;

    //EA variables.
    
    public int geneSize = 11;		
    public Float[] gene= new Float[geneSize];


    // no longer used or relevant
    public Boid(byte spec, Vector3 pos, Vector3 vel) {
        this(SimulationManager.speciesData.get(spec));
        position = pos.cpy();
        velocity = vel.cpy();
    }
//    public Boid( Vector3 pos, Vector3 vel) {
//        this((byte)1, pos, vel);
//    }

    public Boid(byte spec) {
        this(spec, new Vector3(), new Vector3());
    }


    /**
     * constructor that takes the standard species values from the species file provided.
     * @param species
     */
    public Boid(Species species) {
        type = 1;
        subType =  species.getSpbyte();
        nearRadius = species.getNearRadius();
        sightRadius = species.getSightRadius();
        flockRadius = species.getFlockRadius();

        maxSpeed = species.getMaxSpeed();

        maxForce = species.getMaxForce();

        maxStamina = species.getStamina();
        stamina = maxStamina;

        position = new Vector3(500f,500f,0f);
        velocity = new Vector3();
        orientation = Math.toDegrees(Math.atan2( - velocity.x, velocity.y));

        cohesion = species.getCohesion();
        alignment = species.getAlignment();
        separation = species.getSeparation();
        wander = species.getWander();

       
        panicLevel = species.getPanicLevel();
        hungerLevel = species.getHungerLevel();
        thirstLevel = species.getThirstLevel();

        setGene(cohesion,separation,alignment,wander,flockRadius, nearRadius, sightRadius, maxStamina,  panicLevel, hungerLevel , thirstLevel);

        size = species.getMaxSize();
        bounds.set(position.x, position.y, size, size);//*1.5f, size*1.5f);
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
        tertiaryType = boid.tertiaryType;
        nearRadius = boid.nearRadius;
        sightRadius = boid.sightRadius;
        flockRadius = boid.flockRadius;

        maxSpeed = boid.maxSpeed;
        maxForce = boid.maxForce;

        position = new Vector3(boid.getPosition());
        velocity = new Vector3(boid.getVelocity());

        orientation = Math.toDegrees(Math.atan2( - velocity.x, velocity.y));

        velocity = new Vector3(); //boid.getVelocity());

        hunger = boid.hunger;
        thirst = boid.thirst;
        age = boid.age;

        bounds = new Rectangle(boid.bounds);

        cohesion = boid.cohesion;
        alignment = boid.alignment;
        separation = boid.separation;
        wander = boid.wander;

        panicLevel = boid.panicLevel;
        thirstLevel = boid.thirstLevel;
        hungerLevel = boid.hungerLevel;
        
        setGene(cohesion,separation,alignment,wander,flockRadius, nearRadius, sightRadius, maxStamina,  panicLevel, hungerLevel , thirstLevel);


    }


    public void setAcceleration(Vector3 acceleration) {
        this.acceleration.set(acceleration);
    }


    public void update() {
        //TODO: Add in better limiter for speed. Possibly??
        //move
//        velocity.sub(acceleration.set(velocity).scl(0.08f));  //drag??
        velocity.add(acceleration).limit(maxSpeed);
        velocity.sub(acceleration.set(velocity).scl(0.04f)); //drag
        //TODO add method to calc stamina usage -> based on velocity.len % of maxspeed - 0-1
        //TODO make it so stamina must be above xx amount to move

        //Stamina related calcs
        float speed = velocity.len();
        if(speed > sprintThreshold)
            useStamina(speed);
        else recoverStamina(speed);
        position.add(velocity);

        //check for out of bounds
        checkInBounds();

//        bounds.setPosition(position.x, position.y);
        //TODO: potentially have different species "degrade" at different rates
        hunger += (float) 0.5 /60;
        thirst += (float) 1 /60;

        fertility += 0.1/60;

//        bounds.setPosition(position.x - bounds.width/2, position.y - bounds.height/2);
        bounds.setPosition(position.x - bounds.width/2, position.y - bounds.height/2);
    }

    /**
     * Should be called when boid is moving over the "sprintThreshold" 70% of maxSpeed
     * @param speed current speed, length of boids velocity
     * @return  true if stamina is used // this may be redundant now.
     */
    private boolean useStamina(float speed) {
        boolean haveStamina = stamina > 0;
        float sprintThreshold = maxSpeed*0.7f;
        float staminaChange = (speed-sprintThreshold)*0.3f;  //+ if using stamina / - if regaining stamina
        //has stamina and
        if(haveStamina && stamina - staminaChange > 0) {
            stamina -= staminaChange;
            if(stamina > maxStamina)
                stamina = maxStamina;
        }
        else {
            System.out.print("run out of stamina, old speed " + speed);
            velocity.scl(sprintThreshold/speed*0.8f);
            System.out.println(" ,new speed = " + velocity.len());
        }
        return stamina > 0;
    }

    /**
     * Recovers the stamina of the boid if it is not travelling over "sprintThreshold"
     * @param speed of the boid, length of velocity
     */
    private void recoverStamina(float speed) {
        stamina -= (speed-sprintThreshold)*0.3f;
        if(stamina > maxStamina)
            stamina = maxStamina;
    }


    /**
     * checks the boids position against the mapwidth held in constants. Adjusts position to within bounds (Wraps around)
     */
    private void checkInBounds() {
        if(position.x > Constants.mapWidth - bounds.height/2) {
            position.x = position.x - Constants.mapWidth + bounds.height;
        } else if(position.x < + bounds.width/2) {
            position.x = position.x + Constants.mapWidth - bounds.height;
        }
        if(position.y > Constants.mapHeight - bounds.width/2) {
            position.y = position.y - Constants.mapHeight + bounds.width;
        } else if(position.y < bounds.width/2) {
            position.y = position.y + Constants.mapHeight - bounds.width;
        }
    }

    public Vector3 getPosition() {
        return position;//.cpy();
    }

    public void setPosition(Vector3 position) {
        this.position = position;
        this.bounds.setPosition(position.x, position.y);
    }
    public void setPosition( float x, float y, float z) {
        setPosition(new Vector3(x, y, z));
    }



    public void setAge(int newAge) {
        age = newAge;
    }
    public void age() {
        float maturity = SimulationManager.speciesData.get(getSpecies()).getMaturity();
        if (age < maturity && size < SimulationManager.speciesData.get(getSpecies()).getMaxSize()){
            size += ((100 - hunger)/100) * SimulationManager.speciesData.get(getSpecies()).getGrowthPerDay();
        }
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

    public Vector3 getVelocity() {
        return velocity;
    }

    public double getOrientation() {
        if(velocity.len2() > 0) {
            double newOrt = Math.toDegrees(Math.atan2(-velocity.x, velocity.y));
            orientation = newOrt;
            if (newOrt != orientation) {
                double change = orientation - newOrt;
                orientation = orientation + change / 2;
                //            orientation /= 2;
            }

            while (orientation > 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;

            }
            return newOrt; //made x negative.
        }
        else {
            return orientation;
        }
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }

    public byte getSpecies() {
        return subType;
    }
    @Override
    public String toString() {
        String string = "";

        string += "type: \t" + SimulationManager.speciesData.get(getSpecies()).getName();
        string += "\t position: \t" + (int)position.x + "/" + (int)position.y;
        string += "\n\t group:" + tertiaryType;
        string += "\n\t hunger:" + (int)hunger;
        string += "\n\t thirst:" + (int)thirst;
        string += "\n\t panic:" + (int)panic + "/" + panicLevel;
        string += "\n\t age:" + age ;
        string += "\n\t fertility:" + fertility;
        string += "\n\t stamina:" + stamina;
        string += "\n\t size:" + size ;
        string += "\n\t state:" + state;
        string += "\n\t orientation:" + (int)orientation;
        string += "\n";
//        string += "\n\t Weightings";
        string += "\n\t cohesion:" + cohesion;
        string += "\n\t separation:" + separation;
        string += "\n\t alignment:" + alignment;
        string += "\n\t wander:" + wander;


        string += "\n";
        string += "\n\t bounds pos:" + (int)bounds.x + "," + (int)bounds.y + "," + (int)bounds.width + "," + (int)bounds.height + ",";
        ;

        return string;
    }

    public void setTracked(boolean tracked){
    	this.tracked = tracked;
    }

    public void setGene(float cohesion, float separation, float alignment, float wander, float flockRadius, float nearRadius, float sightRadius,
    		float maxStamina, float hungerLevel, float thirstLevel, float panicLevel) {

    	gene[0] = cohesion;
    	gene[1] = separation;
    	gene[2] = alignment;
    	gene[3] = wander;
    	gene[4] = flockRadius; 
    	gene[5]	= nearRadius; 
    	gene[6]	= sightRadius;
    	gene[7] = maxStamina;
    	gene[8] = hungerLevel;
    	gene[9] = thirstLevel;
    	gene[10] = panicLevel;
    				
    	
    }      
    
    public void setGene(Float[] newGene ) {
    	for(int i = 0 ; i<geneSize; i++){
    		gene[i] = newGene[i];
    	}
    	setCohesion(newGene[0]); 
    	setSpearation(newGene[1]);
    	setAlignment(newGene[2]);
    	setWander(newGene[3]);
    	setFlockRadius(newGene[4]);
    	setNearRadius(newGene[5]);
    	setSightRadius(newGene[6]);
    	setMaxStamina(newGene[7]);
    	setHungerLevel(newGene[8]);
    	setThirstLevel(newGene[9]);
    	setPanicLevel(newGene[10]);
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
    public void setHunger( float hunger) {
        this.hunger = hunger;
    }
    public float getHunger() {
    	return hunger;
    }
    public void setThirst( float thirst) {
        this.thirst = thirst;
    }
    public float getThirst() {
    	return thirst;
    }
    public void setPanic( float panic) {
        this.panic = panic;
    }
    public float getPanic() {
    	return panic;
    }
    public void setNearRadius( float nearRadius) {
        this.nearRadius = nearRadius;
    }
    public float getNearRadius() {
    	return nearRadius;
    }
    public void setSightRadius( float sightRadius) {
        this.sightRadius = sightRadius;
    }
    public float getSightRadius() {
    	return sightRadius;
    }
    public void setFlockRadius( float flockRadius) {
        this.flockRadius = flockRadius;
    }
    public float getFlockRadius() {
    	return flockRadius;
    }
    /**
     * Added by ben Nicholls as replacement for getSpecies(), etc - rather pull from entity
     */
    public void setGroup( byte group) {
        tertiaryType= group;
    }
    public byte getGroup() {
    	return tertiaryType;
    }
    public void setStamina(Float newStamina){
    	stamina = newStamina;
    }
    public float getStamina(){
    	return stamina;
    }

    public void setMaxStamina(Float newMaxStamina){
    	maxStamina = newMaxStamina;
    }
    public float getMaxStamina(){
    	return maxStamina;
    }
    public void setThirstLevel( float newThirstLevel) {
        thirstLevel = newThirstLevel;
    }
    public void setHungerLevel( float newHungerLevel) {
    	hungerLevel = newHungerLevel;
    }
    public void setPanicLevel( float newPanicLevel) {
        panicLevel = newPanicLevel;
    }
    public float getThirstLevel() {
        return thirstLevel;
    }
    public float getHungerLevel() {
        return hungerLevel;
    }
    public float getPanicLevel() {
        return panicLevel;
    }
    public void setSize( float newSize) {
    	size = newSize;
    }
    public float getSize() {
    	return size;
    }
    public void setFertility(float fertility){
    	this.fertility=fertility;
    }
    public float getFertility(){
    	return fertility;
    }
    public void setNumberOfOffspring(float numberOfOffspring){
    	this.numberOfOffspring=numberOfOffspring;
    }
    public float getNumberOfOffspring(){
    	return numberOfOffspring;
    }
}
