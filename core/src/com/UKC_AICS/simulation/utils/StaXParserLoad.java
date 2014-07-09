package com.UKC_AICS.simulation.utils;

import com.UKC_AICS.simulation.entity.Species;

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
import com.UKC_AICS.simulation.managers.BoidManager;
import com.UKC_AICS.simulation.entity.Boid;
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
    private int age = 0;
    private int bDay = 0;
   // private float pX = 0;
  //  private float pY = 0;
   // private float pZ = 0;
  //  private float vX = 0;
  //  private float vY = 0;
  //  private float vZ = 0;
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
                    // If we have an item element, we create a new item
                  //  if (startElement.getName().getLocalPart() == (BOID)) {
                     
                        // We read the attributes from this tag and add the date
                        // attribute to our object
                      //  Iterator<Attribute> attributes = startElement
                         //       .getAttributes();
                      //  while (attributes.hasNext()) {
                        	
                          //  Attribute attribute = attributes.next();
                           // if (attribute.getName().toString().equals(AGE)) {
                            	// age = Integer.parseInt(attribute.getValue());
                            	// System.out.println("Age "+age);
                           // }
                       // }
                    
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(AGE)) {
                            event = eventReader.nextEvent();
                             age = Integer.parseInt(event.asCharacters().getData());
                            // System.out.println("Age "+age);
                            continue;
                        }
                    
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(BIRTHDAY)) {
                            event = eventReader.nextEvent();
                             bDay = Integer.parseInt(event.asCharacters().getData());
                            // System.out.println("Bday "+bDay);
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
                         //   for(String s : strArray){
                          //  	int i = 0;
                          //  	Float f = Float.parseFloat(s);
                          //  	System.out.println(f);
                           // 	
                          //  	fltArray[i]=f;
                          ///  	System.out.println(fltArray[i]);
                          //  	i++;
                           // }
                            
                           // System.out.println("Startpx" + pX);
                          //  System.out.println("Startpy" + pY);
                          //  System.out.println("Startpz" + pZ);
                           // pX = fltArray[0];
                           // System.out.println(pX);
                            //pY = fltArray[1];
                           // System.out.println(pY);
                          //  pZ = fltArray[2];
                          //  System.out.println(pZ);
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
                            
                            
                           // for(String s : strArray2){
                            //	int i = 0;
                           // 	Float f = Float.parseFloat(s);
                            //	fltArray2[i]=f;
                            //	i++;
                            	
                           // }
                            
                         //   vX = fltArray2[0];
                           // vY = fltArray2[1];
                           // vZ = fltArray2[2];
                            
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

                    
                }
                // If we reach the end of an item element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart() == (BOID)) {
                    	System.out.println("AGE " + age);
                    	System.out.println("bday " + bDay);
                    	System.out.println(fltArray[0]);
                    	System.out.println(fltArray[1]);
                    	System.out.println(fltArray[2]);
                    	System.out.println(fltArray2[0]);
                    	System.out.println(fltArray2[1]);
                    	System.out.println(fltArray2[2]);
                    	System.out.println(spec);
                        BoidManager.createBoid(spec ,age ,bDay ,fltArray[0] ,fltArray[1] ,fltArray[2] ,fltArray2[0] ,fltArray2[1] ,fltArray2[2]);
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
