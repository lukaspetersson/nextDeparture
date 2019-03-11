/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.test.nextDeparture.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
       String message = "Departs in ";
       try {
          int stationId = 9123;
          String trainTag = "Tram";

          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          DocumentBuilder db = dbf.newDocumentBuilder();
          Document doc = db.parse(new URL("http://api.sl.se/api2/realtimedeparturesV4.xml?key=bf027a76219844feb489b57a4c9b29fc&siteid="+stationId+"&timewindow=60").openStream());

         NodeList listOfTrains = doc.getElementsByTagName(trainTag);

         for(int i=0; i<listOfTrains.getLength() ; i++){

             Node firstTrainNode = listOfTrains.item(i);
             if(firstTrainNode.getNodeType() == Node.ELEMENT_NODE){


                 Element firstTrainElement = (Element)firstTrainNode;
                 
                 NodeList directionList = firstTrainElement.getElementsByTagName("JourneyDirection");
                 Element directionElement = (Element)directionList.item(0);
                 NodeList textDirectionList = directionElement.getChildNodes();
                 String direction = textDirectionList.item(0).getNodeValue().trim();
                 
                 if(direction.equals("2") && message.length() < 55){
                    NodeList timeList = firstTrainElement.getElementsByTagName("ExpectedDateTime");
                    Element timeElement = (Element)timeList.item(0);
                    NodeList textTimeList = timeElement.getChildNodes();
                    String departDate = textTimeList.item(0).getNodeValue().trim().replace("T", " ");
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(departDate);
                    long millis = date.getTime();
                    
                    SimpleDateFormat showMinutes = new SimpleDateFormat("m");
                    long currentTime = System.currentTimeMillis();
                    
                    if(millis >= currentTime) {
                       long  time = millis - currentTime;
                       
                       
                       String timeString = showMinutes.format(time);
                       if(message.length() >= 14) {
                          message += ", then  ";
                       }
                       message += timeString+" minutes"; 
                    }
                 }
             }
         }
     }catch (SAXParseException err) {
        message = "error " + err.getMessage ();
     }catch (SAXException e) {
     Exception x = e.getException ();
     ((x == null) ? e : x).printStackTrace ();

     }catch (Throwable t) {
     t.printStackTrace ();
     }
        return input.getResponseBuilder()
                .withSpeech(message)
                .withSimpleCard("HelloWorld", message)
                .withReprompt(message)
                .withShouldEndSession(true)
                .build();
    }

}
