package org.firstinspires.ftc.teamcode.official;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class WABOTHardware {
    private HardwareMap hardwareMap;


    // OLD
    // leftServo: 0.2 = OPEN, 0.55 = CLOSED
    // rightServo: 0.3 = CLOSED, 0.66 = OPEN
    // foundServo: 0 = DOWN, 0.5 = UP

    // NEW
    // backArm: 1 = DOWN, 0 = UP
    // frontArm: 1 = DOWN, 0 = UP
    // leftFound: 1 = UP, 0.5 = DOWN
    // rightFound: 1 = DOWN, 0.5 = UP

    public DcMotor FLMotor;
    public DcMotor FRMotor;
    public DcMotor BLMotor;
    public DcMotor BRMotor;
    public Servo leftFound;
    public Servo rightFound;
    public Servo backArm;
    public Servo frontArm;
    public Rev2mDistanceSensor ods;

    protected WABOTHardware(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        initializeMap();
    }
    private void initializeMap() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        leftFound = hardwareMap.get(Servo.class, "leftFound");
        rightFound = hardwareMap.get(Servo.class, "rightFound");
        backArm = hardwareMap.get(Servo.class, "backArm");
        frontArm = hardwareMap.get(Servo.class, "frontArm");
        ods = hardwareMap.get(Rev2mDistanceSensor.class, "ods");




        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}