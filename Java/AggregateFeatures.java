import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


public class AggregateFeatures {

	public static void main(String args[]){
	
		String fileInput;
		String destinationFile = "/Users/Krish/Documents/Event-Recommendation/Dataset/Features/feature2.csv";
		String line = "";
		String split = ",";
		Integer totalCount = 22;
		Integer fileCount = 0;
		Integer perFileCount = 0;
		Integer totalOutput = 0;
		
		FileWriter fWriter = null;
		BufferedReader fileReader = null;
		
		try{
			fWriter = new FileWriter(destinationFile);
			fWriter.append("Event,User,Invited Friends,Yes,No,Maybe,Nochoice \n");
			while(totalCount-->0){
				fileCount++;
				perFileCount = 0;
				fileInput = "/Users/Krish/Documents/Event-Recommendation/Dataset/Features/Feature2/feature2-"+fileCount+".csv";
				fileReader = new BufferedReader(new FileReader(fileInput));
				while ((line = fileReader.readLine()) != null) {						
					if(!line.contains("Event")){
						fWriter.append(line+"\n");
						perFileCount++;
					}
					
				}
				totalOutput += perFileCount;
				System.out.println("Input read from file "+ fileInput +" with total records "+ perFileCount);
				fileReader.close();
			}
			System.out.println("Total Records written : "+ totalOutput);
			
			fWriter.flush();
			fWriter.close();
			
		}
		catch(Exception e){
			
		}
		
	}
}
