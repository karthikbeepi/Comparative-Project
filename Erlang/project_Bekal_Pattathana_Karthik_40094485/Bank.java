
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.InetAddress;
import java.net.SocketException;

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
		try {
			//wait(new Random().nextInt(100));
			Thread.sleep(new Random().nextInt(100)+10);
			} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		synchronized (System.out) {
			sendToMaster("\n"+bankName+" has "+balance+" dollar(s) remaining !");
//		}
		
	}
	
	public String sendToMaster(String str) {
		DatagramSocket aSocket = null; 	
		String s= "";
		try{
			aSocket = new DatagramSocket(); 
			byte [] message = str.getBytes(); 
			InetAddress aHost = InetAddress.getByName("localhost"); 
			int serverPort = 6000;
			DatagramPacket request = new DatagramPacket(message, str.length(), aHost, serverPort);//request packet read
			
			aSocket.send(request);
		}
		catch(SocketException e){
			// System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			// System.out.println("IO: "+e.getMessage());
		}
		finally{
			if(aSocket != null) aSocket.close();
		}
		return s.toString();
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
						request.getPort());
				
				aSocket.send(reply);
				
			}
		} catch (SocketException e) {
			// System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			// System.out.println("IO: " + e.getMessage());
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
				sendToMaster(bankName+" approves the loan of "+reqAmt+" from "+sp[1]);
				return "YES";
			}
			else
			{
				sendToMaster(bankName+" denies the loan of "+reqAmt+" from "+sp[1]);
				return "NO";
			}
				
		}
		
		return null;
	}
}