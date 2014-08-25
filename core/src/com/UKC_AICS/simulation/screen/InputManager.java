package com.UKC_AICS.simulation.screen;

import com.UKC_AICS.simulation.screen.controlutils.ControlState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.InputProcessor InputProcessor} for implementing some controls for the simulation
 * Allows clicking, scroll zoom, and panning (right or left click and drag) around the graphical representation of the environment
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public class InputManager implements InputProcessor{
	
	private Boolean lClick = false, rClick = false;
	private SimulationScreen screen;
	private OrthographicCamera camera;
	private float maxZoom = 3f;

	private boolean inBounds = false;

	private Rectangle viewportRectangle;
	private Viewport view;
    private boolean dragging;
    private int dragX = 0, dragY = 0;

    /**
     * Initialises the input manager for a given {}screen, camera, and viewport.
     * @param screen
     * @param camera
     * @param view
     */
	public InputManager(SimulationScreen screen, OrthographicCamera camera, Viewport view){
		this.screen = screen;
		this.camera = camera;
		camera.zoom = 1.2f;
		this.view = view;
	}
	

	@Override
	public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.SPACE:
                screen.flipRunning();
                break;
            case Keys.S:
                screen.flipRender();
                break;
            default:
                break;
        }
		return false;
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

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		System.out.println("Mouse down " + inBounds);
		if(!inBounds(screenX, screenY)) return false;
//		inDragBounds = true;
		if(button == Input.Buttons.LEFT){
			lClick = true;
			
			dragX = screenX; dragY= screenY;

			//screen.pickPoint(screenX, flipY(screenY));
			
		}
		if(button == Input.Buttons.RIGHT){
			rClick = true;
			
			dragX = screenX; dragY= screenY;
		}
		return inBounds;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(lClick|| rClick){
			lClick = false;
			rClick = false;
			
			if(!dragging){
				Vector3 screenToMouse = view.unproject(new Vector3(screenX, screenY, 0));
				screen.pickPoint((int)screenToMouse.x, (int)screenToMouse.y);
			}
				dragging = false;
			
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(lClick || rClick){
			if(inBounds(screenX, screenY)){
				if(ControlState.STATE == ControlState.State.NAVIGATE || rClick){
					if(!dragging)
						dragging = true;
					if(dragging){
						camera.translate(dragX-screenX, screenY-dragY);
						dragX = screenX;
						dragY = screenY;
					}
				}
			}
		}
		return inBounds;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		inBounds = inBounds(screenX, screenY);
//		
		screen.setMousePosition(screenX, Gdx.graphics.getHeight() - screenY);
		if(inBounds){
			Vector3 screenToMouse = view.unproject(new Vector3(screenX, screenY, 0));
			screen.setMouseWorldPosition((int)screenToMouse.x, (int)screenToMouse.y);
		}
		if(inBounds)
			return true;
		else
			return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(!inBounds) return false;
		//Zoom out
        if (amount > 0 && camera.zoom < maxZoom) {
        	camera.zoom += 0.1f;            
        }

        //Zoom in
        if (amount < 0 && camera.zoom > 0.1) {
            camera.zoom -= 0.1f;
        }
        return true;
	}

    /**
     * Called on resizing the simulation screen. Alters the bounding rectangle used in some of the
     * controls. Named resize for convenience - calls {@link #setViewportRect(com.badlogic.gdx.math.Rectangle) setViewPort}
     * method.
     * @param viewRect Bounding rectangle object to use
     */
	public void resize(Rectangle viewRect){
		setViewportRect(viewRect);
	}

    /**
     *  Called to set the bounding rectangle for the {@link #inBounds(int, int) inBounds} method.
     * @param rect Rectangle to set as the bounding rectangle
     */
	public void setViewportRect(Rectangle rect){
		viewportRectangle = rect;
	}

    /**
     * Checks if a given coordinate is within the bounding {@link #viewportRectangle rectangle} for some of the
     * click events. Inverts the y coordinate to match the simulation coordinates.
     * @param x X coordinate to check
     * @param y Y coordinate to check
     * @return True if the bounding rectangle contains the coordinates, otherwise false.
     */
	private boolean inBounds(int x, int y){
		int inverseY = Gdx.graphics.getHeight() - y;
		if(viewportRectangle.contains(x, inverseY)) return true;
		return false;
	}

}
