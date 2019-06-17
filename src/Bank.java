import java.util.ArrayList;
import java.util.Random;

public class Bank implements Runnable {

	float balance;
	String bankName;
	money obj;
	
	public Bank(String bname, float bal, money masterClass ) {
		
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
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			continue;
		}
		
		while(obj.customersDone.size()!=obj.customers.size())
		{
			ArrayList<String> temp = obj.bankMessages.get(bankName);
			while(temp.size()==0)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println(obj.bankMessages+" "+bankName+obj.customerMessages);
				continue;
			}
			while(temp.size()!=0)
			{
				if(temp==null)
				{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					System.out.println(obj.bankMessages+" "+bankName+obj.customerMessages);
					continue;
				}
					
				String s = temp.get(0);
				String[] sp = s.split(" ");
				Float f = Float.parseFloat(sp[1]);
				if(balance<f)
				{
					System.out.println(bankName+" denies a loan of "+(int) Math.round(f)+" dollar(s) from "+sp[0]);
					obj.customerMessages.put(sp[0], "NO");
				}
				else
				{
					System.out.println(bankName+" approves a loan of "+(int) Math.round(f)+" dollar(s) from "+sp[0]);
					obj.customerMessages.put(sp[0], "YES");
					balance-=f;
//					System.out.println("Current balance "+bankName+" : "+balance);
				}
				temp.remove(0);
//				System.out.println(obj.bankMessages+" "+bankName+obj.customerMessages);
			}
//		System.out.println("Done "+bankName+" "+balance);
		}
		System.out.println("\n"+bankName+" has "+(int)balance+" dollar(s) remaining !");
	}
}
