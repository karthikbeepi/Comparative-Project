
public class BankDetails implements Runnable{

	float balance;
	String bankName;
	
	public BankDetails(String bname, float bal) {
		
		bankName = bname;
		balance = bal;
		
	}
	
	@Override
	public String toString() {

		return bankName+" "+balance;
	}

	@Override
	public void run() {
		
		Thread t = new Thread(this);
		
		
	}
	
}
