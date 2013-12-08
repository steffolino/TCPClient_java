
/*
 * cf. http://myandroidsolutions.blogspot.de/2012/07/android-tcp-connection-tutorial.html
 */

package de.mobilecomp.tcpclient_java;


//import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 
 * @author - Stefan Stretz, Pascal Lueders, Elen Schicker, Michael Suenkel
 * connects to a tcp server according to port and address
 * sends a message
 * closes connection
 * optionally can listen to messages from the server - feature not used yet
 */
public class TCPClient {

	//Default settings: to be changed later
	private String serverMessage;
	private static String SERVERIP = "127.0.0.1"; //your computer IP address
	private static int SERVERPORT = 8080;

	PrintWriter out;
	BufferedReader in;
	Socket socket;

	/**
	 *  Constructor of the class
	 *  TCPClient needs Server-Port and address to be initialized
	 */
	public TCPClient(int port, String address) {
		setSERVERIP(address);
		setSERVERPORT(port);
		System.out.println("TCPClient created");
	}

	/**
	 * Sends the message entered by client to the server
	 * @param message text entered by client
	 * writes message to PrintWriter out
	 */
	public void sendMessage(String message){
		System.out.println("TCPClient Sending message: "+ message);
		if (out != null && !out.checkError()) {
			out.println(message);
			out.flush();
		}
	}

	/**
	 * establishes connection to server
	 * 
	 */
	public void run() {

		try {
			InetAddress serverAddr = InetAddress.getByName(getSERVERIP());

			System.out.println("TCP Client C: Connecting...");
			System.out.println("TCP Client Port: "+getSERVERPORT()+" IP: "+getSERVERIP());

			//create a socket to make the connection with the server
			this.socket = new Socket(serverAddr, getSERVERPORT());
			System.out.println("TCPClient "+socket.getPort()+" "+socket.getInetAddress());

			try {

				//send the message to the server
				System.out.println("TCPClient Socket Outputstream: "+socket.getOutputStream());
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

				//receive the message which the server sends back
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				//in this while the client listens for the messages sent by the server
				serverMessage = in.readLine();

				if (serverMessage != null) {
					//call the method messageReceived from MyActivity class
					System.out.println("RESPONSE FROM SERVER S: Received Message: '" + serverMessage + "'");
				}
				serverMessage = null;

			} catch (Exception e) {
				System.out.println("TCP S: Error"+e);
			}

		} catch (Exception e) {

			System.out.println("TCP C: Error"+ e);

		}

	}

	/**
	 * standard getter for port
	 * @return
	 */
	public int getSERVERPORT() {
		return this.SERVERPORT;
	}

	/**
	 * standard setter for port
	 */
	public void setSERVERPORT(int serverPort) {
		this.SERVERPORT = serverPort;
		System.out.println("TCPClient serverPort "+serverPort+"this.Port"+this.SERVERPORT);
	}

	/**
	 * standard getter for address / ip
	 * @return
	 */
	public String getSERVERIP() {
		return this.SERVERIP;
	}

	/**
	 * standard getter for address / ip
	 */
	public void setSERVERIP(String serverIP) {
		this.SERVERIP = serverIP;
	}

	/**
	 * close socket and leave
	 */
	public void close(){	
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("TCPClient Socket closed");
	}
}