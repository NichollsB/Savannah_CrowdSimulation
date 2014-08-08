package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Object extends Entity {

    float mass;

    public Object(byte type, byte subType, Vector3 position, float mass) {
        this.type = type;
        this.subType = subType;
        this.position = position;
        initMass(mass);
    }

    public Object(byte type, byte subType, int x, int y) {
        this.type = type;
        this.subType = subType;
        this.position = new Vector3(x, y, 0);
        initMass(mass);
    }
    
    public Object(ObjectData objData, int x, int y) {
        this.type = objData.getType();
        this.subType = objData.getSubType();
        this.position = new Vector3(x, y, 0);
        initMass();
    }

    private void initMass(float mass) {
        if(type == (byte) 0) {
//            this.mass = mass;
            this.mass = 50f;
        }
    }

    public float getMass() {
        return mass;
    }

    public void reduceMass(float amount) {
        mass -= amount;
    }
    
    @Override
    public String toString() {
    	String string = "";
        string += "OBJECT: " + "\t" + "\t position: \n \t" + position.toString() ;


    	return string;
    }
    
    @Override
    public byte getTertiaryType(){
    	return subType;
    }

}
