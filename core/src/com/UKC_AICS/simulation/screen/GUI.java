package com.UKC_AICS.simulation.screen;

import com.UKC_AICS.simulation.Simulation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by James on 02/07/2014.
 */
public class GUI {

    SimulationScreen simScreen;
    // GUI stuff
    Stage stage;
    Table table;
    Skin skin;
    Label fps;

    public GUI(SimulationScreen ss) {
        simScreen = ss;
    }

    public void setStage() {
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
                if(simScreen.running){
                    playButton.setText("Play");
                }
                else {
                    playButton.setText("Pause");
                }
                simScreen.flipRunning();
            }
        });

        //
        final TextButton resetButton = new TextButton("Reset", skin, "default");
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                simScreen.simulationManager.reset();
                simScreen.setup();
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

    public String getFPSString() {
        return "fps: " + Gdx.graphics.getFramesPerSecond() ;
    }

}
