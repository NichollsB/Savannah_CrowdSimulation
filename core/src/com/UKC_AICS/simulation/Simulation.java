package com.UKC_AICS.simulation;

import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Game;

public class Simulation extends Game {

    // list of all screens in application
    SimulationScreen simulationScreen;

    @Override
	public void create () {
        //  initalising screens, passed this for setScreen functionality
        simulationScreen = new SimulationScreen(this);

        //set startup screen
        setScreen(simulationScreen);
	}
}
