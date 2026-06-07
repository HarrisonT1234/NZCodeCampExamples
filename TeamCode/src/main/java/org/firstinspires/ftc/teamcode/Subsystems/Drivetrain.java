package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {
    // initializing the member variables. These are declared as motors
    DcMotor fl; // front left
    DcMotor fr; // front right
    DcMotor bl; // back left
    DcMotor br; // back right
    public Drivetrain(HardwareMap hardwareMap){
        // declare each of the motors as a part of the control hub, and names them what they will be named in config
        fl = hardwareMap.get(DcMotor.class,"FL"); // in config you tell the control hub what port "FL" is in
        fr = hardwareMap.get(DcMotor.class,"FR");
        bl = hardwareMap.get(DcMotor.class,"BL");
        br = hardwareMap.get(DcMotor.class,"BR");

        // some of the motors will be reversed, so you will need to change this code so the right motors are reversed
        // when reversing the motors I would use the drive without driver  oriented class to test if they go the right direction
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.FORWARD);

        // the motors now can take in powers instead fo go to position. the odo pods will account for your position
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // makes your motors hold their position rather than coasting for more control over your robot
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // these methods are so in other classes you can call drivetrain.setFL and it will move the motor called "FL"
    public void setFl(double power){
        fl.setPower(power);
    }
    public void setFr(double power){
        fr.setPower(power);
    }
    public void setBl(double power){
        bl.setPower(power);
    }
    public void setBr(double power){
        br.setPower(power);
    }

    // drive without driver oriented
    public void drive(double drive,double strafe,double turn,double speed){
        // mecanum equation, tells the motors what to do individually in order to make the whole robot move forward, sideways, or turn
        // if your robot is not driving straight or is moving in wierd ways, it may be that your mecanum equation is wrong.
        fl.setPower((drive + strafe + turn)*speed);
        fr.setPower((drive - strafe - turn)*speed);
        bl.setPower((drive - strafe + turn)*speed);
        br.setPower((drive + strafe - turn)*speed);
    }

    // drive with driver oriented
    public void driveDriverOriented(double driveDriverOriented,double strafeDriverOriented,double turn,double speed,double heading){
        // equation using trigonometry to figure out the correct drive and strafe values based on the heading

        // these values are for the robot
        double drive = Math.cos(Math.toRadians(heading))*strafeDriverOriented - Math.sin(Math.toRadians(heading))*driveDriverOriented;
        double strafe = Math.sin(Math.toRadians(heading))*strafeDriverOriented - Math.cos(Math.toRadians(heading))*driveDriverOriented;

        // mecanum equation again
        fl.setPower((drive + strafe + turn)*speed);
        fr.setPower((drive - strafe - turn)*speed);
        bl.setPower((drive - strafe + turn)*speed);
        br.setPower((drive + strafe - turn)*speed);

    }
}
