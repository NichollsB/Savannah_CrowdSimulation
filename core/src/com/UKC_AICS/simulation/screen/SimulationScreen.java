package com.UKC_AICS.simulation.screen;

import com.UKC_AICS.simulation.Simulation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.UKC_AICS.simulation.managers.SimulationManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 *
 * @author Emily
 */
public class SimulationScreen implements Screen {

    private boolean running = true;  //for play pausing.

    private final Simulation simulation;
    private Camera camera;
    
    private Environment environment; //lighting things
    
    private SimulationManager simulationManager = new SimulationManager();

    private BitmapFont font = new BitmapFont();
    private SpriteBatch spriteBatch = new SpriteBatch();
    
    private BoidGraphics boidGraphics = new BoidGraphics();


    // GUI stuff
    private Stage stage;
    private Table table;
    private Skin skin;
    private Label fps;

    public SimulationScreen(Simulation simulation) {
        this.simulation = simulation;
        setStage();  //sets up GUI
        
        setup();
    }

    @Override
    public void render(float delta) {
        //kind of the update loop.
        if (running) {
            simulationManager.update();
        }
        fps.setText(getFPSString());
        tickPhysics(delta);
        clearOpenGL();
        boidGraphics.update(spriteBatch);
        renderSpriteBatches();

        //do render calls for models, sprites, whatever. 
        //(probably done in another class)

        //draw models
        //draw hud/ui etc
    }

    private void renderSpriteBatches() {
        spriteBatch.begin();

        stage.draw();  //GUI stuff
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
        stage.getViewport().update(width, height, true);
        //setup();
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
        setupCameraController();
        boidGraphics.initBoidSprites(simulationManager.getBoids());
    }

    /**
     * setups up the UI.
     */
    private void setStage() {
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.debug();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        fps = new Label(getFPSString(), skin);
        stage.addActor(table);

        table.setFillParent(true);

        // play/pause button
        final TextButton playButton = new TextButton("Play", skin, "default");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(running){
                    playButton.setText("Play");
                }
                else {
                    playButton.setText("Pause");
                }
                flipRunning();
            }
        });

        //
        final TextButton resetButton = new TextButton("Reset", skin, "default");
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            
            	simulationManager.reset();
            	setup();
                //TODO reset of simulation
            	
            	
            }
        });

        //
        TextArea console = new TextArea("console log",skin);

        table.add(fps).bottom().left().expandY().width(500f).pad(0f, 10f, 10f, 0f);
        table.add(playButton).size(100f, 30f).bottom().left().padLeft(20f).padBottom(10f);
        table.add(resetButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        //
        table.add(console).size(300f,30f).bottom();


        Gdx.input.setInputProcessor(stage);
    }

    /**
     *
     * @return gives the current fps and current time count
     */
    private String getFPSString() {
        return "fps: " + Gdx.graphics.getFramesPerSecond() +
                "; Time " + simulationManager.minutes + " mins; " + simulationManager.hours + " hrs; "
                + simulationManager.days + " days; " + simulationManager.weeks + " wks.";
    }

    /**
     * flips the running boolean for simulation updating.
     */
    private void flipRunning() {
        if (running)
            running = false;
        else
            running = true;
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
}
