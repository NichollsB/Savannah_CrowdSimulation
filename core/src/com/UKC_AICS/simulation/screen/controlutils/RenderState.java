package com.UKC_AICS.simulation.screen.controlutils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Class for specifying the render state to some {@link com.UKC_AICS.simulation.screen.graphics graphics}
 * components. Primarily this is to distinguish between rendering the dynamic environment layers (see
 * {@link com.UKC_AICS.simulation.world.LandMap land map}) using a dynamic {@link com.UKC_AICS.simulation.screen.graphics.TileMesh mesh},
 * or using a {@link com.UKC_AICS.simulation.screen.graphics.TileGraphics sprite chache}.
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public abstract class RenderState extends ControlState {

    /**
     * Enum of render states
     */
	public enum State{
        /**
         * State for rendering with the {@link com.UKC_AICS.simulation.screen.graphics.TileGraphics sprite chache}
         */
		TILED ("tiled"),
        /**
         * State for rendering with a {@link com.UKC_AICS.simulation.screen.graphics.TileMesh mesh}
         */
		MESH ("mesh"),
        OFF("off");
		public String stateName;
		State(String stateName){
			this.stateName = stateName;
		}
	}

    /**
     * Defaul render state
     */
	private static final State DEFAULT = State.TILED;
    /**
     * Current tiling state
     */
	public static State TILESTATE = DEFAULT;
    public static State last_TILESTATE = TILESTATE;
	public static String TILESTATE_STRING = TILESTATE.stateName;

    /**
     * Map of available states, to their string name keys
     */
	private static ObjectMap<String, State> tileStates = new ObjectMap<String, State>(){{
		put(State.TILED.stateName, State.TILED);
		put(State.MESH.stateName, State.MESH);
        put(State.OFF.stateName, State.OFF);
	}};

    /**
     * Retrieve the tile state names
     * @return Array of tile states as Strings
     */
	public static Array<String> getTileStates(){
//		System.out.println("Tile states " + tileStates.keys().toArray().size);
		return tileStates.keys().toArray();
	}

    /**
     * Change the current {@link #TILESTATE tile state}.
     * @param stateChanged Changed from. If not the current state, or state does not exist, then method wont do anything
     * @param changedTo Changed to
     */
	public static void changeTileState(final String stateChanged, final String changedTo){
		System.out.println("CHANGING TILE STATE" + TILESTATE.stateName + " change from " + stateChanged +
				" to " + changedTo);
		if(!tileStates.containsKey(stateChanged) || !tileStates.containsKey(changedTo)) return;
		TILESTATE = tileStates.get(changedTo);
		System.out.println(TILESTATE.stateName);
	}

    /**
     * Change the currently active state to that matching the String parameter
     * @param state String name of the {@link com.UKC_AICS.simulation.screen.controlutils.RenderState.State state}
     *              to change to
     */
    public static void changeTileState(String state){
        if(!tileStates.containsKey(state)) return;
        if(TILESTATE.stateName.equalsIgnoreCase(state)) return;
        TILESTATE = tileStates.get(state);
    }

    /**
     * Revert the current render {@link #TILESTATE state} to the last state it was in.
     */
	public static void revertTileState(){
        State s = TILESTATE;
        TILESTATE = last_TILESTATE;
        last_TILESTATE = s;
    }

}
