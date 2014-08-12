package com.UKC_AICS.simulation.screen.gui;

import java.util.HashMap;

import com.UKC_AICS.simulation.entity.*;
import com.UKC_AICS.simulation.gui.controlutils.DialogueWindowHandler;
import com.UKC_AICS.simulation.gui.controlutils.HoverListener;
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
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
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
public class SimScreenGUI extends Stage implements DialogueWindowHandler, HoverListener {
	final private ScreenViewport uiViewport = new ScreenViewport();
	private SimulationScreen simScreen;
	public Stage stage;
	private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));;
	Table table;
	//Sizing parameters
	private final int 
			NORTH_HEIGHT = 50,
			EAST_WIDTH = 200,
			WEST_WIDTH = 200,
			SOUTH_HEIGHT = 80;
	
	private final int screenOffset = 6;
	private final Rectangle screenRect = new Rectangle(1,1,1,1);
	//Changing components:
	 TextArea console;

    

    //east
	 private Label boidInfo;
	 private boolean showBoidInfo = false;
	 private boolean showSpeciesInfo = false;
	 private Boid boidDisplaying;
	 private byte speciesDisplaying;
    
    
    //South
    public Label fps;


    //Centre
    private Table viewArea;
    
    //West
    private Array<Entity> boids;
    private final BoidListWindow boidTree = new BoidListWindow("Boids", skin, this, true, (byte)1);
    private final BoidListWindow objectTree = new BoidListWindow("Objects", skin, this, false, (byte)1);
    
	//Hovering popup
	private final Window hoverWindow = new Window("", skin);
	private final Label hoverLabel = new Label("", skin);
	private Actor hoverTip;
	
	//SplitPane's
	private SplitPane north_1;
	private SplitPane south_2;
	private SplitPane east_3;
	private SplitPane west_4;
	
	//Table areas
	private Table west = new Table();
	private Table east = new Table();
	private Table north = new Table();
	private Table south = new Table();
    
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
//        table.setPosition(0, 0);
       
//        table.pack();
//        table.debug();
   
        
        stage.addActor(table);

        table.setFillParent(true);

        //
        console = new TextArea("console log",skin);
        
        north = createNorth(table);
        
        
        table.add(north).top().height(NORTH_HEIGHT).expandX().fillX();
        table.row();
        
    	
        west = createWest(table);
//        table.add(west).left().width(WEST_WIDTH).fillY().expandY();
        viewArea = createCentre(table);
//        table.add(viewArea).center().fill().expand();
        east = createEast(table);
//        table.add(east).left().width(EAST_WIDTH).fillY().expandY();
//        table.row();
        float splitwidth = (float)width/2;
//        float maxsplit = (1f/width)*splitwidth;
        System.out.println("Split width " + splitwidth);
        float split;
        split = ((1f/splitwidth) * (float)WEST_WIDTH);
        west_4 = new SplitPane(west, null, false, skin);
        west_4.setSplitAmount(split);
        west_4.setMaxSplitAmount(1f);
        west_4.setMinSplitAmount(0.01f);
//      pane
        east_3 = new SplitPane(null, east, false, skin);
//        pane1.setMaxSplitAmount(720);
//        pane1.setMinSplitAmount(0);
        
        split = 1 - ((1f/splitwidth) * (float)EAST_WIDTH);
        System.out.println(split);
        
        System.out.println("SPLIT AMOUNT " + split);
//        split = 1-split;
        east_3.setSplitAmount(split);
        
        east_3.setMinSplitAmount(0f);
        east_3.setMaxSplitAmount(0.99f);
        Table splitPanes = new Table();
        splitPanes.add(west_4).left().width(width).fill().expand();//fillY().expandY();
        splitPanes.add(east_3).left().width(width).fill().expand();//fillY().expandY();
        
        Table bottom = new Table();
        SplitPane pane = new SplitPane(splitPanes, bottom, true, skin);
        pane.setSplitAmount(1);
       
