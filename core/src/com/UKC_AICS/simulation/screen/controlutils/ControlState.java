package com.UKC_AICS.simulation.screen.controlutils;

public abstract class ControlState {
	
	public enum State{
		NAVIGATE ("navigate"),
		PLACEMENT ("placement");
		public String stateName;
		State(String stateName){
			this.stateName = stateName;
		}
	}
	
	
	private static final State DEFAULT = State.NAVIGATE;
	public static State STATE = DEFAULT;
	
	public static void changeState(boolean isChecked, State stateChanged){
		
		State s = STATE;
		if(stateChanged.equals(State.PLACEMENT)) s = (isChecked) ? State.PLACEMENT : DEFAULT;
		
		STATE = s;
	}

	

	
}
