public class Bank implements Runnable {

	float balance;
	String bankName;
	
	public Bank(String bname, float bal) {
		
		bankName = bname;
		balance = bal;
		
	}
	
	@Override
	public String toString() {

		return bankName+": "+balance;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
