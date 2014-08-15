package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Emily on 30/06/2014.
 */
public class Entity {
    public Vector3 position; // explicitly 3d vectors for extensibility
    public Vector3 velocity; // explicitly 3d vectors for extensibility
    public double orientation; // explicitly 3d vectors for extensibility

    public byte type = 0; //what category of object is it? // 1 == boid(any boid),
    public byte subType = 0; //what type of object is it? // for boids this would be subType(species)
    
    //Added by Ben Nicholls
    public byte tertiaryType = 0;//Any additional sub-classification of boid or object
    //Added to check if boid info is being displayed or not - for highlighting in graphics
  	public boolean tracked = false;
  	public boolean toDelete = false;
    /**
     *
     * @return the position of the object as a Vector3 for its map location.
     */
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(int x, int y, int z) {
        setPosition(new Vector3(x,y,z));
    }

    /**
     *
     * @param position sets the position of the object to this location.
     */

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public byte getType(){
        return type;
    }
    public void setType(byte type) {
        this.type = type;
    }

    public byte getSubType() {
        return subType;
    }
    public void setSubType(byte subType) {
        this.subType = subType;
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
        setVelocity(new Vector3(x, y, z));
    }
    
    public String toString(){
    	 String string = "";

         string += "ENTITY: " + "\t" + "\t position: \n \t" + position.toString() ;
         return string;
    }
    public double getOrientation() {
        return orientation;
    }
    
    /**
     * Added by ben nicholls
     * @param tracked
     */
    public void setTracked(boolean tracked){
    	this.tracked = tracked;
    }

	public byte getTertiaryType() {
		return tertiaryType;
	}

	public void delete() {
		this.toDelete = true;
	}

}
