package com.UKC_AICS.simulation.screen.gui;

import java.io.File;
import java.util.HashMap;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.screen.controlutils.HoverListener;
import com.UKC_AICS.simulation.screen.controlutils.MenuSelectListener;
import com.UKC_AICS.simulation.screen.controlutils.RenderState;
import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by James on 02/07/2014.
 * Class for the creation of gui for simulationScreen
 */
public class SimScreenGUI extends Stage implements HoverListener {
	final private ScreenViewport uiViewport = new ScreenViewport();
	private SimulationScreen simScreen;
	public Stage stage;
	private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	Table table;
	//Sizing parameters
	private final int 
			NORTH_HEIGHT = 50,
			EAST_WIDTH = 200,
			WEST_WIDTH = 200,
			SOUTH_HEIGHT = 80,
			BOTTOM_HEIGHT = 200;
	
	private final int screenOffset = 6;
	private final Rectangle screenRect = new Rectangle(1,1,1,1);
	//Changing components:
	 TextArea console;

    

    //east
	 private Label boidInfo;
	 private Boid boidDisplaying;

    //South
    public Label fps;

    //West
    private Array<Entity> boids;
    private Array<Entity> objects;
    private final EntityTreeTable boidTree = new EntityTreeTable("Boids", skin, this, true, (byte)1);
    private final EntityTreeTable objectTree = new EntityTreeTable("Objects", skin, this, false, (byte)1);
    
	//Hovering popup
	private final Label hoverLabel = new Label("", skin);

	//SplitPane's
	private SplitPane eastSplit;
	private SplitPane westSplit;
	
	//Table areas
	private Table west = new Table();
	private Table east = new Table();
	private Table north = new Table();
	private Table south = new Table();

	
	//File Choosers
	private FileChooser fileChooser;
	private final ObjectMap<Button, FileChooser> fileChoosers = new ObjectMap<Button, FileChooser>();

    private Actor infoItemSelected;

	private RenderOptionsWindow renderOptions;
    private EnvironmentFileWindow environmentOptions;

    private final Table legend = new Table();

    private ObjectMap<Button, FileChooser> chooserMap = new ObjectMap<Button, FileChooser>();
	
    /**
     *
     * @param ss the simulationScreen creating the gui
     */
    public SimScreenGUI (SimulationScreen ss, int width, int height) {
        simScreen = ss;
        setStage(width, height);
        
        hoverLabel.setTouchable(Touchable.disabled);
//        hoverWindow.add(hoverLabel).expand().fill();
    }


