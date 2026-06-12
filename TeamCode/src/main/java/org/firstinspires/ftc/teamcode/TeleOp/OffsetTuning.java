package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Movement;

@TeleOp(name = "offset tuning",group = "tuning Opmodes")
public class OffsetTuning extends OpMode {
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
        // updates offsets based on what we set them to be
        movement.updateOffsets();
        // telemetry to dashboard
        telemetry.addData("x position",movement.getX());
        telemetry.addData("y position",movement.getY());
        telemetry.addData("heading",movement.getHeading());
        telemetry.update();
        // just turns
        dt.driveDriverOriented(0,0,1,1,0);
    }
}
