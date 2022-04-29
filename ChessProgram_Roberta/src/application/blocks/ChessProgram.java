package application.blocks;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;
import java.io.IOException;
import javax.inject.Inject;
import com.kuka.appframework.ApplicationBlock;
import com.kuka.grippertoolbox.gripper.zimmer.ZimmerR840;
import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.World;


/**
 * Auto-generated ApplicationBlock class.
 */
public class ChessProgram extends ApplicationBlock<ChessProgram.Result>
{
	// To use the sensors of roberta and to create the move command
	@Inject
	private LBR roberta;
 
	// To use the frames of roberta
	@Inject
	private IApplicationData iApp; 
	
	// To grip and release with roberta
	@Inject
	ZimmerR840 ZiGripper;

	public enum Result
	{
		OK
	}

	// string to stop the server
	static private String terminateServerString = "kill";

	// variables for the program
	private static boolean progRunning = false; 
	private static void setProgRunning(boolean running) {
		progRunning = running;
	}
	private static boolean getProgRunning() {
		return progRunning;
	}

	// test if the client should get terminated
	private static boolean testTerminate(String messageString) {
		if (messageString.contentEquals(terminateServerString)) {
			setProgRunning(false);
			return true;
		} else {
			return false;
		}
	}
	
	// calculates the rotation of the piece 
	public static double getRotation(double[] rotXYZ, double angle2D) {
		//Calculate the shortest distance to a multiple of 90
		double[] rotXYZ_distance_to_multiples_of_90 = new double[3];
		for(int j = 0; j < rotXYZ.length; j++)
		{
			if (rotXYZ[j] % 90 > 45)
			{
				rotXYZ_distance_to_multiples_of_90[j] = Math.abs((rotXYZ[j] % 90) - 90);
			}
			else
			{
				rotXYZ_distance_to_multiples_of_90[j] = Math.abs((rotXYZ[j] % 90));
			}
		}
		
		//Get the Index with the highest value in rotXYZ_distance_to_multiples_of_90
		int maxIndex = 0;
		for (int j = 0; j < rotXYZ_distance_to_multiples_of_90.length; j++) {
			maxIndex = rotXYZ_distance_to_multiples_of_90[j] > rotXYZ_distance_to_multiples_of_90[maxIndex] ? j : maxIndex;
		}

		//Two cases: rotation coordinate stays the same and inverting the rotation coordinate
		double rot = rotXYZ[maxIndex];
		double rot_inverted = (360 - rotXYZ[maxIndex]);
		
		//calculate rot modulo 90, because of cube symmetry
		double rot_modulo = rot % 90;
		double rot_inverted_modulo = rot_inverted % 90;
		
		//calculate the rotation coordinate in rad
		double rot_rad =  (rot_modulo / 180) * Math.PI;
		double rot_inverted_rad =  (rot_inverted_modulo / 180) * Math.PI;
		
		//put the definition area of angle2D in [0, 2pi] (for whatever reason in halcon its between ~ -0.05 and ~ 2pi-0.05)
		if (angle2D < 0)
		{
			angle2D = 2*Math.PI + angle2D;
		}
		
		//to grab the piece always from left-right and not bottom-top, we need to add pi/2 to the rotation in two cases of the four quadrants
		//top left: direction = 1 / bottom left: direction = 0 / bottom right: direction = 1 / top right: direction = 0
		double direction = (Math.floor(angle2D / (Math.PI/2)) + 1) % 2;
		
		//use modulo pi/2 so the angle can be compared to the rotation coordinates
		double angle2D_modulo = angle2D % (Math.PI/2);
		
		//check if either the normal or the inverted rotation is closer to Angle2D_modulo and take the closer one for following movements and calculations
		double rot_real;
		if (Math.abs(rot_rad - angle2D_modulo) < Math.abs(rot_inverted_rad - angle2D_modulo))
		{
			rot_real = rot_rad;
		}
		else
		{
			rot_real = rot_inverted_rad;
		}
		
		//in case the rotation is not matching with the 2D angle at all, probably the 3D matching of the cube in halcon was not perfect and the cube was "seen" with a rotation within the chessboard (not only on the chessboard). If this rotation is bigger than the rotation on the chessboard then it will be taken.
		//so if the angles are more than 10° off, take the 2D angle (even if it has a small error)
		if (Math.abs(rot_real - angle2D_modulo) > (10*Math.PI/180))
		{
			rot_real = angle2D_modulo;
		}
		
		//create the complete rotation
		double rot_complete = rot_real + direction*(Math.PI/2);
		
		return rot_complete;
	}
			
