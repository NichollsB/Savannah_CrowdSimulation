package com.UKC_AICS.simulation.screen.gui;

import java.util.HashMap;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.entity.Species;
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
    private Array<Boid> boids;
    private ObjectMap<Byte, String> species;
    private final BoidListWindow boidTree = new BoidListWindow("Boids", skin, this);
    private final HashMap<Byte, Tree.Node> speciesNodeMap = new HashMap<Byte, Tree.Node>();
    private final HashMap<Boid, Tree.Node> boidNodeMap = new HashMap<Boid, Tree.Node>();
    
	//Hovering popup
	private final Window hoverWindow = new Window("", skin);
	private final Label hoverLabel = new Label("", skin);
	private Actor hoverTip;
    
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

        Table north = createNorth(table);
        table.row();
        
    	
        Table west = createWest(table);
        viewArea = createCentre(table);
        Table east = createEast(table);
        table.row();
        Table south = createSouth(table);
        table.pack();
        setViewRect(north, south, east, west);
//        screenRect.set(0, 0, width, height);
    }    
        
	private Table createNorth(Table t){
    	Table menuTable = new Table(skin);
    	t.add(menuTable).top().height(NORTH_HEIGHT).expandX().fillX().colspan(3);
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
    	t.add(southTable).bottom().height(SOUTH_HEIGHT).expandX().fillX().colspan(3);
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
    	t.add(centreTable).center().fill().expand();
    	return centreTable;
    }
    
    private Table createEast(Table t){
    	Table eastTable = new Table(skin);
    	t.add(eastTable).left().width(EAST_WIDTH).fillY().expandY();
    	boidInfo = new Label("some stuff ", skin);
    	boidInfo.setAlignment(Align.left);

    	eastTable.add(createScrollPane(boidInfo)).top().fill().expand();
		eastTable.pack();
		
    	return eastTable;
    }
    private Table createWest(Table t){
    	Table westTable = new Table(skin);
    	t.add(westTable).left().width(WEST_WIDTH).fillY().expandY();
    	westTable.add(new Label("Boids", skin));
    	westTable.row();
   	 	westTable.add(boidTree).top().fill().expand();
   	 	westTable.pack();
    	return westTable;
    }


    public void createBoidTree(HashMap<Byte, Species> species, Array<Boid> boids){
    	this.boids = boids;
    	for(Byte b : species.keySet()){
//    		boidTree.addBoidNode(b, species.get(b).getName(), species.get(b).toString(), null);
    		boidTree.addRootNode(b, species.get(b).getName(), species.get(b).toString());
    	}
    	boidTree.compareAndUpdateNodes(boids);
    }
    
    public void setConsole(String log){
    	if(log != null)
    		console.setText(log);
    	else
    		console.setText("");
    }
    
    public void resize(int width, int height){
    	setViewRect(width, height);
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
    	float width = Gdx.graphics.getWidth() - (east.getWidth() + west.getWidth());
    	float height = Gdx.graphics.getHeight() - (north.getHeight() + south.getHeight());
  		screenRect.set(west.getWidth(), north.getHeight(), width, height);
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
        	if(boids != null){
//        		System.out.println("update tree");s
        		boidInfo.setText(boidTree.update(boids, true));
        	}
	        stage.act();
	    
	    	stage.draw();  //GUI stuff
	    	
        }
//        batch.end();
//        Table.drawDebug(stage);  //debug lines for UI
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
	
	public void selectBoid(Boid boid){
		if(boid == null) boidTree.selectNodeByBoid(boid, false);
		else boidTree.selectNodeByBoid(boid, true);
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
		System.out.println("x " + simScreen.mousePosition.x + " y " + simScreen.mousePosition.y + " labelx " + hoverLabel.getX() + " y " + hoverLabel.getY());
	}


	@Override
	public void unhover(InputEvent event) {
//		System.out.println("exit hover");
		hoverLabel.remove();
//		hoverWindow.remove();
	}
}
