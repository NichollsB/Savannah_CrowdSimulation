package com.UKC_AICS.simulation.utils;

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

    static final String NAME = "name";
    static final String SPECIES = "species";
    static final String COHESION = "cohesion";
    static final String ALIGNMENT = "alignment";
    static final String SEPARATION  = "separation";
    static final String WANDER = "wander";
    static final String NUMBER = "number";
    static final String BYTE = "byte";

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
