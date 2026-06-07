package org.firstinspires.ftc.teamcode.Subsystems;

public class PDFL {
    double kP;
    double kD;
    double kF;
    double kL;
    double lastError;
    public PDFL(){
        kP = 0;
        kD = 0;
        kF = 0;
        kL = 0;
    }
    public void setTuningValuesAndReset(double p,double d,double f,double l){
        kP = p;
        kD = d;
        kF = f;
        kL = l;
        lastError = 0;
    }
    public double runPDFL(double target, double current){
        double error = target - current;
        double proportional = error * kP;
        double derivative = lastError - error;
        double feedforward = kF;
        double correction = proportional*kP + derivative*kD + feedforward;
        if(Math.abs(correction) < kL){
            correction = kL*Math.signum(correction);
        }
        lastError = error;
        return correction;
    }
}