    /**
     * setups up the UI.
     */
    public void setStage(int width, int height) {


    	screenRect.setSize(width, height);
    	
    	this.setViewport(uiViewport);
        stage = this;
    	
        table = new Table();
        table.setSize(width, height);
        fileChooser = new FileChooser("Load", skin, "Load", "Species", ".", "Confirm", "Cancel", this);
        fileChooser.addSelectionListener(new MenuSelectListener(){
			public void selectionMade(java.lang.Object menu, java.lang.Object object) {
				FileChooser chooser = (FileChooser) menu;
				simScreen.simulationManager.loadSaveCall(chooser.getCommand(), chooser.getIdentifier(), (File)object);
			}
		});
        
        renderOptions = new RenderOptionsWindow("Render Options", skin, null, null, null, stage);
        renderOptions.addSelectionListener(new MenuSelectListener(){
        	public void selectionMade(java.lang.Object menu, java.lang.Object object) {
//        		System.out.println();
        		RenderOptionsWindow window = (RenderOptionsWindow) menu;
        		RenderState.changeTileState(RenderState.TILESTATE.stateName, window.getRenderType());
        	}
        });
        environmentOptions = new EnvironmentFileWindow("Environment Options", skin, stage);
        environmentOptions.addSelectionListener(new MenuSelectListener(){
            public void selectionMade(java.lang.Object menu, java.lang.Object object) {
        		System.out.println("istener triggered");
               EnvironmentFileWindow window = (EnvironmentFileWindow) menu;
                HashMap<String, File> files = new HashMap<String, File>();
                if(window.fromPackFile()) {
                    for (String s : window.getPackFiles().keys()) {
                        files.put(s, window.getPackFiles().get(s));
                    }
                    simScreen.simulationManager.loadSaveCall("load", "envpack", files);
                }
                else{
                    for (String s : window.getFiles().keys()) {
                        files.put(s, window.getFiles().get(s));
                    }
                    simScreen.simulationManager.loadSaveCall("load", "envatlas", files);
                }
            }
        });
//        fileChooser.hide();
//        fileChooser.open(this);
        stage.addActor(table);
        table.setFillParent(true);
        console = new TextArea("console log",skin);
        north = createNorth(table);
        table.add(north).top().height(NORTH_HEIGHT).expandX().fillX();
        table.row();

        west = createWest(table);
        east = createEast(table);
        float splitwidth = (float)width/2;
//        float maxsplit = (1f/width)*splitwidth;
        float split;
        split = ((1f/splitwidth) * (float)WEST_WIDTH);
        westSplit = new SplitPane(west, null, false, skin);
        westSplit.setSplitAmount(split);
        westSplit.setMaxSplitAmount(1f);
        westSplit.setMinSplitAmount(0.01f);
        eastSplit = new SplitPane(null, east, false, skin);
        split = 1 - ((1f/splitwidth) * (float)WEST_WIDTH);
        eastSplit.setSplitAmount(split);
        eastSplit.setMinSplitAmount(0f);
        eastSplit.setMaxSplitAmount(0.99f);

        Table splitPanes = new Table();
//        splitPanes.add(westSplit).left().width(width).fill().expand();
        splitPanes.add(westSplit).left().width(width).fill().expand();//fillY().expandY();
        splitPanes.add(eastSplit).left().width(width).fill().expand();//fillY().expandY();
        
        Table bottom = new Table();

        table.add(splitPanes).fill().expand();
//        table.add(splitPanes).fill().expand();
        table.row();
        
        south = createSouth(table);
      
        
        table.add(south).bottom().height(SOUTH_HEIGHT).expandX().fillX();
        table.pack();
        setViewRect(north, south, east, west);

//        SplitPane pane4 = new SplitPane(west, viewArea, false, skin);
//        pane4.setSplitAmount(EAST_WIDTH + screenRect.width);

//        SplitPane pane3 = new SplitPane(east, pane4, false, skin);
//        pane3.setMinSplitAmount(EAST_WIDTH);
//        pane3.setMaxSplitAmount(EAST_WIDTH + screenRect.width);
//        pane3.setSplitAmount(EAST_WIDTH);



//        table.add(pane1).top().expandX().fillX();
//        screenRect.set(0, 0, width, height);
    }
    

	private Table createNorth(Actor a){
    	Table menuTable = new Table(skin);
        final SelectBox renderSelect = new SelectBox(skin);
        renderSelect.setItems(new String[]{"Tiles", "Mesh", "Off"});
        renderSelect.setSelected("Tiles");
//    	//SPECIES LOAD/SAVE
//    	final MenuDropdown menu = createFileMenu(new String[]{"load", "save"}, new String[]{"Load", "Save"}, "Species Settings",
//    			"SPECIES");
//    	menuTable.add(menu).padLeft(5);

        TextButton button;
        //SET EA DIRECTORY
        button = new TextButton("EA File Location", skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setOptionsText("Load EA File", "Cancel");
                fileChooser.setCommand("load");
                fileChooser.setIdentifier("eafile");
                fileChooser.open(stage);
            }
        });
        menuTable.add(button).padLeft(5);
//
       button = new TextButton("Sim Record Location", skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fileChooser.setOptionsText("Set Record Location", "Cancel");
                fileChooser.setCommand("load");
                fileChooser.setIdentifier("record");
                fileChooser.open(stage);
            }
        });
        menuTable.add(button).padLeft(5);

        final CheckBox recordCheck = new CheckBox("Record Simulation", skin);
        recordCheck.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(recordCheck.isChecked()){
                    RenderState.changeTileState("off");
                    renderSelect.setSelected("Off");
                    simScreen.simulationManager.commandCall("record", "start");
                }
                else if(!recordCheck.isChecked())
                    simScreen.simulationManager.commandCall("record", "stop");
            }
        });
        menuTable.add(recordCheck).padLeft(5);

        //ENVIRONMENT OPTIONS
        menuTable.add(new Table()).fillX().expandX();
        button = new TextButton("Load Environment", skin);
        button.padLeft(5).padRight(5);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                environmentOptions.open(stage);
            }
        });
        menuTable.add(button).padLeft(5);

        //RENDER OPTIONS
