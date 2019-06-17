package UDP;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Customer implements Runnable {

	String customerName;
	Integer loanAmt;
	ArrayList<String> bankNames;
	ArrayList<String> banksDelisted = new ArrayList<>();
	ArrayList<String> customersDone;
	
	public Customer(String s, Integer integer, LinkedBlockingQueue<String> queue, Set<String> set) {
		customerName = s;
		loanAmt = integer;
		bankNames = new ArrayList<>();
		for(String temp : set)
			bankNames.add(temp);
	}

	@Override
	public synchronized void run() {
		
	}

}
