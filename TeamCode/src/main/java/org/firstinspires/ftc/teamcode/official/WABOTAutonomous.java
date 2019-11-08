package org.firstinspires.ftc.teamcode.official;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;

/**
 * OFFICIAL AUTONOMOUS CODE
 *
 * WRIGHT ANGLE ROBOTICS (Skystone 2019-2020)
 *
 * Contributor:
 * Owen Boseley (2016-2021)
 *
 * NOTES:
 * - Anything marked with the comment (// DO NOT TOUCH) should not be touched xD
 *
 *
 */
@Autonomous(name="WABOTAutonomous", group="WABOT")
public class WABOTAutonomous extends LinearOpMode {


    // Percent representing decrease in speed for driver controls
    private final double PRECISION_SPEED_MODIFIER = 0.5;

    // This provides the tick count for each rotation of an encoder, it's helpful for using run to position
    private final int ENCODER_TICK = 1680;
    // ANDYMARK 60:1 = 1680

    // Conversion constants
    private final double CM_PER_INCH = 2.56;
    private final double CM_PER_FOOT = 30.48;

    // Wheel diameter NOTE: Measured in cm
    private final double DIAMETER = CM_PER_INCH * 5.15;

    // This value is the distance of 1 rev of the wheels measured in CM!!!!
    private final double CIRCUMFERENCE = Math.PI*DIAMETER;

    // Our custom vuforia object
    private WABOTVuforia vuforia;

    // Hardware map object
    private WABOTHardware h;

    // IMU
    WABOTImu imu;


    /*
    VVVV SETTINGS VVVV
     */


    // Parameters for initializing vuforia
    // NOTE: If Webcam: Direction = BACK, isPortrait = true;
    private final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";
    private final CameraDirection CAMERA_DIRECTION = CameraDirection.BACK;
    private final boolean CAMERA_IS_PORTRAIT = true;

















    // Initializing robot here!
    @Override
    public void runOpMode() {

        // Init of robot

        telemetry.addLine("Status: Initializing, DO NOT PRESS PLAY");
        telemetry.update();

        h = new WABOTHardware(hardwareMap);

        telemetry.addLine("Status: Hardware Map");
        telemetry.update();


        runEncoder(true);

        h.leftLatch.setPosition(0.55f);
        h.rightLatch.setPosition(0.3f);
        h.foundServo.setPosition(0.5f);

        telemetry.addLine("Status: Initializing Vuforia");
        telemetry.update();

        vuforia = new WABOTVuforia(VUFORIA_KEY, CAMERA_DIRECTION, hardwareMap, false, CAMERA_IS_PORTRAIT);

        vuforia.activate();

        telemetry.addLine("Status: Imu");
        telemetry.update();

        imu = new WABOTImu(hardwareMap);

        telemetry.addLine("Status: READY!");
        telemetry.update();

        waitForStart();

        imu.activate();

        run();
    }








    // LEFT GOES TO: 0.2 (OPEN) to 0.55 (CLOSED)
    // RIGHT GOES TO: 0.3 (CLOSED) to 0.66 (OPEN)
    // FOUNDATION: 0 (DOWN) to 0.5 (UP)

    // Actual instructions for robot! All autonomous code goes here!!!
    private void run(){
        runToPos(29*CM_PER_INCH, -0.5f);
        sleep(1000);
        strafe(13.8*CM_PER_INCH, 1f);
        sleep(1000);
        h.foundServo.setPosition(0);
        sleep(1000);
        runToPos(30*CM_PER_INCH, 0.3f);
        sleep(1000);
        h.foundServo.setPosition(0.5f);
        sleep(2000);
        strafe(55*CM_PER_INCH, -1f);

    }















