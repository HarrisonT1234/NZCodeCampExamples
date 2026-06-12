package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
@Config
public class Dash {
    public static double offsetX = -142;
    public static double offsetY = -15;
    // for tuning the movement PDFL
    public static double targetX = 0;
    public static double targetY = 0;
    public static double targetHeading = 0;

    // drive
    public static double driveKP = -0.022;
    public static double driveKD = 0.0025;
    public static double driveKF = 0;
    public static double driveKL = 0.09;
    public static double driveDeadZone = 0.5;

    // strafe
    public static double strafeKP = -0.043;
    public static double strafeKD = 0.01;
    public static double strafeKF = 0;
    public static double strafeKL = 0.19;
    public static double strafeDeadZone = 0.7;

    // turn
    public static double turnKP = 0.008;
    public static double turnKD = -0.0001;
    public static double turnKF = 0;
    public static double turnKL = 0.13;
    public static double turnDeadZone = 1.4;

    // for motors
    public static double motorKP = 0;
    public static double motorKD = 0;
    public static double motorKF = 0;
    public static double motorKL = 0;
    public static double motorDeadZone = 0;

}
