import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class ParserCSV {
	
	
	public static void main(String[] args) throws Exception {
		
		String line = null;
		
		FileReader rFile = new FileReader("database.csv");
		BufferedReader rBuffer = new BufferedReader(rFile);
		
		FileWriter wFile = new FileWriter("new_database.csv");
		BufferedWriter wBuffer = new BufferedWriter(wFile);
		
		while((line = rBuffer.readLine()) != null)  {
			String outPut = "";
			outPut += line.charAt(0);
			
			for(int i = 1; i < line.length(); i ++) {
				if(line.charAt(i) == line.charAt(i - 1) && line.charAt(i) == ',') {
					outPut += "?";
					if(i == line.length() - 1) {	
						outPut += ",?";
					} else {
						outPut += ",";
					}
				} else {
					if(line.charAt(i) != '\'')
						outPut += line.charAt(i);
				}
			}
			
			wBuffer.write(outPut + '\n');
        }
		
		rBuffer.close();
		wBuffer.close();
		
	}
	
}
