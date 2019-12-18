package org.firstinspires.ftc.teamcode.angleDrifter;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Vector3;

import java.util.ArrayList;
import java.util.List;

public class RoutableRobot {

    public List<RoutePoint> points = new ArrayList<>();

    public void runToPoint(double magnitudeS, RoutePoint point, double heading, Vector3 robotPos, DcMotor FLMotor, DcMotor FRMotor, DcMotor BLMotor, DcMotor BRMotor) {
        for(RoutePoint x: points){

            double offsetX = point.offsetX - robotPos.x;
            double offsetY = point.offsetY - robotPos.y;

            while(Math.abs(offsetX) > 10 && Math.abs(offsetY) > 10) {

                offsetX = point.offsetX - robotPos.x;
                offsetY = point.offsetY - robotPos.y;

                double angleS = Math.atan2(offsetY, offsetX);

                angleS *= Math.toDegrees(angleS);

                double difference = angleS - heading;

                int quadrantS = 0;

                // Calculating current quadrant
                if (difference >= 0 && difference <= 90) {
                    quadrantS = 1;
                } else if (difference > 90 && difference <= 180) {
                    quadrantS = 2;
                } else if (difference > 180 && difference <= 270) {
                    quadrantS = 3;
                } else if (difference > 270 && difference <= 360) {
                    quadrantS = 4;
                }

                double v1 = 0, v2 = 0, v3 = 0, v4 = 0;

                if (quadrantS == 1) {
                    v1 = magnitudeS * ((angleS - 45) / 45);
                    v3 = magnitudeS;
                    v2 = magnitudeS;
                    v4 = magnitudeS * ((angleS - 45) / 45);
                } else if (quadrantS == 2) {
                    v1 = magnitudeS;
                    v3 = magnitudeS * ((135 - angleS) / 45);
                    v2 = magnitudeS * ((135 - angleS) / 45);
                    v4 = magnitudeS;
                } else if (quadrantS == 3) {
                    v1 = magnitudeS * ((225 - angleS) / 45);
                    v3 = -1 * magnitudeS;
                    v2 = -1 * magnitudeS;
                    v4 = magnitudeS * ((255 - angleS) / 45);
                } else if (quadrantS == 4) {
                    v1 = -1 * magnitudeS;
                    v3 = -1 * magnitudeS * ((angleS - 315) / 45);
                    v2 = -1 * magnitudeS * ((angleS - 315) / 45);
                    v4 = -1 * magnitudeS;
                } else if (quadrantS == 0) {
                    v1 = 0;
                    v2 = 0;
                    v3 = 0;
                    v4 = 0;
                }

                FRMotor.setPower(v1);
                FLMotor.setPower(v2);
                BLMotor.setPower(v3);
                BRMotor.setPower(v4);
            }

        }
    }
}
