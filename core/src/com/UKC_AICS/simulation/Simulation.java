package com.UKC_AICS.simulation;

import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Game;

public class Simulation extends Game {

    @Override
	public void create () {
	        setScreen(new SimulationScreen());
	}
}
