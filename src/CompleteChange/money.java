package CompleteChange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

class Pair{
	String name;
	int value;
	public Pair(String n, int val) {
		name = n;
		value = val;
	}
}

class ReadFromFile {

	BufferedReader br;
	String type;
	
	public ReadFromFile(String filePath, String type) {
	
		try {
			
			br = new BufferedReader(new FileReader(filePath));
			
			this.type = type;
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		
	}

	public Pair next() {
		
		try {
			String str = br.readLine();
			if(str==null)
				return null;
			int posComma = str.indexOf(",");
			return new Pair(str.substring(1, posComma), Integer.parseInt(str.substring(posComma+1, str.length()-2)));
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		return null;
	}	
}

public class money {
	HashMap<String , Integer> banks = new HashMap<>();
	HashMap<String, Integer> customers = new HashMap<>();
	ArrayList<String> customersDone = new ArrayList<>();
	
	
	Thread[] bankThreads;
	Thread[] customerThreads;
	
	void startUDPConnectionDual() {
		DatagramSocket aSocket = null;
		int bankCount =0;
		try {
			aSocket = new DatagramSocket(6000);
			byte[] buffer = new byte[1000];		
			while (bankCount<banks.size()) {
				
				buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				
				aSocket.receive(request);
				
				String req = new String(request.getData());
				
				if(!req.trim().contains(" remaining"))
					System.out.println(req);
				else
				{
					System.out.println(req);
					bankCount++;
				}
				if(bankCount==banks.size())
					break;
				
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
	
	public money() {
		
		File testFile = new File("");
		String currentPath = testFile.getAbsolutePath();
		// System.out.println("current path is: " + currentPath);
		ReadFromFile readCustomer;
		ReadFromFile readBank;
		// if(currentPath.contains("src/CompleteChange/"))
		// {
			readCustomer = new ReadFromFile("customers.txt", "customers");
			readBank = new ReadFromFile("banks.txt", "banks");
		// }
		// else
		// {
			// readCustomer = new ReadFromFile("./src/CompleteChange/customers.txt", "customers");
			// readBank = new ReadFromFile("./src/CompleteChange/banks.txt", "banks");	
		// }
		
//		readCustomer = new ReadFromFile("./UDP/customers.txt", "customers");
//		readBank = new ReadFromFile("./UDP/banks.txt", "banks");	
//	
		Pair temp;
		System.out.println("*** Customers and loan objectives ***");
		while(true)
		{
			temp =  readCustomer.next();
			if(temp==null)
				break;
			customers.put(temp.name, temp.value);
			System.out.println(temp.name+": "+(int)temp.value);
			
		}
		
		System.out.println("\n*** Banks and financial resources ***");
		while(true)
		{
			temp =  readBank.next();
			if(temp==null)
				break;
			banks.put(temp.name, temp.value);
			System.out.println(temp.name+": "+(int)temp.value);
		}
		
		System.out.println();
	}
	
	public static void main(String[] args) {
		
		money obj = new money();
		Runnable masterReceiver = () -> {
			obj.startUDPConnectionDual();
		};
		Thread t = new Thread(masterReceiver);
		t.start();
		obj.runBanks();
		obj.runCustomers();
		
	}

private void runCustomers() {
		
		customerThreads = new Thread[customers.size()];
		int i=0;
		for(String s: customers.keySet())
		{
			customerThreads[i] = new Thread(new Customer(s, customers.get(s), banks.keySet()));
			customerThreads[i].start();
			i++;
		}
	}

	private void runBanks() {
		
		bankThreads = new Thread[banks.size()];
		int i=0;
		for(String s: banks.keySet())
		{
			bankThreads[i] = new Thread(new Bank(s, banks.get(s), customers.keySet(), i));
			bankThreads[i].start();
			i++;
		}
		
	}
	
}

