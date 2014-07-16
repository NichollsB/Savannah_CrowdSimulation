package com.UKC_AICS.simulation.screen;

import java.util.HashMap;

import com.UKC_AICS.simulation.Simulation;
import com.UKC_AICS.simulation.screen.gui.SimScreenGUI;
import com.UKC_AICS.simulation.screen.gui.SimViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.UKC_AICS.simulation.managers.SimulationManager;

/**
 * @author Emily
 */
public class SimulationScreen implements Screen {

    private boolean render = true;   // for render pausing
    private boolean update = false;
    boolean running = true;  //for play pausing.

    private final Simulation simulation;
    

    private Environment environment; //lighting things

    public SimulationManager simulationManager = new SimulationManager();

    private BitmapFont font = new BitmapFont();

    
    private OrthographicCamera simViewcamera;
    private Camera uiCamera;
    private SimViewport simViewport;
    private Viewport uiViewport;
    private SpriteBatch simViewBatch = new SpriteBatch();

    private Rectangle viewRect;
    
    
    private Graphics boidGraphics;

    private SimScreenGUI gui; // Creates gui instance for this screen

    private InputMultiplexer input;
    private InputManager inputManager;
    


    public SimulationScreen(Simulation simulation) {
        this.simulation = simulation;
        gui = new SimScreenGUI(this, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setup();
        
    }
    private long time = 0;
    private long nextRender = 0;
    @Override
    public void render(float delta) {
        //kind of the update loop.
        if (running) {
            simulationManager.update();
        }

        // checks if simulation needs to be rendered or can be run "offline"
        if (render) {
        	simViewcamera.update();

        	time = System.nanoTime();
        	if(time >= nextRender){
        		update = false;
	            gui.fps.setText(getFPSString() + simulationManager.getTime());
	            tickPhysics(delta);
	            renderSpriteBatches();
	            nextRender = System.nanoTime() + (long)33333333.33333333;
        	}

	            try {
	                Thread.sleep((long) (1000 / 60 - Gdx.graphics.getDeltaTime()));
	            } catch (InterruptedException e) {
	                System.out.print("Error...");
	                e.printStackTrace();
	            }
	       
        } else {
            clearOpenGL();
            gui.fps.setText(getFPSString() + simulationManager.getTime());
//            renderSpriteBatches();
        }
    }

    /**
     * Calls the update method to trigger the rendering calls in the gui and simulation view
     */
    private void renderSpriteBatches() {
    	clearOpenGL();
    	//Update the simulation view and render, clipping to the scissor rectangle provided by the specified gui
    	//area for the view
    	simViewport.update();
    	simViewBatch.setProjectionMatrix(simViewcamera.combined);
        ScissorStack.pushScissors(viewRect);
    	boidGraphics.update(simViewBatch);
    	ScissorStack.popScissors();
    	//Update and render the gui
    	uiViewport.update();
        simViewBatch.setProjectionMatrix(uiCamera.combined);
        gui.update(simViewBatch);
        simViewBatch.flush();
    }

    /**
     * clears the screen.
     */
    private void clearOpenGL() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void resize(int width, int height) {
    	//Call the gui resize method and retrieve the viewRect specifying the provided area in which the
    	//simulation will be viewed - also update and center the viewports with the resize dimensions
    	gui.resize(width, height);
        viewRect = gui.getViewArea();
        simViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    /**
     * Create and set up the cameras and viewports for the user interface and simulation view
     *
     * @param width
     * @param height
     */
    private void initialiseCameras(int width, int height) {
        //create a camera. perspective? orthographic? etc etc.
    	viewRect =  gui.getViewArea();
    	uiCamera = gui.getCamera();
    	uiViewport = gui.getViewport();

        simViewcamera = (OrthographicCamera) boidGraphics.getCamera();
        simViewport = new SimViewport(Scaling.none, width, height, simViewcamera);

    	inputManager = new InputManager(this, (int)width, (int)height, simViewcamera);
    	input = new InputMultiplexer();
        input.addProcessor(gui);  //sets up GUI
        input.addProcessor(inputManager);

        Gdx.input.setInputProcessor(input);
        resize(width, height);

    }

    /**
     * 
     */
    public void setup() {
    	boidGraphics = new Graphics(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setupCameraController();
        initialiseCameras(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        boidGraphics.initBoidSprites(simulationManager.getBoids(), simulationManager.getTextureLocations());
        boidGraphics.initObjSprites(simulationManager.getObjects());
        boidGraphics.initTileSprites(simulationManager.getMapTiles());
        //boidGraphics.initTileSprites(simulationManager.getMapTiles());
    }

    /**
     * @return gives the current fps and current time count
     */
    public String getFPSString() {
        return "fps: " + Gdx.graphics.getFramesPerSecond();

    }

    /**
     * flips the running boolean for simulation updating.
     */
    public void flipRunning() {
        if (running)
            running = false;
        else
            running = true;
    }

    /**
     * @return running boolean
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * flips the render boolean for simulation rendering.
     */
    public void flipRender() {
        if (render)
            render = false;
        else
            render = true;
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
        gui.stage.dispose();
    }

    private void tickPhysics(float delta) {
        //send delta to camera controller using its update.
        //send delta to camera using its update
    }

    /**
     * Reacts to clicking on the simulations viewport - called by InputManagers touchDown method
     */
    public void pickPoint(int screenX, int screenY) {
        //What should happen when clicking on the screen
        gui.setConsole("x: " + screenX + " y: " + screenY);

    }

}
