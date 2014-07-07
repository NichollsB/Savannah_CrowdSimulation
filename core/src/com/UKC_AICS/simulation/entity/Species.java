package com.UKC_AICS.simulation.entity;

/**
 * Created by james on 03/07/2014.
 */
public class Species {
    private String name;
    private int number;
    private byte spbyte;
    private float cohesion;
    private float separation;
    private float alignment;
    private float wander;

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

    private float sightRadius;
    private float flockRadius;
    private float nearRadius;
    private String spriteLocation;

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

    public String getSpriteLocation() {
        return spriteLocation;
    }
    public void setSpriteLocation(String spriteLocation) {
        this.spriteLocation = spriteLocation;
    }

    @Override
    public String toString() {
        return "Species [name=" + name + ", number=" + number + ", byte="
                + spbyte + ", cohesion=" + cohesion + ", separation=" + separation + ", alignment=" + alignment + ", wander=" + wander + "]";
    }
}
