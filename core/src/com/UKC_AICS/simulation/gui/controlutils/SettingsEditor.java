package com.UKC_AICS.simulation.gui.controlutils;

import java.util.HashMap;

import com.UKC_AICS.simulation.gui.controlutils.ControlState.State;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Settings Editor - 
 * @author temp
 *
 */
public class SettingsEditor implements DialogueWindowHandler, TreeOptionsListener{
	
	enum SettingType{
		SPECIES("Species"),
		OBJECT("Object");
		private final String name;
		SettingType(String name){
			this.name = name;
		}
		private String getName(){
			return name;
		}
	}
	private HashMap<SettingType, String> settingTypes = new HashMap<SettingType, String>(){{
		put(SettingType.SPECIES, SettingType.SPECIES.getName());
		put(SettingType.OBJECT, SettingType.OBJECT.getName());
	}};
	
	public Array<String> getSettingTypes(){
		Array<String> typeNames = new Array<String>();
		for(String type : settingTypes.values()){
			typeNames.add(type);
		}
		return typeNames;
	}

	@Override
	public void onConfirmed(ObjectMap<String, String> value, Window window) {
		System.out.println("Settings Confirmed " + window.getName() + "\n"
				+ "Field : Value");
		for(String s : value.keys()){
			System.out.println(s + " : " + value.get(s));
		}
	}

	@Override
	public void onCancelled(Window window) {
		System.out.println("Settings cancelled" + window.getName());
	}

	@Override
	public void onAdd(byte type, byte subtype, Object object) {
		System.out.println("Add in tree window");
	}

	@Override
	public void onRemove(byte type, byte subtype, Object object) {
		// TODO Auto-generated method stub
		
	}

	
	

	@Override
	public void onCheck(byte type, byte subtype, Object object,
			boolean isChecked, State stateChanged) {
		// TODO Auto-generated method stub
		
	}

}
