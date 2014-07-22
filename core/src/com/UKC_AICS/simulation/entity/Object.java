package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Object extends Entity {

    float mass;

    public Object(byte type, byte subType, Vector3 position) {
        this.type = type;
        this.position = position;
        initMass();
    }

    public Object(byte type, byte subType, int x, int y) {
        this.type = type;
        this.subType = subType;
        this.position = new Vector3(x, y, 0);
        initMass();
    }
    
    public Object(ObjectData objData, int x, int y) {
        this.type = objData.getType();
        this.subType = objData.getSubType();
        this.position = new Vector3(x, y, 0);
    }

    private void initMass() {
        if(type == (byte) 0) {
            mass = 100f;
        }
    }

    public float getMass() {
        return mass;
    }

    public void reduceMass(float amount) {
        mass -= amount;
    }

}
