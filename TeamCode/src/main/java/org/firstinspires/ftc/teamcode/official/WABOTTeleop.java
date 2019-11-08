package org.firstinspires.ftc.teamcode.official;

/*
 * Wright Angle Robotics #6427 2019-2020
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="WABOTTeleop", group="WABOT")
public class  WABOTTeleop extends OpMode {


    // Declare OpMode members.
    WABOTHardware h;

    // Intermediate values for input
    double as1 = 0;
    double as2 = 0.178;
    double as3 = 0.8;
    float intakePow = 0;

    // Speed modifier for drive controls
    private final double PRECISION_SPEED_MODIFIER = 0.5;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Tell the driver that initialization is complete.
        h = new WABOTHardware(hardwareMap);
        runEncoder(false);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        // Starting Positions for Servos
        h.leftLatch.setPosition(0.36);
        h.rightLatch.setPosition(0.48);
        h.foundServo.setPosition(0.5);
        h.armServo1.setPosition(0);
        h.armServo2.setPosition(0.1564);
        h.armServo3.setPosition(0.791);
        h.armServo4.setPosition(0.5);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Gamepad2 input
        input();
        // Drive train controls
        superDrive();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status:", "Stopped");

        // Stops motors just in case
        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);
    }


    // Swing Arm: (OPEN) 1 - 0 (Down)
    // END arm: (UP) 1 - 0 (DOWN)
    // Middle Servo: 0 = (IN) 1 = (OUT)


    private void input(){

        // Triggers control intake/outtake
        if(gamepad2.right_trigger > 0){
            intakePow = gamepad2.right_trigger;
        }else if(gamepad2.left_trigger > 0){
            intakePow = -gamepad2.left_trigger;
        } else {
            intakePow = 0;
        }
        h.leftIntake.setPower(intakePow);
        h.rightIntake.setPower(intakePow);

        if(gamepad2.start){
            h.armServo4.setPosition(1);
        }
        if(gamepad2.back){
            h.armServo4.setPosition(0);
        }


        // Four positions for intake wheels (A-B-X-Y)
        if(gamepad2.a){
            h.leftLatch.setPosition(0.33f);
            h.rightLatch.setPosition(0.55f);
        }
        if(gamepad2.b){
            as1 = 0;
            as2 = 0.1564;
            as3 = 0.791;
        }
        if(gamepad2.x){
            h.leftLatch.setPosition(0.283);
            h.rightLatch.setPosition(0.597);
        }
        if(gamepad2.y){
            h.leftLatch.setPosition(0.55);
            h.rightLatch.setPosition(0.3);
        }

        // 1 = middle 2 = top 3 = end

        // Moving forward/back
        h.armMotor.setPower(gamepad2.right_stick_y);

        // Binary movement
        if(gamepad2.dpad_left){
            as1 = 0;
        }
        if(gamepad2.dpad_right){
            as1 = 1;
        }

        // Changing position by gamepad input
        as2 += 0.002 * -gamepad2.left_stick_y;
        as3 += 0.005 * gamepad2.left_stick_x;

        // Clamps servo values
        as1 = clamp(0.3, 1, as1);
        as2 = clamp(0, 0.32, as2);
        as3 = clamp(0.41, 1, as3);

        // Setting the servo position
        h.armServo1.setPosition(as1);
        h.armServo2.setPosition(as2);
        h.armServo3.setPosition(as3);

        // Updating telemetry
        telemetry.addData("Middle: ", as1);
        telemetry.addData("Swing: ", as2);
        telemetry.addData("End: ", as3);
    }











    // Switch between encoder and non-encoder settings
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
