package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Species;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by james on 03/07/2014.
 */
public class StaXParser {


    private static final String NAME = "name";
    private static final String SPECIES = "species";
    private static final String COHESION = "cohesion";
    private static final String ALIGNMENT = "alignment";
    private static final String SEPARATION  = "separation";
    private static final String WANDER = "wander";
    private static final String NUMBER = "number";
    private static final String BYTE = "byte";
    private static final String SPRITELOCATION = "spriteLocation";
    private static final String SIGHTRADIUS = "sightRadius";
    private static final String FLOCKRADIUS= "flockRadius";
    private static final String NEARRADIUS = "nearRadius";
    private static final String MAXFORCE = "maxForce";
    private static final String MAXSPEED = "maxSpeed";
    private static final String LIFESPAN = "lifespan";
    private static final String MATURITY = "maturity";
    private static final String DIET = "diet";

    private static final String SPRITERGB = "spritergb";
    private static final String MAXSIZE = "maxSize";
    private static final String NEWBORNSIZE = "newbornSize";

    @SuppressWarnings({ "unchecked", "null" })
    public HashMap<Byte, Species> readConfig(String configFile) {
        HashMap<Byte, Species> speciesData = new HashMap<Byte, Species>();
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Species species = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have an item element, we create a new item
                    if (startElement.getName().getLocalPart() == (SPECIES)) {
                        species = new Species();
                        // We read the attributes from this tag and add the date
                        // attribute to our object
                        Iterator<Attribute> attributes = startElement
                                .getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(NAME)) {
                                species.setName(attribute.getValue());
                            }
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NUMBER)) {
                            event = eventReader.nextEvent();
                            species.setNumber(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(BYTE)) {
                            event = eventReader.nextEvent();
                            species.setSpbyte(Byte.valueOf(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SPRITELOCATION)) {
                            event = eventReader.nextEvent();
                            species.setSpriteLocation(event.asCharacters().getData());
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(DIET)) {
                            event = eventReader.nextEvent();
                            species.setDiet(event.asCharacters().getData());
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(LIFESPAN)) {
                            event = eventReader.nextEvent();
                            species.setLifespan(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(MATURITY)) {
                            event = eventReader.nextEvent();
                            species.setMaturity(Integer.parseInt(event.asCharacters().getData()));
                            continue;
                        }
                    }
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(MAXSIZE)) {
                            event = eventReader.nextEvent();
                            species.setMaxSize(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NEWBORNSIZE)) {
                            event = eventReader.nextEvent();
                            species.setNewbornSize(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(COHESION)) {
                            event = eventReader.nextEvent();
                            species.setCohesion(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SEPARATION)) {
                            event = eventReader.nextEvent();
                            species.setSeparation(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(ALIGNMENT)) {
                            event = eventReader.nextEvent();
                            species.setAlignment(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }


                    if (event.asStartElement().getName().getLocalPart()
                            .equals(WANDER)) {
                        event = eventReader.nextEvent();
                        species.setWander(Float.parseFloat(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SIGHTRADIUS)) {
                            event = eventReader.nextEvent();
                            species.setSightRadius(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(FLOCKRADIUS)) {
                            event = eventReader.nextEvent();
                            species.setFlockRadius(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NEARRADIUS)) {
                            event = eventReader.nextEvent();
                            species.setNearRadius(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(MAXFORCE)) {
                            event = eventReader.nextEvent();
                            species.setMaxForce(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(MAXSPEED)) {
                            event = eventReader.nextEvent();
                            species.setMaxSpeed(Float.parseFloat(event.asCharacters().getData()));
                            continue;
                        }
                    }
                    /////////////////////////////////////////////////////
                    //Added by Ben Nicholls for graphics purposes
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(SPRITERGB)) {
                        	float rgb[] = {0f, 0f, 0f};
                        	Iterator iterator = event.asStartElement().getAttributes();
                            while (iterator.hasNext()) {
                              Attribute attribute = (Attribute) iterator.next();
                              String name = attribute.getName().toString();
                              String value = attribute.getValue();
                              try {
	                              if(name == "r"){
	                            	  	rgb[0] = Float.parseFloat(value);
	                            	  
	                              }
	                              else if(name == "g"){
	                            	  rgb[1] = Float.parseFloat(attribute.getValue());
	                              }
	                              else if(name == "b"){
	                            	  rgb[2] = Float.parseFloat(attribute.getValue());
	                              }
                              }
                              catch (NumberFormatException nfe) {
                        		  System.out.println("Non-numeric value in "+ name + " colour attribute of species xml");
                        	  }
                              species.setRGB(rgb);
                            }
//                            event = eventReader.nextEvent();
                            
//                            species.setSpriteLocation(event.asCharacters().getData());
                            continue;
                        }
                    }
                    ////////////////////////////////////////////////
                }
                // If we reach the end of an item element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart() == (SPECIES)) {
                        speciesData.put(species.getSpbyte(), species);
                    }
                }
                
                
      

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return speciesData;
    }
}
