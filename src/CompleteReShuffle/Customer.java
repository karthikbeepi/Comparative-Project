package CompleteReShuffle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Customer implements Runnable {

	String customerName;
	Integer loanAmt;
	LinkedBlockingQueue<String> q;
	ArrayList<String> bankNames;
	ArrayList<String> banksDelisted = new ArrayList<>();
	ArrayList<String> customersDone;
	
	public Customer(String s, Integer integer, LinkedBlockingQueue<String> queue, Set<String> set, ArrayList<String> customersDone) {
		customerName = s;
		loanAmt = integer;
		q = queue;
		bankNames = new ArrayList<>();
		for(String temp : set)
			bankNames.add(temp);
		this.customersDone = customersDone;
	}

	@Override
	public synchronized void run() {
		try {
			wait(new Random().nextInt(100)+100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(loanAmt>0 && banksDelisted.size()<bankNames.size())
		{
			Random rand = new Random();
			String bankSelected = bankNames.get(rand.nextInt(bankNames.size()));
			while(banksDelisted.contains(bankSelected))
			{
				bankSelected = bankNames.get(rand.nextInt(bankNames.size()));
			}
			int loanReq = rand.nextInt(50)+1;
			String result[] ;
			synchronized (q) {
				if(!q.isEmpty())
					continue;
				q.add("request "+bankSelected+" "+customerName+" "+loanReq);
				System.out.println(customerName+" requests a loan of "+loanReq+" dollar(s) from "+bankSelected);
				
				try {
					wait(new Random().nextInt(100)+10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				result = q.peek().split(" ");
				while(result[0].compareToIgnoreCase("response")!=0||result[1].compareToIgnoreCase(customerName)!=0)
				{
					try {
						wait(new Random().nextInt(100)+10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					result = q.peek().split(" ");
				}
				q.poll();
			}
			
			if(result[2].compareToIgnoreCase("YES")==0)
			{
				loanAmt -= loanReq;
				continue;
			}
			else
			{
				banksDelisted.add(bankSelected);
			}
		}
		
		synchronized (customersDone) {
			customersDone.add(customerName);
		}
		
	}

}
