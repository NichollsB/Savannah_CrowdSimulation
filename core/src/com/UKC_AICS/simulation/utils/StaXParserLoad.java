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

	static final String BOID = "boid";
    static final String AGE = "age";
    static final String BIRTHDAY = "birthday";
    static final String POSITION = "position";
    static final String VELOCITY  = "velocity";
    static final String SPECIES = "species";
    static final String COHESION = "cohesion";
    static final String ALIGNMENT = "alignment";
    static final String SEPARATION = "separation";
    static final String WANDER = "wander";
    private int age = 0;
    private int bDay = 0;
    private float cohesion, separation,  alignment, wander;
    private byte spec = 0; 
    public Float[] fltArray = new Float[3] ;
    public Float[] fltArray2 = new Float[3];
    @SuppressWarnings({ "unchecked" })
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
                        BoidManager.createBoid(spec, age, bDay, fltArray[0], fltArray[1], fltArray[2], fltArray2[0], fltArray2[1], fltArray2[2], cohesion, separation, alignment, wander);
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
