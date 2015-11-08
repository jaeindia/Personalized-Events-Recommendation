package com.eventrecommendation.features;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GenerateFeature4 {

	public static void main(String[] args) throws IOException {

		//long startTime = System.currentTimeMillis();
		File directory = new File("/Users/Krish/Documents/Event-Recommendation/Graph/eventrecommendationnew.db");
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(directory);
		
		String rows = "";
		FileWriter fWriter = null;
		Integer lowerLimit = 12000;
		Integer step = 2000;
		Integer upperLimit = 1000000;
		
		fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/Features/feature4.csv");
		fWriter.append("Event,User,Event_Date,Event_Attendees,Past_Events,Past_Event_Attendees,Past_And_Current_Event_Attendees\n");
		
		while(lowerLimit < upperLimit){
			
			System.out.println("Processing " +lowerLimit+ " to "+ (lowerLimit+step));
		
			try ( Transaction ignored = db.beginTx();
					Result result = db.execute("Match "
							+ "(e:Event)-[i:Invited{response:'YES'}]->(u:User)  "
							+ "where id(e) > "+lowerLimit+" and id(e) <= "+(lowerLimit+step)+" "
							+ "with collect(u.userId) as collections,e,count(distinct(u.userId)) as u "
							+ "Match "
							+ "(event:Event)-[invitation:Invited{response:'YES'}]->(users:User) "
							+ "where id(event) > "+lowerLimit+" and id(event) <= "+(lowerLimit+step)+ " and id(event) = id(e) "
							+ "with event,users,collections,u "
							+ "Match "
							+ "(users)<-[pastinvitations:Invited{response:'YES'}]-(pastevent:Event) "
							+ "where event.startTime > pastevent.startTime "
							+ "with event,users,collections,pastevent,u "
							+ "match "
							+ "(pastevent)-[otherinvitations:Invited{response:'YES'}]->(otherusers:User)"
							+ "return  event.eventId as Event, users.userId as User,"
							+ "event.startTime as Event_Date,u as Event_Attendees,"
							+ "count(distinct(pastevent.eventId)) as Past_Events, "
							+ "count(distinct(otherusers.userId)) as Past_Event_Attendees, "
							+ "sum(case when otherusers.userId in collections then (case when otherusers.userId <> users.userId then 1 else 0 end) else 0 end) as Past_And_Current_Event_Attendees"))
				{
				
				
				
				    while ( result.hasNext() )
				    {
				    	rows = "";
				    	Map<String, Object> row = result.next();
				         for ( String key : result.columns() )
				         {
				            rows +=   row.get(key)+"," ;
				         }
				         
				         rows = StringUtils.chop(rows);
				         
				         fWriter.append(rows+"\n");
				        
				    }
				    
				
				}
			
			
			
			fWriter.flush();
			
			lowerLimit += step;
			
			System.out.println("Processed " + lowerLimit +" Events");
		}
		
		 fWriter.flush();
		 fWriter.close();
		
			
		
		
//		long endTime   = System.currentTimeMillis();
//		long totalTime = endTime - startTime;
//		System.out.println("Total Time " +(totalTime)/1000 + " Seconds");
		
	}
	
}
