
public class CustomerDetails {

	float loanAmt;
	String customerName;
	
	public CustomerDetails(String cname, float loan) {
		
		customerName = cname;
		loanAmt = loan;
		
	}
	
	@Override
	public String toString() {
		
		return customerName+" "+loanAmt;
	}
	
}
