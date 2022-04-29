package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/* 
 * Server to send and receive strings from a client.
 * 
 * start: use the start() method
 * 
 * set the port: setPort()
 * 
 * send: use the method sendString("String to send!")
 * 
 * receive: test if something was received use the method hasReceivedString()
 *            read the message with getReceivedString()
 * 
 * end: use the stop() method
 */

public class RobertaServer {

	// port for the TCP/IP network
	private static int port = 30001;
	public void setPort(int newPort) {
		port = newPort;
	}
	
	// server variables
	private ServerSocket serverSocket;
	private Socket socket;

	private InputStreamReader iStreamReader;
	private BufferedReader bufReader;
	
	private PrintWriter pw;

	// start the server
	public void start() {
		System.out.println("Server: Hi, I am ready to serve you on port " + port + "!");
		System.out.println("Server: Trying to connect.");
		
		// get a connection
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Server: I got a connection!");

		// create the buffer and stream reader
		try {
			iStreamReader = new InputStreamReader(socket.getInputStream());
			bufReader = new BufferedReader(iStreamReader);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// send the string
	public void sendString(String msg) {
		pw.println(msg);
		pw.flush();	
	}

	// list to store received messages
	private ArrayList<String> receivedList = new ArrayList<String>();
	
	// check if something is received
	public boolean hasReceivedString() {
		receiveString();
		if (receivedList.size() > 0) {
			return true;
		}
		return false;
	}
	
	// get the received string to master
	public String getReceivedString() {
		String recString = receivedList.get(0);
		System.out.println("recString = " + recString);
		receivedList.remove(0);
		return recString;		
	}
	
	// receive the message
	private void receiveString() {
		try {
			if(bufReader.ready()) {
				receivedList.add(bufReader.readLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// stop the server
	public void stop() throws IOException {
		
		pw.close();		
		iStreamReader.close();
		socket.close();		
		serverSocket.close();
		System.out.println("Server has stopped");
	}
}
