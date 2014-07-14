package com.UKC_AICS.simulation.screen;

import java.util.HashMap;

import com.UKC_AICS.simulation.Simulation;
import com.UKC_AICS.simulation.screen.gui.SimScreenGUI;
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
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Scaling;
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
    private Viewport simViewport;
    private SpriteBatch simViewBatch = new SpriteBatch();
    private Rectangle viewWindow;
    private Rectangle camView;
    
    
    private Graphics boidGraphics = new Graphics();

    SimScreenGUI gui = new SimScreenGUI(this); // Creates gui instance for this screen

    private InputMultiplexer input;
    private InputManager inputManager;
    


    public SimulationScreen(Simulation simulation) {
        this.simulation = simulation;

        
        //Gdx.input.setInputProcessor(inputManager);


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
        	simViewBatch.setProjectionMatrix(simViewcamera.combined);
        	time = System.nanoTime();
        	if(time >= nextRender){
        		update = false;
	            gui.fps.setText(getFPSString() + simulationManager.getTime());
	            tickPhysics(delta);
	            clearOpenGL();
	            ScissorStack.calculateScissors(simViewcamera, simViewBatch.getTransformMatrix(), camView, viewWindow);
	            ScissorStack.pushScissors(viewWindow);

	            boidGraphics.update(simViewBatch);
	            renderSpriteBatches();
	            simViewBatch.flush();
	            ScissorStack.popScissors();
	            nextRender = System.nanoTime() + (long)33333333.33333333;
        	}
//        	else{
//        		update = true;
//        	}
	            try {
	                Thread.sleep((long) (1000 / 60 - Gdx.graphics.getDeltaTime()));
	            } catch (InterruptedException e) {
	                System.out.print("Error...");
	                e.printStackTrace();
	            }
	       
        } else {
            clearOpenGL();
            gui.fps.setText(getFPSString() + simulationManager.getTime());
            renderSpriteBatches();
        }
    }


    private void renderSpriteBatches() {

        gui.update(simViewBatch);
          //GUI stuff


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
        
        gui.stage.getViewport().update(width, height, true);
        camView.setWidth(width);
        camView.setHeight(height);
        viewWindow = gui.getViewArea();
        simViewcamera.position.set(width/2, height/2, 0);
        simViewcamera.viewportWidth = width;
        simViewcamera.viewportHeight = height;
        //createCamera(width, height, 0, 0);
        //setup();
    }

    /**
     * creates a new camera with specified height and width.
     *
     * @param width
     * @param height
     */
    private void createCamera(int width, int height) {
        //create a camera. perspective? orthographic? etc etc.
    	camView =  new Rectangle(width, height, 0, 0);
    	viewWindow = gui.getViewArea();
        simViewcamera = new OrthographicCamera(width, height);
        simViewcamera.setToOrtho(false);
        simViewcamera.position.set(width/2, height/2, 0);
        //clipCamera(camView, viewWindow);
    	inputManager = new InputManager(this, (int)width, (int)height, simViewcamera);
    	input = new InputMultiplexer();
        input.addProcessor(gui.setStage());  //sets up GUI
        input.addProcessor(inputManager);

        Gdx.input.setInputProcessor(input);

    }
    
    private void clipCamera(Rectangle view, Rectangle clip, ScissorStack stack){
    	//stack.calculateScissors(simViewcamera, batchTransform, area, scissor);
        //simViewcamera.position.set(view.getWidth()/2, view.getHeight()/2, 0);
        
        
    }

    public void setup() {
    	
        setupCameraController();
        createCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
