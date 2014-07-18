package com.UKC_AICS.simulation.screen.gui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class SimViewport extends ScalingViewport {

	public SimViewport(Scaling scaling, float worldWidth, float worldHeight,
			Camera camera) {
		super(scaling, worldWidth, worldHeight, camera);
		// TODO Auto-generated constructor stub
	}
	
	public void update(int x, int y, int width, int height, boolean center){
		super.update(width, height, center);
		super.viewportX = x;
		super.viewportY = y;
	}

}
