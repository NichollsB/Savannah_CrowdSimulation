package com.UKC_AICS.simulation.screen.controlutils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * Interface for hovering functionality for some of the {@link com.UKC_AICS.simulation.screen.gui.SimScreenGUI gui} components
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public interface HoverListener {

    /**
     * Method called when an component is being hovered over
     * @param event
     * @param x
     * @param y
     * @param helper
     */
	public void hover(InputEvent event, float x, float y, String helper);

    /**
     * Method called to end the hovering even
     * @param event
     */
	public void unhover(InputEvent event);
	
}
