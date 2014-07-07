package com.UKC_AICS.simulation.utils;

	import java.io.FileOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.badlogic.gdx.math.Vector3;

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

	    // create config open tag
	    StartElement configStartElement = eventFactory.createStartElement("",
	        "", "config");
	    eventWriter.add(configStartElement);
	    eventWriter.add(end);
	    // Write the different nodes
	    
	    for(Boid b : BoidManager.boids) {
	    	
	    	int ageInt = b.getAge();
	    	String age = "" + ageInt;
	    	
	    	int bDayInt = b.getBirthDay();
	    	String bDay = "" + bDayInt;
	    	
	    	Vector3 positionVec = b.getPosition();
	    	String position = "" + positionVec;
	    	
	    	Vector3 velocityVec = b.getVelocity();
	    	String velocity = "" + velocityVec;
	    	
	    	byte speciesByte = b.getSpecies();
	    	String species = "" + speciesByte;
	    	
	    createNode(eventWriter, "age", age);
	    createNode(eventWriter, "birthday", bDay);
	    createNode(eventWriter, "position", position);
	    createNode(eventWriter, "velocity", velocity);
	    createNode(eventWriter, "species", species);
	    }
      
	    eventWriter.add(eventFactory.createEndElement("", "", "config"));
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

