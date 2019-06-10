import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromFile {

	BufferedReader br;
	String type;
	
	public ReadFromFile(String filePath, String type) {
	
		try {
			
			br = new BufferedReader(new FileReader(filePath));
			
			this.type = type;
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		
	}

	public Object next() {
		
		if(type.compareToIgnoreCase("customers")==0)
			return readCustomer();
		else
			return readBank();
		
	}

	private Object readBank() {
		
		try {
			String str = br.readLine();
			if(str==null)
				return null;
			int posComma = str.indexOf(",");
			return new BankDetails(str.substring(1, posComma), Float.parseFloat(str.substring(posComma+1, str.length()-2)));
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return null;
	}

	private Object readCustomer() {
		try {
			String str = br.readLine();
			if(str==null)
				return null;
			int posComma = str.indexOf(",");
			return new CustomerDetails(str.substring(1, posComma), Float.parseFloat(str.substring(posComma+1, str.length()-2)));
		} catch (IOException e) {
	
			e.printStackTrace();
		}
		return null;
	}

	
	
}
