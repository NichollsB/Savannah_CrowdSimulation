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
import com.UKC_AICS.simulation.managers.SimulationManager;

/**
 * @author Emily
 */
public class SimulationScreen implements Screen {

    private boolean render = true;   // for render pausing
    boolean running = true;  //for play pausing.

    private final Simulation simulation;
    private Camera camera;

    private Environment environment; //lighting things

    public SimulationManager simulationManager = new SimulationManager();

    private BitmapFont font = new BitmapFont();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private BoidGraphics boidGraphics = new BoidGraphics();

    SimScreenGUI gui = new SimScreenGUI(this); // Creates gui instance for this screen

    private InputMultiplexer input;
    private InputManager inputManager = new InputManager(this);

    public SimulationScreen(Simulation simulation) {
        this.simulation = simulation;

        input = new InputMultiplexer();

        input.addProcessor(gui.setStage());  //sets up GUI
        input.addProcessor(inputManager);

        Gdx.input.setInputProcessor(input);
        //Gdx.input.setInputProcessor(inputManager);


        setup();

    }

    @Override
    public void render(float delta) {
        //kind of the update loop.
        if (running) {
            simulationManager.update();
        }

        // checks if simulation needs to be rendered or can be run "offline"
        if (render) {

            gui.fps.setText(getFPSString() + simulationManager.getTime());
            tickPhysics(delta);
            clearOpenGL();
            boidGraphics.update(spriteBatch);
            renderSpriteBatches();

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
        spriteBatch.begin();

        gui.stage.draw();  //GUI stuff
//        Table.drawDebug(stage);  //debug lines for UI
//        font.draw(spriteBatch, getFPSString(), 0, 20);
        spriteBatch.end();
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
        createCamera(width, height);
        gui.stage.getViewport().update(width, height, true);
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

        camera = new OrthographicCamera();

    }

    public void setup() {
        setupCameraController();
        boidGraphics.initBoidSprites(simulationManager.getBoids(), simulationManager.getTextureLocations());
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
