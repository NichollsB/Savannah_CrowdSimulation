package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.managers.StateMachine;
import com.badlogic.gdx.math.Vector3;
import com.UKC_AICS.simulation.entity.states.State;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;
/**
 * 
 * @author M J Odinga
 *
 */
	public class StaxWriter {
		StateMachine stateMachine; 
	  private String configFile;
	  
	  private File file;
	  /**
	   * This creates the file which the class will write the boid information to
	   * @param configFile
	   */
	  public void setFile(File configFile){
		  try {
			  if(file == null || !file.exists()){
				  file = configFile;
				  file.mkdir();
				  file.createNewFile();
			  }
		  } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  
	  public void setFile(String configFile) {
	    this.configFile = configFile;
	  }
	  /**
	   * Stores each piece of information in sequence in the XML file
	   * @param stateMachine
	   * @throws Exception
	   */
	  public void saveConfig(StateMachine stateMachine) throws Exception {
		 this.stateMachine=stateMachine;
	    // create an XMLOutputFactory
	    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	  
	    // create XMLEventWriter
	    XMLEventWriter eventWriter = outputFactory
	        .createXMLEventWriter(new FileOutputStream(file));
	    // create an EventFactory
	    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	    XMLEvent end = eventFactory.createDTD("\n");
	    // create and write Start Tag
	    StartDocument startDocument = eventFactory.createStartDocument();
	    eventWriter.add(startDocument);
	    
	    StartElement configStartElement = eventFactory.createStartElement("",
	        "", "boidlist");
	    eventWriter.add(configStartElement);
	    eventWriter.add(end);
	    //All information stored in the boid must be converted to string before it can be written to the file
	    	for(Boid b : BoidManager.boids) {
	 
	    	//	System.out.println("boid stack " +stateMachine.getStack(b) );
	    		
				Stack<State> stateStore = stateMachine.getStack(b);
				
				//System.out.println("STATESTORE" + stateStore);
						
				
				String states = stateStore.toString();
			
				//System.out.println("boid stack " +stateMachine.boidStates.get(b) );
	    	
				
	    		int ageInt = b.getAge();
	    		String age = "" + ageInt;

	    		Vector3 positionVec = b.getPosition();
	    		String position = "" + positionVec;
	    	
	    		Vector3 velocityVec = b.getVelocity();
	    		String velocity = "" + velocityVec;
	    	
	    		byte speciesByte = b.getSpecies();
	    		String species = "" + speciesByte;
	    		
	    		float cohesionVal = b.getCohesion();
	    		String cohesion = "" + cohesionVal;
	    		
	    		float alignmentVal = b.getAlignment();
	    		String alignment = "" + alignmentVal;
	    		
	    		float separationVal = b.getSeparation();
	    		String separation = "" + separationVal;
	    		
	    		float wanderVal = b.getWander();
	    		String wander = "" + wanderVal;
	    		
	    		byte groupVal = b.getGroup();
	    		String group = "" + groupVal;
	    		
	    		float nearRadiusVal = b.getNearRadius();
	    		String nearRadius = "" + nearRadiusVal;
	    		
	    		float flockRadiusVal = b.getFlockRadius();
	    		String flockRadius = "" + flockRadiusVal;
	    		
	    		float sightRadiusVal = b.getSightRadius();
	    		String sightRadius = "" + sightRadiusVal;
	    		
	    		float sizeVal = b.getSize();
	    		//System.out.println("SIZE SAVE " + sizeVal);
	    		String size = "" + sizeVal;
	    		
	    		float staminaVal = b.getStamina();
	    		String stamina = "" + staminaVal;
	    		
	    		float maxStaminaVal = b.getMaxStamina();
	    		String maxStamina = "" + maxStaminaVal;
	    		
	    		float hungerVal = b.getHunger();
	    		String hunger = "" + hungerVal;
	    		
	    		float thirstVal = b.getThirst();
	    		String thirst = "" + thirstVal;
	    		
	    		float panicVal = b.getPanic();
	    		String panic = "" + panicVal;
	    				
	    		float hungerLevelVal = b.getHungerLevel();
	    		String hungerLevel = "" + hungerLevelVal;
	    		
	    		float thirstLevelVal = b.getThirstLevel();
	    		String thirstLevel = "" + thirstLevelVal;
	    		
	    		float panicLevelVal = b.getPanicLevel();
	    		String panicLevel = "" + panicLevelVal;		
	    				
	    		String state = b.getState();
	    		
	    		float fertilityVal = b.getFertility();
	    		String fertility = "" + fertilityVal;
	    		
	    		float offspringVal = b.getNumberOfOffspring();
	    		String offspring = "" + offspringVal;
	    	
	    		StartElement configElement = eventFactory.createStartElement("",
	    				"", "boid");
	    		eventWriter.add(configElement);
	    		eventWriter.add(end);
	    		   
	    		createNode(eventWriter, "age", age);
	    		createNode(eventWriter, "position", position);
	    		createNode(eventWriter, "velocity", velocity);
	    		createNode(eventWriter, "species", species);
	    		createNode(eventWriter, "group", group);
	    		createNode(eventWriter, "nearRadius", nearRadius);
	    		createNode(eventWriter, "flockRadius", flockRadius);
	    		createNode(eventWriter, "signtRadius", sightRadius);
	    		createNode(eventWriter, "size", size);
	    		createNode(eventWriter, "stamina", stamina);
	    		createNode(eventWriter, "maxStamina", maxStamina);
	    		createNode(eventWriter, "panic", panic);
	    		createNode(eventWriter,	"hunger", hunger);
	    		createNode(eventWriter,	"thirst", thirst);
	    		createNode(eventWriter, "panicLevel", panicLevel);
	    		createNode(eventWriter,	"hungerLevel", hungerLevel);
	    		createNode(eventWriter,	"thirstLevel", thirstLevel);
	    		createNode(eventWriter, "cohesion", cohesion);
	    		createNode(eventWriter, "alignment", alignment);
	    		createNode(eventWriter, "separation", separation);
	    		createNode(eventWriter, "wander", wander);
	    		createNode(eventWriter, "state", state);
	    		createNode(eventWriter, "fertility", fertility);
	    		createNode(eventWriter, "states", states);
	    		createNode(eventWriter, "offspring", offspring);
	    		eventWriter.add(eventFactory.createEndElement("", "", "boid"));
	    		eventWriter.add(end);
	  
	    	} 
	   eventWriter.add(eventFactory.createEndElement("", "", "boidlist"));
	   eventWriter.add(end);
	   eventWriter.add(eventFactory.createEndDocument());
	   eventWriter.close();
	  }

	  private void createNode(XMLEventWriter eventWriter, String name,
	      String value) throws XMLStreamException {

		  XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		  XMLEvent end = eventFactory.createDTD("\n");
		  XMLEvent tab = eventFactory.createDTD("\t");
		  // create Start node
		  StartElement sElement = eventFactory.createStartElement("", "", name);
		  eventWriter.add(tab);
		  eventWriter.add(sElement);
		  // create Content
		  Characters characters = eventFactory.createCharacters(value);
		  eventWriter.add(characters);
		  // create End node
		  EndElement eElement = eventFactory.createEndElement("", "", name);
		  eventWriter.add(eElement);
		  eventWriter.add(end);
	  }


}
	  
	

