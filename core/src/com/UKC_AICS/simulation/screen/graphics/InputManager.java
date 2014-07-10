package com.UKC_AICS.simulation.screen.graphics;

import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class InputManager implements InputProcessor{
	
	private Boolean lClick = false;
	private SimulationScreen screen;
	private int width;
	private int height;
	private OrthographicCamera camera;
	
	public InputManager(SimulationScreen screen, int width, int height, OrthographicCamera camera){
		this.screen = screen;
		this.width = width;
		this.height = height;
		this.camera = camera;
	}
	
	public int flipY(int y){
		return height - y;
	}

	@Override
	public boolean keyDown(int keycode) {
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	private boolean dragging;
	private int dragX = 0, dragY = 0;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT){
			lClick = true;
			dragging = true;
			dragX = screenX; dragY= screenY;
			Vector3 screenToMouse = new Vector3(screenX, screenY, 0);
			screenToMouse = camera.unproject(screenToMouse);
			screen.pickPoint((int)screenToMouse.x, (int)screenToMouse.y);
			//screen.pickPoint(screenX, flipY(screenY));
			
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(lClick){
			lClick = false;
			dragging = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(lClick){
			Vector3 screenToMouse = camera.unproject(new Vector3(screenX, screenY, 0));
			screen.pickPoint((int)screenToMouse.x, (int)screenToMouse.y);
			if(dragging){
				camera.translate(dragX-screenX, screenY-dragY);
//				camera.update();
				dragX = screenX;
				dragY = screenY;
			}
			//screen.pickPoint(screenX, flipY(screenY));
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		//Zoom out
        if (amount > 0 && camera.zoom < 1) {
            camera.zoom += 0.1f;
        }

        //Zoom in
        if (amount < 0 && camera.zoom > 0.1) {
            camera.zoom -= 0.1f;
        }

        return true;
	}

}
