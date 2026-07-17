package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Dash.targetHeading;
import static org.firstinspires.ftc.teamcode.Dash.targetX;
import static org.firstinspires.ftc.teamcode.Dash.targetY;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Movement;

@TeleOp(name = "Movement Tuning",group = "tuning Opmodes")
public class PDFL_Tuning extends OpMode {
    Drivetrain dt;
    Movement movement;
    public void init(){
        dt = new Drivetrain(hardwareMap);
        movement = new Movement(dt,hardwareMap);
        movement.setPos(0,0,0);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }
    public void loop(){
        movement.update();


        // telemetry to graph
        telemetry.addData("x position",movement.getX());
        telemetry.addData("y position",movement.getY());
        telemetry.addData("heading",movement.getHeading());
        telemetry.update();
        // this is to run the goto position software to go to the positions you can change in dash
        // make sure to tune turn first, then drive and strafe.
        movement.goToPosition(targetX,targetY,targetHeading,1);
    }
}