    // Switching motor direction
    private void motorDir(boolean forward){
        if(forward){
            h.BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            h.BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            h.FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            h.FLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        } else {
            h.BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            h.BLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            h.FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            h.FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }












    // Uses encoders to move a specific distance away given powers for each motor
    private void runToPos(double distanceCM, float power1, float power2, float power3, float power4){
        if(power1 < 0){
            h.FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            power1 *= -1;
        }
        if(power2 < 0){
            h.FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            power2 *= -1;
        }
        if(power3 < 0){
            h.BLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            power3 *= -1;
        }
        if(power4 < 0){
            h.BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            power4 *= -1;
        }
        double revs = distanceCM/CIRCUMFERENCE;
        int ticksToRun = (int)(revs * ENCODER_TICK);
        runEncoder(true);
        h.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FLMotor.setTargetPosition(ticksToRun);
        h.FRMotor.setTargetPosition(ticksToRun);
        h.BLMotor.setTargetPosition(ticksToRun);
        h.BRMotor.setTargetPosition(ticksToRun);
        h.FLMotor.setPower(power1);
        h.FRMotor.setPower(power2);
        h.BLMotor.setPower(power3);
        h.BRMotor.setPower(power4);
        while (h.FLMotor.isBusy() && h.FRMotor.isBusy() && h.BLMotor.isBusy() && h.BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
        motorDir(true);
        runEncoder(false);
    }












    // Position the Robot According to Vuforia marker
    private void vuforiaPosition(){
        // TODO Code this
    }


























    // DO NOT TOUCH
    // Robot moves some distance (CM) with a specified power applied
    private void runToPos(double distanceCM, float power){

        if(power < 0){
            power *= -1;
            motorDir(false);
        }
        double revs = distanceCM/CIRCUMFERENCE;
        int ticksToRun = (int)(revs * ENCODER_TICK);
        runEncoder(true);
        h.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FLMotor.setTargetPosition(ticksToRun);
        h.FRMotor.setTargetPosition(ticksToRun);
        h.BLMotor.setTargetPosition(ticksToRun);
        h.BRMotor.setTargetPosition(ticksToRun);
        h.FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        h.BLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        h.FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        h.BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        linearDrive(power);
        while (h.FLMotor.isBusy() && h.FRMotor.isBusy() && h.BLMotor.isBusy() && h.BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
        runEncoder(false);
        motorDir(true);
    }
















    // DO NOT TOUCH
    // Switch between non-encoder and encoder modes of the motors
    private void runEncoder(boolean withEncoder){
        if(withEncoder) {
            h.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            h.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            h.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            h.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }else{
            h.FLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            h.FRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            h.BLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            h.BRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

    }















    // DO NOT TOUCH
    // Runs motors at a specified power
    private void linearDrive(float power){
        h.FLMotor.setPower(power);
        h.FRMotor.setPower(power);
        h.BLMotor.setPower(power);
        h.BRMotor.setPower(power);
    }















    // DO NOT TOUCH
    // Same as "linearDrive" but sets power to 0
    private void stopMotors() {
        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);
    }
















    // DO NOT TOUCH
    // NOTE: Used only with mecanum wheels!
    // Strafes based on power and distance
    private void strafe (double distanceCM, float power){
        distanceCM *= 1.43*0.79;
        if(power < 0){
            power *= -1;
            runToPos(distanceCM, -power, power, power,-power);
        } else if (power > 0){
            runToPos(distanceCM, power, -power, -power,power);
        }
    }














    // DO NOT TOUCH
    // Turns robot
    private void turn (int direction, float power){
        if(direction == -1){
            h.FLMotor.setPower(-power);
            h.BRMotor.setPower(power);
            h.FRMotor.setPower(power);
            h.BLMotor.setPower(-power);
        } else if (direction == 1){
            h.FLMotor.setPower(power);
            h.BRMotor.setPower(-power);
            h.FRMotor.setPower(-power);
            h.BLMotor.setPower(power);
        }
    }















    // DO NOT TOUCH
    // Maintains heading and adjusts if pushed: NEED GYRO
    private void driveStraight(int targetHeading, double startSpeed){
        double heading = imu.getHeading();

        // Depends on IMU vs. GYRO
        // TODO Test this on imu
        if(heading > 180){
            heading = heading - 360;
        }

        double difference = targetHeading - heading;
        double power = difference/90.0;

        h.FLMotor.setPower(startSpeed + power);
        h.FRMotor.setPower(startSpeed - power);
        h.BLMotor.setPower(startSpeed + power);
        h.BRMotor.setPower(startSpeed - power);

    }










    private void strafeLinear(int direction, double power){
        if(direction > 0){
            h.FRMotor.setPower(-power);
            h.FLMotor.setPower(power);
            h.BRMotor.setPower(power);
            h.BLMotor.setPower(-power);
        } else if(direction < 0){
            h.FRMotor.setPower(power);
            h.FLMotor.setPower(-power);
            h.BRMotor.setPower(-power);
            h.BLMotor.setPower(power);
        } else {
            h.FRMotor.setPower(0);
            h.FLMotor.setPower(0);
            h.BRMotor.setPower(0);
            h.BLMotor.setPower(0);
        }
    }










    // DO NOT TOUCH
    // Usable is there is a gyro installed
    private void turnByDegree (int degree) {
        double currentPower = 1;
        boolean right;
        double turnTo;

        if(degree < 0){
            right = false;
            turnTo = convertedHeading((degree*-1) + imu.getHeading());
        } else {
            right = true;
            turnTo = convertedHeading(degree + imu.getHeading());
        }

        // if to the right, turn right, vise versa
        if (right) {
            while (convertedHeading(imu.getHeading()) < turnTo) {

                double difference = turnTo - convertedHeading(imu.getHeading());

                telemetry.addData("Difference: ", difference);
                telemetry.addData("Heading: ", convertedHeading(imu.getHeading()));
                telemetry.addData("CurrentPower: ", currentPower);
                telemetry.update();


                h.FLMotor.setPower(currentPower);
                h.FRMotor.setPower(-currentPower);
                h.BLMotor.setPower(currentPower);
                h.BRMotor.setPower(-currentPower);

                if(difference < 12){
                    currentPower = Math.pow(1.2, 0.3*difference) - 1;
                }
            }
        } else if (!right) {
            while (convertedHeading(imu.getHeading()) < turnTo) {

                double difference = turnTo - convertedHeading(imu.getHeading());

                telemetry.addData("Difference: ", difference);
                telemetry.addData("Heading: ", convertedHeading(imu.getHeading()));
                telemetry.addData("CurrentPower: ", currentPower);
                telemetry.update();

                h.FLMotor.setPower(-currentPower);
                h.FRMotor.setPower(currentPower);
                h.BLMotor.setPower(-currentPower);
                h.BRMotor.setPower(currentPower);

                if(difference < 12){
                    currentPower = Math.pow(1.2, 0.3*difference) - 1;
                }
            }
        }

        stopMotors();
    }


















    // DO NOT TOUCH
    // Used for "turn by degree" ONLY
    public double convertedHeading(double h){
        // TODO Check this for imu
        double heading = h;
        while(heading > 180){
            heading -= (2*(heading-180));

            if(heading < 0){
                heading *= -1;
            }
        }
        return heading;
    }








    // Tank drive controls
    private void tankDrive(){
        double leftStickY = gamepad1.left_stick_y;
        double rightStickY = gamepad1.right_stick_y;

        h.FLMotor.setPower(leftStickY);
        h.FRMotor.setPower(rightStickY);
        h.BLMotor.setPower(leftStickY);
        h.BRMotor.setPower(rightStickY);
    }

    // 360 omni-drive controls
    private void superDrive(){

        // Input
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = -gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;

        // Calculating angle between X and Y inputs on the stick
        double angle = Math.atan2(leftStickY, leftStickX);
        angle = Math.toDegrees(angle);
        angle = Math.abs(angle);
        // Altering value for sake of the program
        if(leftStickY < 0){
            angle = 360 - angle;
        }

        // Power variables
        double v1 = 0, v2 = 0, v3 = 0, v4 = 0;

        // Represents what quadrant our stick is in
        int quadrant = 0;

        // Calculating current quadrant
        if(leftStickX == 0 && leftStickY == 0){
            quadrant = 0;
        } else if(angle >= 0 && angle <= 90){
            quadrant = 1;
        } else if(angle > 90 && angle <= 180){
            quadrant = 2;
        } else if(angle > 180 && angle <= 270){
            quadrant = 3;
        } else if(angle > 270 && angle <= 360) {
            quadrant = 4;
        }

        // Getting our composite input used as a backbone value for movement
        // Short explanation: Always a net Y value, but uses a different percent from each direction based on Y value
        double sampleY = leftStickY;
        double magnitude = Math.abs(sampleY) + Math.abs((1-Math.abs(sampleY))*leftStickX);

        // Based on the quadrant, change the underlying function each wheel depends on
        if(quadrant == 1){
            v1 = magnitude*((angle-45)/45);
            v3 = magnitude*((angle-45)/45);
            v2 = magnitude;
            v4 = magnitude;
        } else if(quadrant == 2){
            v1 = magnitude;
            v3 = magnitude;
            v2 = magnitude*((135-angle)/45);
            v4 = magnitude*((135-angle)/45);
        } else if(quadrant == 3){
            v1 = magnitude*((225-angle)/45);
            v3 = magnitude*((225-angle)/45);
            v2 = -1*magnitude;
            v4 = -1*magnitude;
        } else if(quadrant == 4){
            v1 = -1*magnitude;
            v3 = -1*magnitude;
            v2 = -1*magnitude*((315-angle)/315);
            v4 = -1*magnitude*((315-angle)/315);
        } else if(quadrant == 0){
            v1 = 0;
            v2 = 0;
            v3 = 0;
            v4 = 0;
        }

        // If not using omni-drive, switch to normal turn
        if(rightStickX != 0){
            v1 = -1*rightStickX;
            v2 = rightStickX;
            v3 = rightStickX;
            v4 = -1*rightStickX;
        }

        // Precision controls based on bumpers pressed
        if(gamepad1.left_bumper && !gamepad1.right_bumper){
            v1 *= 0.5;
            v2 *= 0.5;
            v3 *= 0.5;
            v4 *= 0.5;
        }
        if(gamepad1.right_bumper && !gamepad1.left_bumper){
            v1 *= 0.5;
            v2 *= 0.5;
            v3 *= 0.5;
            v4 *= 0.5;
        }
        if(gamepad1.right_bumper && gamepad1.left_bumper){
            v1 *= 0.25;
            v2 *= 0.25;
            v3 *= 0.25;
            v4 *= 0.25;
        }
    }

    // Normal holonomic drive
    private void holoDrive(){
        double leftStickX = gamepad1.right_stick_x;
        double leftStickY = -gamepad1.left_stick_y;
        double rightStickX = gamepad1.left_stick_x;
        double rightStickY = gamepad1.right_stick_y;

        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - (Math.PI / 4);
        double leftX = rightStickX;
        double turn = leftX;

        double v1 = r * Math.cos(robotAngle) + turn;
        double v2 = r * Math.sin(robotAngle) - turn;
        double v3 = r * Math.sin(robotAngle) + turn;
        double v4 = r * Math.cos(robotAngle) - turn;

        if(gamepad1.right_bumper || gamepad1.left_bumper){
            v1 = v1 * PRECISION_SPEED_MODIFIER;
            v2 = v2 * PRECISION_SPEED_MODIFIER;
            v3 = v3 * PRECISION_SPEED_MODIFIER;
            v4 = v4 * PRECISION_SPEED_MODIFIER;
        }
        h.FLMotor.setPower(v1);
        h.FRMotor.setPower(v2);
        h.BLMotor.setPower(v3);
        h.BRMotor.setPower(v4);
    }

    // Clamp function
    public double clamp(double min, double max, double value){
        if(value < min){
            value = min;
        } else if(value > max){
            value = max;
        }

        return value;
    }
}
