package com.UKC_AICS.simulation.gui.controlutils;

import java.util.Observable;
import java.util.Observer;

import com.UKC_AICS.simulation.entity.Entity;

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
	
	public static void set(boolean select){
//		selected.valueOf(select);
		SELECTED = select;
//		System.out.println("set selected " + selected);
	}
	public static void set(byte type, byte subtype, byte group){
//		TYPE.valueOf(type);
//		SUBTYPE.valueOf(subtype);
//		GROUP.valueOf(group);
//		selected.valueOf(true);
		if(type == 1){
			BOID = true;
		}
		else
			BOID = false;
		TYPE = type;
		SUBTYPE = subtype;
		GROUP = group;
		SELECTED = true;
		
		System.out.println("selected entity " + SELECTED);
		
	}
	
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
//		TYPE.valueOf(selectedEntity.getType());
//		SUBTYPE.valueOf(selectedEntity.getSubType());
//		GROUP.valueOf(selectedEntity.tertiaryType);
//		selected.valueOf(false);
	}
	public static byte type(){ byte b = TYPE; return b;}
	public static byte subType(){ byte b = SUBTYPE; return b;}
	public static byte group(){ byte b = GROUP; return b;}
	public static boolean selected(){boolean b = SELECTED; return b;}
	public static boolean boid(){boolean b = BOID; return b;}
//	public byte getType(){
//		final byte b = entityType;
//		return b;
//	}
//	public byte getSubType(){
//		final byte b = entitySubtype;
//		return b;
//	}
//	public byte getGroup(){
//		final byte b = entityGroup;
//		return b;
//	}
//	public Entity getEntity(){
//		return entity;
//	}
	

}
