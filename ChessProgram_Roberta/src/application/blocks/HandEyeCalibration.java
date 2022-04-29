package application.blocks;

import javax.inject.Inject;

import com.kuka.appframework.ApplicationBlock;
import com.kuka.grippertoolbox.gripper.zimmer.ZimmerR840;
import com.kuka.roboticsAPI.deviceModel.LBR;


public class HandEyeCalibration extends ApplicationBlock<HandEyeCalibration.Result>
{
	public enum Result
	{
		OK
	}

	@Inject 
	LBR Roberta;
	@Inject
	ZimmerR840 ZiGripper;
	
	@Override
	public Result run() throws Exception
	{
		// create an instance of the server
		Client myClient = new Client();

		//myClient.setHostname("192.168.70.12");
		myClient.setHostname("localhost"); 
		myClient.setPort(30001);

		// start client
		myClient.start();
		
    	System.out.println("Welcome to the Hand-Eye-Calibration for the chess program!");
    	
    	// Attach gripper to Roberta and open it
    	ZiGripper.attachTo(Roberta.getFlange());
		
		ZiGripper.attachTo(Roberta.getFlange());
		ZiGripper.initialize();

		boolean progRunning = true;
		
		while (progRunning) {
			//receive string from Halcon
			String message = "";
			boolean answer = false;
			while (answer == false){	
				Thread.sleep(50);
				if (myClient.hasReceivedString()){
					message = myClient.getReceivedString();
					answer = true;
				}
			}
			//if the pose is requested send the coordinates to Halcon
			System.out.println(message);
			if (message.equals("requestPose")){
				System.out.println("sending Pose");
				
				//get the x,y,z coordinates of the current TCP position
				double ZgTCPx = Roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getX();
				double ZgTCPy = Roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getY();
				double ZgTCPz = Roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getZ();
				
				//send the 3 coordinates seperated with a "/" to Halcon
				myClient.sendString(String.valueOf(ZgTCPx) + "/" + String.valueOf(ZgTCPy) + "/" + String.valueOf(ZgTCPz) + "@cas");
			}
			
			//close the connection if Halcon sends the string "end"
			if (message.equals("end")){
				progRunning = false;
			}
		}
		//stop the client
		myClient.stop();

		return Result.OK;
	}
}
