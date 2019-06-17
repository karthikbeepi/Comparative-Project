package CompleteReShuffle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank implements Runnable {

	String bankName;
	Integer balance;
	LinkedBlockingQueue<String> q;
	ArrayList<String> customersDone = new ArrayList<>();
	ArrayList<String> customers = new ArrayList<>();
	
	public Bank(String s, Integer integer, LinkedBlockingQueue<String> queue, ArrayList<String> customersDone, Set<String> set) {
		bankName = s;
		balance = integer;
		q = queue;
		this.customersDone = customersDone;
		customers = new ArrayList<>();
		for(String temp : set)
			customers.add(temp);
	}

	@Override
	public synchronized void run() {
		Random rand = new Random();
		while(true)
		{
			if(customersDone.size()==customers.size())
				break;
			String result[];
			if(q.peek()==null)
			{
				try {
					wait(new Random().nextInt(100)+10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
				result = q.peek().split(" ");
				while(result[0].compareToIgnoreCase("request")!=0||result[1].compareToIgnoreCase(bankName)!=0)
				{
					try {
						wait(new Random().nextInt(100)+10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				q.poll();
				if(Integer.parseInt(result[3])<=balance)
				{
					q.add("response "+result[2]+" YES");
					balance-=Integer.parseInt(result[3]);
					System.out.println(bankName+" approves a loan of "+Integer.parseInt(result[3])+" dollar(s) from "+result[2]);
					
				}
				else
				{
					q.add("response "+result[2]+" NO");
					System.out.println(bankName+" denies a loan of "+Integer.parseInt(result[3])+" dollar(s) from "+result[2]);
					
				}
			}
	}

}
