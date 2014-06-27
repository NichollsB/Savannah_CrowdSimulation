package com.UKC_AICS.simulation.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.UKC_AICS.simulation.managers.SimulationManager;

/**
 *
 * @author Emily
 */
public class SimulationScreen implements Screen {

    private Camera camera;
    
    private Environment environment; //lighting things
    
    private SimulationManager simulationManager;
    
    @Override
    public void render(float delta) {
        //kind of the update loop.
        simulationManager.update();
        
        tickPhysics(delta);
        clearOpenGL();
        
        //do render calls for models, sprites, whatever. 
        //(probably done in another class)
        
        //draw models
        //draw hud/ui etc
    }

    /**
     * clears the screen.
     */
    private void clearOpenGL() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }
    
    
    @Override
    public void resize(int width, int height) {
        createCamera(width, height);
        setup();
    }

    /**
     * creates a new camera with specified height and width.
     * @param width
     * @param height 
     */
    private void createCamera(int width, int height) {
        //create a camera. perspective? orthographic? etc etc.
    }

    private void setup() {
        simulationManager = new SimulationManager();
        setupCameraController();
    }
    
    private void setupCameraController() {
        //blah blah create the controller
        //set the controller
        //Gdx.input.setInputProcessor(SOMECAMERCONTROLLER);
    }
    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        //when the program is not "selected" or "active"
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private void tickPhysics(float delta) {
        //send delta to camera controller using its update.
        //send delta to camera using its update
    }


}
