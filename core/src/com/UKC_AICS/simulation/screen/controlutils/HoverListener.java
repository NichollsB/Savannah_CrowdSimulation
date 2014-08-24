package com.UKC_AICS.simulation.screen.controlutils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface HoverListener {
	
	public void hover(InputEvent event, float x, float y, String helper);
	public void unhover(InputEvent event);
	
}
