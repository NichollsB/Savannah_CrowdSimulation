package com.UKC_AICS.simulation.screen;

import com.UKC_AICS.simulation.Simulation;
import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.UKC_AICS.simulation.utils.OrthoController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Emily
 */
public class SimulationScreen extends InputMultiplexer implements Screen {

    private Simulation simulation;

    public Camera camera;
    
    private Environment environment; //lighting things
    
    public SimulationManager simulationManager = new SimulationManager();

    private BitmapFont font = new BitmapFont();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private ShapeRenderer shRend = new ShapeRenderer();
    private OrthoController camController;

    public SimulationScreen(Simulation sim) {
        this.simulation = sim;
    }

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
        renderSpriteBatches();
        drawDots();
    }

    private void renderSpriteBatches() {
        spriteBatch.begin();
        font.draw(spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond() +
                        "; Time " + simulationManager.minutes + " mins; " + simulationManager.hours + " hrs; "
                        + simulationManager.days + " days; " + simulationManager.weeks + " wks.",
                0, 20);
        spriteBatch.end();
    }

    private void drawDots() {
//        tempRend.render(simulationManager.getBoids());

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shRend.setProjectionMatrix(camera.combined);
        shRend.setColor(Color.CYAN);
        shRend.begin(ShapeRenderer.ShapeType.Point);
        Vector3 pos;

        for ( Boid b : simulationManager.getBoids() ) {
            pos = b.getPosition();
            shRend.point(pos.x, pos.y, pos.z);
        }
        shRend.end();

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
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, -10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
    }

    private void setup() {
        setupCameraController();
    }
    
    private void setupCameraController() {
        //blah blah create the controller
        //set the controller
        //Gdx.input.setInputProcessor(SOMECAMERCONTROLLER);
        //Gdx.input.setInputProcessor(this);
        camController = new OrthoController(camera);
        Gdx.input.setInputProcessor(camController);
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
        camera.update();
        camController.update();
    }


}
