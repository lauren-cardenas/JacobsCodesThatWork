/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final WPI_VictorSPX leftMotor1 = new WPI_VictorSPX(Map.m_leftMotor1);
  private final WPI_VictorSPX leftMotor2 = new WPI_VictorSPX(Map.m_leftMotor2);
  private final WPI_VictorSPX rightMotor1 = new WPI_VictorSPX(Map.m_rightMotor1);
  private final WPI_VictorSPX rightMotor2 = new WPI_VictorSPX(Map.m_rightMotor2);

  private final SpeedControllerGroup m_leftMotors = new SpeedControllerGroup(leftMotor1, leftMotor2);
  private final SpeedControllerGroup m_rightMotors = new SpeedControllerGroup(rightMotor1, rightMotor2);

  private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftMotors, m_rightMotors);
  private final Timer m_timer = new Timer();
     // controller definitions
     private final XboxController m_driverController = new XboxController(Map.DRIVER_CONTROLLER);
     private final XboxController m_operatorController = new XboxController(Map.OPERATOR_CONTROLLER);
  // Constants
    //
    // Encoder Wiring:
    //
    // Left Encoder A Channel  -> DIO 1, Module 1
    // Left Encoder B Channel  -> DIO 2, Module 1
    // Right Encoder A Channel -> DIO 3, Module 1
    // Right Encoder B Channel -> DIO 4, Module 1
    //
    // Details on WPILib Encoder class: http://wpilib.screenstepslive.com/s/3120/m/7912/l/85770-measuring-rotation-of-a-wheel-or-other-shaft-using-encoders
    //
    private Encoder m_leftEncoders = new Encoder(Map.m_leftEnc1, Map.m_leftEnc2, false, EncodingType.k4X);
    private Encoder m_rightEncoders = new Encoder(Map.m_rightEnc1, Map.m_rightEnc2, true, EncodingType.k4X);    // Count direction reversed

   

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_leftEncoders.setDistancePerPulse(Map.kDistancePerPulse);
    m_rightEncoders.setDistancePerPulse(Map.kDistancePerPulse);
    resetEncoders();

  }


  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    m_timer.reset();
    m_timer.start();
    resetEncoders();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    updateEncoders();


    // Ecoders have been converted to the absolute values, Negatives should not appear regardless of direction
  if (getAverageEncoderPosition() < 19.685) {

      m_driveTrain.arcadeDrive(-.5, 0);
      

  } else if (getAverageEncoderPosition() == 19.687){

    
    m_driveTrain.arcadeDrive(0,0);
    Timer.delay(2); //pause to see position
    
  } else if(m_leftEncoders.getDistance() < 20){

    m_leftMotors.set(-.6);

  } else if(m_leftEncoders.getDistance() == 20.2){

    m_leftMotors.set(0);
    Timer.delay(2); // pause to see position
    

  } else if(getAverageEncoderPosition() < 38.5){

    m_driveTrain.arcadeDrive(.5, 0);

  } else if(getAverageEncoderPosition() == 38.52){

    m_driveTrain.arcadeDrive(0,0);
    Timer.delay(2); //pause to see position

  }







 /* {
      m_driveTrain.arcadeDrive(0, 0);}
       /*resetEncoders(); 
  }else if (getAverageEncoderPosition()< 11.26) {
       m_leftMotors.set(-.5);
     }*/
}
    
    
    
    
    
    
    /*switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:*/
      /*arcadeDrive.setSafetyEnabled(false);
      while(m_timer.get() <15){
        m_driveTrain.arcadeDrive(.5, 0);
      }*/
      

      
        // Put default auto code here
   /*if(m_timer.get()< 4){
      m_driveTrain.arcadeDrive(.5, 0);    
  } 
  else if(m_timer.get()< 5.57){
    m_rightMotors.set(-.5);
   // m_leftMotors.set(-.5);
  } 
  else if(m_timer.get()< 9.1){
    m_driveTrain.arcadeDrive(.5, 0);
   }
    else if(m_timer.get()< 10.62){
     m_rightMotors  .set(-.5);
    // m_leftMotors.set(-.5);
   } 
   /*else if(m_timer.get()<11){
     m_driveTrain.arcadeDrive(.5, 0);
   } 
   else if(m_timer.get()<12){
     m_leftMotors.set(.5);
    // m_rightMotors.set(.5);
   }
   else if(m_timer.get()<14){
     m_driveTrain.arcadeDrive(.5, 0);
   }
   else if(m_timer.get()<15){
     m_leftMotors.set(.5);
    // m_rightMotors.set(.5);
  }
   else{
     m_driveTrain.stopMotor();
   }
    break;
  }*/
    
    /**
     * Initialize the robot.
     */
    
    

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        // Drive forwards for 50 units

        resetEncoders();
    }

    private double getAverageEncoderPosition() {
      return ((Math.abs (m_leftEncoders.getDistance()))) + (Math.abs (m_leftEncoders.getDistance())) / 2;
  }


    
    private void resetEncoders() {
        m_leftEncoders.reset();
        m_rightEncoders.reset();
    }
  
    private void updateEncoders(){

      SmartDashboard.putNumber("Encoder Average", getAverageEncoderPosition());
      SmartDashboard.putNumber("Left Encoder's Distance" , (Math.abs (m_leftEncoders.getDistance()))); 
      SmartDashboard.putNumber("Right Encoder's Distance" ,(Math.abs (m_rightEncoders.getDistance())));

    }
    

    /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    SmartDashboard.putNumber("Left Encoder's Distance" , m_leftEncoders.getDistance()); 
    SmartDashboard.putNumber("Right Encoder's Distance" , m_rightEncoders.getDistance()); 

    double triggerVal = (m_driverController.getTriggerAxis(Hand.kRight) - m_driverController.getTriggerAxis(Hand.kLeft)) * Map.DRIVING_SPEED;
    
    double stick = (m_driverController.getX(Hand.kLeft)) *Map.TURNING_RATE;
    double left_command = (triggerVal + stick) * Map.DRIVING_SPEED;
    double right_command = (triggerVal - stick) *Map.DRIVING_SPEED;
    m_driveTrain.tankDrive(left_command , right_command);
      
    double kP = -0.1;
      double min_command = 0.03;
  
      double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
  
      if(m_driverController.getXButton()){
        double heading_error = -tx;
        double steering_adjust = 0.0;
        if(tx>1.0){
          steering_adjust = kP * heading_error - min_command;
        }
        else if(tx<1.0){
          steering_adjust = kP * heading_error + min_command;
        }
        left_command += steering_adjust;
        right_command -= steering_adjust;
      }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
