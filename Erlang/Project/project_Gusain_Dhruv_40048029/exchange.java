package project.copy;

 
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

	public static int count;
	public static int numberOfCallsMade;
	public static String nameForThread, threadHead;
	public static ArrayList<String> listOfThreads = new ArrayList<String>();
	public static BlockingQueue<String> messageQueue;
	public static ArrayList<String> checkForDuplicates = new ArrayList<String>();
	public static LinkedHashMap<String, ArrayList<String>> storeDataForProcessing = new LinkedHashMap<String, ArrayList<String>>();

	public static void main(String[] args) {

		// read data that will act as input
		readDataFromFile();

		displayCallsToBeMade();

		messageQueue = new ArrayBlockingQueue<String>(numberOfCallsMade);
		// creating and executing threads
		intiateThreadExecution();
	}

	public void run() {

		synchronized (messageQueue) {

			for (int i = 0; i <= listOfThreads.size() - 1; i++) {

				if (Thread.currentThread().getName().equals(listOfThreads.get(i))) {
 					ArrayList<String> dataToBeSent = storeDataForProcessing.get(listOfThreads.get(i));
					try {

						for (String data : dataToBeSent) {

 							checkForDuplicates.add(data + "," + listOfThreads.get(i));

							messageQueue.put(data + "," + listOfThreads.get(i));
							Thread.sleep(100);
							printIntro(data, listOfThreads.get(i), System.currentTimeMillis());
							// }
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			for (int j = 0; j <= listOfThreads.size() - 1; j++) {

				if (!messageQueue.isEmpty() && messageQueue.peek().startsWith(listOfThreads.get(j).toString())) {
					try {
						if (messageQueue.peek() == null)
							endScenario();
						String head = messageQueue.take();
						String[] splitHead = head.split(",");
						displayMessageRecieved(splitHead[1], listOfThreads.get(j), System.currentTimeMillis());
						Thread.sleep(100);
						count = count + 1;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			if (checkForDuplicates.size() == numberOfCallsMade) {

				while (!messageQueue.isEmpty()) {
					for (int p = 0; p <= listOfThreads.size() - 1; p++) {
						try {
							if (messageQueue.peek() == null)
								endScenario();
							String head = messageQueue.take();
							String[] splitHead = head.split(",");
							displayMessageRecieved(splitHead[1], splitHead[0], System.currentTimeMillis());
							Thread.sleep(100);
							count = count + 1;

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}

		}

	}

	private void endScenario() {
 		System.out.println("\n");
		for (String thread : listOfThreads) {
			System.out.println("Process " + thread + " has received no calls for 1 second, ending...\n\n");
		}

		System.out.println("Master has received no replies for 1.5 seconds, ending...");
	}

	private void displayMessageRecieved(String string1, String string2, long l) {
 		System.out.println(string1 + " received reply message from " + string2 + " " + l);
	}

	private void printIntro(String string1, String string2, long l) {
 		System.out.println(string1 + " received intro message from " + string2 + " " + l);
	}

	public static void readDataFromFile() {
		BufferedReader input;
		try {

			input = new BufferedReader(new FileReader("calls.txt"));
			String line;
			String[] keys, spliValues, valz;

			while ((line = input.readLine()) != null) {

				keys = line.split(",");
				spliValues = line.split("\\[");
				valz = spliValues[1].split("\\]");
				ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(valz[0].split(",")));
				storeDataForProcessing.put(keys[0].substring(1, keys[0].length()), keyList);
				numberOfCallsMade = numberOfCallsMade + keyList.size();
			}

			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void displayCallsToBeMade() {
		System.out.println("** Calls to be made **");

		for (Entry<String, ArrayList<String>> mapEntry : storeDataForProcessing.entrySet()) {
			System.out.println(mapEntry.getKey() + ": " + mapEntry.getValue());
		}

	}

	public static void intiateThreadExecution() {
		for (Entry<String, ArrayList<String>> maploop : storeDataForProcessing.entrySet()) {
			nameForThread = threadHead = maploop.getKey();
			listOfThreads.add(exchange.threadHead);
			Thread threadStart = new exchange();
			threadStart.start();
			threadStart.setName(nameForThread);
			
		}
	}
}