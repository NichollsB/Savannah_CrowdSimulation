package com.UKC_AICS.simulation.entity;

/**
 * Created by james on 03/07/2014.
 */
public class Species {
    private String name;
    private int number;
    private byte spbyte;
    private String spriteLocation;

    private float sightRadius;
    private float flockRadius;
    private float nearRadius;

    private float maxSpeed;
    private float maxForce;

    private float cohesion;
    private float separation;
    private float alignment;
    private float wander;
    private float lifespan;
    private String diet;

    private float rgb[];
    
    //Added by Ben Nicholls for information output/graphics/ui
	public boolean tracked;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(float number) {
        this.number = (int)number;
    }

    public byte getSpbyte() {
        return spbyte;
    }
    public void setSpbyte(byte spbyte) {
        this.spbyte = spbyte;
    }

  

    public float getNearRadius() {
        return nearRadius;
    }
    public void setNearRadius(float nearRadius) {
        this.nearRadius = nearRadius;
    }

    public float getFlockRadius() {
        return flockRadius;
    }
    public void setFlockRadius(float flockRadius) {
        this.flockRadius = flockRadius;
    }

    public float getSightRadius() {
        return sightRadius;
    }
    public void setSightRadius(float sightRadius) {
        this.sightRadius = sightRadius;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getMaxForce() {
        return maxForce;
    }
    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public float getCohesion() {
        return cohesion;
    }
    public void setCohesion(float cohesion) {
        this.cohesion = cohesion;
    }

    public float getSeparation() {
        return separation;
    }
    public void setSeparation(float separation) {
        this.separation = separation;
    }

    public float getAlignment() {
        return alignment;
    }
    public void setAlignment(float alignment) {
        this.alignment = alignment;
    }

    public float getWander() {
        return wander;
    }
    public void setWander(float wander) {
        this.wander = wander;
    }

    @Override
    public String toString() {
        return "Species [name=" + name + ", number=" + number + ", byte="
                + spbyte + ", cohesion=" + cohesion + ", separation=" + separation + ", alignment=" + alignment + ", wander=" + wander + "]";
    }

    public float getLifespan() {
        return lifespan;
    }
    public void setLifespan(float lifespan) {
        this.lifespan = lifespan;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }
    
    public String getSpriteLocation() {
        return spriteLocation;
    }
    public void setSpriteLocation(String spriteLocation) {
        this.spriteLocation = spriteLocation;
    }
    public boolean hasSpriteLocation(){
    	if(spriteLocation != null && !spriteLocation.isEmpty())
    		return true;
    	return false;
    }
    //Added by Ben Nicholls for graphics purposes - storing sprite colour
    public void setRGB(float[] rgb){
    	this.rgb = rgb;
    }
    public float[] getRGB(){
    	return this.rgb;
    }
    public boolean hasRGB(){
    	if(rgb != null && rgb.length == 3){
    		return true;
    	}
    	return false;
    }
    
    //For tracking/gui/graphics
    public void setTracked(boolean tracked){
    	this.tracked = tracked;
    }
    
}
