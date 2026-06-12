package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.teamcode.Dash.motorDeadZone;
import static org.firstinspires.ftc.teamcode.Dash.motorKD;
import static org.firstinspires.ftc.teamcode.Dash.motorKF;
import static org.firstinspires.ftc.teamcode.Dash.motorKL;
import static org.firstinspires.ftc.teamcode.Dash.motorKP;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HowToUseServosAndMotors {
    DcMotor testMotor1;
    DcMotor testMotor2;
    Servo testServo;
    CRServo testCRServo;
    PDFL pdfl;
    public HowToUseServosAndMotors(HardwareMap hardwareMap){
        testMotor1 = hardwareMap.get(DcMotor.class,"test Motor 1");
        testMotor2 = hardwareMap.get(DcMotor.class,"test Motor 2");
        testServo = hardwareMap.get(Servo.class,"test Servo");
        testCRServo = hardwareMap.get(CRServo.class,"test CR Servo");
        pdfl = new PDFL();

        // run without encoder is for a system that you do not need to go to precise locations, like an intake or the drive wheels
        testMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // motors are automatically set to run with their encoders, so here you just need to stop and reset the encoder.
        // this will make sure that the place the motor is currently at is 0 and it will start counting from there.
        // linear/horizontal slides are great uses of the motor encoders.
        testMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // these are useful if you do not want to have to deal with negatives when you are running a PDFL with multiple motors
        testMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        testMotor2.setDirection(DcMotorSimple.Direction.FORWARD);

        // you do not have to do anything for the servos because they automatically know there positions even if they turn off

    }
    /** THESE NEXT METHODS ARE JUST TO SHOW HOW TO USE THE METHODS IN EACH SENSOR, YOU DO NOT NEED TO WRITE THE WHOLE METHOD OUT AND USE IT **/

    public void runCRServo(double power){ // CR servos just act as motors and cannot return positional data. power is a value between -1 and 1.
        testCRServo.setPower(power);
    }
    public void runServo(double pos){
        /*
        this makes the servo go to a position. the position is between 0 and 1, and coordinates to degrees
        based off the range of the servo. for example if with a servo with range 0 - 300, telling the servo
        to go to position 0.5 would have the servo move to 150 degrees. the best way to test servo positions
        is to take off the part attaching the servo to the mechanism, then test positions by running code,
        then put the part back on and test it again
         */
        testServo.setPosition(pos);
    }
    public void runMotor1(double power){ // for running any motor that you just want to spin
        // this will make the motor turn at a speed from -1 to 1
        testMotor1.setPower(power);
    }

    public void runMotor2(double targetPosition){ // for a motor you want to control with the motor encoders. best for linear slides.
        /*
        The target position is in encoder ticks, which are somewhat small measurements of the internal motor.
        if you have a motor with a gear bos the amount of encoder ticks per on revolution of a motor will vary.
        the best way to find out the correct position you want your motors to go it is by using telemetry to
        output the current position of the motors and then move your motor until it is in the position you want.
        then read off the current position of the motor from telemetry and that is your target position for that
        spot.
         */
        // To move the motor you could use "testMotor2.setTargetPosition(targetPosition);" but this is less accurate and can result in problems.
        // The best way to move the motor is to use a PDFL, what we used in the movement function to control where we were.
        // A PDFL is just a controller that changes the speed of the motor based on how close the current position is to the target

        // first set up the correct tuning values
        pdfl.setTuningValuesAndReset(motorKP,motorKD,motorKF,motorKL,motorDeadZone);
        // then set the speed of the motor to the output of the pdfl controller software.
        // if you are wondering how to tune the pdfl look in the PDFL class for comments
        testMotor2.setPower(pdfl.runPDFL(targetPosition - testMotor2.getCurrentPosition()));
    }
}
