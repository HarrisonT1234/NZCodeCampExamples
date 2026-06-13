package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Movement;

@TeleOp(name = "test Driving",group = "test Opmodes")
public class TestDrive extends OpMode {
    Drivetrain dt;
    Movement movement;
    public void init(){
        dt = new Drivetrain(hardwareMap);
        movement = new Movement(dt,hardwareMap);
        movement.setPos(0,0,0);
    }
    public void loop(){
        movement.update();
        // drive robot oriented
//        dt.drive(gamepad1.left_stick_y,-gamepad1.left_stick_x, -gamepad1.right_stick_x, 1);
        // drive driver oriented
        dt.driveDriverOriented(gamepad1.left_stick_y,-gamepad1.left_stick_x, -gamepad1.right_stick_x, 1, movement.getHeading());
    }

}
