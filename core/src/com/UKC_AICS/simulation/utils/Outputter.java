package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Boid;
import com.UKC_AICS.simulation.managers.BoidManager;
import com.badlogic.gdx.math.Vector3;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Outputter {
	private File file;
	
	public void setFile(File file){
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.file = file;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
	            .newInstance();
	        DocumentBuilder documentBuilder = null;
			try {
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Document document = documentBuilder.newDocument();

	        // Root Element
	        Element rootElement = document.createElement("Record");
	        document.appendChild(rootElement);
	        Element content = document.createElement("Root");
	        rootElement.appendChild(content);
	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = null;
			try {
				transformer = transformerFactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        DOMSource source = new DOMSource(document);

	        StreamResult result = new StreamResult(file);
	        try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void appendOutput(int frame, File file){
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			
				
				System.out.println("Builder " + documentBuilder);
			
	        Document document = documentBuilder.parse(file);
				
//			System.out.println("Doc " + document + " file " + this.file.getPath());
	
	        Element root = document.getDocumentElement();
	
	        // Root Element
	        Element rootElement = document.getDocumentElement();
	        Element time = document.createElement("Frame");
	        time.appendChild(document.createTextNode(String.valueOf(frame)));
	       
	        int i = 0;
	        for(Boid b : BoidManager.boids){
	        	Element boid = document.createElement("Boid");
	        	time.appendChild(boid);
	        	
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
	    		
	    		//TODO size
	    		float sizeVal = b.getSize();
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
	    		
	    		createNode(document, boid, "age", age);
	    		createNode(document, boid, "position", position);
	    		createNode(document, boid, "velocity", velocity);
	    		createNode(document, boid, "species", species);
	    		createNode(document, boid, "group", group);
	    		createNode(document, boid, "nearRadius", nearRadius);
	    		createNode(document, boid, "flockRadius", flockRadius);
	    		createNode(document, boid, "signtRadius", sightRadius);
	    		createNode(document, boid, "size", size);
	    		createNode(document, boid, "stamina", stamina);
	    		createNode(document, boid, "maxStamina", maxStamina);
	    		createNode(document, boid, "hunger", hunger);
	    		createNode(document, boid, "thirst", thirst);
	    		createNode(document, boid, "panicLevel", panicLevel);
	    		createNode(document, boid, "hungerLevel", hungerLevel);
	    		createNode(document, boid, "thirstLevel", thirstLevel);
	    		createNode(document, boid,"cohesion", cohesion);
	    		createNode(document, boid, "alignment", alignment);
	    		createNode(document, boid, "separation", separation);
	    		createNode(document, boid, "wander", wander);
	    		createNode(document, boid, "state", state);
	    		createNode(document, boid, "fertility", fertility);
	        	time.appendChild(boid);
	        }
	        root.appendChild(time);
	        DOMSource source = new DOMSource(document);
	
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = null;
			try {
				transformer = transformerFactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        StreamResult result = new StreamResult("server.xml");
	        try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void createNode(Document document, Element boidElem, String name, String value){
        Element e = document.createElement(name);
        e.appendChild(document.createTextNode(value));
        boidElem.appendChild(e);
	}
}
