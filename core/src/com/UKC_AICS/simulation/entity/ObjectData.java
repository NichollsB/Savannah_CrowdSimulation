package com.UKC_AICS.simulation.entity;

/**
 * Created by Emily on 07/07/2014.
 */
public class ObjectData {

    private byte type;
    private byte subType;
    private String imagePath;
    private String name;

//    public ObjectData(byte type, byte subType, String imagePath) {
//        this.type = type;
//        this.subType = subType;
//        this.imagePath = imagePath;
//    }
    public ObjectData(byte type, byte subType, String name) {
        this.type = type;
        this.subType = subType;
        this.name = name;
    }
    public byte getType() {
        return type;
    }

    public byte getSubType() {
        return subType;
    }

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
