package com.UKC_AICS.simulation.screen.controlutils;

import com.UKC_AICS.simulation.entity.Entity;

/**
 * Used to keep track of the selected entity in the GUI. 
 * Used primarily to keep track of the types of the entity selected for the explicit creation of new boids
 * @author Ben Nicholls
 *
 */
public abstract class SelectedEntity{
	
//	public static final Byte TYPE = new Byte((byte) 0);
//	public static final Byte SUBTYPE = new Byte((byte) 0);
//	public static final Byte GROUP = new Byte((byte)0);
//	public static final Boolean selected = new Boolean(false);
	private static byte TYPE;
	private static byte SUBTYPE;
	private static byte GROUP;
	private static boolean SELECTED;
	private static boolean BOID = false;
	private static Entity entity;
	
	/**
	 * Set the SELECTED boolean flag
	 * @param select Applied to SELECTED flag
	 */
	public static void set(boolean select){
		SELECTED = select;
	}
	/**
	 * Set the currently selected entity
	 * @param type byte type of the selected entity
	 * @param subtype byte subtype of the selected entity
	 * @param group byte group of the selected entity
	 */
	public static void set(byte type, byte subtype, byte group){
		if(type == 1){
			BOID = true;
		}
		else
			BOID = false;
		TYPE = type;
		SUBTYPE = subtype;
		GROUP = group;
		SELECTED = true;
		
	}
	
	/**
	 * Set the currently selected entity
	 * @param selectedEntity Entity that is selected
	 */
	public static void set(Entity selectedEntity){
		if(selectedEntity.getType() == 1){
			BOID = true;
		}
		else BOID = false;
		
		entity = selectedEntity;
		TYPE = entity.getType();
		SUBTYPE = entity.getSubType();
		GROUP = entity.tertiaryType;
		SELECTED = true;
	}
	/**
	 * Get the type
	 * @return type of the selected entity
	 */
	public static byte type(){ byte b = TYPE; return b;}
	/**
	 * 
	 * @return subtype of the selected entity
	 */
	public static byte subType(){ byte b = SUBTYPE; return b;}
	/**
	 * 
	 * @return group of the selected entity
	 */
	public static byte group(){ byte b = GROUP; return b;}
	/**
	 * 
	 * @return True if SELECTED flag is true
	 */
	public static boolean selected(){boolean b = SELECTED; return b;}
	/**
	 * 
	 * @return True if the selected entity is a boid
	 */
	public static boolean boid(){boolean b = BOID; return b;}
	

}
