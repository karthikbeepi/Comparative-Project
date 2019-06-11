import java.util.ArrayList;

public class MasterClass {
	
	ArrayList<BankDetails> banks = new ArrayList<>();
	ArrayList<CustomerDetails> customers = new ArrayList<>();
	
	public MasterClass() {
		ReadFromFile readCustomer = new ReadFromFile("customers.txt", "customers");
		ReadFromFile readBank = new ReadFromFile("banks.txt", "banks");
		BankDetails b;
		CustomerDetails c;
		
		System.out.println("** Customers and loan objectives **");
		while(true)
		{
			c = (CustomerDetails) readCustomer.next();
			if(c==null)
				break;
			customers.add(c);
			System.out.println(c);
		}
		
		System.out.println("\n** Banks and financial resources **");
		while(true)
		{
			b = (BankDetails) readBank.next();
			if(b==null)
				break;
			banks.add(b);
			System.out.println(b);
		}
		
	}
	
	public static void main(String args[])
	{
		
		MasterClass obj = new MasterClass();
		obj.runBanks();
		obj.runCustomers();
		
	}

	private void runCustomers() {
		// TODO Auto-generated method stub
		
	}

	private void runBanks() {
		// TODO Auto-generated method stub
		
	}
	
}
