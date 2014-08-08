package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.managers.BoidManager;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Matt on 08/07/2014.
 */
public class StaXParserLoad {

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
    private String HUNGER = "hunger";
    private String THIRST = "thirst";
    private String PANIC = "panic";
    private String COHESION = "cohesion";
    private String ALIGNMENT = "alignment";
    private String SEPARATION = "separation";
    private String WANDER = "wander";
    private int age = 0;
    private int bDay = 0;
    private float cohesion, separation,  alignment, wander ,hunger ,thirst, panic, sightRadius, nearRadius, flockRadius;
    private byte spec = 0; 
    private byte group = 0;
    public Float[] fltArray = new Float[3] ;
    public Float[] fltArray2 = new Float[3];
    @SuppressWarnings({ })
    public void readConfig(String configFile) {
    
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(configFile);
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
                                .equals(SIGHTRADIUS)) {
                            event = eventReader.nextEvent();
                            sightRadius = Float.valueOf(event.asCharacters().getData());
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

                    
                }
                // If we reach the end of an item element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart() == (BOID)) {
                        BoidManager.createBoid(spec, group, age, bDay, fltArray[0], fltArray[1], fltArray[2], fltArray2[0], fltArray2[1], fltArray2[2], cohesion, separation, alignment, wander,
                        		sightRadius, nearRadius, flockRadius, hunger, thirst, panic );
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
       
    }
}
