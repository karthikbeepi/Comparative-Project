import java.util.ArrayList;

public class Bank implements Runnable {

	float balance;
	String bankName;
	MasterClass obj;
	
	public Bank(String bname, float bal, MasterClass masterClass ) {
		
		bankName = bname;
		balance = bal;
		obj = masterClass;
	}
	
	@Override
	public String toString() {

		return bankName+": "+balance;
	}

	@Override
	public synchronized void run() {
		
		ArrayList<String> msg = obj.bankMessages.get(bankName);
		while(msg.size()==0)
		{
			try {
				wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			continue;
		}
		
		while(obj.customersDone.size()!=obj.customers.size())
		{
			ArrayList<String> temp = obj.bankMessages.get(bankName);
			if(temp.size()==0)
			{
				try {
					wait(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
//			for(String s: temp)
//			for(int j=0; j<temp.size(); j++)
			while(temp.size()!=0)
			{
				String s = temp.get(0);
				String[] sp = s.split(" ");
				Float f = Float.parseFloat(sp[1]);
				if(balance<f)
				{
					System.out.println(bankName+" denies a loan of "+sp[1]+" dollar(s) from "+sp[0]);
					obj.customerMessages.put(sp[0], "NO");
				}
				else
				{
					System.out.println(bankName+" approves a loan of "+sp[1]+" dollar(s) from "+sp[0]);
					obj.customerMessages.put(sp[0], "YES");
					balance-=f;
//					System.out.println("Current balance "+bankName+" : "+balance);
				}
				temp.remove(0);
			}
			
//			temp.clear();
			try {
				wait(100);
				continue;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.out.println("Done "+bankName+" "+balance);
		if(balance>0)
			System.out.println("\n"+bankName+" has "+balance+" dollar(s) remaining !");
	}
	
}
