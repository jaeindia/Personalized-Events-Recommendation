package com.eventrecommedation.events;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;


public class EventFilter {
	
	public static void main(String args[]) throws ParseException{
		
		String eventFile = "/Users/Krish/Documents/Event-Recommendation/Dataset/events.csv";
		String eventAttendees = "/Users/Krish/Documents/Event-Recommendation/Dataset/Import/event_attendees_import_new.csv";
		
		
		BufferedReader eventReader = null;
		BufferedReader eventAttendeesReader = null;
		
		
		HashSet<String> eventStringSet = new HashSet<String>();
		
		HashSet<Events> eventSet = new HashSet<Events>();
		
		FileWriter fWriter = null;
		
		String line = "";
		String split = ",";
		
	
		
		try {
			fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/events_enriched.csv");
			fWriter.append("eventId:ID(Event),creatorId:long,startTime:long,city,state,zip,country,latitude,longitude,:LABEL"+"\n");
			
			
			eventReader = new BufferedReader(new FileReader(eventFile));
			while ((line = eventReader.readLine()) != null) {
				
				Events event = new Events();
				
				String[] record = line.split(split);
					if(!record[0].equals("eventId:ID(Event)")){
						if(eventStringSet.add(record[0])){

							event.setEventId(Long.parseLong(record[0]));
							//Add User ID
							if(!record[1].isEmpty()){
								event.setCreatorId(Long.parseLong(record[1]));
							}
							else{
								event.setCreatorId(Long.parseLong("0"));
							}
							
							//Add time
							if(!record[2].isEmpty()){
								String timeString = record[2].replace("T", " ");
								timeString = timeString.replace("Z", "");
								SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
								Date date = dateFormat.parse(timeString);
								event.setStartTime(date.getTime());
							}
							else{
								event.setStartTime(0L);
							}
							
							//Add city
							if(!record[3].isEmpty()){
								event.setCity(record[3]);
							}
							else{
								event.setCity("None");
							}
							
							//Add state
							if(!record[4].isEmpty()){
								event.setState(record[4]);
							}
							else{
								event.setState("None");
							}
							
							//Add zip
							if(!record[5].isEmpty()){
								event.setZip(record[5]);
							}
							else{
								event.setZip("None");
							}
							
							//Add country
							if(!record[6].isEmpty()){
								event.setCountry(record[6]);
							}
							else{
								event.setCountry("None");
							}
							
							//Add latitude
							if(!record[7].isEmpty()){
								event.setLatitude(record[7]);
							}
							else{
								event.setLatitude("None");
							}
							
							//Add longitude
							if(!record[8].isEmpty()){
								event.setLongitude(record[8]);
							}
							else{
								event.setLongitude("None");
							}
							
							eventSet.add(event);
						}
					}
					
					
			}
			int count = 0;
			System.out.println(eventStringSet.size());
			
			eventAttendeesReader = new BufferedReader(new FileReader(eventAttendees));
			while ((line = eventAttendeesReader.readLine()) != null) {
				Events event = new Events();
				count++;
				String[] record = line.split(split);
					
				if(!record[0].equals(":START_ID(Event)")){
						if(eventStringSet.add(record[0])){
							
								event.setEventId(Long.parseLong(record[0]));
								
								//Add User ID
									event.setCreatorId(Long.parseLong("0"));
								
								//Add time
									event.setStartTime(Long.parseLong("0"));
								
								//Add city
									event.setCity("None");
								
								//Add state
									event.setState("None");
								
								//Add zip
									event.setZip("None");
								
								//Add country
									event.setCountry("None");
								
								//Add latitude
									event.setLatitude("None");
								
								//Add longitude
									event.setLongitude("None");
								
								eventSet.add(event);
							}
						}
						if(count%1000000==0){
							System.out.println(count+ "Iteration");
						}
			}
			
			
			
			
			
			for(Events event : eventSet){
				fWriter.append(event.getEventId()+","+event.getCreatorId()+","+event.getStartTime()+","+event.getCity()+","+event.getState()+","+event.getZip()+","+event.getCountry()+","+event.getLatitude()+","+event.getLongitude()+",Event"+"\n");
			}
			
			eventReader.close();
			eventAttendeesReader.close();
			fWriter.flush();
			fWriter.close();
			
			
			System.out.println(eventStringSet.size());
			System.out.println(eventSet.size());
	
	}
		catch(FileNotFoundException e){
			
		}
		catch(IOException e){
			
		}

		
		
		
		
	}
}
