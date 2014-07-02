package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by James on 02/07/2014.
 */
public class SimScreenGUI {

    SimulationScreen simScreen;

    public Stage stage;
    Table table;
    Skin skin;
    public Label fps;

    public SimScreenGUI (SimulationScreen ss) {
        simScreen = ss;
    }

    /**
     * setups up the UI.
     */
    public void setStage() {
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.debug();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        fps = new Label(simScreen.getFPSString()+ simScreen.simulationManager.getTime(), skin);
        stage.addActor(table);

        table.setFillParent(true);

        // play/pause button
        final TextButton playButton = new TextButton("Play", skin, "default");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(simScreen.getRunning()){
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
}
