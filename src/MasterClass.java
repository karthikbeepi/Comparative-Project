import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MasterClass {
	
	HashMap<String , Float> banks = new HashMap<>();
	HashMap<String, Float> customers = new HashMap<>();
	ArrayList<String> bankNames = new ArrayList<>();
	ArrayList<String> customersDone = new ArrayList<>();
	
	HashMap<String, ArrayList<String>> bankMessages = new HashMap<>();
	HashMap<String, String> customerMessages = new HashMap<>();
	
	Thread[] bankThreads;
	Thread[] customerThreads;
	
	public MasterClass() {
		File testFile = new File("");
		String currentPath = testFile.getAbsolutePath();
		System.out.println("current path is: " + currentPath);
		ReadFromFile readCustomer;
		ReadFromFile readBank;
		if(!currentPath.contains("src"))
		{
			readCustomer = new ReadFromFile("./src/customers.txt", "customers");
			readBank = new ReadFromFile("./src/banks.txt", "banks");
		}
		else
		{
			readCustomer = new ReadFromFile("customers.txt", "customers");
			readBank = new ReadFromFile("banks.txt", "banks");	
		}
			
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
			bankNames.add(temp.name);
			System.out.println(temp.name+" "+temp.value);
		}
		
		System.out.println();
	}
	
	public static void main(String args[])
	{
		
		MasterClass obj = new MasterClass();
		obj.runBanks();
		obj.runCustomers();
		
	}

	private void runCustomers() {
		
		customerThreads = new Thread[customers.size()];
		int i=0;
		for(String s: customers.keySet())
		{
			customerThreads[i] = new Thread(new Customer(s, customers.get(s), this));
			customerThreads[i].start();
			i++;
		}
	}

	private void runBanks() {
		
		bankThreads = new Thread[banks.size()];
		int i=0;
		for(String s: banks.keySet())
		{
			bankThreads[i] = new Thread(new Bank(s, banks.get(s), this));
			bankMessages.put(s, new ArrayList<String>());
			bankThreads[i].start();
			i++;
		}
		
	}
	
}
