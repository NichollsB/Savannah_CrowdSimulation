package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.badlogic.gdx.math.Vector3;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileOutputStream;

/**
 * 
 * @author Matt
 *
 */
	public class StaxWriter {
		 
	  private String configFile;

	  public void setFile(String configFile) {
	    this.configFile = configFile;
	  }

	  public void saveConfig() throws Exception {
		  System.out.println("Executing");
	
	    // create an XMLOutputFactory
	    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	  
	    // create XMLEventWriter
	    XMLEventWriter eventWriter = outputFactory
	        .createXMLEventWriter(new FileOutputStream(configFile));
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
	 
	    	for(Boid b : BoidManager.boids) {
	 
	    		int ageInt = b.getAge();
	    		String age = "" + ageInt;
	    	
//	    		int bDayInt = b.getBirthDay();
//	    		String bDay = "" + bDayInt;
	    	
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
	    		
	    		StartElement configElement = eventFactory.createStartElement("",
	    				"", "boid");
	    		eventWriter.add(configElement);
	    		eventWriter.add(end);
	    		   
	    		createNode(eventWriter, "age", age);
//	    		createNode(eventWriter, "birthday", bDay);
	    		createNode(eventWriter, "position", position);
	    		createNode(eventWriter, "velocity", velocity);
	    		createNode(eventWriter, "species", species);
	    		createNode(eventWriter, "cohesion", cohesion);
	    		createNode(eventWriter, "alignment", alignment);
	    		createNode(eventWriter, "separation", separation);
	    		createNode(eventWriter, "wander", wander);
	    	
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
	  
	

