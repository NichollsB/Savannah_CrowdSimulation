package com.UKC_AICS.simulation.screen.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ObjectMap;

public interface WindowListener {
	
	public void onConfirmed(ObjectMap<String, String> value, Window window);
	
	public void onCancelled(Window window);
	

}
