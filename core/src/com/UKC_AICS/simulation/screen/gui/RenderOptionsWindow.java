package com.UKC_AICS.simulation.screen.gui;

import java.io.File;

import com.UKC_AICS.simulation.gui.controlutils.MenuSelectEvent;
import com.UKC_AICS.simulation.gui.controlutils.MenuSelectListener;
import com.UKC_AICS.simulation.gui.controlutils.RenderState;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class RenderOptionsWindow extends Dialog implements MenuSelectEvent {

	private File grassFile;
	private File waterFile;
	private File texturePackFile;
	private SelectBox backgroundOptions;
	
	private TextField grassField;
	private TextField waterField;
	
	private TextField texturePackField;
	private Skin skin;
	private FileChooser fileChooser;
	private Stage stage;
	
	private final Array<MenuSelectListener> listeners = new Array<MenuSelectListener>();
	
	public RenderOptionsWindow(String title, Skin skin, 
			File water, File grass, File textureSheet, Stage stage ) {
		super(title, skin);
		this.skin=skin;
		this.stage = stage;
//		stage.addActor(this);
//		this.hide();
		backgroundOptions = new SelectBox(skin);
		grassField = new TextField("Grass texture location", skin);
		waterField = new TextField("Water texture location", skin);
		texturePackField = new TextField("Texture sheet location", skin);
		this.setWidth(400);
		this.setHeight(400);
		this.setPosition((stage.getWidth()/2)-(this.getWidth()/2), (stage.getHeight()/2)-(this.getHeight()/2));
		
		if(water != null){
			if(!water.getName().isEmpty()){
				waterFile = water;
				grassField.setText(waterFile.getPath());
				waterField.setDisabled(true);
			}
		}
		if(grass != null){
			if(!grass.getName().isEmpty()){
				grassFile = grass;
				grassField.setText(grassFile.getPath());
				grassField.setDisabled(true);
			}
		}
		if(textureSheet != null){
			if(!textureSheet.getName().isEmpty()){
				texturePackFile = textureSheet;
				texturePackField.setText(texturePackFile.getPath());
				texturePackField.setDisabled(true);
			}
		}
		System.out.println("Num options " + RenderState.getTileStates());
		backgroundOptions.setItems(RenderState.getTileStates());
		backgroundOptions.setSelected(RenderState.TILESTATE.stateName);
		
		   fileChooser = new FileChooser("Load", skin, "Load", "Species", "../", true, "Confirm", "Cancel", stage);
	        fileChooser.addSelectionListener(new MenuSelectListener(){
				public void selectionMade(java.lang.Object menu, java.lang.Object object) {
					FileChooser chooser = (FileChooser) menu;
					if(chooser.getIdentifier().equals("sheet")){
						texturePackFile = (File)object;
						texturePackField.setDisabled(false);
						texturePackField.setText(texturePackFile.getPath());
						texturePackField.setDisabled(true);
					}
//					simScreen.simulationManager.loadSaveCall(chooser.getCommand(), chooser.getIdentifier(), (File)object);
				}
			});
	        create();
	}
	
	public void create(){
		Table content = this.getContentTable();
		content.add(new Label("Render type and texture import for the background, water, and grass graphics", skin)).expandX().fillX().pad(10);
		content.row();
		Table table = new Table();
		table.add(new Label("Render Type", skin)).left().pad(10);
		table.add(backgroundOptions).fillX().expandX();
		content.add(table).fillX().expandX();
		content.row();
		table = new Table();
		table.add(new Label("Texture sheet location", skin));
		table.add(texturePackField).fillX().expandX();
		TextButton btn = new TextButton("Load", skin);
		btn.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				fileChooser.setOptionsText("Load", "Cancel");
				fileChooser.setCommand("load");
				fileChooser.setIdentifier("sheet");
				fileChooser.open(stage);
			}
		});
		table.add(btn);
		content.add(table).fillX().expandX();
		content.row();
		
		button("Confirm", "confirm");
		button("Cancel", "cancel");
		this.pack();
		
	}
	
	public String getRenderType(){
		return (String)backgroundOptions.getSelected();
	}
	public File getSheetFile(){
		return texturePackFile;
	}
	public File getGrassFile(){
		return grassFile;
	}
	public File getWaterFile(){
		return waterFile;
	}
	
	/**
	 * Should be called upon dialog button selection
	 */
	protected void result (Object object){
		String btn;
		btn = (String)object;
		if(btn.equalsIgnoreCase("confirm")){
			for(MenuSelectListener l: listeners ){
				l.selectionMade(this, object);
			}
		}
		
	}

	@Override
	public void addSelectionListener(MenuSelectListener listener) {
		listeners.add(listener);
	}
	
	public void open(Stage stage){
//		stage.addActor(this);
		this.show(stage);
		this.setHeight(400);
		this.setWidth(400);
		this.setPosition((stage.getWidth()/2)-(this.getWidth()/2), (stage.getHeight()/2)-(this.getHeight()/2));
	}
	

}
