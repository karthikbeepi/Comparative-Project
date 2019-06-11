public class BankDetails {

	float balance;
	String bankName;
	
	public BankDetails(String bname, float bal) {
		
		bankName = bname;
		balance = bal;
		
	}
	
	@Override
	public String toString() {

		return bankName+": "+balance;
	}
	
}
