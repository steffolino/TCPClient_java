/**
 * 
 */
package de.mobilecomp.tcpclient_java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author - Stefan Stretz, Pascal Lueders, Elen Schicker, Michael Suenkel
 *
 */
public class MainActivity {
	/**
	 * @param args
	 */
	public static String addressText;
	public static int port;
	public static String message;
	static Thread connectT;
	static Thread sendingT;
	static TCPClient mTCPClient;

	/**
	 * standard Main Method to read User input from Console and then start a TCPClient in a Thread
	 * @param args
	 */
	public static void main(String[] args) {
		/***
		 * Creation of TCPClient started in a Thread
		 */
		connectT = new Thread() {
			public void run(){
				try {
					mTCPClient = new TCPClient(port, addressText);
					mTCPClient.run();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		sendingT = new Thread() {
			public void run(){
				try {
					if(mTCPClient != null) {
						mTCPClient.sendMessage(message);
						System.out.println(message);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		/**
		 * Read User Input
		 */
		BufferedReader input1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Eingabe fuer Server Addresse:");
		try {
			addressText = input1.readLine();
			System.out.println("Addresse: "+addressText+" ist gespeichert");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Read User Input
		 */
		BufferedReader input2 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Eingabe fuer Server Port:");
		try {
			String portText = input2.readLine();
			port = Integer.parseInt(portText);
			System.out.println("Port: "+port+" ist gespeichert");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Read User Input
		 */
		BufferedReader input3 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Eingabe fuer die Nachricht:");
		try {
			message = input3.readLine();
			System.out.println("Nachricht: "+message+" ist gespeichert");
			if(message=="exit") {
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			connectT.run();
			connectT.join();
			System.out.println("MainActivity: created TCPClient");			

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("entered sending msgs");
			if(mTCPClient != null) {
				for(int i = 0; i< 10; i++) {
					sendingT.run();
					Thread.sleep(1000);
				}
			}
		} catch (Error e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(mTCPClient!=null) {
			mTCPClient.close();
		}
		return;
	}
}