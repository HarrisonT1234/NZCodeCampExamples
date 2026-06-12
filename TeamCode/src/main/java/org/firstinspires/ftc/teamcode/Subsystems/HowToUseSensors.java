package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class HowToUseSensors {
    //Initialize the sensor you are using
    TouchSensor testTouchSensor;
    NormalizedColorSensor testColorSensor;
    DistanceSensor testDistanceSensor;
    TouchSensor testDigital;
    /*
    you can declare any sensor that gets information that is binary (yes/no). for example break beams, limit switches,
    magnetic limit switches, and distance sensors acting as break beams. A good way to tell is if you plug it into
    the digital port on the control or expansion hub than it should be declared as a touch sensor
    */


    public HowToUseSensors(HardwareMap hardwareMap){
        // tells the control and expansion hub what ports these sensors are located. You adjust the ports in config.
        testTouchSensor = hardwareMap.get(TouchSensor.class,"touchSensor");
        testColorSensor = hardwareMap.get(NormalizedColorSensor.class,"colorSensor");
        testDistanceSensor = hardwareMap.get(DistanceSensor.class,"distanceSensor");
        testDigital = hardwareMap.get(TouchSensor.class,"testDigital");
    }

    /** THESE NEXT METHODS ARE JUST TO SHOW HOW TO USE THE METHODS IN EACH SENSOR, YOU DO NOT NEED TO WRITE THE WHOLE METHOD OUT AND USE IT **/


    public boolean isTouchSensorPressed(){
        // this method is very useful and returns a true or false. It is used most with break beams and laser distance sensors
        return testTouchSensor.isPressed(); // you may need to put a ! in front of this so it returns the opposite. you can test this using telemetry
    }

    public double touchSensorPressure(){
        // this is a way to get the amount the touch sensor is pressed. I have not found a good application for this as it requires a lot of looptime and is less reliable than a digital sensor
        return testTouchSensor.getValue();
    }
    public float[] hsv = {0,0,0};
    public double colorSensorColor(){
        // this is the code to get the color that a color sensor sees
        NormalizedRGBA colors = testColorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsv); // this converts the color sensors RGB to HSV
        // HSV stands for Hue, Saturation, and Value. A good way to visualize this is https://math.hws.edu/graphicsbook/demos/c2/rgb-hsv.html
        return hsv[0]; // hsv[0] is the hue of the color the color sensor is seeing
    }
    public boolean isGreen(){ // the name of this is isGreen as an example
        NormalizedRGBA colors = testColorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsv); // converts to HSV like last time
        boolean green = hsv[0] > 100 && hsv[0] < 170; // this checks if the hue is within a certain range.
        /*
        if you are trying to test for the color of a specific object, I would us telemetry to print out the hue
        that the color sensor is seeing and then put the object up to the color senor and check the value of the hue.
        then pick a range of values around that hue that is big enough so it always detects the object but small enough
        so that it does not detect when the object is not there or a different object is there.
         */
        return green;
    }
    public boolean digitalSensor(){ // this is the same as the touchSensorPressed method because this function can be used for any sensor that returns true or false
        return testDigital.isPressed(); // may need to be ! to be the opposite. test this using telemetry
    }
}
