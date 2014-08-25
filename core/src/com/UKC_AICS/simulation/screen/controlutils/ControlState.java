package com.UKC_AICS.simulation.screen.controlutils;


/**
 * Class for designating the control state of the simulation interface. Primarily involved in allowing
 * the explicit placement of entities upon the simulation environment
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public abstract class ControlState {

    /**
     * State enum designating navigation or placement
     */
	public enum State{
        /**
         * Navigations state
         */
		NAVIGATE ("navigate"),
        /**
         * Placement state
         */
		PLACEMENT ("placement");
		public String stateName;
		State(String stateName){
			this.stateName = stateName;
		}
	}

    /**
     * Default state is navigation state
     */
	private static final State DEFAULT = State.NAVIGATE;
    /**
     * The currently active {@link com.UKC_AICS.simulation.screen.controlutils.ControlState.State state} of the
     * simulation interface.
     */
	public static State STATE = DEFAULT;

    /**
     * Toggles the current {@link com.UKC_AICS.simulation.screen.controlutils.ControlState.State state}
     * between navigation and placement
     * @param isChecked If true, will set the State to the placement state, otherwise sets it to its default
     *                  navigation state
     * @param stateChanged The State to change
     */
	public static void changeState(boolean isChecked, State stateChanged){
		
		State s = STATE;
		if(stateChanged.equals(State.PLACEMENT)) s = (isChecked) ? State.PLACEMENT : DEFAULT;
		
		STATE = s;
	}

	

	
}