//        button = new TextButton("Render Options", skin);
//    	button.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                renderOptions.open(stage);
//            }
//        });
//    	menuTable.add(button).padLeft(5);

        //Render type
//        MenuDropdown renderSelection = new MenuDropdown(skin, "Environment Render Type", "rendertype");
//        renderSelection.addItems(new String[]{"Tiles", "Mesh"}, true);
//        renderSelection.addSelectionListener(new MenuSelectListener(){
//            @Override
//            public void selectionMade(java.lang.Object menu, java.lang.Object object){
//                String s = (String)object;
//                if(s.equalsIgnoreCase("tiles")){
//                    RenderState.changeTileState("tiled");
//                }
//                if(s.equalsIgnoreCase("mesh")){
//                    RenderState.changeTileState("mesh");
//                }
//            }
//        });

        renderSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (renderSelect.getSelected().equals("Tiles")) {
                    simScreen.simulationManager.commandCall("record", "stop");
                    RenderState.changeTileState("tiled");
                }
                if (renderSelect.getSelected().equals("Mesh")) {
                    simScreen.simulationManager.commandCall("record", "stop");
                    RenderState.changeTileState("mesh");
                }
                if(renderSelect.getSelected().equals("Off")){
                    RenderState.changeTileState("off");
                }
            }
        });

        menuTable.add(new Label("Environment Render Type:", skin)).padRight(5).padLeft(20);
        menuTable.add(renderSelect).width(100).padLeft(5).padRight(20);
//    	menuTable.add(new Table()).fillX().expandX();
    	return menuTable;
    }
	
	private MenuDropdown createFileMenu(final String options[], final String optionsText[], String name, final String identifier){
    	final MenuDropdown menu = new MenuDropdown(skin, name, identifier);  
//    	String items[] = {"Load", "Save"};
    	menu.addItems(optionsText, true);
    	
    	menu.addSelectionListener(new MenuSelectListener(){
    		@Override
    		public void selectionMade(java.lang.Object menu, java.lang.Object object){
    			for(int i = 0; i < options.length; i ++){
    				String option = options[i];
    				if(option.equalsIgnoreCase((String)object)){
    					fileChooser.setOptionsText(optionsText[i], "Cancel");
    					fileChooser.setCommand(option);
    					fileChooser.setIdentifier(identifier);
    					fileChooser.open(stage);
    				}
    			}
    		}
    	});
    	return menu;
	}


    private Table createSouth(Actor a){
    	Table southTable = new Table(skin);
//    	t.add(southTable).bottom().height(SOUTH_HEIGHT).expandX().fillX().colspan(3);
//    	southTable.add("south");
        // play/pause button
        final TextButton playButton = new TextButton("Play", skin);
        playButton.pad(5);
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
        final TextButton resetButton = new TextButton("Reset", skin);
        resetButton.pad(5);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simScreen.simulationManager.reset();
                simScreen.setup();
                simScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        });
        
      //Save button
        final TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	simScreen.simulationManager.save();
            	
            }
        });
        
      //Load button
        final TextButton loadButton = new TextButton("Load", skin);
        loadButton.pad(5);
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	simScreen.simulationManager.clear();
            	simScreen.simulationManager.load();
            	
            }
        });

        // Switch mode button.
        final TextButton switchButton = new TextButton("Switch", skin);
        switchButton.pad(5);
        switchButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simScreen.flipRender();
                //Switch running mode

            }
        });
        // EA settings button.
        final TextButton EAButton = new TextButton("EA Settings", skin);
        EAButton.pad(5);
        EAButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //EA Settings menu appears
            	
            	simScreen.flipEARender();               
          
            }
        });
        

        fps = new Label("0", skin);
      
        HorizontalGroup southGroup = new HorizontalGroup();
