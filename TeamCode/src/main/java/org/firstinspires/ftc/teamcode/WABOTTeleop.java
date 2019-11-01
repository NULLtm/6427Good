package org.firstinspires.ftc.teamcode;

/*
 * Wright Angle Robotics #6427 2019-2020
 *
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="WABOTTeleop", group="WABOT")
//@Disabled
public class  WABOTTeleop extends OpMode {
    // Declare OpMode members.
    WABOTHardware h;
    float intakePow = 0;

    double as1 = 0;
    double as2 = 0.178;
    double as3 = 0.8;
    double middleServoTarget = 0;

    // Constant
    private final double PRECISION_SPEED_MODIFIER = 0.5;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        final double PRECISION_SPEED_MODIFIER = 0.5;
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
        //holoDrive();
        superDrive();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status:", "Stopped");

        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);
    }


    // Swing Arm: (OPEN) 1 - 0 (Down)
    // END arm: (UP) 1 - 0 (DOWN)
    // Middle Servo: 0 = (IN) 1 = (OUT)

    private void input(){
        if(gamepad2.right_trigger > 0){
            intakePow = gamepad2.right_trigger;
        }else if(gamepad2.left_trigger > 0){
            intakePow = -gamepad2.left_trigger;
        } else {
            intakePow = 0;
        }

        h.leftIntake.setPower(intakePow);
        h.rightIntake.setPower(intakePow);

        /*while(Math.abs(as1-middleServoTarget) > 0.01){
            if(as1 > middleServoTarget){
                as1 -= 0.0002;
            }
            if(as1 < middleServoTarget){
                as1 += 0.0002;
            }
        }*/

        if(gamepad2.start){
            h.armServo4.setPosition(1);
        }
        if(gamepad2.back){
            h.armServo4.setPosition(0);
        }

        // IMPORTANT: Ideal servo pos for intake: LEFT: 0.33 RIGHT: 0.55
        // IMPORTANT: Ideal for closed but pickup, LEFT: 0.283 RIGHT: 0.597
        // IMPORTANT: STARTING ARM POS: Swing: 0.1564 END: 0.791
        // IMPORTANT: Block Grabber Servo Pos: 1 = Close, 0 = Open
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
        h.armMotor.setPower(gamepad2.right_stick_y);

        if(gamepad2.dpad_left){
            as1 = 0;
        }
        if(gamepad2.dpad_right){
            as1 = 1;
        }

        as2 += 0.002 * -gamepad2.left_stick_y;
        as3 += 0.005 * gamepad2.left_stick_x;

        as1 = clamp(0.3, 1, as1);
        as2 = clamp(0, 0.32, as2);
        as3 = clamp(0.41, 1, as3);

        h.armServo1.setPosition(as1);
        h.armServo2.setPosition(as2);
        h.armServo3.setPosition(as3);

        telemetry.addData("Middle: ", as1);
        telemetry.addData("Swing: ", as2);
        telemetry.addData("End: ", as3);
    }
// RIGHT IS IN
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

    private void tankDrive(){
        double leftStickY = gamepad1.left_stick_y;
        double rightStickY = gamepad1.right_stick_y;

        h.FLMotor.setPower(leftStickY);
        h.FRMotor.setPower(rightStickY);
        h.BLMotor.setPower(leftStickY);
        h.BRMotor.setPower(rightStickY);
    }

    private void superDrive(){
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = -gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;

        double angle = Math.atan2(leftStickY, leftStickX);

        angle = Math.toDegrees(angle);

        angle = Math.abs(angle);

        if(leftStickY < 0){
            angle = 360 - angle;
        }

        telemetry.addData("Angle:", angle);

        double v1 = 0, v2 = 0, v3 = 0, v4 = 0;

        int quadrant = 0;

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

        double sampleY = leftStickY;
        double magnitude = Math.abs(sampleY) + Math.abs((1-Math.abs(sampleY))*leftStickX);

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

        if(rightStickX != 0){
            v1 = -1*rightStickX;
            v2 = rightStickX;
            v3 = rightStickX;
            v4 = -1*rightStickX;
        }

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

        telemetry.addData("Front Right:", v1);
        h.FRMotor.setPower(v1);
        telemetry.addData("Front Left:", v2);
        h.FLMotor.setPower(v2);
        telemetry.addData("Back Left:", v3);
        h.BLMotor.setPower(v3);
        telemetry.addData("Back Right:", v4);
        h.BRMotor.setPower(v4);
        telemetry.addData("Quadrant", quadrant);
        telemetry.addData("Left Y Value:", magnitude);
    }

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

    public double clamp(double min, double max, double value){
        if(value < min){
            value = min;
        } else if(value > max){
            value = max;
        }

        return value;
    }

}
