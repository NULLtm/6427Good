package org.firstinspires.ftc.teamcode.official;

/*
 * Wright Angle Robotics #6427 2019-2020
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;


@TeleOp(name="WABOTTeleop", group="WABOT")
public class  WABOTTeleop extends OpMode {


    // Declare OpMode members.
    WABOTHardware h;

    // IMU
    WABOTImu imu;

    private WABOTVuforia vuforia;

    private final String VUFORIA_KEY = "ATs85vP/////AAABmedvSEuRQ0j9uYwlATaryQxyeVF6AtDWjTZ/2e6s8KELjPp1fDUV3Nn3X1xEZSoPk0Y81/6kr2k/8Q0xdlNkCDIJ+qBpXM8vpA+5qL7mYY6KthDalcBqD8pKiEBiSy0gW0wzniDtDR/Bf4ndSizQgoI10u9PD248vTfkt8NxJLsgM98pyCyeYZ2c16yLcASypCOhFJvljA7M6DM+qfWgWnOWXiVd2OZLsLtFcHZu4aEKjCHwqnlk9KYSI5BT8I4i+3FoE/JffsIzAl/iXMPu7w6eJJXYqNq7lGCzMRwfn+6OoYA51sy/Ahr/uyWUj/u0nzgF/IlRkteKXks+eUok5kFLeT2KxkbpNVwie11YgQRg";
    private final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = VuforiaLocalizer.CameraDirection.BACK;
    private final boolean CAMERA_IS_PORTRAIT = false;

    // Intermediate values for input
    double as1 = 0;
    double as2 = 0.178;
    double as3 = 0.8;
    float intakePow = 0;

    double servoPosLeft = 0.8;
    double servoPosRight = 0.5;

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
        //imu = new WABOTImu(hardwareMap);

        //vuforia = new WABOTVuforia(VUFORIA_KEY, CAMERA_DIRECTION, hardwareMap, true, CAMERA_IS_PORTRAIT, h);

        //vuforia.activate();
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
        //imu.activate();

        h.leftFound.setPosition(1f);
        h.rightFound.setPosition(0.5f);
        h.backArm.setPosition(0.45f);
        h.frontArm.setPosition(1f);
        h.leftIntakeServo.setPosition(0.74);
        h.rightIntakeServo.setPosition(0.558);
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
        // Updating to log
        telemetry.addData("Status:", "Stopped");

        // Stops motors just in case
        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);
    }

    private void input(){

        //telemetry.addData("Heading: ", imu.getHeading());
        telemetry.addData("Distance Side: ", h.ods.getDistance(DistanceUnit.CM));
        telemetry.addData("Distance Back: ", h.ods2.getDistance(DistanceUnit.CM));


        // Triggers control intake/outtake
        if(gamepad1.right_trigger > 0){
            intakePow = gamepad1.right_trigger;
            h.rightIntakeMini.setPosition(0);
            h.leftIntakeMini.setPosition(1);
        }else if(gamepad1.left_trigger > 0){
            intakePow = -gamepad1.left_trigger;
            h.rightIntakeMini.setPosition(1);
            h.leftIntakeMini.setPosition(0);
        } else {
            h.rightIntakeMini.setPosition(0.5);
            h.leftIntakeMini.setPosition(0.5);
            intakePow = 0;
        }
        h.leftIntake.setPower(-intakePow);
        h.rightIntake.setPower(intakePow);

        h.slideMotorLeft.setPower(-gamepad2.left_stick_y);
        h.slideMotorRight.setPower(-gamepad2.left_stick_y);


        // TESTING SERVO POSITIONS
        /*
        servoPosLeft += gamepad2.right_stick_y*0.001;
        servoPosRight += gamepad2.left_stick_y*0.001;

        h.leftIntakeServo.setPosition(servoPosLeft);
        h.rightIntakeServo.setPosition(servoPosRight);

        telemetry.addData("Servo Pos LEFT: ", servoPosLeft);
        telemetry.addData("Servo Pos RIGHT: ", servoPosRight);
         */



        if(gamepad2.right_stick_y < -0.5){
            h.linearServoLeft.setPosition(0.84);
            h.linearServoRight.setPosition(0.20);
        }
        if(gamepad2.right_stick_y > 0.5){
            h.linearServoLeft.setPosition(0.42);
            h.linearServoRight.setPosition(0.63);
        }

//        if(gamepad2.back){
//            h.linearServoLeft.setPosition(0.49);
//            h.linearServoRight.setPosition(0.57);
//        }


        if(gamepad1.dpad_right){
            h.leftIntakeServo.setPosition(0.74);
            h.rightIntakeServo.setPosition(0.558);
        }

        if(gamepad1.dpad_left){
            h.leftIntakeServo.setPosition(0.343);
            h.rightIntakeServo.setPosition(0.854);
        }

        if(gamepad1.dpad_up){
            h.leftIntakeServo.setPosition(0.55);
            h.rightIntakeServo.setPosition(0.713);
        }

        if(gamepad1.x){
            h.leftFound.setPosition(0.5f);
            h.rightFound.setPosition(1f);
        }

        if(gamepad1.b){
            h.leftFound.setPosition(1f);
            h.rightFound.setPosition(0.5f);
        }

        if(gamepad2.y){
            h.armServo.setPosition(0.59f);
        }

        if(gamepad2.a){
            h.armServo.setPosition(0.23f);
        }


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

        h.FRMotor.setPower(v1);
        h.FLMotor.setPower(v2);
        h.BLMotor.setPower(v3);
        h.BRMotor.setPower(v4);
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

    // Clamp a double between a max and a min
    public double clamp(double min, double max, double value){
        if(value < min){
            value = min;
        } else if(value > max){
            value = max;
        }

        return value;
    }

}
