package EvolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.Object;
import EvolutionaryAlgorithm.EA2;

import com.UKC_AICS.simulation.screen.SimulationScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class EvolutionaryAlgorithmGUI extends Stage {
	private SimulationScreen simScreen;
	public Stage stage;
	Table table;
	private int geneLength = EA2.getGeneLength();
	private int totalSpecies = EA2.getTotalSpecies();
	private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	private Float[] currentVals= new Float[geneLength];
	private HashMap<Byte,Float[]> tempHeldValues = new HashMap<Byte,Float[]>();
	private Byte b = 0;
	private ArrayList<Label> labels = new ArrayList<Label>();
	private ArrayList<Label> labelVals = new ArrayList<Label>();
	private ArrayList<TextField> textField = new ArrayList<TextField>();
	private ArrayList<String> values = new ArrayList<String>();
	private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
	Window window = new Window("EA Settings", skin);
	
	Label onOffLabel = new Label("Switch EA on or off: ", skin);
	
	Label crossLabel = new Label("Crossover Rate", skin);
	Label muteLabel = new Label("Mutation Rate", skin);
	
	Label crossValLabel = new Label("0", skin);
	Label muteValLabel = new Label("0", skin);
	
	TextField crossTextfield = new TextField("", skin);	
	TextField muteTextfield = new TextField("", skin);
			
	String crossValue = null;
	String muteValue = null;
	
	CheckBox checkBoxHoldAll = new CheckBox("Hold All", skin);
	CheckBox checkBoxOnOff = new CheckBox("EA ON", skin);

	
	public EvolutionaryAlgorithmGUI(SimulationScreen ss, EA2 ea){
			simScreen = ss; //In case needed later.
			setup();
			setStage(ea);
		}
		
		//Creates the window and its contents
		public void setStage(EA2 ea) {
			
			for(Byte i = 0 ; i< 4; i++){
				System.out.println("EA stage" + Arrays.toString(ea.heldValues.get(i)));
			}
			
			stage = this;
			Table t2 = new Table();
			t2.setSize(100, 200);
			
			stage.addActor(t2);
			
			//creates the settings table to add to the window
			createSettingsTable(t2,ea);
			
			window.setPosition(200, 200);
			window.defaults().spaceBottom(10);
			window.row().fill().expandX();
			window.add(t2).expandX().fillX();
			window.setResizable(true);
			window.pack();
			stage.addActor(window);		
		}
			
		private void setup(){
			
			
			
			Label cohLabel = new Label("Cohesion", skin);
			Label alignLabel = new Label("Alignment", skin);
			Label sepLabel = new Label("Separation", skin);
			Label wanLabel = new Label("Wander", skin);
			Label FRLabel = new Label("Flock Radius", skin);
			Label NRLabel = new Label("Near Radius", skin);
			Label SRLabel = new Label("Sight Radius", skin);
			Label maxStaminaLabel = new Label("Max Stamina", skin);
			
			labels.add(cohLabel);
			labels.add(alignLabel);
			labels.add(sepLabel);
			labels.add(wanLabel);
			labels.add(FRLabel);
			labels.add(NRLabel);
			labels.add(SRLabel);
			labels.add(maxStaminaLabel);
			
			for(int i = 0 ; i<labels.size() ;i++){
				Label valuelabel = new Label("0", skin);
				TextField textfield = new TextField("", skin);		
				CheckBox checkBox = new CheckBox("Hold", skin);
				String value = null;
			
				labelVals.add(i,valuelabel);
				textField.add(i,textfield);
				checkBoxes.add(i,checkBox);
				values.add(i,value);
			}
		}
		private Table createSettingsTable(Table t2, final EA2 ea) {
			final String[] options = {"Species 1", "Species 2", "Species 3", "Species 4,", "Species 5" };
	    	final SelectBox<String> dropdown = new SelectBox<String>(skin);
			dropdown.setItems(options);
			
			//Add dropdown and buttons
			final String selection= dropdown.getSelected();
			setSpeciesInfo(dropdown,selection, options, ea);
			System.out.println("Selection " + selection);
			
			dropdown.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					//Get table values
					String newSelection= dropdown.getSelected();
					
					System.out.println("Selection new " + newSelection);
					checkBoxHoldAll.setChecked(false);
					setSpeciesInfo(dropdown,newSelection,options, ea);
					
				}
			});
			checkBoxOnOff.addListener(new ChangeListener(){
				public void changed(ChangeEvent event, Actor actor) {
					if(checkBoxOnOff.isChecked()){
						ea.setEaOn(true);
					
					}
					else{
						ea.setEaOn(false);
					}
				}
				
			});
			
		
			checkBoxHoldAll.addListener(new ChangeListener(){

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(checkBoxHoldAll.isChecked()){
						for(CheckBox cb : checkBoxes){
							cb.setChecked(true);
						}
					
					}
					else{
						for(CheckBox cb : checkBoxes){
							cb.setChecked(false);
						}
						
					}
					
				}
				
			});
			// EA close settings menu.
			final TextButton closeButton = new TextButton("Close", skin,"default");
			closeButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					//EA Settings menu disappears
