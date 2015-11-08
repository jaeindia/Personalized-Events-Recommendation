import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UniqueUsers {

	public static void main(String args[]){
		
		HashSet<String> userSet = new HashSet<String>();
		
		
		String usersFile = "/Users/Krish/Documents/Event-Recommendation/Dataset/users.csv";
		String eventAttendees = "/Users/Krish/Documents/Event-Recommendation/Dataset/Import/event_attendees_import.csv";
		String user_connections = "/Users/Krish/Documents/Event-Recommendation/Dataset/user_connections_v1.csv";
		
		BufferedReader ur = null;
		BufferedReader ea = null;
		BufferedReader uc = null;
		
		FileWriter fWriter = null;
		String line = "";
		String split = ",";
		try {
			fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/uniqueusers.csv");
			fWriter.append("userId:ID(User),"+":LABEL"+"\n");
			ur = new BufferedReader(new FileReader(usersFile));
			while ((line = ur.readLine()) != null) {
				
				
				String[] record = line.split(split);
					if(!record[0].equals("user_id")){
						userSet.add(record[0]);
					}
					
					
			}
			System.out.println(userSet.size());
			
			ea = new BufferedReader(new FileReader(eventAttendees));
			while ((line = ea.readLine()) != null) {
				
				
				String[] record = line.split(split);
					if(!record[1].equals("user_id")){
						userSet.add(record[1]);
					}
					
					
			}
			System.out.println(userSet.size());
			uc = new BufferedReader(new FileReader(user_connections));
			int count = 0;
			while ((line = uc.readLine()) != null) {
				
				
				count++;
				String[] record = line.split(split);
				
					userSet.add(record[0]);
					userSet.add(record[1]);
					
				if(count%1000000==0){
					System.out.println(count+ "Iteration");
				}
			}
			
			for(String userId : userSet){
				fWriter.append(userId+",User"+"\n");
			}
			
			ur.close();
			ea.close();
			fWriter.flush();
			fWriter.close();
			
			
			System.out.println(userSet.size());
	
	}
		catch(FileNotFoundException e){
			
		}
		catch(IOException e){
			
		}
	

		
	}
	
	
}
