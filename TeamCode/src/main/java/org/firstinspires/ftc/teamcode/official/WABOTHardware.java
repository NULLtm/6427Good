package org.firstinspires.ftc.teamcode.official;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class WABOTHardware {
    private HardwareMap hardwareMap;


    // OLD
    // leftServo: 0.2 = OPEN, 0.55 = CLOSED
    // rightServo: 0.3 = CLOSED, 0.66 = OPEN
    // foundServo: 0 = DOWN, 0.5 = UP

    // NEW
    // backArm: 1 = DOWN, 0 = UP
    // frontArm: 0 = DOWN, 1 = UP
    // leftFound: 1 = UP, 0.5 = DOWN
    // rightFound: 1 = DOWN, 0.5 = UP
    // leftIntakeServo: 0.62 = OUT, 1 = IN, 0.8 = INTAKE
    // rightIntakeServo: 0.558 = IN, 0.854 = OUT, 0.68 = INTAKE

    final double LEFTINTAKESERVO_IN = 1;
    final double LEFTINTAKESERVO_OUT = 0.62;
    final double LEFTINTAKESERVO_INTAKE = 0.76;
    final double RIGHTINTAKESERVO_IN = 0.558;
    final double RIGHTINTAKESERVO_OUT = 0.854;
    final double RIGHTINTAKESERVO_INTAKE = 0.72;

    // 850-2150 RANGE

    public DcMotor FLMotor;
    public DcMotor FRMotor;
    public RevTouchSensor touch;
    public DcMotor BLMotor;
    public DcMotor BRMotor;
    public DcMotor leftIntake;
    public DcMotor rightIntake;
    public Servo leftFound;
    public Servo rightFound;
    public Servo backArm;
    public Servo frontArm;
    public Rev2mDistanceSensor ods;
    public Rev2mDistanceSensor ods2;
    public Rev2mDistanceSensor ods3;
    public WebcamName webcam;
    public Servo linearServoLeft;
    public Servo linearServoRight;
    public DcMotor slideMotorLeft;
    public DcMotor slideMotorRight;
    public Servo armServo;
    public Servo leftIntakeServo;
    public Servo rightIntakeServo;
    public Servo leftIntakeMini;
    public Servo rightIntakeMini;


//hi owen
//you're adopted
    protected WABOTHardware(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        initializeMap();
    }
    private void initializeMap() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        webcam = hardwareMap.get(WebcamName.class, "webcam");
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        touch = hardwareMap.get(RevTouchSensor.class, "touch");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        leftIntakeServo = hardwareMap.get(Servo.class, "leftIntakeServo");
        rightIntakeServo = hardwareMap.get(Servo.class, "rightIntakeServo");
        leftIntake = hardwareMap.get(DcMotor.class, "leftIntake");
        rightIntake = hardwareMap.get(DcMotor.class, "rightIntake");
        leftIntakeMini = hardwareMap.get(Servo.class, "leftIntakeMini");
        rightIntakeMini = hardwareMap.get(Servo.class, "rightIntakeMini");
        leftFound = hardwareMap.get(Servo.class, "leftFound");
        rightFound = hardwareMap.get(Servo.class, "rightFound");
        backArm = hardwareMap.get(Servo.class, "backArm");
        frontArm = hardwareMap.get(Servo.class, "frontArm");
        ods = hardwareMap.get(Rev2mDistanceSensor.class, "ods");
        ods2 = hardwareMap.get(Rev2mDistanceSensor.class, "ods2");
        ods3 = hardwareMap.get(Rev2mDistanceSensor.class, "ods3");
        linearServoLeft = hardwareMap.get(Servo.class, "linearServoLeft");
        linearServoRight = hardwareMap.get(Servo.class, "linearServoRight");
        slideMotorLeft = hardwareMap.get(DcMotor.class, "slideMotorLeft");
        slideMotorRight = hardwareMap.get(DcMotor.class, "slideMotorRight");
        armServo = hardwareMap.get(Servo.class, "armServo");



        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}