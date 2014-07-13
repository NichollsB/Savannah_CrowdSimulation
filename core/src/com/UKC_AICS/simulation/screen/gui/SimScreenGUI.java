package com.UKC_AICS.simulation.screen.gui;

import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by James on 02/07/2014.
 * Class for the creation of gui for simulationScreen
 */
public class SimScreenGUI {
	
	//Changing components:
	 TextArea console;

    SimulationScreen simScreen;

    public Stage stage;
    Table table;
    Skin skin;
    public Label fps;
    public Window graphicsWindow;
    private Rectangle screenRect = new Rectangle();
    private Table viewArea;

    /**
     *
     * @param ss the simulationScreen creating the gui
     */
    public SimScreenGUI (SimulationScreen ss) {
        simScreen = ss;
    }

    public void update(){
    	table.drawDebug(stage);
    	stage.draw();
    }
    /**
     * setups up the UI.
     */
    public Stage setStage() {

    	screenRect.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setSize(1280, 720);
        table.pack();
        table.debug();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        fps = new Label(simScreen.getFPSString()+ simScreen.simulationManager.getTime(), skin);
        stage.addActor(table);
        
    	graphicsWindow = new Window("Graphics Window", skin);
    	graphicsWindow.setMovable(true);
    	graphicsWindow.setResizable(true);
    	
        table.setFillParent(true);



        //
        console = new TextArea("console log",skin);
        
        //table.addActor(graphicsWindow);
        Table north = createNorth(table);
        table.row();
        Table east = createEast(table);
        viewArea = createCentre(table);
    	
        Table west = createWest(table);
        table.row();
        Table south = createSouth(table);

        //table.add(createEast());
        //table.row();

        table.pack();
        setViewRect();
        return stage;
        //Gdx.input.setInputProcessor(stage);
    }
    private Table createNorth(Table t){
    	Table menuTable = new Table(skin);
    	t.add(menuTable).top().height(80).expandX().fillX().colspan(3);
    	menuTable.add("north").fill();
//    	menuGroup.fill(Gdx.graphics.getWidth());
    	return menuTable;
    }
    private Table createSouth(Table t){
    	Table southTable = new Table(skin);
    	t.add(southTable).bottom().height(80f).expandX().fillX().colspan(3);
//    	southTable.add("south");
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

        //Reset button
        final TextButton resetButton = new TextButton("Reset", skin, "default");
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simScreen.simulationManager.reset();
                simScreen.setup();
            }
        });
        
      //Save button
        final TextButton saveButton = new TextButton("Save", skin, "default");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	simScreen.simulationManager.save();
            	System.out.println("Saving");
            }
        });

        // Switch mode button.
        final TextButton switchButton = new TextButton("Switch", skin,"default");
        switchButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simScreen.flipRender();
                //Switch running mode

            }
        });
        HorizontalGroup southGroup = new HorizontalGroup();
//        southGroup.addActor(fps);
//        fps.setWidth(500f);
//        southTable.addActor(playButton);
//        playButton.setSize(100f, 30f);
        southTable.add(fps).size(100f, 30f).bottom().left().padLeft(20f).padBottom(10f);
        southTable.row();
        southTable.add(playButton).size(100f, 30f).bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(resetButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(switchButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(saveButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        //table.add(graphicsWindow).size(500f,500f);
        southTable.add(console).size(300f,30f).bottom();
		return southTable;
    }
    private Table createCentre(Table t){
    	Table centreTable = new Table(skin);
    	t.add(centreTable).center().fill().expand();
    	return centreTable;
    }
    
    private Table createEast(Table t){
    	Table eastTable = new Table(skin);
    	t.add(eastTable).left().width(100).fillY().expandY();
    	eastTable.add("east");
    	return eastTable;
    }
    private Table createWest(Table t){
    	Table westTable = new Table(skin);
    	t.add(westTable).right().width(100f).fillY().expandY();
    	westTable.add("west");
    	return westTable;
    }
    
    public void setConsole(String log){
    	if(log != null)
    		console.setText(log);
    	else
    		console.setText("");
    }
    
    public void resize(int width, int height){
    	setViewRect();
    }
    
    private void setViewRect(){
        screenRect.setSize(viewArea.getWidth(), viewArea.getHeight());
        screenRect.setPosition(viewArea.getX(), viewArea.getY());
    }
    
    public Rectangle getViewArea(){
    	return screenRect;
    }
}
