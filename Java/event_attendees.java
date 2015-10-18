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


public class event_attendees {

	public static void main(String args[]){
		Integer count = 0;
		
		
		String fileLoc = "/Users/Krish/Documents/Event-Recommendation/Dataset/event_attendees.csv";
		BufferedReader br = null;
		FileWriter fWriter = null;
		String line = "";
		String split = ",";
		String internalSplit = " ";
		try {
			fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/event_attendees_mod.csv");
			br = new BufferedReader(new FileReader(fileLoc));
			while ((line = br.readLine()) != null) {

				List<String> eventList = new ArrayList<String>();
				Set<String> userSet = new HashSet<String>(); 
				List<String> invitedList = new ArrayList<String>();
				List<String> yesList = new ArrayList<String>();
				List<String> noList = new ArrayList<String>();
				List<String> maybeList = new ArrayList<String>();
				
				String[] record = line.split(split);
					
					for(int i = 0; i < record.length; i++){
						if(i == 0){
							eventList = Arrays.asList(record[0].split(internalSplit));
						}
						if(i == 1){
							yesList = Arrays.asList(record[1].split(internalSplit));
						}
						if(i == 2){
							maybeList = Arrays.asList(record[2].split(internalSplit));
						}
						if(i == 3){
							invitedList = Arrays.asList(record[3].split(internalSplit));
						}
						if(i == 4){
							noList = Arrays.asList(record[4].split(internalSplit));
						}
						
					}
					
					userSet.addAll(invitedList);
					userSet.addAll(yesList);
					userSet.addAll(noList);
					userSet.addAll(maybeList);
					
					
					
					
					
				
				
				for(String events : eventList){
					for(String user : userSet){
						count++;
						if(yesList.contains(user)){
							fWriter.append(events +","+ user + ",YES\n");
						}
						else if(noList.contains(user)){
							fWriter.append(events +","+ user + ",NO\n");
						}
						else if(maybeList.contains(user)){
							fWriter.append(events +","+ user + ",MAYBE\n");
						}
						else{
							fWriter.append(events +","+ user + ",\n");
						}
						
					}
				}
				System.out.println(count + " lines written");
			}
			
			fWriter.flush();
			fWriter.close();
			br.close();
	
	}
		catch(FileNotFoundException e){
			
		}
		catch(IOException e){
			
		}
	
	
	}
}
