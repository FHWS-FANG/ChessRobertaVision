package server;

import java.io.IOException;


/* 
 * Class to use the RobertaServer class.
 * 
 * First the port numbers and server names must be specified. Names are 3 letters.
 * 	use setServerName and setServerPort for this
 * e.g. progServerMaster.setServerPort(1, 30002);  
 * 		progServerMaster.setServerName(0, "rob");
 * 
 * then the class is executed by calling hasReceivedString()
 * e.g. progServerMaster.hasReceivedString();
 * 
 * the program will receive messages from all open servers and send them with 
 * an @ and 3 letters @xxx to the server xxx. If the message is not ended like this it will be send 
 * to all servers.   
 *  
 */

public class ProgramServerMaster {

	// string to stop the server
	private static String terminateServerString = "kill";
	public void setTeminateServerString(String msg) {
		terminateServerString = msg;
	}
	
	// create a boolean to see if the program should be terminated
	private static boolean terminated = false;
	private void setTerminated (boolean terminat) {
		terminated = terminat;
	}
	public boolean getTerminated() {
		return terminated;
	}
	
	// test if the server should get terminated
	private void testTerminate(String messageString) {
		if (messageString.contentEquals(terminateServerString)) {
			try {
				setTerminated(true);
				closeServerMaster();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// get the program to work
	public void hasReceivedString() {
		receiveString();
	}
	
	// test if something can be received and get it to send
	private void receiveString () {
		// test if server01 has a string received and get it to send if the server is running
		if(serverPort[0] != 0 && myServer01.hasReceivedString()) {
			setSendString(myServer01.getReceivedString());
		}
		
		if(serverPort[1] != 0 && myServer02.hasReceivedString()) {
			setSendString(myServer02.getReceivedString());
		}
		
		if(serverPort[2] != 0 && myServer03.hasReceivedString()) {
			setSendString(myServer03.getReceivedString());
		}
		
		if(serverPort[3] != 0 && myServer04.hasReceivedString()) {
			setSendString(myServer04.getReceivedString());
		}
		
		if(serverPort[4] != 0 && myServer05.hasReceivedString()) {
			setSendString(myServer05.getReceivedString());
		}
	} 
	
	// send the received message
	private void setSendString(String msg) {
		String msgString;
		String msgAddress;
		
		// check if the @ is on last position -4 if not set address to ""
		if ((msg.length() > 3) && (msg.indexOf("@", (msg.length()-4)) == (msg.length()-4))) {
			msgString = msg.substring(0, msg.length()-4);
			msgAddress = msg.substring(msg.length()-3, msg.length());
		} else {
			msgString = msg;
			msgAddress = "";
		}
		
		// if there is a server port and the address is "" or the right name send is
		if (serverPort[0] != 0 && (msgAddress.contentEquals("") || (msgAddress.contentEquals(serverName[0])))) {
			myServer01.sendString(msgString);
		}
		if (serverPort[1] != 0 && (msgAddress.contentEquals("") || (msgAddress.contentEquals(serverName[1])))) {
			myServer02.sendString(msgString);
		}
		if (serverPort[2] != 0 && (msgAddress.contentEquals("") || (msgAddress.contentEquals(serverName[2])))) {
			myServer03.sendString(msgString);
		}
		if (serverPort[3] != 0 && (msgAddress.contentEquals("") || (msgAddress.contentEquals(serverName[3])))) {
			myServer04.sendString(msgString);
		}
		if (serverPort[4] != 0 && (msgAddress.contentEquals("") || (msgAddress.contentEquals(serverName[4])))) {
			myServer05.sendString(msgString);
		}
		
		// test if terminate string is send 
		if (msgAddress.contentEquals("") || msgString.contentEquals(terminateServerString)) {
			testTerminate(msgString);
		}
	}
	
	// initiate  the servers
	private RobertaServer myServer01 = new RobertaServer();
	private RobertaServer myServer02 = new RobertaServer();
	private RobertaServer myServer03 = new RobertaServer();
	private RobertaServer myServer04 = new RobertaServer();
	private RobertaServer myServer05 = new RobertaServer();
	private int[] serverPort = new int[5];
	private String[] serverName = new String[5];
	
	// get the portlist
	public void setServerPort(int listPos, int portNo) {
		serverPort[listPos] = portNo;
	}

	// get the servernames
	public void setServerName(int listPos, String portName) {
		serverName[listPos] = portName;
	}
		
	// start the servers
	public void startServerMaster () {
		System.out.println("The master server started!");
		
		for(int i = 0; i < serverPort.length; i++) {
			// start the servers if a port is given
			if(serverPort[i] != 0) {
				switch(i) {
					case(0):
						myServer01.setPort(serverPort[i]);
						myServer01.start();
						break;
					case(1):
						myServer02.setPort(serverPort[i]);
						myServer02.start();
						break;
					case(2):
						myServer03.setPort(serverPort[i]);
						myServer03.start();
						break;
					case(3):
						myServer04.setPort(serverPort[i]);
						myServer04.start();
						break;
					case(4):
						myServer05.setPort(serverPort[i]);
						myServer05.start();
						break;
				}
			} 
		}
	}
	
	// stop the servers
	public void closeServerMaster() throws IOException {
		for(int i = 0; i < serverPort.length; i++) {
			// start the servers if a port is given
			if(serverPort[i] != 0) {
				switch(i) {
					case(0):
						myServer01.stop();
						break;
					case(1):
						myServer02.stop();
						break;
					case(2):
						myServer03.stop();
						break;
					case(3):
						myServer04.stop();
						break;
					case(4):
						myServer05.stop();
						break;
				}
			}
		}
		System.out.println("Goodbye");	
	}	
}