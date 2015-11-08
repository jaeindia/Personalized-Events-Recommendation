package com.eventrecommendation.features;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;



public class FeatureGen {

	
	
	public static void main(String args[]) throws IOException{
		
		
		
		int eventAttendeesCount = 0;
		File directory = new File("/Users/Krish/Documents/Event-Recommendation/Graph/eventrecommendationnew.db");
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(directory);
		Transaction tx = db.beginTx();
		
		//Node event = db.findNode(DynamicLabel.label("Event"), "eventId", "2665105550");
		//System.out.println(event.getId());
		
		/*
		 * Here Events are always StartNode
		 * Users are always EndNode
		 * 
		 * (Event)-[]->(Users)
		 * 
		 */
		
		FileWriter fWriter = null;
		fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/Features/feature4.csv");
		fWriter.append("Event,User,Event_Date,Event_Attendees,Past_Events,Past_Event_Attendees,Past_And_Current_Event_Attendees\n");
		Long nodeId = 0L;
		Long iterationCount = 0L;
		Set<Node> eventAttendees = new HashSet<Node>();
		Set<Node> pastEvents = new HashSet<Node>();
		Set<Node> pastEventAttendees = new HashSet<Node>();
		Set<Node> userAttendingBoth;
		
		while(nodeId++ <= 3138209){
			iterationCount++;
			eventAttendees.clear();
			Node events = db.getNodeById(nodeId);
			
			//Add EventAttendees
			for(Relationship eventAttendee : events.getRelationships()){
				if(eventAttendee.getProperty("response").equals("YES"))
				{
					eventAttendees.add(eventAttendee.getEndNode());
				}
			}
			//Iterate for every user and get past events list
			for(Node eventAttendee : eventAttendees){
					pastEvents.clear();
					pastEventAttendees.clear();
					for(Relationship pastEventRelationship : eventAttendee.getRelationships(rType.Invited)){
						//Check current event and past event time
						Node pastEvent = pastEventRelationship.getStartNode();
						//Node[] pastEventNode = pastEventRelationship.getNodes();
						if(pastEventRelationship.getProperty("response").equals("YES") && ((Long)events.getProperty("startTime") > (Long)pastEvent.getProperty("startTime"))){
								pastEvents.add(pastEventRelationship.getStartNode());
						}
					}
					
					for(Node pastEvent :  pastEvents){
						for(Relationship pastEventRelationship : pastEvent.getRelationships(rType.Invited)){
							Node pastEventUsers = pastEventRelationship.getEndNode();
							if((pastEventRelationship.getProperty("response").equals("YES")) && !eventAttendee.equals(pastEventUsers)){
								pastEventAttendees.add(pastEventUsers);
							}
						}
					}
					
					userAttendingBoth = new HashSet<Node>(eventAttendees);
					userAttendingBoth.retainAll(pastEventAttendees);
					
					if(pastEvents.size() > 0){
						fWriter.append(events.getProperty("eventId")+","+eventAttendee.getProperty("userId")+","+events.getProperty("startTime")+","+ eventAttendees.size()+","+ pastEvents.size()+","+ pastEventAttendees.size()+","+userAttendingBoth.size()+"\n");
					}
			
					userAttendingBoth.clear();
			}
		
			if(iterationCount%1000==0){
				System.out.println("Processed "+iterationCount+" nodes");
				fWriter.flush();
			}
			
		}
		
		
		
		
		fWriter.flush();
		fWriter.close();
		tx.success();
	}
	
}
