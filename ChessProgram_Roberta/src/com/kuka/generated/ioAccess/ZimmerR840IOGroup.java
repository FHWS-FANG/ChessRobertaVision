package com.kuka.generated.ioAccess;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.ioModel.AbstractIOGroup;
import com.kuka.roboticsAPI.ioModel.IOTypes;

/**
 * Automatically generated class to abstract I/O access to I/O group <b>ZimmerR840</b>.<br>
 * <i>Please, do not modify!</i>
 * <p>
 * <b>I/O group description:</b><br>
 * This I/O Group contains the In-/Outputs which are used for controlling the gripper zimmer R840.
     
 IMPORTANT: The Signal Group must have the name "ZimmerR840", if its intended to import the delivered partial project.
 */
@Singleton
public class ZimmerR840IOGroup extends AbstractIOGroup
{
	/**
	 * Constructor to create an instance of class 'ZimmerR840'.<br>
	 * <i>This constructor is automatically generated. Please, do not modify!</i>
	 *
	 * @param controller
	 *            the controller, which has access to the I/O group 'ZimmerR840'
	 */
	@Inject
	public ZimmerR840IOGroup(Controller controller)
	{
		super(controller, "ZimmerR840");

		addInput("CurrentActualValue", IOTypes.INTEGER, 16);
		addDigitalOutput("ControlWord", IOTypes.UNSIGNED_INTEGER, 16);
		addInput("EDIByte3Bit0_STO_1", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EDOByte1Bit4_ToggleBit", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit0_IsReferenced", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EDOByte1Bit7_ParameterStrobe", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EDOByte3_TargetValue", IOTypes.UNSIGNED_INTEGER, 16);
		addDigitalOutput("EDOByte2_ParameterID", IOTypes.UNSIGNED_INTEGER, 4);
		addDigitalOutput("EDOByte2_MotionProfile", IOTypes.UNSIGNED_INTEGER, 4);
		addInput("EDIByte1Bit1_Moving", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit2_ProgramError", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit3_CurrentLimitation", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit4_Toggle", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit5_SoftwareLimitNeg", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit7_ParameterAck", IOTypes.BOOLEAN, 1);
		addInput("EDIByte1Bit6_SoftwareLimitPos", IOTypes.BOOLEAN, 1);
		addInput("EDIByte2_2_MotionProfile", IOTypes.UNSIGNED_INTEGER, 4);
		addInput("EDIByte2_1_ParameterID", IOTypes.UNSIGNED_INTEGER, 4);
		addInput("HelpDI_IdValue", IOTypes.UNSIGNED_INTEGER, 16);
		addInput("SWBit00_ReadyToSwitchOn", IOTypes.BOOLEAN, 1);
		addInput("SWBit01_SwitchedOn", IOTypes.BOOLEAN, 1);
		addInput("SWBit02_OperationEnabled", IOTypes.BOOLEAN, 1);
		addInput("SWBit03_MotorFailure", IOTypes.BOOLEAN, 1);
		addInput("SWBit04_VoltageEnabled", IOTypes.BOOLEAN, 1);
		addInput("SWBit05_QuickStop", IOTypes.BOOLEAN, 1);
		addInput("SWBit06_SwitchOnDisabled", IOTypes.BOOLEAN, 1);
		addInput("SWBit07_Warning", IOTypes.BOOLEAN, 1);
		addInput("SWBit10_TargetReached", IOTypes.BOOLEAN, 1);
		addInput("SWBit11_InternalLimitActive", IOTypes.BOOLEAN, 1);
		addInput("VelocityActualValue", IOTypes.INTEGER, 32);
		addInput("PositionActualValue", IOTypes.INTEGER, 32);
		addInput("FollowingErrorActualValue", IOTypes.INTEGER, 32);
		addInput("EDIByte3Bit1_STO_2", IOTypes.BOOLEAN, 1);
	}

	/**
	 * Gets the value of the <b>digital input '<i>CurrentActualValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [-32768; 32767]
	 *
	 * @return current value of the digital input 'CurrentActualValue'
	 */
	public java.lang.Integer getCurrentActualValue()
	{
		return getNumberIOValue("CurrentActualValue", false).intValue();
	}

	/**
	 * Gets the value of the <b>digital output '<i>ControlWord</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 65535]
	 *
	 * @return current value of the digital output 'ControlWord'
	 */
	public java.lang.Integer getControlWord()
	{
		return getNumberIOValue("ControlWord", true).intValue();
	}

	/**
	 * Sets the value of the <b>digital output '<i>ControlWord</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 65535]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'ControlWord'
	 */
	public void setControlWord(java.lang.Integer value)
	{
		setDigitalOutput("ControlWord", value);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte3Bit0_STO_1</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows the state of the STO 1. 
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte3Bit0_STO_1'
	 */
	public boolean getEDIByte3Bit0_STO_1()
	{
		return getBooleanIOValue("EDIByte3Bit0_STO_1", false);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EDOByte1Bit4_ToggleBit</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Extends the ControlWord with additional bits. Toggle bit is used as an alive signal.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EDOByte1Bit4_ToggleBit'
	 */
	public boolean getEDOByte1Bit4_ToggleBit()
	{
		return getBooleanIOValue("EDOByte1Bit4_ToggleBit", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EDOByte1Bit4_ToggleBit</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Extends the ControlWord with additional bits. Toggle bit is used as an alive signal.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EDOByte1Bit4_ToggleBit'
	 */
	public void setEDOByte1Bit4_ToggleBit(java.lang.Boolean value)
	{
		setDigitalOutput("EDOByte1Bit4_ToggleBit", value);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit0_IsReferenced</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows if the gripper is referenced.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit0_IsReferenced'
	 */
	public boolean getEDIByte1Bit0_IsReferenced()
	{
		return getBooleanIOValue("EDIByte1Bit0_IsReferenced", false);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EDOByte1Bit7_ParameterStrobe</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Extends the ControlWord with additional bits. Parameter strobe is used to trigger a handshake protokol to write parameters.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EDOByte1Bit7_ParameterStrobe'
	 */
	public boolean getEDOByte1Bit7_ParameterStrobe()
	{
		return getBooleanIOValue("EDOByte1Bit7_ParameterStrobe", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EDOByte1Bit7_ParameterStrobe</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Extends the ControlWord with additional bits. Parameter strobe is used to trigger a handshake protokol to write parameters.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EDOByte1Bit7_ParameterStrobe'
	 */
	public void setEDOByte1Bit7_ParameterStrobe(java.lang.Boolean value)
	{
		setDigitalOutput("EDOByte1Bit7_ParameterStrobe", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EDOByte3_TargetValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 65535]
	 *
	 * @return current value of the digital output 'EDOByte3_TargetValue'
	 */
	public java.lang.Integer getEDOByte3_TargetValue()
	{
		return getNumberIOValue("EDOByte3_TargetValue", true).intValue();
	}

	/**
	 * Sets the value of the <b>digital output '<i>EDOByte3_TargetValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 65535]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EDOByte3_TargetValue'
	 */
	public void setEDOByte3_TargetValue(java.lang.Integer value)
	{
		setDigitalOutput("EDOByte3_TargetValue", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EDOByte2_ParameterID</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ExtendedDigitalOutput Byte 2 Bit 0 to 3 - Used to chose a parameter
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 15]
	 *
	 * @return current value of the digital output 'EDOByte2_ParameterID'
	 */
	public java.lang.Integer getEDOByte2_ParameterID()
	{
		return getNumberIOValue("EDOByte2_ParameterID", true).intValue();
	}

	/**
	 * Sets the value of the <b>digital output '<i>EDOByte2_ParameterID</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ExtendedDigitalOutput Byte 2 Bit 0 to 3 - Used to chose a parameter
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 15]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EDOByte2_ParameterID'
	 */
	public void setEDOByte2_ParameterID(java.lang.Integer value)
	{
		setDigitalOutput("EDOByte2_ParameterID", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EDOByte2_MotionProfile</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ExtendedDigitalOutput Byte 2 Bit 4 to 7 - Used to trigger motion profiles
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 15]
	 *
	 * @return current value of the digital output 'EDOByte2_MotionProfile'
	 */
	public java.lang.Integer getEDOByte2_MotionProfile()
	{
		return getNumberIOValue("EDOByte2_MotionProfile", true).intValue();
	}

	/**
	 * Sets the value of the <b>digital output '<i>EDOByte2_MotionProfile</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ExtendedDigitalOutput Byte 2 Bit 4 to 7 - Used to trigger motion profiles
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 15]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EDOByte2_MotionProfile'
	 */
	public void setEDOByte2_MotionProfile(java.lang.Integer value)
	{
		setDigitalOutput("EDOByte2_MotionProfile", value);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit1_Moving</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows if the gripper is moving.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit1_Moving'
	 */
	public boolean getEDIByte1Bit1_Moving()
	{
		return getBooleanIOValue("EDIByte1Bit1_Moving", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit2_ProgramError</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Indicates a program error.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit2_ProgramError'
	 */
	public boolean getEDIByte1Bit2_ProgramError()
	{
		return getBooleanIOValue("EDIByte1Bit2_ProgramError", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit3_CurrentLimitation</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Initcates if the current limitation is active
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit3_CurrentLimitation'
	 */
	public boolean getEDIByte1Bit3_CurrentLimitation()
	{
		return getBooleanIOValue("EDIByte1Bit3_CurrentLimitation", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit4_Toggle</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows if the controller is running (alive bit)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit4_Toggle'
	 */
	public boolean getEDIByte1Bit4_Toggle()
	{
		return getBooleanIOValue("EDIByte1Bit4_Toggle", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit5_SoftwareLimitNeg</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows if the outer software limit is reached.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit5_SoftwareLimitNeg'
	 */
	public boolean getEDIByte1Bit5_SoftwareLimitNeg()
	{
		return getBooleanIOValue("EDIByte1Bit5_SoftwareLimitNeg", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit7_ParameterAck</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ParameterAck is used to acknowledge a handshake protokol to write parameters.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit7_ParameterAck'
	 */
	public boolean getEDIByte1Bit7_ParameterAck()
	{
		return getBooleanIOValue("EDIByte1Bit7_ParameterAck", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte1Bit6_SoftwareLimitPos</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows if the inner software limit is reached.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte1Bit6_SoftwareLimitPos'
	 */
	public boolean getEDIByte1Bit6_SoftwareLimitPos()
	{
		return getBooleanIOValue("EDIByte1Bit6_SoftwareLimitPos", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte2_2_MotionProfile</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ExtendedDigitalInput Byte 2 Bit 4 to 7 - Used to show the active motion profile
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 15]
	 *
	 * @return current value of the digital input 'EDIByte2_2_MotionProfile'
	 */
	public java.lang.Integer getEDIByte2_2_MotionProfile()
	{
		return getNumberIOValue("EDIByte2_2_MotionProfile", false).intValue();
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte2_1_ParameterID</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ExtendedDigitalInput Byte 2 Bit 0 to 3 - Used to show a parameter
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 15]
	 *
	 * @return current value of the digital input 'EDIByte2_1_ParameterID'
	 */
	public java.lang.Integer getEDIByte2_1_ParameterID()
	{
		return getNumberIOValue("EDIByte2_1_ParameterID", false).intValue();
	}

	/**
	 * Gets the value of the <b>digital input '<i>HelpDI_IdValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * These two byte are used to transmit the actual value of a specific parameter or the error value of PE, MF or EC.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [0; 65535]
	 *
	 * @return current value of the digital input 'HelpDI_IdValue'
	 */
	public java.lang.Integer getHelpDI_IdValue()
	{
		return getNumberIOValue("HelpDI_IdValue", false).intValue();
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit00_ReadyToSwitchOn</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit00_ReadyToSwitchOn'
	 */
	public boolean getSWBit00_ReadyToSwitchOn()
	{
		return getBooleanIOValue("SWBit00_ReadyToSwitchOn", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit01_SwitchedOn</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit01_SwitchedOn'
	 */
	public boolean getSWBit01_SwitchedOn()
	{
		return getBooleanIOValue("SWBit01_SwitchedOn", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit02_OperationEnabled</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit02_OperationEnabled'
	 */
	public boolean getSWBit02_OperationEnabled()
	{
		return getBooleanIOValue("SWBit02_OperationEnabled", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit03_MotorFailure</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit03_MotorFailure'
	 */
	public boolean getSWBit03_MotorFailure()
	{
		return getBooleanIOValue("SWBit03_MotorFailure", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit04_VoltageEnabled</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit04_VoltageEnabled'
	 */
	public boolean getSWBit04_VoltageEnabled()
	{
		return getBooleanIOValue("SWBit04_VoltageEnabled", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit05_QuickStop</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit05_QuickStop'
	 */
	public boolean getSWBit05_QuickStop()
	{
		return getBooleanIOValue("SWBit05_QuickStop", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit06_SwitchOnDisabled</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit06_SwitchOnDisabled'
	 */
	public boolean getSWBit06_SwitchOnDisabled()
	{
		return getBooleanIOValue("SWBit06_SwitchOnDisabled", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit07_Warning</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord)
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit07_Warning'
	 */
	public boolean getSWBit07_Warning()
	{
		return getBooleanIOValue("SWBit07_Warning", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit10_TargetReached</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord).
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit10_TargetReached'
	 */
	public boolean getSWBit10_TargetReached()
	{
		return getBooleanIOValue("SWBit10_TargetReached", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>SWBit11_InternalLimitActive</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Specific bit of the SW (StatausWord).
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'SWBit11_InternalLimitActive'
	 */
	public boolean getSWBit11_InternalLimitActive()
	{
		return getBooleanIOValue("SWBit11_InternalLimitActive", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>VelocityActualValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [-2147483648; 2147483647]
	 *
	 * @return current value of the digital input 'VelocityActualValue'
	 */
	public java.lang.Long getVelocityActualValue()
	{
		return getNumberIOValue("VelocityActualValue", false).longValue();
	}

	/**
	 * Gets the value of the <b>digital input '<i>PositionActualValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [-2147483648; 2147483647]
	 *
	 * @return current value of the digital input 'PositionActualValue'
	 */
	public java.lang.Long getPositionActualValue()
	{
		return getNumberIOValue("PositionActualValue", false).longValue();
	}

	/**
	 * Gets the value of the <b>digital input '<i>FollowingErrorActualValue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [-2147483648; 2147483647]
	 *
	 * @return current value of the digital input 'FollowingErrorActualValue'
	 */
	public java.lang.Long getFollowingErrorActualValue()
	{
		return getNumberIOValue("FollowingErrorActualValue", false).longValue();
	}

	/**
	 * Gets the value of the <b>digital input '<i>EDIByte3Bit1_STO_2</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Shows the state of the STO 2. 
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'EDIByte3Bit1_STO_2'
	 */
	public boolean getEDIByte3Bit1_STO_2()
	{
		return getBooleanIOValue("EDIByte3Bit1_STO_2", false);
	}

}
