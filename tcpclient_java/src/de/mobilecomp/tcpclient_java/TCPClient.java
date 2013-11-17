
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
 * @author -
 * connects to a tcp server according to port and address
 * sends a message
 * closes connection
 * optionally can listen to messages from the server - feature not used yet
 */
public class TCPClient {

	private String serverMessage;
	//Default settings: to be changed later
	private static String SERVERIP = "127.0.0.1"; //your computer IP address
	private static int SERVERPORT = 8080;
	private OnMessageReceived mMessageListener = null;
	public boolean mRun = false;

	PrintWriter out;
	BufferedReader in;

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
		System.out.println("TCPClient: Sending message: "+ message);
		if (out != null && !out.checkError()) {
			out.println(message);
			out.flush();
		}
	}

	/**
	 * stops connection by setting mRun - flag to false
	 */
	public void stopClient(){
		System.out.println("TCPClient: Stopping client");
		mRun = false;
	}

	/**
	 * sets mRun - flag to true
	 * establishes connection to server
	 * 
	 */
	public void run() {

		mRun = true;

		try {
			InetAddress serverAddr = InetAddress.getByName(getSERVERIP());

			System.out.println("TCP Client: C: Connecting...");
			System.out.println("TCP Client: Port: "+getSERVERPORT()+" IP: "+getSERVERIP());

			//create a socket to make the connection with the server
			Socket socket = new Socket(serverAddr, getSERVERPORT());

			System.out.println("TCPClient "+socket.getPort()+" "+socket.getInetAddress());
			try {
				//send the message to the server
				System.out.println("TCPClient: Socket Outputstream: "+socket.getOutputStream());
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

				sendMessage("Hallo Server");
				
				System.out.println("TCP Client C: Sent.");

				System.out.println("TCP Client C: Done.");
								
				//receive the message which the server sends back
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				//in this while the client listens for the messages sent by the server
				while (mRun) {
					serverMessage = in.readLine();

					if (serverMessage != null && mMessageListener != null) {
						//call the method messageReceived from MyActivity class
						mMessageListener.messageReceived(serverMessage);
					}
					serverMessage = null;
					//mRun = false;
				}

				System.out.println("RESPONSE FROM SERVER S: Received Message: '" + serverMessage + "'");

			} catch (Exception e) {

				System.err.println("TCP S: Error "+ e);
			} finally {
				//the socket must be closed. It is not possible to reconnect to this socket
				// after it is closed, which means a new socket instance has to be created.
				socket.close();
				System.out.println("TCPClient Socket closed");
			}
		} catch (Exception e) {

			System.out.println("TCP C: Error "+e);

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
		System.out.println("TCPClient serverPort "+serverPort+" this.Port "+this.SERVERPORT);
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
	 * 
	 * @author -
	 * handler if server should send a response
	 * not used yet
	 */
	//class at on asynckTask doInBackground
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
}