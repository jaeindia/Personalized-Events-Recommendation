import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class user_friends {

	public static void main(String args[]){
		Integer count = 0;
		
		
		String fileLoc = "/Users/Krish/Documents/Event-Recommendation/Dataset/user_friends.csv";
		BufferedReader br = null;
		FileWriter fWriter = null;
		String line = "";
		String split = ",";
		String internalSplit = " ";
		try {
			fWriter = new FileWriter("/Users/Krish/Documents/Event-Recommendation/Dataset/user_friends_mod.csv");
			br = new BufferedReader(new FileReader(fileLoc));
			while ((line = br.readLine()) != null) {
				
				List<String> userList = new ArrayList<String>();
				List<String> friendsList = new ArrayList<String>();
				
				String[] record = line.split(split);
					
					for(int i = 0; i < record.length; i++){
						if(i == 0){
							userList = Arrays.asList(record[0].split(internalSplit));
						}
						if(i == 1){
							friendsList = Arrays.asList(record[1].split(internalSplit));
						}
					}
					
				for(String user : userList){
					for(String friend : friendsList){
						count++;
						fWriter.append(user +","+ friend + "\n");
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
