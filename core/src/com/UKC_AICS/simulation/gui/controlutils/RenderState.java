package com.UKC_AICS.simulation.gui.controlutils;

import com.UKC_AICS.simulation.gui.controlutils.ControlState.State;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class RenderState extends ControlState {
	
	public enum State{
		TILED ("tiled"),
		MESH ("mesh");
		public String stateName;
		State(String stateName){
			this.stateName = stateName;
		}
	}
	
	private static final State DEFAULT = State.TILED;
	public static State TILESTATE = DEFAULT;
	public static String TILESTATE_STRING = TILESTATE.stateName;
	
	private static ObjectMap<String, State> tileStates = new ObjectMap<String, State>(){{
		put(State.TILED.stateName, State.TILED);
		put(State.MESH.stateName, State.MESH);
	}};
	
	public static Array<String> getTileStates(){
//		System.out.println("Tile states " + tileStates.keys().toArray().size);
		return tileStates.keys().toArray();
	}
	
	
	public static void changeTileState(final String stateChanged, final String changedTo){
		System.out.println("CHANGING TILE STATE" + TILESTATE.stateName + " change from " + stateChanged +
				" to " + changedTo);
		if(!tileStates.containsKey(stateChanged) || !tileStates.containsKey(changedTo)) return;
		TILESTATE = tileStates.get(changedTo);
		System.out.println(TILESTATE.stateName);
		
	}
    public static void changeTileState(String state){
        if(!tileStates.containsKey(state)) return;
        if(TILESTATE.stateName.equalsIgnoreCase(state)) return;
        TILESTATE = tileStates.get(state);
    }
	

}
