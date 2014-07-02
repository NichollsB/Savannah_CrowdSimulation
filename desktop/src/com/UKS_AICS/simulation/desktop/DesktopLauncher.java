package com.UKS_AICS.simulation.desktop;

import com.UKC_AICS.simulation.Simulation;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Project Savannah Simulation";
        config.width = 1280;
        config.height = 720;
        config.vSyncEnabled = false; 
        config.foregroundFPS = 0; 
        config.backgroundFPS = 0;
		new LwjglApplication(new Simulation(), config);
	}
}
