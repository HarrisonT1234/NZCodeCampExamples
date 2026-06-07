package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;
import static org.firstinspires.ftc.teamcode.Dash.driveKD;
import static org.firstinspires.ftc.teamcode.Dash.driveKF;
import static org.firstinspires.ftc.teamcode.Dash.driveKL;
import static org.firstinspires.ftc.teamcode.Dash.driveKP;
import static org.firstinspires.ftc.teamcode.Dash.offsetX;
import static org.firstinspires.ftc.teamcode.Dash.offsetY;
import static org.firstinspires.ftc.teamcode.Dash.strafeKD;
import static org.firstinspires.ftc.teamcode.Dash.strafeKF;
import static org.firstinspires.ftc.teamcode.Dash.strafeKL;
import static org.firstinspires.ftc.teamcode.Dash.strafeKP;
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

    Drivetrain drivetrain;
    // these are the units that you use for everything, make sure
    PDFL pdfl;
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
        pinpoint = hardware.get(GoBildaPinpointDriver.class, "odo_pods");
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
        // this is a type of class that just records the position of your robot
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
    One of the most important methods in the class. This method takes in your target the current position of the robot
    and puts them through a PDFL with values that you can tune to find the speed at which you need to go in both x and y.
    It then puts those through the driver oriented drive function which tells the motors what to do for the robot to get
    to the desired target position.
     */
    public void goToPosition(double targetX,double targetY,double targetHeading,double speed){
        // run drive PDFL
        pdfl.setTuningValuesAndReset(driveKP,driveKD,driveKF,driveKL);
        double drive = pdfl.runPDFL(targetY,getY());
        // run strafe PDFL
        pdfl.setTuningValuesAndReset(strafeKP,strafeKD,strafeKF,strafeKL);
        double strafe = pdfl.runPDFL(targetX,getX());
        // run turn PDFL
        pdfl.setTuningValuesAndReset(turnKP,turnKD,turnKF,turnKL);
        double turn = pdfl.runPDFL(targetHeading,getHeading());
        // tell the motors to drive according to the errors
        drivetrain.driveDriverOriented(drive,strafe,turn,speed,getHeading());
    }
}
