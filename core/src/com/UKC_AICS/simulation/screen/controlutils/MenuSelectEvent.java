package com.UKC_AICS.simulation.screen.controlutils;

/**
 * Interface for menus that make calls to {@link com.UKC_AICS.simulation.screen.controlutils.MenuSelectListener MenuSelectListeners}
 * Allows listeners to be registered
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public interface MenuSelectEvent {
    /**
     * Implementing classes should register any listeners here
     * @param listener
     */
	public void addSelectionListener(MenuSelectListener listener);
}
