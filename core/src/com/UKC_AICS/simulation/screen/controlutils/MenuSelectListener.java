package com.UKC_AICS.simulation.screen.controlutils;

/**
 * Abstract listener type class for some of the {@link com.UKC_AICS.simulation.screen.gui.SimScreenGUI gui}
 * components
 * @author Ben Nicholls
 */
public abstract class MenuSelectListener  {
    /**
     * Called when a selection is made
     * @param menu menu gui object that triggered the even
     * @param object Object associated with the even call
     */
	public void selectionMade(java.lang.Object menu, java.lang.Object object) {}

}
