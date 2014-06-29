package com.UKS_AICS.simulation.desktop;

import com.UKC_AICS.simulation.Simulation;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1280;

		new LwjglApplication(new Simulation(), config);
	}
}
