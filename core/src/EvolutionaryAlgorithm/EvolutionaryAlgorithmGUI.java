package EvolutionaryAlgorithm;

import java.util.Arrays;
import java.util.HashMap;

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

	Window window = new Window("EA Settings", skin);
	
	CheckBox checkBoxCoh = new CheckBox("Hold", skin);
	CheckBox checkBoxAlign = new CheckBox("Hold", skin);   	
	CheckBox checkBoxSep = new CheckBox("Hold", skin);   	
	CheckBox checkBoxWan = new CheckBox("Hold", skin); 	
	CheckBox checkBoxHoldAll = new CheckBox("Hold All", skin);
	
	Label cohLabel = new Label("Cohesion", skin);
	Label alignLabel = new Label("Alignment", skin);
	Label sepLabel = new Label("Separation", skin);
	Label wanLabel = new Label("Wander", skin);
	
	Label cohVal = new Label("0", skin);
	Label alignVal = new Label("0", skin);
	Label sepVal = new Label("0", skin);
	Label wanVal= new Label("0", skin);
	
	TextField textfieldCoh = new TextField("", skin);		
	TextField textfieldAlign = new TextField("", skin);	
	TextField textfieldSep = new TextField("", skin);
	TextField textfieldWan = new TextField("", skin);
	
	String coh = null;
	String align =null;
	String sep =null;
	String wan =null;	
	
	public EvolutionaryAlgorithmGUI(SimulationScreen ss, EA2 ea){
			simScreen = ss; //In case needed later.
			//setup(ea);
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
			
		
		private Table createSettingsTable(Table t2, final EA2 ea) {
			final String[] options = {"Species 1", "Species 2", "Species 3", "Species 4"};
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
		
			checkBoxHoldAll.addListener(new ChangeListener(){

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(checkBoxHoldAll.isChecked()){
						checkBoxCoh.setChecked(true);
						checkBoxAlign.setChecked(true);
						checkBoxSep.setChecked(true);
						checkBoxWan.setChecked(true);
					}
					else{
						checkBoxCoh.setChecked(false);
						checkBoxAlign.setChecked(false);
						checkBoxSep.setChecked(false);
						checkBoxWan.setChecked(false);
					}
					
				}
				
			});
			// sets the values that will be added to the table
			setTableInfo();
			
			t2.add(dropdown).top().expandX().fillX();
			t2.add(checkBoxHoldAll).top().expandX().fillX();
			t2.row();
			t2.add(cohLabel).height(20).expandX().fillX();
			t2.add(cohVal).height(20).expandX().fillX();
			t2.add(textfieldCoh).expandX().fillX();
			t2.add(checkBoxCoh).top().height(20).expandX().fillX();
			t2.row();
			t2.add(alignLabel).height(20).expandX().fillX();
			t2.add(alignVal).height(20).expandX().fillX();
			t2.add(textfieldAlign).expandX().fillX();
			t2.add(checkBoxAlign).top().height(20).expandX().fillX();
			t2.row();
			t2.add(sepLabel).height(20).expandX().fillX();
			t2.add(sepVal).height(20).expandX().fillX();
			t2.add(textfieldSep).expandX().fillX();
			t2.add(checkBoxSep).top().height(20).expandX().fillX();
			t2.row();
			t2.add(wanLabel).height(20).expandX().fillX();
			t2.add(wanVal).height(20).expandX().fillX();
			t2.add(textfieldWan).expandX().fillX();
			t2.add(checkBoxWan).top().height(20).expandX().fillX();
			
			// EA settings button.
			final TextButton closeButton = new TextButton("Close", skin,"default");
			closeButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					//EA Settings menu disappears
					simScreen.flipEARender();               
          
				}
			});
			final TextButton applyButton = new TextButton("Apply", skin,"default");
			applyButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					//Commits changes to held values hashMap
					applyChanges(ea);
				}
			});
			
			t2.row();
			t2.add(closeButton).top().height(20).expandX().fillX();
			t2.add(applyButton).top().height(20).expandX().fillX();
			return t2;
		}
		
		
		public void applyChanges(EA2 ea) {
			Float[] newheldvalues = new Float[EA2.getGeneLength()];
			Float temp1 = null;
			Float temp2 = null;
			Float temp3 = null;
			Float temp4 = null;
			
			 if(checkBoxCoh.isChecked()){
				 
				 if(!textfieldCoh.getText().isEmpty()){
					 System.out.println("I am not empty");
					 temp1 =  Float.parseFloat(textfieldCoh.getText());
					 System.out.println(temp1); 	 
				 } 
			 }		        
				                
			 if(checkBoxAlign.isChecked()){	        
				 
				 if(!textfieldAlign.getText().isEmpty()){
					 System.out.println("I am not empty");	 
					 temp2 =  Float.parseFloat(textfieldAlign.getText());
					 System.out.println(temp2);
				 }	
				 
			 }
					        
			 if(checkBoxSep.isChecked()){	        
				 
				 if(!textfieldSep.getText().isEmpty()){
					 System.out.println("I am not empty");
					 temp3 = Float.parseFloat(textfieldSep.getText());
					 System.out.println(temp3);
				 }
				
			 }	        
					  
			 if(checkBoxCoh.isChecked()){	
				 
				 if(!textfieldWan.getText().isEmpty()){
					 System.out.println("I am not empty");
					 temp4 = Float.parseFloat(textfieldWan.getText());
					 System.out.println(temp4);
				 }
			 }
			 newheldvalues[0]=temp1;
			 newheldvalues[1]=temp2;
			 newheldvalues[2]=temp3;
			 newheldvalues[3]=temp4;
			 System.arraycopy(newheldvalues, 0, currentVals ,0 , geneLength);
			 
			 	System.out.println("Before");
			 	for(byte b = 0 ; b<totalSpecies; b++){
			 		System.out.println(Arrays.toString(ea.heldValues.get(b))); 
			 	}
			 	
			 	
			 	ea.heldValues.put(b ,newheldvalues);
			 	
			 	 System.out.println("After");
			 	for(byte b = 0 ; b<totalSpecies; b++){
			 		System.out.println(Arrays.toString(ea.heldValues.get(b))); 
			 	}
			 	
				setTableInfo();	       		        
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
			setTableInfo();
			j=0;
		}
		
		public void setTableInfo() {
			System.out.println("Currentvals"+Arrays.toString(currentVals));
			System.out.println("Before changes");	
			System.out.println(coh);
			System.out.println(align);
			System.out.println(sep);
			System.out.println(wan);
			
			if(currentVals[0]!=null){
				coh = Float.toString(currentVals[0]);
			}
			else{
				coh = "null";
			}
			
			if(currentVals[1]!=null){
				align = Float.toString(currentVals[1]);
			}
			else{
				align = "null";
			}
			
			if(currentVals[2]!=null){
				sep = Float.toString(currentVals[2]);
			}
			else{
				sep = "null";
			}
			
			if(currentVals[3]!=null){
				wan = Float.toString(currentVals[3]);
			}
			else{
				wan = "null";
			}
			 
		
			
			System.out.println("After get");
			System.out.println("Currentvals"+Arrays.toString(currentVals));
			System.out.println(coh);
			System.out.println(align);
			System.out.println(sep);
			System.out.println(wan);
			
			cohVal.setText(coh);
			alignVal.setText(align);
			sepVal.setText(sep);
			wanVal.setText(wan);  
			
			System.out.println("After set");
			System.out.println("Currentvals"+Arrays.toString(currentVals));
			System.out.println(coh);
			System.out.println(align);
			System.out.println(sep);
			System.out.println(wan);
		}
		
		
		public void update(Boolean eaRender) {
			if(eaRender){
			stage.act();
	        stage.draw();	
			}
		}
		
}

