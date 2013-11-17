/**
 * 
 */
package de.mobilecomp.tcpclient_java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.*;

/**
 * @author - Stefan Stretz, Pascal Lüders
 *
 */
public class MainActivity {
	/**
	 * @param args
	 */
	public static String addressText;
	public static int port;
	public static String messageText;
	static TCPClient mTCPClient;
	public static boolean propertiesSet = false;

	/**
	 * standard Main Method to read User input from Console and then start a TCPClient in a Thread
	 * @param args
	 */
	public static void main(String[] args) {
		/***
		 * Creation of TCPClient started in a Thread
		 */
		Thread t = new Thread() {
			public void run(){
				try {
					mTCPClient = new TCPClient(port, addressText);
					Thread.sleep(1000);
					if(mTCPClient!=null) {
						mTCPClient.run();
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
			String messageText = input3.readLine();
			System.out.println("Nachricht: "+messageText+" ist gespeichert");
			if(messageText=="exit") {
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		//TODO: check for port
		if((addressText.length()> 0)) {
			t.run();
			System.out.println("mTCPClient "+mTCPClient);
		}

		return;
	}
}