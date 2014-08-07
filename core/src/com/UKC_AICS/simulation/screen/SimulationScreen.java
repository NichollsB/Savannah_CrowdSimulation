package com.UKC_AICS.simulation.screen;

import static com.UKC_AICS.simulation.Constants.TILE_SIZE;

import com.UKC_AICS.simulation.Constants;
import com.UKC_AICS.simulation.Simulation;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.gui.controlutils.ControlState;
import com.UKC_AICS.simulation.gui.controlutils.ControlState.State;
import com.UKC_AICS.simulation.gui.controlutils.SelectedEntity;
import com.UKC_AICS.simulation.gui.controlutils.TreeOptionsListener;
import com.UKC_AICS.simulation.screen.graphics.Graphics;
import com.UKC_AICS.simulation.screen.graphics.TileGraphics;
import com.UKC_AICS.simulation.screen.gui.SimScreenGUI;
import com.UKC_AICS.simulation.screen.gui.SimViewport;
import com.UKC_AICS.simulation.utils.EnvironmentLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

import java.util.HashMap;


/**
 * @author Emily
 */
public class SimulationScreen implements Screen, TreeOptionsListener {

    private boolean render = true;   // for render pausing
    private boolean update = false;
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

    private Rectangle viewRect;
    
    
    private Graphics boidGraphics;

    public SimScreenGUI gui; //= new SimScreenGUI(this); // Creates gui instance for this screen

    private InputMultiplexer input;
    private InputManager inputManager;
    
    TileGraphics tiling;
    
    public final Vector2 mousePosition = new Vector2();
    

    public SimulationScreen(Simulation simulation) {
        this.simulation = simulation;
        gui = new SimScreenGUI(this, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setup();
        
        
        
        //Test
//        EnvironmentLoader.loadMaps(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        byte[][] imgArray = EnvironmentLoader.getLayer_values("InitialGrass_Map");
//        for(int x = 0; x < imgArray.length; x++){
//        	for(int y = 0; y < imgArray[x].length; y++){
//        		System.out.print(imgArray[x][y] + " : ");
//        	}
//        	System.out.println();
//        }

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
    	if (render) {
    		 try {
                 long number = (long) (1000 / 60 - Gdx.graphics.getDeltaTime());
                 if(number < 0) number = 0;//fixed?
                 Thread.sleep(number); //FIXME: this can go negative after leaving the screen alone for a while. crashes program
             } catch (InterruptedException e) {
                 System.out.print("Error...");
                 e.printStackTrace();
             }
    	
//            simBatch.disableBlending();
            clearOpenGL();
//            simBatch.begin();
            renderSpriteBatches(render);
            renderUIBatch(render);
//            simBatch.end();
    	}
     
    	
//        simBatch.flush();
//        simBatch.end();
//        Gdx.gl.glFlush();
//        Gdx.gl.glFinish();
    }

    private void renderUIBatch(boolean render){
    	simBatch.begin();
    	gui.fps.setText(getFPSString() + simulationManager.getTime());
    	uiViewport.update();
        simBatch.setProjectionMatrix(uiCamera.combined);
        gui.update(simBatch, render);
        simBatch.end();
    }
    
   
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
	    	boidGraphics.update(simBatch, viewRect);
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
    	gui.resize(width, height);
        viewRect = gui.getViewArea();
        inputManager.resize(viewRect);
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
    	input.addProcessor(inputManager);
        input.addProcessor(gui);  //sets up GUI
        

        Gdx.input.setInputProcessor(input);
        resize(width, height);

    }

    /**
     * 
     */
    public void setup() {
    	EnvironmentLoader.loadMaps();
    	
    	boidGraphics = new Graphics(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setupCameraController();
        initialiseCameras(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //Graphics components
        boidGraphics.initBackground();
        boidGraphics.setBoids(simulationManager.getBoids());
        boidGraphics.initBoidSprites(simulationManager.getTextureLocations());
        boidGraphics.setBoidSprite_Colours(simulationManager.getRGBValues());
        boidGraphics.initObjSprites(simulationManager.getObjects());
        boidGraphics.initTileSprites(simulationManager.getFullInfo());
        //boidGraphics.initTileSprites(simulationManager.getMapTiles());
        
        //UI
        gui.createBoidTree(simulationManager.getSpeciesInfo(), simulationManager.getBoids());
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
    	HashMap<String, Byte> tileInfo = simulationManager.getTileInfo(screenX, screenY);
        gui.setConsole("x: " + screenX + " y: " + screenY + " t:" + tileInfo.get("terrain") + " g:" + tileInfo.get("grass"));
        //What should happen when clicking on the screen
    	if(ControlState.STATE == ControlState.State.NAVIGATE){
	        Boid boid = simulationManager.getBoidAt(screenX,screenY);
	//        System.out.println(simulationManager.getBoidAt(screenX,screenY));
	        if(viewRect.contains(screenX, screenY)) gui.selectBoid(boid);
	        return;
    	}
    	if(ControlState.STATE == ControlState.State.PLACEMENT){
    		if(SelectedEntity.selected()){
    			simulationManager.generateBoid(SelectedEntity.subType(), SelectedEntity.group(), screenX, screenY);
    		}
			return;
    	}
//        if (boid == null) {

    }
    public void setMousePosition(int x, int y){
    	mousePosition.x = x;
    	mousePosition.y = y;
    }

	@Override
	public void onAdd(byte type, byte subtype, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemove(byte type, byte subtype, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheck(byte type, byte subtype, Object object,
			boolean isChecked, State stateChanged) {
		// TODO Auto-generated method stub
		
	}


}
