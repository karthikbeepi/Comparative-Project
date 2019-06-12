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
		
		int times =0;
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
		
		while(true)
		{
			ArrayList<String> temp = obj.bankMessages.get(bankName);
			if(temp.size()==0)
			{
				times++;
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(times==10)
					break;
				continue;
			}
			for(String s: temp)
			{
				String[] sp = s.split(" ");
				Float f = Float.parseFloat(sp[1]);
				if(balance<f)
				{
					obj.customerMessages.put(sp[0], "NO");
				}
				else
				{
					obj.customerMessages.put(sp[0], "YES");
					balance-=f;
				}
			}
			
			temp.clear();
			try {
				wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(balance==0)
			{
				System.out.println("DOne "+bankName);
				break;
			}
		}
	}
	
}
