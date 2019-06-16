import java.util.ArrayList;
import java.util.Random;

public class Customer implements Runnable {

	float loanAmt;
	String customerName;
	money obj;
	ArrayList<String> banksDelisted = new ArrayList<>();
	
	public Customer(String cname, float loan, money m) {
		
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
			
			float loanReq = (float)rand.nextInt(50)+1;
			
//			loanReq = (float)(rand.nextInt(5000)*0.01)+1;
			
			if(loanReq>=loanAmt)
				loanReq = loanAmt;
//			loanReq = 100;
			System.out.println(customerName+" requests a loan of "+(int)loanReq+" dollar(s) from "+bankSelected);
			temp.add(customerName+" "+loanReq);
			try {
				wait(rand.nextInt(200)+10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String s = obj.customerMessages.get(customerName);
//			int c =0;
			while(s==null)
			{
//				c++;break;
				try {
					
					wait(rand.nextInt(100));
//					notifyAll();
				} catch (InterruptedException e) {
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
			
		}
		if(loanAmt==0)
			System.out.println("\n"+customerName+" has reached the objective of "+(int) Math.round(obj.customers.get(customerName))+" Woo Hoo!");
		else
			System.out.println("\n"+customerName+" was only able to borrow "+(int) Math.round(obj.customers.get(customerName)-loanAmt)+" Boo Hoo!!! ");
		obj.customersDone.add(customerName);
		try {
			wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