//					window.setVisible(false);
					simScreen.flipEARender();               
					
				}
			});
			// Apply changes
			final TextButton applyButton = new TextButton("Apply", skin,"default");
			applyButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					//Commits changes to held values hashMap
					applyChanges(ea);
				}
			});
			final TextButton setCrossButton = new TextButton("Set Crossover Rate", skin,"default");
			setCrossButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!crossTextfield.getText().isEmpty()){
						double newCross = Double.parseDouble(crossTextfield.getText());
						
							ea.setCrossRate(newCross);
							setTableInfo(ea);
						
					}
				}
			});
			final TextButton setMuteButton = new TextButton("Set Mutation Rate", skin,"default");
			setMuteButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!muteTextfield.getText().isEmpty()){
						double newMute = Double.parseDouble(muteTextfield.getText());
						ea.setMuteRate(newMute);
						setTableInfo(ea);
					}
				}
			});
			// sets the values that will be added to the table
			setTableInfo(ea);
			
			
			
			t2.add(onOffLabel).height(20).expandX().fillX();
			t2.add(checkBoxOnOff).height(20).expandX().fillX();
			t2.row();
			t2.add(crossLabel).height(20).expandX().fillX();
			t2.add(crossValLabel).height(20).expandX().fillX();
			t2.add(crossTextfield).height(20).expandX().fillX();
			t2.add(setCrossButton).height(20).expandX().fillX();
			t2.row();
			t2.add(muteLabel).height(20).expandX().fillX();
			t2.add(muteValLabel).height(20).expandX().fillX();
			t2.add(muteTextfield).height(20).expandX().fillX();
			t2.add(setMuteButton).height(20).expandX().fillX();
			t2.row();
			t2.add(dropdown).top().expandX().fillX();
			t2.add(checkBoxHoldAll).top().expandX().fillX();
			t2.row();
			
			for(int i = 0 ; i< labels.size() ; i++){
				t2.add(labels.get(i)).height(20).expandX().fillX();
				t2.add(labelVals.get(i)).height(20).expandX().fillX();
				t2.add(textField.get(i)).height(20).expandX().fillX();
				t2.add(checkBoxes.get(i)).height(20).expandX().fillX();
				t2.row();
			}
			
		
			
			
			
			
			t2.add(closeButton).top().height(20).expandX().fillX();
			t2.add(applyButton).top().height(20).expandX().fillX();
			return t2;
		}
		
		
		public void applyChanges(EA2 ea) {
			Float[] newheldvalues = new Float[EA2.getGeneLength()];
			
			for(int i = 0 ; i < labels.size(); i++ ){
			 if(checkBoxes.get(i).isChecked()){
				 
				 if(!textField.get(i).getText().isEmpty()){
					 System.out.println("I am not empty");
					newheldvalues[i] =  Float.parseFloat(textField.get(i).getText());
					  	 
				 } 
				 else{
					 newheldvalues[i] = null;
				 }
			 }
			 
			}
			 System.arraycopy(newheldvalues, 0, currentVals ,0 , geneLength);
			 
			 //ps
			 	System.out.println("Before");
			 	for(byte b = 0 ; b<totalSpecies; b++){
			 		System.out.println(Arrays.toString(ea.heldValues.get(b))); 
			 	}
			 	
			 	
			 	ea.heldValues.put(b ,newheldvalues);
			 	
			 	//ps
			 	 System.out.println("After");
			 	for(byte b = 0 ; b<totalSpecies; b++){
			 		System.out.println(Arrays.toString(ea.heldValues.get(b))); 
			 	}
			 	
				setTableInfo(ea);	       		        
		}
	
		
		public void setSpeciesInfo(SelectBox<String> dropdown,String newSelection,String[] options, EA2 ea) {
			System.out.println("SET SPECIES INFO CALLED");
			int j =0;
			
			for(byte i=0;i<totalSpecies;i++){
				if(newSelection.equals(options[j])) {
				System.out.println("option "+options[j]);
				b = i;
				System.out.println("Before");
				System.out.println("TempHeldValues " + Arrays.toString(ea.heldValues.get(b)));
				System.out.println("Current " + Arrays.toString(currentVals));
				
				System.arraycopy(ea.heldValues.get(b), 0, currentVals ,0 , geneLength);
				
				System.out.println("After");
				System.out.println("TempHeldValues " + Arrays.toString(tempHeldValues.get(b)));
				System.out.println("Current " + Arrays.toString(currentVals));
				}
			j++;
			}
			setTableInfo(ea);
			j=0;
		}
		
		public void setTableInfo(EA2 ea) {
			System.out.println("Currentvals"+Arrays.toString(currentVals));
			System.out.println("Before changes");	
			String val = null;
			
			for(int i = 0 ; i < labels.size();i++){
				if(currentVals[i]!=null){
					
					 val = Float.toString(currentVals[i]);
				}
				else{
					 val = "null";
				}
				
				
				labelVals.get(i).setText(val);
			}
			
			
			crossValue=""+ ea.getCrossRate();
			muteValue=""+ ea.getMuteRate();
			
			crossValLabel.setText(crossValue);
			muteValLabel.setText(muteValue);
		
		
		}
		
		
		public void update(Boolean eaRender) {
			if(eaRender){
				stage.act();
				stage.draw();	
			}
		}
		
		//Added by ben nicholls
		public void toggleWindowVisible(){
			window.setVisible(!window.isVisible());
		}
		
}

