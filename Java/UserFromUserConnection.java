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


public class UserFromUserConnection {

	public static void main(String args[]){
		
		HashSet<String> userSet = new HashSet<String>();
		
		
		
		
		String user_connections = "/Users/Krish/Documents/Event-Recommendation/Dataset/Import/user_connections_import.csv";
		
		BufferedReader ur = null;
		BufferedReader ea = null;
		BufferedReader uc = null;
		
		FileWriter fWriter = null;
		String line = "";
		String split = ",";
		try {
			fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/uniqueusers.csv");
			fWriter.append("userId:ID(User),"+":LABEL"+"\n");
			
			
			uc = new BufferedReader(new FileReader(user_connections));
			while ((line = ea.readLine()) != null) {
				
				
				
				String[] record = line.split(split);
				if(record[0] == "497102886" || record[1] == "497102886"){
					System.out.println("497102886 Found");
				}
					userSet.add(record[0]);
					userSet.add(record[1]);
					
					
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
