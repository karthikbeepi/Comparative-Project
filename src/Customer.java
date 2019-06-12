import java.util.ArrayList;
import java.util.Random;

public class Customer implements Runnable {

	float loanAmt;
	String customerName;
	MasterClass obj;
	ArrayList<String> banksDelisted = new ArrayList<>();
	
	public Customer(String cname, float loan, MasterClass m) {
		
		customerName = cname;
		loanAmt = loan;
		obj = m;
		
	}
	
	@Override
	public String toString() {
		
		return customerName+": "+loanAmt;
	}

	@Override
	public synchronized void run() {
		
		while(loanAmt>0 && banksDelisted.size()<obj.banks.size())
		{
			Random rand = new Random();
			String bankSelected = obj.bankNames.get(rand.nextInt(obj.bankNames.size()));
			
			while(banksDelisted.contains(bankSelected))
			{
				bankSelected = obj.bankNames.get(rand.nextInt(obj.bankNames.size()));
			}
			ArrayList<String> temp = obj.bankMessages.get(bankSelected);
			float loanReq = (float)rand.nextInt(50);
//			if(loanReq>loanAmt)
//				loanReq = (float) rand.nextInt((int)loanAmt-1)+1;
			loanReq = 100;
//			System.out.println(customerName+" requests a loan of "+loanReq+" dollar(s) from "+bankSelected);
			temp.add(customerName+" "+loanReq);
			try {
				wait(rand.nextInt(10)+100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = obj.customerMessages.get(customerName);
			
			while(s==null)
			{
				try {
					wait(rand.nextInt(10)+100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				s = obj.customerMessages.get(customerName);
			}
			
			if(s.compareToIgnoreCase("YES")==0)
				loanAmt-=loanReq;
			else
			{
				banksDelisted.add(bankSelected);
			}
			obj.customerMessages.remove(customerName);
			try {
				wait(rand.nextInt(10)+100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("DONE "+customerName+" "+loanAmt);
		obj.customersDone.add(customerName);
		
	}
	
}
