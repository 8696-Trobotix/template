// This is an "external hardware class".
// It allows us to share hardware initialization between the TeleOp and autonomous programs.

package org.firstinspires.ftc.teamcode;

// Import mollusc packages.
import org.firstinspires.ftc.teamcode.mollusc.auto.odometry.*;
import org.firstinspires.ftc.teamcode.mollusc.drivetrain.*;
import org.firstinspires.ftc.teamcode.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class SampleHardwareClass {

    // `config` will be used to retrieve values from `config.txt`.
    public Configuration config;

    // `base` contains references to all four motors used in a Mecanum drivetrain.
    public DrivetrainBaseFourWheel base;

    // `deadWheels` references the three encoders on the odometry pods.
    // It will be used to calculate the robot's position + orientation (pose).
    public DeadWheels deadWheels;

    public IMU imu;

    // In the event that a configuration value cannot be retrieved, this constructor must throw an exception.
    public SampleHardwareClass(Configuration config, boolean resetIMU) throws Exception {
        this.config = config;

        // `make` provides a shortcut to initializing hardware devices.
        Make make = new Make();

        imu = make.imu(
            "imu",
            RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD,
            resetIMU
        );

        base = new DrivetrainBaseFourWheel(
            make.motor("frontLeft", getStringConfigDirection("frontLeft direction").motor), 
            make.motor("frontRight", getStringConfigDirection("frontRight direction").motor), 
            make.motor("rearLeft", getStringConfigDirection("rearLeft direction").motor), 
            make.motor("rearRight", getStringConfigDirection("rearRight direction").motor)
        );

        // `TPR` is short for ticks-per-rotation and is used in the context of encoders.
        int TPR = config.getInteger("TPR");

        // Important: adjust these motor names to suit your set up.
        DcMotorEx leftEncoderMotor = make.motor("leftEncoder", DcMotorEx.Direction.FORWARD);
        DcMotorEx rightEncoderMotor = make.motor("rightEncoder", DcMotorEx.Direction.FORWARD);
        DcMotorEx centerEncoderMotor = make.motor("centerEncoder", DcMotorEx.Direction.FORWARD);

        // The `Encoder` class wraps a motor and provides encoder distance values.
        Encoder leftEncoder = new Encoder(leftEncoderMotor, -1.0, TPR, 35);
        Encoder rightEncoder = new Encoder(rightEncoderMotor, 1.0, TPR, 35);
        Encoder centerEncoder = new Encoder(centerEncoderMotor, 1.0, TPR, 35);

        deadWheels = new DeadWheels(
            new Pose(0, 0, 0),
            leftEncoder, rightEncoder, centerEncoder, 
            -352.435, 30
        );

        // Please see class declarations in mollusc for a better description 
        // on how to initialize certain classes.
    }

    public DirectionGroup getStringConfigDirection(String key) throws Exception {
        String direction = config.getString(key);
        return new DirectionGroup(direction);
    }

    public class DirectionGroup {
        public DcMotorEx.Direction motor;

        public DirectionGroup(String direction) throws Exception {
            if (!direction.equals("forward") && !direction.equals("reverse")) {
                throw new Exception("Invalid motor direction value: " + direction);
            }
            if (direction.equals("forward")) {
                this.motor = DcMotorEx.Direction.FORWARD;
            } else {
                this.motor = DcMotorEx.Direction.REVERSE;
            }
        }
    }
}
