package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
	int udpPort;
	
	public Bank(String s, Integer integer, Set<String> set, int i) {
		bankName = s;
		balance = integer;
		customers = new ArrayList<>();
		for(String temp : set)
			customers.add(temp);
		udpPort= i+7570;
	}

	@Override
	public void run() {
		
		startUDPConnectionDual();
		synchronized (System.out) {
			System.out.println("\n"+bankName+" has "+balance+" dollar(s) remaining !");
		}
		
	}

	void startUDPConnectionDual() {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(udpPort);
			byte[] buffer = new byte[1000];		
			while (customersDone.size()!=customers.size()) {
				
				buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				
				aSocket.receive(request);
				
				String req = new String(request.getData());
				
				String replyStr = replyGenerator(req.trim());
				
				byte[] rep = new byte[1000];
				rep = replyStr.getBytes();
				DatagramPacket reply = new DatagramPacket(rep, replyStr.length(), request.getAddress(),
						request.getPort());// reply packet ready
				
				aSocket.send(reply);// reply sent
				
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

	private String replyGenerator(String s) {
		
		String sp[]= s.split(" ");
		
		if(sp[0].compareToIgnoreCase("DONE")==0)
		{
			customersDone.add(sp[1]);
			return "Updated customers done "+bankName;
		}
		
		else if(sp[0].compareToIgnoreCase("request")==0)
		{
			int reqAmt = Integer.parseInt(sp[2]);
			if(reqAmt<=balance)
			{
				balance-=reqAmt;
				System.out.println(bankName+" approves the loan of "+reqAmt+" from "+sp[1]);
				return "YES";
			}
			else
			{
				System.out.println(bankName+" denies the loan of "+reqAmt+" from "+sp[1]);
				return "NO";
			}
				
		}
		
		return null;
	}
}
