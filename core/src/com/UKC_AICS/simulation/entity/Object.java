package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 * @author Emily
 */
public class Object extends Entity {

    public Object(byte type, byte subType, Vector3 position) {
        this.type = type;
        this.position = position;
        initCircle();
    }

    public Object(byte type, byte subType, int x, int y) {
        this.type = type;
        this.subType = subType;
        this.position = new Vector3(x, y, 0);
        initCircle();
    }
    
    public Object(ObjectData objData, int x, int y) {
        this.type = objData.getType();
        this.subType = objData.getSubType();
        this.position = new Vector3(x, y, 0);
        initCircle();
    }
}
