package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class exchange extends Thread {

	public static LinkedHashMap<String, ArrayList<String>> dataToProcess = new LinkedHashMap<String, ArrayList<String>>();
	public static String threadName, threadStart;
	public static ArrayList<String> threadList = new ArrayList<String>();
	public static ArrayList<String> listToCheckDuplicateInsert = new ArrayList<String>();
	public static BlockingQueue<String> bqMsg;
	public static int count, countCalls;
	public static boolean timeOutCheck = false;

	public static void main(String[] args) {

		fetchInput();
		printData();
		bqMsg = new ArrayBlockingQueue<String>(countCalls);
		createThreads();

	}

	public void run() {

		synchronized (bqMsg) {

			for (int k = 0; k <= threadList.size() - 1; k++) {
				if (Thread.currentThread().getName().equals(threadList.get(k))) {
					ArrayList<String> MsgToSend = dataToProcess.get(threadList.get(k));
					try {
						for (String st : MsgToSend) {
							listToCheckDuplicateInsert.add(st + "," + threadList.get(k));
							long l = System.currentTimeMillis();
							bqMsg.put(st + "," + threadList.get(k) + "," + l);
							Thread.sleep(100);
							printIntro(st, threadList.get(k), l);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			for (int a = 0; a <= threadList.size() - 1; a++) {
				if (!bqMsg.isEmpty() && bqMsg.peek().startsWith(threadList.get(a).toString())) {
					try {
						String value = bqMsg.take();
						String[] values = value.split(",");
						printReply(values[1], threadList.get(a), values[2]);
						Thread.sleep(100);
						count = count + 1;

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (listToCheckDuplicateInsert.size() == countCalls) {
				while (!bqMsg.isEmpty()) {
					for (int n = 0; n <= threadList.size() - 1; n++) {
						try {
							if (bqMsg.peek() == null) {
								timeOut();
								timeOutCheck = true;
							}
							String value = bqMsg.take();
							String[] values = value.split(",");
							printReply(values[1], values[0], values[2]);
							Thread.sleep(100);
							count = count + 1;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private void printReply(String string1, String string2, String string3) {
		// TODO Auto-generated method stub
		System.out.println(string1 + " received reply message from " + string2 + " " + string3);
	}

	private static void timeOut() {
		if (timeOutCheck == false) {
			System.out.println("\n");
			for (String str : threadList) {
				System.out.println("Process " + str + " has received no calls for 1 second, ending...\n");
			}
			System.out.println("Master has received no replies for 1.5 seconds, ending...");
			timeOutCheck = true;
		}
	}



	private void printIntro(String string1, String string2, long l) {
		System.out.println(string1 + " received intro message from " + string2 + " " + l);
	}

	public static void fetchInput() {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("calls.txt"));
			String currentLine;
			String[] mapKeys, ValuesToSplit, mapValues;
			while ((currentLine = in.readLine()) != null) {

				mapKeys = currentLine.split(",");
				ValuesToSplit = currentLine.split("\\[");
				mapValues = ValuesToSplit[1].split("\\]");
				ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(mapValues[0].split(",")));
				dataToProcess.put(mapKeys[0].substring(1, mapKeys[0].length()), keyList);
				countCalls = countCalls + keyList.size();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printData() {
		System.out.println("** Calls to be made **");
		for (Entry<String, ArrayList<String>> maploop : dataToProcess.entrySet()) {
			System.out.println(maploop.getKey() + ": " + maploop.getValue());
		}

	}

	public static void createThreads() {
		for (Entry<String, ArrayList<String>> maploop : dataToProcess.entrySet()) {
			threadName = threadStart = maploop.getKey();
			threadList.add(exchange.threadStart);
			Thread threadStart = new exchange();
			threadStart.start();
			threadStart.setName(threadName);

		}
	}
}
