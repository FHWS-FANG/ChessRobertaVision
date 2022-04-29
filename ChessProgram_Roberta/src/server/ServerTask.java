package server;

import java.util.concurrent.TimeUnit;
import com.kuka.roboticsAPI.applicationModel.tasks.CycleBehavior;
import com.kuka.roboticsAPI.applicationModel.tasks.RoboticsAPICyclicBackgroundTask;


/**
 * Implementation of a cyclic background task.
 * <p>
 * It provides the {@link RoboticsAPICyclicBackgroundTask#runCyclic} method 
 * which will be called cyclically with the specified period.<br>
 * Cycle period and initial delay can be set by calling 
 * {@link RoboticsAPICyclicBackgroundTask#initializeCyclic} method in the 
 * {@link RoboticsAPIBackgroundTask#initialize()} method of the inheriting 
 * class.<br>
 * The cyclic background task can be terminated via 
 * {@link RoboticsAPICyclicBackgroundTask#getCyclicFuture()#cancel()} method or 
 * stopping of the task.
 * @see UseRoboticsAPIContext
 * 
 */

/* 
 * Program to use the Server class by using ProgrammServerMaster with up to 5 different clients
 * 
 * You need to give a port number and a name to the servers and they will start to work
 * 		e.g. progServerMaster.setServerPort(0, 30001); 
 * 			 progServerMaster.setServerName(0, "rob");
 * 
 * The program will end, if a client sends the terminalServerString
 */
public class ServerTask extends RoboticsAPICyclicBackgroundTask {
	
	@Override
	public void initialize() {
		// initialize your task here
		initializeCyclic(0, 10000, TimeUnit.MILLISECONDS,
				CycleBehavior.BestEffort);
	}

	// string to stop the server and the clients
	static String terminateServerString = "kill";

	// variables to set the program running and check if the program is running
	private static boolean progRunning;
	private static void setProgRunning(boolean running) {
		progRunning = running;
	}
	private static boolean getProgRunning() {
		return progRunning;
	}

	@Override
	public void runCyclic() {
		// start the ServerMaster which controls the servers for the clients
		ProgramServerMaster progServerMaster = new ProgramServerMaster();
		
		// ports (remove third port and name if HandEyeCalibration program is used)
		progServerMaster.setServerPort(0, 30001);
		progServerMaster.setServerPort(1, 30002);
		progServerMaster.setServerPort(2, 30003);
		
		// names
		progServerMaster.setServerName(0, "rob");
		progServerMaster.setServerName(1, "cas");
		progServerMaster.setServerName(2, "pyt");
		
		// set terminate string
		progServerMaster.setTeminateServerString("kill");
		
		// start the server
		progServerMaster.startServerMaster();

		// repeat receiving
		setProgRunning(true);

		while(getProgRunning()) {

			// look up if something is received and send it
			progServerMaster.hasReceivedString();
			
			// see if everything is to be terminated
			if (progServerMaster.getTerminated()) {
				setProgRunning(false);
				break;
			}
		}
	}
}