//        southGroup.addActor(fps);
//        fps.setWidth(500f);
//        southTable.addActor(playButton);
//        playButton.setSize(100f, 30f);
        Table legendTable = new Table();
        legendTable.add(fps).bottom().left().padLeft(20f).padBottom(10f);
        legendTable.add(legend).fillX().expandX();
//        southTable.add(fps).bottom().left().padLeft(20f).padBottom(10f).fillX().expandX();
//        southTable.add(new Table()).fillX().expandX().colspan(6);
        southTable.add(legendTable).fillX().expandX();
        southTable.row();
        Table btnTable = new Table();
        btnTable.add(playButton).bottom().left().padLeft(20f).padBottom(10f);
        btnTable.add(resetButton).bottom().left().padLeft(20f).padBottom(10f);
        btnTable.add(EAButton).bottom().left().padLeft(20f).padBottom(10f);
        btnTable.add(switchButton).bottom().left().padLeft(20f).padBottom(10f);
        btnTable.add(saveButton).bottom().left().padLeft(20f).padBottom(10f);
        btnTable.add(loadButton).bottom().left().padLeft(20f).padBottom(10f);
       
        //table.add(graphicsWindow).size(500f,500f);
        btnTable.add(new Table()).expandX().fillX();
        btnTable.add(console).bottom().width(400).padLeft(20).padRight(10);
        southTable.add(btnTable).fillX().expandX().padBottom(20);
        southTable.padTop(20);
		return southTable;
    }

    private Table createEast(Actor a){
    	Table eastTable = new Table(skin);
//    	t.add(eastTable).left().width(EAST_WIDTH).fillY().expandY();
    	boidInfo = new Label("", skin);
    	boidInfo.setAlignment(Align.left);
        eastTable.add(new Label("Information Display", skin)).center().pad(5).fillX().expandX();
        eastTable.row();
    	eastTable.add(createScrollPane(boidInfo, false, true, true, true)).top().fill().expand();
		eastTable.pack();
		
		
    	return eastTable;
    }
    private Table createWest(Actor a){
    	Table westTable = new Table(skin);
    	SplitPane pane = new SplitPane(boidTree, objectTree, true, skin);
    	westTable.add(pane).fill().expand();
    	return westTable;
    }

    public void createBoidTree(HashMap<Byte, Species> species, Array boids){
    	try{
	    	this.boids = boids;
	    	for(Byte b : species.keySet()){
	//    		boidTree.addBoidNode(b, species.get(b).getName(), species.get(b).toString(), null);
	    		boidTree.addRootNode(b, species.get(b).getName(), species.get(b).toString());
	    	}
	    	boidTree.compareAndUpdateNodes(boids);
    	}catch(NullPointerException e){
    		System.out.println(this.getClass() + " failed to create EntityListWindow - ");
    	}
    }
    public void createObjectTree(HashMap<Byte, ObjectData> objData, Array objects){
//    	System.out.println("creating objectree datamap = " + objData + " objects = " + objects);
    	try{
    		this.objects = objects;
	    	for(Byte b : objData.keySet()){
	    		objectTree.addRootNode(b, objData.get(b).getName(), objData.get(b).toString());
	    	}
	    	objectTree.compareAndUpdateNodes(objects);
	    }catch(NullPointerException e){
			System.out.println(this.getClass() + "failed to create EntityListWindow - ");
		}
    }
    
    public void setConsole(String log){
    	if(log != null)
    		console.setText(log);
    	else
    		console.setText("");
    }

    private void setViewRect(Table north, Table south, Table east, Table west) {
//    	float width = Gdx.graphics.getWidth() - (east.getWidth() + west.getWidth())-(screenOffset*2);
//    	float height = Gdx.graphics.getHeight() - (north.getHeight() + south.getHeight())-(screenOffset*2);
//    	System.out.println("Widths " + east.getWidth() + " screen " + Gdx.graphics.getWidth() + " " + 
//    			(Gdx.graphics.getWidth() - east.getWidth()));
    	float width = (Gdx.graphics.getWidth() - east.getWidth() - west.getWidth()) - (screenOffset*2);
    	float height = Gdx.graphics.getHeight() - (north.getHeight() + south.getHeight()) - (screenOffset*2);
  		screenRect.set(west.getWidth()+screenOffset, south.getHeight()+(screenOffset), width, height);
    }
    
    public Rectangle getViewArea(){
    	return screenRect;
    }

	public void update(SpriteBatch batch, boolean render) {

        if(render){
        	setViewRect(north, south, east, west);
            if(infoItemSelected != null) {
                if(infoItemSelected.equals(boidTree)) {
                    if (boids != null) {
                        boidInfo.setText(boidTree.update(boids, true));
                        objectTree.selectNodeByEntity(null, false);
                    }
                }
                else if(infoItemSelected.equals(objectTree)) {
                    if (objects != null) {
                        boidInfo.setText(objectTree.update(objects, true));
                        boidTree.selectNodeByEntity(null, false);
                    }
                }
            }
	        stage.act();
	    
	    	stage.draw();  //GUI stuff
	    	
        }
        Table.drawDebug(stage);  //debug lines for UI

	}
    private Array<String> legendEntries = new Array<String>();

    public boolean addObjectsToLegend(HashMap<Byte, ObjectData> map, HashMap<Byte, Image> images){
        boolean added = false;
        boolean corpseAdded = false;
        for(ObjectData b : map.values()){
            if(b.getType() == (byte)0 && !corpseAdded) {
                added = addToLegend("Corpse", images.get(b.getType()));
                corpseAdded = added;
            }
            else if(b.getType() != (byte)0)
                added = addToLegend(b.getName(), images.get(b.getType()));
        }
        return added;
    }
    public boolean addBoidsToLegend(HashMap<Byte, Species> map, HashMap<Byte, Image> images){
        boolean added = false;
        for(byte b : map.keySet()){
            if(images.containsKey(b)){
                added = addToLegend(map.get(b).getName(), images.get(b));
            }
        }
        return added;
    }
    public boolean addToLegend(String entity, Image img){
        if(entity == null || img == null) return false;
        if(legendEntries.contains(entity, false)) return true;
        Table entry = new Table();
        img.setScale(0.8f, 0.8f);
        entry.add(img).top().padBottom(5);
        entry.add(new Label(entity, skin)).padBottom(5);
        legend.add(entry).padLeft(40);
        return true;
    }
	

	private Actor createScrollPane(Actor content, boolean showX, boolean showY, boolean bottomBar, boolean rightBar){
    	Table scrollTable = new Table(skin);
//    	scrollTable.add("east");
    	Table fillTable = new Table();
		scrollTable.add(content).left();
//		scrollTable.add(fillTable).fill().expand();
		scrollTable.pad(10);
		if(!rightBar)
			scrollTable.padLeft(40);
		else
			scrollTable.padRight(40);
		ScrollPane scroll = new ScrollPane(scrollTable, skin);
    	InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
//		scroll.setForceScroll(true, true);
		scroll.setSmoothScrolling(true);
		scroll.setScrollBarPositions(bottomBar, rightBar);
		scroll.setForceScroll(showX, showY);
//	    scroll.setFlickScroll(true);
	    scroll.setOverscroll(false, false);
	    scroll.setFadeScrollBars(false);
		return scroll;
	}
	
	public void selectEntity(Entity entity){
		boidTree.selectNodeByEntity(entity, (entity == null) ? false : true);
	}


	
	@Override
	public void hover(InputEvent event, float x, float y, String helper) {
		hoverLabel.setText(helper);
//		stage.addActor(hoverLabel);
//		Actor act = (Actor)hoverLabel;
//		hoverLabel.layout();
		hoverLabel.pack();
		
		stage.addActor(hoverLabel);
//		act.setPosition(x, y);
		hoverLabel.setPosition(simScreen.mousePosition.x, simScreen.mousePosition.y);
//		Table.drawDebug(stage);
//		System.out.println("x " + simScreen.mousePosition.x + " y " + simScreen.mousePosition.y + " labelx " + hoverLabel.getX() + " y " + hoverLabel.getY());
	}


	@Override
	public void unhover(InputEvent event) {
//		System.out.println("exit hover");
		hoverLabel.remove();
//		hoverWindow.remove();
	}

    public void setSelectedInfoItem(Actor item){
        infoItemSelected = item;

    }

 
}
