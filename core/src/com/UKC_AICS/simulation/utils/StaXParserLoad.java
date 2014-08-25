package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.managers.BoidManager;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Matt on 08/07/2014.
 */

public class StaXParserLoad {
//String variable used to identify each piece of stored info
	private String BOID = "boid";
	private String AGE = "age";
    private String BIRTHDAY = "birthday";
    private String POSITION = "position";
    private String VELOCITY  = "velocity";
    private String SPECIES = "species";
    private String GROUP = "group";
    private String SIGHTRADIUS = "sightRadius";
    private String NEARRADIUS = "nearRadius";
    private String FLOCKRADIUS = "flockRadius";
    private String STAMINA = "stamina";
    private String MAXSTAMINA = "maxStamina";
    private String HUNGER = "hunger";
    private String THIRST = "thirst";
    private String PANIC = "panic";
    private String HUNGERLEVEL = "hungerLevel";
    private String THIRSTLEVEL = "thirstLevel";
    private String PANICLEVEL = "paniclevel";
    private String COHESION = "cohesion";
    private String ALIGNMENT = "alignment";
    private String SEPARATION = "separation";
    private String WANDER = "wander";
    private String SIZE = "size";
    private String STATE = "state";
    private String FERTILITY = "fertility";
    private String STATES = "states";
    private String OFFSPRING = "offspring";
  // Variables used to store the info for the creation of the boid  
    private int age = 0;
    private int bDay = 0;
    private float cohesion, separation,  alignment, wander ,hunger ,thirst, panic, sightRadius, nearRadius, flockRadius, stamina, maxStamina, hungerLevel, thirstLevel, panicLevel, size, fertility, offspring;
    private byte spec = 0; 
    private byte group = 0;
    private String currentState = null;
    private String states ;
    public Float[] fltArray = new Float[3] ;
    public Float[] fltArray2 = new Float[3];
    private InputStream in;
    @SuppressWarnings({ })
    /**
     * Parses over the file and assigns each piece of information to its relevant variable
     * @param configFile
     */
    public void readConfig(File configFile){
    	try{
	    	in = new FileInputStream(configFile);
	    	readConfig();
    	}
    	catch(FileNotFoundException e){
    		
    	}
    }
    
    public void readConfig(String configFile) throws FileNotFoundException {
    	try{
	    	in = new FileInputStream(configFile);
	    	readConfig();
    	}
    	catch(FileNotFoundException e){
    		
    	}
    }
    public void readConfig() {
    
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(AGE)) {
                            event = eventReader.nextEvent();
                             age = Integer.parseInt(event.asCharacters().getData());
                           
                            continue;
                        }
                    
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(BIRTHDAY)) {
                            event = eventReader.nextEvent();
                             bDay = Integer.parseInt(event.asCharacters().getData());
                          
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(POSITION)) {
                            event = eventReader.nextEvent();
                            String str = event.asCharacters().getData();
                            String[] strArray = (str.split(","));
                          
                            for(int i = 0 ; i<3; i++){
                            	fltArray[i]=Float.parseFloat(strArray[i]);                                                     
                            }
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(VELOCITY)) {
                            event = eventReader.nextEvent();
                            String str = event.asCharacters().getData();
                            String[] strArray2 = (str.split(","));

                            for(int i = 0 ; i<3; i++){
                            	fltArray2[i]=Float.parseFloat(strArray2[i]);
                            }
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SPECIES)) {
                            event = eventReader.nextEvent();
                            spec = Byte.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(GROUP)) {
                            event = eventReader.nextEvent();
                            group = Byte.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NEARRADIUS)) {
                            event = eventReader.nextEvent();
                            nearRadius = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(FLOCKRADIUS)) {
                            event = eventReader.nextEvent();
                            flockRadius = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SIGHTRADIUS)) {
                            event = eventReader.nextEvent();
                            sightRadius = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SIZE)) {
                            event = eventReader.nextEvent();
                            size = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(STAMINA)) {
                            event = eventReader.nextEvent();
                            stamina = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(MAXSTAMINA)) {
                            event = eventReader.nextEvent();
                            maxStamina = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(HUNGER)) {
                            event = eventReader.nextEvent();
                            hunger = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(THIRST)) {
                            event = eventReader.nextEvent();
                            thirst = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(PANIC)) {
                            event = eventReader.nextEvent();
                            panic = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(HUNGERLEVEL)) {
                            event = eventReader.nextEvent();
                            hungerLevel = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(THIRSTLEVEL)) {
                            event = eventReader.nextEvent();
                            thirstLevel = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(PANICLEVEL)) {
                            event = eventReader.nextEvent();
                            panicLevel = Float.valueOf(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(COHESION)) {
                            event = eventReader.nextEvent();
                            cohesion = Float.parseFloat(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(ALIGNMENT)) {
                            event = eventReader.nextEvent();
                            alignment = Float.parseFloat(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SEPARATION)) {
                            event = eventReader.nextEvent();
                            separation = Float.parseFloat(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(WANDER)) {
                            event = eventReader.nextEvent();
                            wander = Float.parseFloat(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(STATE)) {
                            event = eventReader.nextEvent();
                            currentState = event.asCharacters().getData();
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(FERTILITY)) {
                            event = eventReader.nextEvent();
                            fertility = Float.parseFloat(event.asCharacters().getData());
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(STATES)) {
                            event = eventReader.nextEvent();
                            states = event.asCharacters().getData();
                            continue;
                        }
                    }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(OFFSPRING)) {
                            event = eventReader.nextEvent();
                            offspring= Float.parseFloat(event.asCharacters().getData());
                            continue;
                        }
                    }
                }
                // If we reach the end of the boid. All of its info should have been collected and can be used with the appropriate method in BoidManager
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart() == (BOID)) {
                        BoidManager.createBoid(spec, group, age, bDay, fltArray[0], fltArray[1], fltArray[2], fltArray2[0], fltArray2[1], fltArray2[2], cohesion, separation, alignment, wander,
                        		sightRadius, nearRadius, flockRadius, size, hunger, thirst, panic, stamina, maxStamina, hungerLevel, thirstLevel, panicLevel, currentState, fertility, states,offspring);
                    }
                }

            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
       
    }
}