//        splitPanes.debug();
        
       
        table.add(pane).fill().expand();
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
        
	private Table createNorth(Table t){
    	Table menuTable = new Table(skin);
//    	t.add(menuTable).top().height(NORTH_HEIGHT).expandX().fillX().colspan(3);
    	menuTable.add("north").fill();
//    	menuGroup.fill(Gdx.graphics.getWidth());
    	return menuTable;
    }
	
	private Table createMenuBar(){
		return table;

	}
	private Table createOptionsMenu(){
		return table;
		
	}
	
    private Table createSouth(Table t){
    	Table southTable = new Table(skin);
//    	t.add(southTable).bottom().height(SOUTH_HEIGHT).expandX().fillX().colspan(3);
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
            	
            }
        });
        
      //Load button
        final TextButton loadButton = new TextButton("Load", skin, "default");
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	simScreen.simulationManager.clear();
            	simScreen.simulationManager.load();
            	
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
        // EA settings button.
        final TextButton EAButton = new TextButton("EA Settings", skin,"default");
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
        southTable.add(fps).size(100f, 30f).bottom().left().padLeft(20f).padBottom(10f);
        southTable.row();
        southTable.add(playButton).size(100f, 30f).bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(resetButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(EAButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(switchButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(saveButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
        southTable.add(loadButton).size(100f, 30f).expandX().bottom().left().padLeft(20f).padBottom(10f);
       
        //table.add(graphicsWindow).size(500f,500f);
        southTable.add(console).size(500f,30f).bottom();
		return southTable;
    }
    private Table createCentre(Table t){
    	Table centreTable = new Table(skin);
//    	t.add(centreTable).center().fill().expand();
    	return centreTable;
    }
    
    private Table createEast(Table t){
    	Table eastTable = new Table(skin);
//    	t.add(eastTable).left().width(EAST_WIDTH).fillY().expandY();
    	boidInfo = new Label("some stuff ", skin);
    	boidInfo.setAlignment(Align.left);

    	eastTable.add(createScrollPane(boidInfo)).top().fill().expand();
		eastTable.pack();
		
    	return eastTable;
    }
    private Table createWest(Table t){
    	Table westTable = new Table(skin);
    	SplitPane pane = new SplitPane(boidTree, objectTree, true, skin);
    	westTable.add(pane).fill().expand();
//    	t.add(westTable).left().width(WEST_WIDTH).fillY().expandY();
//    	westTable.add(new Label("Boids", skin));
//    	westTable.row();
//   	 	westTable.add(boidTree).top().fill().expand();
//   	 	westTable.row();
//   	 	westTable.add(objectTree).top().fill().expand();
//   	 	westTable.pack();
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
    		this.boids = boids;
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
    
    public void resize(int width, int height){
    	setViewRect(width, height);
    	boidTree.resize();
    	
//    	objectTree.resize();
    }
    
    private void setviewRect(int left, int right, int top, int bottom){
    	if(screenRect!=null){
    		screenRect.setSize(right-left, top-bottom);
    		screenRect.setPosition(left, bottom);
    	}
	}
    private void setViewRect(int width, int height){
    	if(screenRect!=null){
    		screenRect.setSize(width - (EAST_WIDTH + WEST_WIDTH),
    				height - (NORTH_HEIGHT + SOUTH_HEIGHT));
    		screenRect.setPosition(WEST_WIDTH, SOUTH_HEIGHT);
    	}
    }
    
    private void setViewRect(){
    	if(viewArea != null){
	        screenRect.setSize(viewArea.getWidth(), viewArea.getHeight());
	        screenRect.setPosition(viewArea.getX(), viewArea.getY());
    	}
    }
    private void setViewRect(Table north, Table south, Table east, Table west) {
    	float width = Gdx.graphics.getWidth() - (east.getWidth() + west.getWidth())-(screenOffset*2);
    	float height = Gdx.graphics.getHeight() - (north.getHeight() + south.getHeight())-(screenOffset*2);
  		screenRect.set(west.getWidth()+screenOffset, north.getHeight()+screenOffset, width, height);
  	}
    
    public Rectangle getViewArea(){
        //setViewRect();
    	return screenRect;
    }

	public void update(SpriteBatch batch, boolean render) {

//		batch.begin();
        if(render){
//        	if(showBoidInfo){
//        		boidInfo.setText(boidDisplaying.toString());
//        	}
//        	else 
//        		boidInfo.setText("");
        	setViewRect(north, south, east, west);
        	if(boids != null){
//        		System.out.println("update tree");s
        		boidInfo.setText(boidTree.update(boids, true));
        	}
	        stage.act();
	    
	    	stage.draw();  //GUI stuff
	    	
        }
//        batch.end();
//        table.debug();
        Table.drawDebug(stage);  //debug lines for UI
//        font.draw(spriteBatch, getFPSString(), 0, 20);
    	
	}
	


	
	public void showBoidInfo(Boid boid, boolean show){
		if(boidDisplaying!= null)boidDisplaying.setTracked(false);
		if(!show){
			showBoidInfo= false;
			return;
		}
		showBoidInfo = true;
		boidDisplaying = boid;
		boidDisplaying.setTracked(true);
	}
	

	
	public void toggleButton(boolean toggled, Button btn){
		btn.setChecked(toggled);
	}
	
	private Actor createScrollPane(Actor content){
    	Table scrollTable = new Table(skin);
//    	scrollTable.add("east");

		scrollTable.add(content).left();

		ScrollPane scroll = new ScrollPane(scrollTable, skin);
    	InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};

		scroll.setSmoothScrolling(true);
		scroll.setScrollBarPositions(false, false);
		
		return scroll;
	}
	
	public void selectEntity(Entity entity){
		boolean select = false; 
		select = (entity == null) ? false : true;
		boidTree.selectNodeByBoid(entity, select);
//		objectTree.selectNodeByBoid(entity, select);
	}
	

	//SETTINGS WINDOWS METHODS
	@Override
	public void onConfirmed(ObjectMap<String, String> value, Window window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelled(Window window) {
		// TODO Auto-generated method stub
		
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
}
