package application.blocks;
import java.io.*;

import java.net.*;
import java.util.ArrayList;

/* 
 * Client to send and receive strings to a server
 * 
 * start: use the start() method
 * 
 * set the ServerIP: use setHostname("IP")
 * set the port: setPort()
 * send: use the method setSendString("String to send!")
 * 
 * receive: test if something was received use the method hasReceivedString()
 *            read the message with getReceivedString()
 * 
 * end: use the stop() method
 */

public class Client {

	// set port and IP for the server
	private String hostname = "localhost"; // "192.168.70.12"; // 
	private int port = 30001;
	
	public void setHostname (String sHost) {
		hostname = sHost;
	}
	
	public void setPort(int sPort) {
		port = sPort;
	}
	
	private InetSocketAddress address;
	
	private void createAddress() {
		address = new InetSocketAddress(hostname, port);
	}

	// create a list for the received strings
	private ArrayList<String> receivedList= new ArrayList<String>();

	// get the received message and delete it
	public String getReceivedString() {
		String temp = receivedList.get(0); 
		receivedList.remove(0);
		return temp;
	}
	
	// check if something came in
	public boolean hasReceivedString() {
		receiveString();
		if (receivedList.size() > 0) { 
			return true;
		}
		else {
			return false;
		}
	}

	// variables for the connection
	private Socket socket;
	private PrintWriter pw;
	private BufferedReader bufReader;
	private InputStreamReader iStreamReader;
	
	// start the server
	public void start() {
		System.out.println("Client: I start myself!");
		startConnection();
	}
	
	// creating a connection
	private void startConnection() {
		System.out.println("Client: creating connection on address " + hostname + " and port " + port + "!");
		
		// create an IP Address 
		createAddress();
		
		// connect
		socket = new Socket();
		try {
			socket.connect(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client: I got a connection!");
		
		// create buffer and reader
		try {
			iStreamReader = new InputStreamReader(socket.getInputStream());
			bufReader = new BufferedReader(iStreamReader);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	// send a message
	public void sendString(String msg){
		pw.println(msg);
		pw.flush();
	}
	
	// receive a message
	private void receiveString() {
		try {	
			if(bufReader.ready()) {
				String message = bufReader.readLine();
				receivedList.add(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// stop the client
	public void stop() throws IOException {
		pw.close();
		bufReader.close();
		socket.close();
		System.out.println("Client has stopped");
	}
}

