package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;
import static org.firstinspires.ftc.teamcode.Dash.driveDeadZone;
import static org.firstinspires.ftc.teamcode.Dash.driveKD;
import static org.firstinspires.ftc.teamcode.Dash.driveKF;
import static org.firstinspires.ftc.teamcode.Dash.driveKL;
import static org.firstinspires.ftc.teamcode.Dash.driveKP;
import static org.firstinspires.ftc.teamcode.Dash.offsetX;
import static org.firstinspires.ftc.teamcode.Dash.offsetY;
import static org.firstinspires.ftc.teamcode.Dash.strafeDeadZone;
import static org.firstinspires.ftc.teamcode.Dash.strafeKD;
import static org.firstinspires.ftc.teamcode.Dash.strafeKF;
import static org.firstinspires.ftc.teamcode.Dash.strafeKL;
import static org.firstinspires.ftc.teamcode.Dash.strafeKP;
import static org.firstinspires.ftc.teamcode.Dash.turnDeadZone;
import static org.firstinspires.ftc.teamcode.Dash.turnKD;
import static org.firstinspires.ftc.teamcode.Dash.turnKF;
import static org.firstinspires.ftc.teamcode.Dash.turnKL;
import static org.firstinspires.ftc.teamcode.Dash.turnKP;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Movement {
    GoBildaPinpointDriver pinpoint;
    PDFL pdfl;

    Drivetrain drivetrain;
    // these are the units that you use for everything, make sure that you keep these consistent and change them to the units you want
    public DistanceUnit unit = INCH;
    public AngleUnit angleUnit = DEGREES;

    // this is what you use to tune the offsets as well as dash.
    public void updateOffsets() {
        pinpoint.setOffsets(offsetX, offsetY,MM);
    }

    public Movement(Drivetrain drivetrain, HardwareMap hardware) {
        // initialize the PDFL
        pdfl = new PDFL();
        // initializes the odo pods through the pinpoint
        pinpoint = hardware.get(GoBildaPinpointDriver.class, "odo");
        // sets the pinpoint offsets, these need to be tuned
        pinpoint.setOffsets(offsetX, offsetY,MM);
        // these set up the pinpoint and odo pod.
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.resetPosAndIMU();
        pinpoint.recalibrateIMU();

        this.drivetrain = drivetrain;
    }

    public void setPos(double x, double y, double h) {
        // this is a object that just records the position of your robot on the x,y plane
        Pose2D pose = new Pose2D(unit,x,y,angleUnit,h);
        pinpoint.setPosition(pose);
    }

    // good methods for use both inside the class and outside the class.
    public double getY(){
        return pinpoint.getPosY(unit);
    }
    public double getX(){
        return pinpoint.getPosX(unit);
    }
    public double getHeading(){
        return pinpoint.getHeading(angleUnit);
    }
    public void update(){
        pinpoint.update();
    }

    /*
    One of the most important methods in this class. This method takes in your target the current position of the robot
    and puts them through a PDFL with values that you can tune to find the speed at which you need to go in both x and y.
    It then puts those through the driver oriented drive function which tells the motors what to do for the robot to get
    to the desired target position. For more information on PDFLs and how to tune them look at the PDFL class.
     */
    public double[] goToPosition(double targetX,double targetY,double targetHeading,double speed){
        // this is the code to rotate the error so it is robot oriented and can be run through the PDFLs.
        // field orienter X and Y errors
        double errorX = targetX - getX();
        double errorY = targetY - getY();
        // just makes the errors robot oriented
        double botErrorY = Math.sin(Math.toRadians(-getHeading()))*errorX + Math.cos(Math.toRadians(-getHeading()))*errorY;
        double botErrorX = Math.cos(Math.toRadians(-getHeading()))*errorX - Math.sin(Math.toRadians(-getHeading()))*errorY;
        // run drive PDFL
        pdfl.setTuningValuesAndReset(driveKP,driveKD,driveKF,driveKL,driveDeadZone);
        double drive = pdfl.runPDFL(botErrorY);
        // run strafe PDFL
        pdfl.setTuningValuesAndReset(strafeKP,strafeKD,strafeKF,strafeKL,strafeDeadZone);
        double strafe = pdfl.runPDFL(botErrorX);
        // run turn PDFL
        pdfl.setTuningValuesAndReset(turnKP,turnKD,turnKF,turnKL,turnDeadZone);
        // the error for heading is computed here.
        double errorHeading = targetHeading - getHeading();
        // this bit of code is for angle wrapping so that when the robot gets to 180 degrees it doesn't think it is going to go to -180 degrees.
        if(errorHeading >= 180){
            errorHeading -= 360;
        }
        if(errorHeading <= -180){
            errorHeading += 360;
        }
        double turn = pdfl.runPDFL(errorHeading);
        // tell the motors to drive according to the errors
        drivetrain.drive(drive,strafe,turn,speed);
        return new double[] {botErrorY,botErrorX};
    }
}
