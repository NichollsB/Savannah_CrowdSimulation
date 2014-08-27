package com.UKC_AICS.simulation.entity;

public enum EntityTypes {
	CORPSE((byte)0),
	BOID((byte)1),
	ATTRACTOR((byte)2),
	REPELLOR((byte)3),
	OBSTACLE((byte)4);
	
	private byte typeByte;
	
	EntityTypes(byte type){
		this.typeByte = type;
	}
	public byte getByte(){
		return this.typeByte;
	}
	
}
