package com.UKC_AICS.simulation.entity;

import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Emily
 */
public class WorldObject {

	private byte type;
	private Vector3 position; // explicitly 3d vectors for extensibility


	public WorldObject(byte type, Vector3 position) {
		this.type = type;
		this.position = position;
	}

	/**
	 * 
	 * @return the type of the object as its byte type
	 */
	public byte getType() {
		return type;

	}

	// NOT ALLOWED TO CHANGE THE TYPE? At least once created?
	//	    public void setType(byte type) {
	//	        this.type = type;
	//	    }

	/**
	 * 
	 * @return the position of the object as a Vector3 for its map location.
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * 
	 * @param position sets the position of the object to this map location.
	 */

	public void setPosition(Vector3 position) {
		this.position = position;
	}

}
