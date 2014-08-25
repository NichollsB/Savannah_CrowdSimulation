package com.UKC_AICS.simulation.screen;

import EvolutionaryAlgorithm.EvolutionaryAlgorithmGUI;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.Simulation;
import com.UKC_AICS.simulation.entity.Entity;
import com.UKC_AICS.simulation.entity.Species;
import com.UKC_AICS.simulation.screen.controlutils.ControlState;
import com.UKC_AICS.simulation.screen.controlutils.RenderState;
import com.UKC_AICS.simulation.screen.controlutils.SelectedEntity;
import com.UKC_AICS.simulation.screen.graphics.Graphics;
import com.UKC_AICS.simulation.screen.graphics.TileGraphics;
import com.UKC_AICS.simulation.screen.gui.SimScreenGUI;
import com.UKC_AICS.simulation.screen.gui.SimViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.UKC_AICS.simulation.managers.SimulationManager;

import java.util.HashMap;


/**
 * @author Emily
 */
public class SimulationScreen implements Screen{

    private boolean render = true;   // for render pausing
    private boolean update = false;
    private boolean eaRender = true;
    boolean running = false;  //for play pausing.

    private final Simulation simulation;
    

    private Environment environment; //lighting things

    public SimulationManager simulationManager = new SimulationManager(this);

    private BitmapFont font = new BitmapFont();

    
    private OrthographicCamera simViewcamera;
    private Camera uiCamera;
    private SimViewport simViewport;
    private Viewport uiViewport;
    private SpriteBatch simBatch = new SpriteBatch();


    private Rectangle scissorRect = new Rectangle();
    
    public Graphics boidGraphics;

    public SimScreenGUI gui; //= new SimScreenGUI(this); // Creates gui instance for this screen
    public EvolutionaryAlgorithmGUI eagui;
    private InputMultiplexer input;
    private InputManager inputManager;
    
    TileGraphics tiling;
    
    public final Vector2 mousePosition = new Vector2();





