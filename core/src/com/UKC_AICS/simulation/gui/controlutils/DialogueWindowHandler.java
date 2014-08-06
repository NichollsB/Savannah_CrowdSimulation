package com.UKC_AICS.simulation.gui.controlutils;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ObjectMap;

public interface DialogueWindowHandler {
	
	public void onConfirmed(ObjectMap<String, String> value, Window window);
	
	public void onCancelled(Window window);
	

}
