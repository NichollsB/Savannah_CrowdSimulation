package com.UKC_AICS.simulation.utils.ControlUtils;

import java.util.HashMap;

import com.UKC_AICS.simulation.screen.gui.WindowListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Settings Editor - 
 * @author temp
 *
 */
public class SettingsEditor implements WindowListener{
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelled(Window window) {
		// TODO Auto-generated method stub
		
	}

}
