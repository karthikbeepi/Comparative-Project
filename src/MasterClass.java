import java.util.ArrayList;

public class MasterClass {
	
	ArrayList<BankDetails> banks = new ArrayList<>();
	ArrayList<CustomerDetails> customers = new ArrayList<>();
	
	public MasterClass() {
		ReadFromFile readCustomer = new ReadFromFile("G:\\My Documents\\GitHub\\Comparative-Project\\src\\customers.txt", "customers");
		ReadFromFile readBank = new ReadFromFile("G:\\My Documents\\GitHub\\Comparative-Project\\src\\banks.txt", "banks");
		BankDetails b;
		CustomerDetails c;
		
		while(true)
		{
			b = (BankDetails) readBank.next();
			if(b==null)
				break;
			banks.add(b);
			System.out.println(b);
		}
		
		while(true)
		{
			c = (CustomerDetails) readCustomer.next();
			if(c==null)
				break;
			customers.add(c);
			System.out.println(c);
		}
	}
	
	public static void main(String args[])
	{
		
		MasterClass obj = new MasterClass();
		
	}
	
}
