package CompleteChange;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Customer implements Runnable {

	String customerName;
	Integer intialLoanAmount;
	Integer loanAmt;
	ArrayList<String> bankNames;
	ArrayList<String> banksDelisted = new ArrayList<>();
	ArrayList<String> customersDone;
	
	public Customer(String s, Integer integer,  Set<String> set) {
		customerName = s;
		loanAmt = integer;
		intialLoanAmount = integer;
		bankNames = new ArrayList<>();
		for(String temp : set)
			bankNames.add(temp);
	}

	@Override
	public void run() {
		
		while(loanAmt>0&&banksDelisted.size()<bankNames.size())
		{
			Random rand = new Random();
			int reqAmt = rand.nextInt(50)+1;
//			reqAmt = 50;
			if(reqAmt>loanAmt)
				reqAmt = loanAmt;
			int bankNo = rand.nextInt(bankNames.size());
			String bankSelected = bankNames.get(bankNo);
			while(banksDelisted.contains(bankSelected))
			{
				bankNo = rand.nextInt(bankNames.size());
				bankSelected = bankNames.get(bankNo);
			}
			System.out.println(customerName+" requests a loan of "+reqAmt+" dollar(s) from "+bankSelected);
			
			String response = interserverComm("request "+customerName+" "+reqAmt, 7570+bankNo);
			try {
//				wait(new Random().nextInt(100));
				Thread.sleep(new Random().nextInt(100)+10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(response.trim().compareToIgnoreCase("NO")==0)
			{
				banksDelisted.add(bankSelected);
				continue;
			}
			else
				loanAmt-=reqAmt;
			try {
//				wait(new Random().nextInt(100));
				Thread.sleep(new Random().nextInt(100)+10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
//			wait(new Random().nextInt(100));
			Thread.sleep(new Random().nextInt(100));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=7570; i<7570+bankNames.size(); i++)
		{
			interserverComm("DONE "+customerName, i);
		}
		synchronized (System.out) {
			if(loanAmt==0)
				System.out.println("\n"+customerName+" has reached the objective of "+intialLoanAmount+" Woo Hoo!");
			else
				System.out.println("\n"+customerName+" was only able to borrow "+(intialLoanAmount-loanAmt)+" Boo Hoo!!! ");
		}
		
	}
	
	public String interserverComm(String str, int port) {
		DatagramSocket aSocket = null; 	
		String s= "";
		try{
			aSocket = new DatagramSocket(); //reference of the original socket
			byte [] message = str.getBytes(); //message to be passed is stored in byte array
			InetAddress aHost = InetAddress.getByName("localhost"); //Host name is specified and the IP address of server host is calculated using DNS. 
			int serverPort = port;//agreed upon port	
			DatagramPacket request = new DatagramPacket(message, str.length(), aHost, serverPort);//request packet read
			
			aSocket.send(request);//request sent out
			
			byte [] buffer = new byte[1000];//to store the received data, it will be populated by what receive method returns
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);//reply packet ready but not populated.
			
			//Client waits until the reply is received-----------------------------------------------------------------------
			aSocket.receive(reply);//reply received and will populate reply packet now.
			String repMSG = new String(reply.getData());

			if(repMSG.charAt(0)!='b')
				s = s.concat(repMSG);
//			System.out.println(s.concat(repMSG));
//			System.out.println(s);
		}
		catch(SocketException e){
			System.out.println("Socket: "+e.getMessage());
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("IO: "+e.getMessage());
		}
		finally{
			if(aSocket != null) aSocket.close();//now all resources used by the socket are returned to the OS, so that there is no
												//resource leakage, therefore, close the socket after it's use is completed to release resources.
		}
		return s.toString();
	}


}