    public SimulationScreen(Simulation simulation) {
        this.simulation = simulation;
        gui = new SimScreenGUI(this, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        eagui = new EvolutionaryAlgorithmGUI(this,simulationManager.ea);
        setup();
        setupUI();

    }

    private long time = 0;
    private long nextRender = 0;
    @Override
    public void render(float delta) {
        //kind of the update loop.
        if (running) {
            simulationManager.update(false); //this is false here because all managers need to take a boolean. Actual decideing is done in SimulationManager.
        }
        simBatch.enableBlending();
//    	simBatch.begin();
//        Rectangle rect = gui.getViewArea();
        scissorRect = gui.getViewArea();
        inputManager.setViewportRect(scissorRect);
        clearOpenGL();
        if (render && !RenderState.TILESTATE.equals(RenderState.State.OFF)) {
    		 try {
                 long number = (long) (1000 / 60 - Gdx.graphics.getDeltaTime());
                 if(number < 0) number = 0;//fixed?
                 Thread.sleep(number); //FIXME: this can go negative after leaving the screen alone for a while. crashes program
             } catch (InterruptedException e) {
                 System.out.print("Error...");
                 e.printStackTrace();
             }
            renderSpriteBatches(render);
    	}
        renderUIBatch(true);
    }

    private void renderUIBatch(boolean render){
    	simBatch.begin();
    	gui.fps.setText(getFPSString() + simulationManager.getTime());
    	uiViewport.update();
        simBatch.setProjectionMatrix(uiCamera.combined);
        gui.update(simBatch, render);
        
        simBatch.end();
        eagui.getBatch().setProjectionMatrix(eagui.getCamera().combined);
        eagui.getViewport().update();
        eagui.update(eaRender);
    }
    
    boolean uiComplete = false;
    /**
     * Calls the update method to trigger the rendering calls in the gui and simulation view
     */
    private void renderSpriteBatches(boolean render) {
    	//Update the simulation view and render, clipping to the scissor rectangle provided by the specified gui
    	//area for the view
    	if(render){
	    	simViewport.update();
	    	simBatch.setProjectionMatrix(simViewcamera.combined);
//	        ScissorStack.pushScissors(viewRect);
//	    	simBatch.begin();
	    	if(boidGraphics.update(simBatch, scissorRect) && !uiComplete){
                HashMap<Byte, Species> map = simulationManager.getSpeciesInfo();
                gui.addBoidsToLegend(simulationManager.getSpeciesInfo(), boidGraphics.spriteManager.getBoidImages());
                gui.addObjectsToLegend(simulationManager.getObjectDataInfo(), boidGraphics.spriteManager.getObjectImages());
                uiComplete = true;
            };
//	    	simBatch.end();
//	    	ScissorStack.popScissors();
    	}
    }

    /**
     * clears the screen.
     */
    private void clearOpenGL() {
    	//for some reason this method slows it down incredibly
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void resize(int width, int height) {
    	//Call the gui resize method and retrieve the viewRect specifying the provided area in which the
    	//simulation will be viewed - also update and center the viewports with the resize dimensions
//        scissorRect = gui.getViewArea();
        inputManager.resize(scissorRect);
        
        uiViewport.update(width, height, true);
        eagui.getViewport().update(width, height, true);
        simViewport.update(width, height, false);
//        uiViewport.
    }

    /**
     * Create and set up the cameras and viewports for the user interface and simulation view
     *
     * @param width
     * @param height
     */
    private void initialiseCameras(int width, int height) {
        //create a camera. perspective? orthographic? etc etc.
    	scissorRect = gui.getViewArea();
    	uiCamera = gui.getCamera();
    	uiViewport = gui.getViewport();

        simViewcamera = (OrthographicCamera) boidGraphics.getCamera();
        simViewport = new SimViewport(Scaling.none, width, height, simViewcamera);

    	inputManager = new InputManager(this, simViewcamera, simViewport);
    	input = new InputMultiplexer();
    	
    	input.addProcessor(eagui);
    	input.addProcessor(gui); 
    	input.addProcessor(inputManager);	
    	
    	
    	
    	simViewport.update(width, height, true);

        Gdx.input.setInputProcessor(input);
        resize(width, height);

    }

    public void setupUI(){
    	gui.createBoidTree(simulationManager.getSpeciesInfo(), simulationManager.getBoids());
        gui.createObjectTree(simulationManager.getObjectDataInfo(), simulationManager.getObjects());
    }
    /**
     * 
     */
    public void setup() {
//    	EnvironmentLoader.loadMaps();
    	
    	boidGraphics = new Graphics(Constants.mapWidth, Constants.mapHeight); //changed these from gdx.graphics.getWidth to this. -Em
        setupCameraController();
        initialiseCameras(Constants.mapWidth, Constants.mapHeight); //changed these from gdx.graphics.getWidth to this. -Em
        //Graphics components
        boidGraphics.setBoids(simulationManager.getBoids());
        boidGraphics.initBoidSprites(simulationManager.getTextureLocations());
        boidGraphics.setBoidSprite_Colours(simulationManager.getRGBValues());
        boidGraphics.initObjSprites(simulationManager.getObjects());
        boidGraphics.initEnvironmentTiling(simulationManager.getFullInfo());
        //boidGraphics.initEnvironmentTiling(simulationManager.getMapTiles());
        boidGraphics.initEnvironmentMeshes(simulationManager.getFullInfo());
    }

    public void resetGraphics(){
        boidGraphics.initEnvironmentTiling(simulationManager.getFullInfo());
        boidGraphics.initEnvironmentMeshes(simulationManager.getFullInfo());
        boidGraphics.setBoids(simulationManager.getBoids());
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
    
    public void flipEARender() {
    	eagui.toggleWindowVisible();
        if (eaRender)
        	eaRender = false;
        else
        	eaRender = true;
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        gui.stage.dispose();
        eagui.stage.dispose();
    }

    /**
     * Reacts to clicking on the simulations viewport - called by InputManagers {@link com.UKC_AICS.simulation.screen.InputManager#touchDown(int, int, int, int) touchDown} method
     * If the {@link com.UKC_AICS.simulation.screen.controlutils.ControlState control state} is set to navigate
     * allows entities to be {@link com.UKC_AICS.simulation.managers.SimulationManager#getBoidAt(int, int) selected}
     * and passed to the {@link com.UKC_AICS.simulation.screen.gui.SimScreenGUI#selectEntity(com.UKC_AICS.simulation.entity.Entity, boolean) gui}.
     * If the control state is set to placement, it will allow the placement of an entity via {@link com.UKC_AICS.simulation.managers.SimulationManager#generateObject(byte, byte, int, int) generate object},
     * or {@link com.UKC_AICS.simulation.managers.SimulationManager#generateBoid(byte, byte, int, int)} generate boid} methods
     * @param screenX
     */
    public void pickPoint(int screenX, int screenY) {
    	HashMap<String, Byte> tileInfo = simulationManager.getTileInfo(screenX, screenY);

        //What should happen when clicking on the screen
    	if(ControlState.STATE == ControlState.State.NAVIGATE){
	        Entity entity = simulationManager.getBoidAt(screenX,screenY);
            if(entity != null) {
                if (scissorRect.contains(screenX, screenY)) gui.selectEntity(entity, true);
                return;
            }
//            else
//                gui.selectEntity(null, true);
	        return;
    	}
    	if(ControlState.STATE == ControlState.State.PLACEMENT){
    		if(SelectedEntity.selected()){
    			if(SelectedEntity.boid())
    				simulationManager.generateBoid(SelectedEntity.subType(), SelectedEntity.group(), screenX, screenY);
    			else
    				simulationManager.generateObject(SelectedEntity.type(), SelectedEntity.subType(), screenY, screenY);
    		}

			return;
    	}
    }

    /**
     * Sets the mouse position {@link #mousePosition vector}
     * @param x
     * @param y
     */
    public void setMousePosition(int x, int y){
    	mousePosition.x = x;
    	mousePosition.y = y;
    }

    /**
     * Displays information concerning the cursor position in world coordinates and info about the simulation environment cell that
     * corresponds to that position.
     * @see com.UKC_AICS.simulation.managers.SimulationManager#getTileInfo(int, int)
     * @param x
     * @param y
     */
	public void setMouseWorldPosition(int x, int y) {

		HashMap<String, Byte> tileInfo = simulationManager.getTileInfo(x, y);

        gui.setConsole("x: " + x + " y: " + y + "\tt:" + tileInfo.get("terrain") + " g:" + tileInfo.get("grass") + " w:" + tileInfo.get("water") + " b:" + tileInfo.get("blocked"));

	}


}

