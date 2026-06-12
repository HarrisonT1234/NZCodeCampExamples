package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PDFL {
    double kP;
    double kD;
    double kF;
    double kL;
    double deadzone;
    double lastError;
    ElapsedTime timer;
    public PDFL(){
        kP = 0;
        kD = 0;
        kF = 0;
        kL = 0;
        deadzone = 0;
        timer = new ElapsedTime();
        timer.reset();
    }
    /** THIS IS VERY IMPORTANT: PLEASE READ TO KNOW HOW TO USE A PDFL **/
    /*
    The main thing a PDFL is used for is to control something accurately and smoothly using a desired target and the current position
    PDFL stand for proportional, derivative, feedfoward, lowerlimit.
    PROPORTIONAL is the main way that the system uses to get to the target position. proportional just
    says that the closer the current position is from the target position, the slower the wheels should turn
    DERIVATIVE is a way for the system to get to the desired position smoother and faster.
    a good resource to understand proportional and derivative is https://www.ctrlaltftc.com/the-pid-controller
    on that site they go into integral as well, which is used to counteract drift, but we don't use it because
    in practice it doesn't always work perfectly.
    FEEDFORWARD is a constant value added to the end result to counteract a constant force. this is mainly used
    on a shooter to apply a constant force forward or on slides to counteract gravity. FEEDFORWARD IS ONE-DIRECTIONAL.
    LOWER LIMIT is the smallest value that makes the motor spin, accounting for friction in a system. in your
    drive wheels you should use lower limit to account for the friction of the wheels and the power required
    to move your robot. LOWER LIMIT IS MULTI-DIRECTIONAL.
    It's good practice to always use lower limit and sometimes us feedforward. Feedforward is good if you need to
    always be pushing in a certain direction.
    another important part is the deadzone. the deadzone is just the area around your target position that you want
    the motors to not do anything at. it is impossible to get a motor to go exactly to a position without being off
    by 0.1 mm because of gear lash and things. to account for this and to make the PDFL not oscillate around the target
    position, we use a deadzone, a zone around the final position in which the motors do not move.
    TUNING:
    order: 1:feedforward 2:lower limit 3:proportional 4:deadzone 5:derivative
    while tuning use the graph system in dashboard.
    if you are using it, tune feedforward until the slides are just barely moving up. then lower it slowly until they stop.
    if you are using slides, then set them to the middle position and tune lower limit.
    first increase lower limit until the motors are moving slowly. while doing this set proportional to be
    very very small, like 0.0000001.
    then do proportional which really matters. you want to increase proportional until the motors get to the place you want,
    then keep adjusting proportional while changing the position you want to go to. the higher proportional is, the faster
    it will get to the desired position yet it will also make it overshoot around the point. the lower proportional is, the
    less it overshoots yet it gets to the position slower. the intent is to make the proportional to go to the target as fast
    as possible without over shooting and oscillating too much.
    then tune deadzone. you want the deadzone to start at 0 and increase it slowly until the motor does not oscillate around the target position.
    Lastly tune derivative, slowly increase derivative until the motor smoothly gets to is desired position. the goal of
    derivative is to get rid any oscillations
     */


    // method to set the tuning values for the PDFL.
    public void setTuningValuesAndReset(double p,double d,double f,double l,double zone){
        kP = p;
        kD = d;
        kF = f;
        kL = l;
        deadzone = zone;
        lastError = 0;
    }
    // PDFL code, code that runs in a loop and iterates to make the current get as close to the target as possible while staying smooth
    public double runPDFL(double error){
        double proportional = error * kP;
        double derivative = (error - lastError);
        double feedforward = kF;
        double correction = proportional + derivative*kD + feedforward;
        // lower limit
        if(Math.abs(correction) < Math.abs(kL)){
            correction = kL*Math.signum(correction);
        }
        if(Math.abs(error) < deadzone){
            correction = 0;
        }
        lastError = error;
        timer.reset();
        return correction;
    }
}
