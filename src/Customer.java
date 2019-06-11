public class Customer implements Runnable {

	float loanAmt;
	String customerName;
	
	public Customer(String cname, float loan) {
		
		customerName = cname;
		loanAmt = loan;
		
	}
	
	@Override
	public String toString() {
		
		return customerName+": "+loanAmt;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
