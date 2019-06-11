import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MasterClass {
	
	HashMap<String , Float> banks = new HashMap<>();
	HashMap<String, Float> customers = new HashMap<>();
	
	public MasterClass() {
//		 File testFile = new File("");
//		    String currentPath = testFile.getAbsolutePath();
//		    System.out.println("current path is: " + currentPath);
		ReadFromFile readCustomer = new ReadFromFile("./src/customers.txt", "customers");
		ReadFromFile readBank = new ReadFromFile("./src/banks.txt", "banks");
//		BankDetails b;
//		CustomerDetails c;
//		
		Pair temp;
		System.out.println("** Customers and loan objectives **");
		while(true)
		{
			temp =  readCustomer.next();
			if(temp==null)
				break;
			customers.put(temp.name, temp.value);
			System.out.println(temp.name+" "+temp.value);
			
		}
		
		System.out.println("\n** Banks and financial resources **");
		while(true)
		{
			temp =  readBank.next();
			if(temp==null)
				break;
			banks.put(temp.name, temp.value);
			System.out.println(temp.name+" "+temp.value);
		}
		
	}
	
	public static void main(String args[])
	{
		
		MasterClass obj = new MasterClass();
		obj.runBanks();
		obj.runCustomers();
		
	}

	private void runCustomers() {
		
	}

	private void runBanks() {
		
	}
	
}