	@Override
	public Result run() throws Exception
	{
		System.out.println("The client program started");

		// create an instance of the server
		Client myClient = new Client();

		//myClient.setHostname("192.168.70.12");
		myClient.setHostname("localhost"); 
		myClient.setPort(30001);

		// start client
		myClient.start();
		
		//define the velocities of the robot
		double speed = 150;
		double rel_speed_joint = 0.3;
		
		//define how wide the gripper should open 
		int gripper_opening = 27; 
		
		//define how heigh the piece get moved up when placing on a different field
		double height = 100;
		
		//define a pause after gripping and releasing
		int pause = 1000; 
		
		//get the translation coordinates of the StartPos and save them in a vector
		double[] StartPosVec = {iApp.getFrame("/StartPos").getX(), iApp.getFrame("/StartPos").getY(), iApp.getFrame("/StartPos").getZ()};
				
		//initialize Gripper
		ZiGripper.attachTo(roberta.getFlange());
	    ZiGripper.initialize();
	    ZiGripper.reference();
	    
	    //open Gripper and set width 
	    ZiGripper.gripAsync(ZimmerR840.createAbsoluteMode(gripper_opening));
	    
	  	//define new StartPos using the translation coordinates of the old StartPos and rotation coordinates such that the gripper is directing down and is parallel to the cube
		//AlphaRad is the rotation in the chessboard plane. For AlphaRad = -0.38 the gripper is parallel to the table (the value is the rotation between the flanch and the TCP of the robot and can only be changed if the hardware gets reattached)
		//BetaRad = 0 and GammaRad = -pi ensure that the gripper is directing down
		Frame StartPosNew = iApp.getFrame("/StartPos").copyWithRedundancy().setAlphaRad(Math.PI/2 - 0.38).setBetaRad(0).setGammaRad(-Math.PI);
		
		//ptp move to the new start position
	  	ZiGripper.getFrame("/TCP").move(ptp(StartPosNew).setJointVelocityRel(rel_speed_joint));
	  	
		//while loop for communication with the clients
		setProgRunning(true);
		while(getProgRunning()) {

			//check if something is received
			if(myClient.hasReceivedString()) {
				String receivedString = myClient.getReceivedString();
				System.out.println("The client 1 received: " + receivedString);
				
				// test if client should be stopped
				if (testTerminate(receivedString)) {
					try {
						myClient.stop();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//in every other case python sends poses for the movement of the robot
				else
				{
					//ptp move to the new start position
				  	//ZiGripper.getFrame("/TCP").move(ptp(StartPosNew).setJointVelocityRel(rel_speed_joint));
				  	
					//define variable for the current location of the TCP
				  	double currentX = 0;
				  	double currentY = 0;
				  	double currentZ = 0;
				  	
					//extract the received string into its poses and the mean angle
					String[] pose_list_string = receivedString.split("\\$");
					String[] pose_list_even = pose_list_string[0].split("_");
					String[] pose_list_odd = pose_list_string[1].split("_");
					double Angle_chessboard = Double.parseDouble(pose_list_string[2]);
					
					//convert the angle of the chessboard so its definition area goes from [0,2pi] to [-pi, pi] 
					if(Angle_chessboard > Math.PI && Angle_chessboard <= 2*Math.PI)
					{
						Angle_chessboard = Angle_chessboard - 2*Math.PI;
					}
					
					//loop over all poses and move with the robot to them
					for(int i = 0; i < pose_list_even.length; i++)
					{
						//split the "from" poses in their coordinates
						String[] pose_from = pose_list_even[i].split("/");
						
						//define the "from" fields
						double transX_from = Double.parseDouble(pose_from[0]);
						double transY_from = Double.parseDouble(pose_from[1]);
						double transZ_from = Double.parseDouble(pose_from[2]);
						double rotX = Double.parseDouble(pose_from[3]);
						double rotY = Double.parseDouble(pose_from[4]);
						double rotZ = Double.parseDouble(pose_from[5]);
						
						//rotation coordinate from camera depends on the coordinate system found within the cube, so the rotation of the coordinate on the field (which we need) can point in the same direction as the robot coordinate system or in the opposite direction
						//to verify which coordinate system is used, we also use the angle of the 2D Match on the found cube
						double angle2D = Double.parseDouble(pose_from[6]);
						
						//get the correct rot value in the chessboard plane
						double[] rotXYZ = {rotX, rotY, rotZ};
						
						//get the rotation of the piece
						double rot_complete = getRotation(rotXYZ, angle2D);
						
						//split the "to" poses in their coordinates
						String[] pose_to = pose_list_odd[i].split("/");
						
						//define the "to" fields
						double transX_to = Double.parseDouble(pose_to[0]);
						double transY_to = Double.parseDouble(pose_to[1]);
						double transZ_to = Double.parseDouble(pose_to[2]);
						
						//save the information if the "to" field is inside or outside (in case of capture) the board in a bool
						String out_or_in = pose_to[3];
						Boolean is_out;
						if(out_or_in.equals("out"))
						{
							is_out = true;
						}
						else
						{
							is_out = false;
						}
						
						//case distinction for promotion pieces (different movement overall)
						String promotion_piece = pose_from[7];
						if(promotion_piece.equals("nopromotion"))
						{
							//in case it´s the first movement take the coordinates relative to the StartPosVec, otherwise to the current coordinate
							if (i == 0)
							{
								//linRel movement to 10cm above the from field such that the gripper is rotated parallel to the piece
							  	ZiGripper.getFrame("/TCP").move(linRel(transX_from - StartPosVec[0], transY_from - StartPosVec[1], transZ_from - StartPosVec[2] + height, rot_complete - Math.PI/2, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
							}
							else
							{
								//linRel movement to 10cm above the from field such that the gripper is rotated parallel to the piece
								ZiGripper.getFrame("/TCP").move(linRel(transX_from - currentX, transY_from - currentY, transZ_from - currentZ + height, rot_complete - Math.PI/2, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
							}
						}
						else
						{
							//in case of promotion rotate the promotion piece as often as necessary to get the correct piece on top
							
							//get the amounts of rotations until the correct piece is on top and the direction the piece will be rotated (so instead of 3 rotations, we go 1 rotation in the opposite direction)
							Boolean opposite_direction = false;
							int amount_of_rotations = 0;
							//the pieces on the promotion piece are static, so the camera don´t need to look at the piece after every rotation and move back to the start position for this
							if(promotion_piece.equals("B") || promotion_piece.equals("b"))
							{
								amount_of_rotations = 1;
							}
							else if(promotion_piece.equals("N") || promotion_piece.equals("n"))
							{
								amount_of_rotations = 2;
							}
							else if(promotion_piece.equals("R") || promotion_piece.equals("r"))
							{
								amount_of_rotations = 1;
								opposite_direction = true; 
							}
							 
							//loop over the amount of rotations, to rotate the piece as often as needed to get the correct piece on top
							for(int j = 0; j < amount_of_rotations; j++)
							{ 
								//in case it´s the first movement take the coordinates relative to the StartPosVec, otherwise to the current coordinate
								if (i == 0 && j == 0 && (currentX != 0 || currentY != 0 || currentZ != 0))
								{
									//linRel movement to 10cm above the promotion field with a rotation around pi/4 to grip the piece from diagonal and a 90 degree rotation around alpharad to grip the piece correctly
								  	ZiGripper.getFrame("/TCP").move(linRel(transX_from - StartPosVec[0], transY_from - StartPosVec[1], transZ_from - StartPosVec[2] + height, - Math.PI/2, 0, -Math.PI/4, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								}
								else
								{
									//linRel movement to 10cm above the promotion field (no rotation)
									ZiGripper.getFrame("/TCP").move(linRel(transX_from - currentX, transY_from - currentY, transZ_from - currentZ + height, 0, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
									
									//rotate the gripper 90 degrees only in the first round (otherwise the gripper will rotate every loop and the motion is no longer possible)
									if(j == 0)
									{
										ZiGripper.getFrame("/TCP").move(linRel(0, 0, 0, Math.PI/2, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
									}
									
									//rotate the gripper around pi/4 to grip the piece from diagonal (depending on the promotion piece in opposite directions)
									if(opposite_direction)
									{
										ZiGripper.getFrame("/TCP").move(linRel(0, 0, 0, 0, 0, Math.PI/4, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
									}
									else
									{
										ZiGripper.getFrame("/TCP").move(linRel(0, 0, 0, 0, 0, -Math.PI/4, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
									}
								}
								
								//linRel move to the promotion piece by moving 10cm down
								ZiGripper.getFrame("/TCP").move(linRel(0,0,-height, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								
								//grip with the gripper
								ZiGripper.gripAsync();
								
								//wait till the robot has gripped
								Thread.sleep(pause);
								
								//linRel move 2cm up to be able to rotate the piece
								ZiGripper.getFrame("/TCP").move(linRel(0,0,20, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								
								//case distinction for the direction of the rotation (depending on the promotion piece)
								if(opposite_direction)
								{
									//rotate the gripper around pi/2 in the opposite direction than before to get the next side on top
									ZiGripper.getFrame("/TCP").move(linRel(0,0,0,0,0,-Math.PI/2, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
									
									//linRel move to go 2cm down again and go 5mm in positive y direction because of the shift of the center (results in better movement)
									ZiGripper.getFrame("/TCP").move(linRel(0,5,-20, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								}
								else
								{
									//rotate the gripper around pi/2 in the opposite direction than before to get the next side on top
									ZiGripper.getFrame("/TCP").move(linRel(0,0,0,0,0,Math.PI/2, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
									
									//linRel move to go 2cm down again and go 5mm in negative y direction because of the shift of the center (results in better movement)
									ZiGripper.getFrame("/TCP").move(linRel(0,-5,-20, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								}

								//release the gripper
								ZiGripper.gripAsync(ZimmerR840.createAbsoluteMode(gripper_opening));
								
								//wait till the robot has released
								Thread.sleep(pause);
								
								//linRel move 10cm up and rotate the gripper so it´s pointing down again (direction depends on promotion piece)
								if(opposite_direction)
								{
									ZiGripper.getFrame("/TCP").move(linRel(0,0,height,0,0,Math.PI/4, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								}
								else
								{
									ZiGripper.getFrame("/TCP").move(linRel(0,0,height,0,0,-Math.PI/4, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
								}
								
								//save the current coordinates for the next round in the j-loop (so the robot don´t need to go to the start position after every rotation of the promotion piece)
								currentX = roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getX();
								currentY = roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getY();
								currentZ = roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getZ();
							}
							
							//linRel movement to 10cm above the from field such that the gripper is rotated parallel to the piece (case distinction so the gripper is not rotated again after no rotation of the promotion piece)
							if(amount_of_rotations == 0)
							{
								ZiGripper.getFrame("/TCP").move(linRel(transX_from - currentX, transY_from - currentY, transZ_from - currentZ + height, 0, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
							}
							else
							{
								ZiGripper.getFrame("/TCP").move(linRel(transX_from - currentX, transY_from - currentY, transZ_from - currentZ + height, Math.PI/2, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
							}
						}
							
						//linRel move to the from pose
						ZiGripper.getFrame("/TCP").move(linRel(0,0,-height, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						 
						//grip with the gripper
						ZiGripper.gripAsync();
						
						//wait till the robot has gripped
						Thread.sleep(pause);
						
						//linRel move again to 10cm above the from pose
						ZiGripper.getFrame("/TCP").move(linRel(0,0, height, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						
						//in case the from field was a promotion piece the alphaRad don´t need to be rotated
						if(promotion_piece.equals("nopromotion"))
						{
							//linRel move to 10cm above the to field such that the gripper is rotated parallel to the chessboard
							ZiGripper.getFrame("/TCP").move(linRel(transX_to - transX_from, transY_to - transY_from, transZ_to - transZ_from, Math.PI/2 - rot_complete + Angle_chessboard, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						}
						else
						{
							//linRel move to 10cm above the to field such that the gripper is rotated parallel to the chessboard
							ZiGripper.getFrame("/TCP").move(linRel(transX_to - transX_from, transY_to - transY_from, transZ_to - transZ_from, Angle_chessboard, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						}
						
						//in case the movement leads to the "out"-field where the captured pieces are placed, the robot don´t need to go 10cm down to release the piece, the robot just releases the piece in the air so it falls in the "out"-box
						if(!is_out)
						{
							//linRel move to the to pose
							ZiGripper.getFrame("/TCP").move(linRel(0,0,-height, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						}
						 
						//release the gripper
						ZiGripper.gripAsync(ZimmerR840.createAbsoluteMode(gripper_opening));
						
						//wait till the robot has released
						Thread.sleep(pause);
						
						//in case the movement leads to the "out"-field where the captured pieces are placed, the robot don´t need to go 10cm down to release the piece, the robot just releases the piece in the air so it falls in the "out"-box
						if(!is_out)
						{
							//linRel move again to 10cm above the to pose
							ZiGripper.getFrame("/TCP").move(linRel(0, 0, height, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						}
						
						//rotate the gripper back for the next iteration unless it´s the last iteration, then just move back to the startposition with ptp motion
						if(i != pose_list_even.length - 1)
						{
							//linRel move again to 10cm above the to pose
							ZiGripper.getFrame("/TCP").move(linRel(0, 0, 0, -Angle_chessboard, 0, 0, World.Current.getRootFrame()).setCartVelocity(speed).setJointVelocityRel(rel_speed_joint));
						}
						
						//save the current coordinates for the next round in the i-loop (so the robot don´t need to go to the start position after every movement of a piece in a whole move)
						currentX = roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getX();
						currentY = roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getY();
						currentZ = roberta.getCurrentCartesianPosition(ZiGripper.getFrame("/TCP")).getZ();
					} 
					
					//ptp move to the new start position (to be ready for the next move)
				  	ZiGripper.getFrame("/TCP").move(ptp(StartPosNew).setJointVelocityRel(rel_speed_joint));
				  	
					//send "done" to python after the move was executed
				  	String sendString = "done" + "@pyt";
					myClient.sendString(sendString);
				} 
			}
		}
		//return the result OK, so the program gets shut down
		return Result.OK;
	}
}
