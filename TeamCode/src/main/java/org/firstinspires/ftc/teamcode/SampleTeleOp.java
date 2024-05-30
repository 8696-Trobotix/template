package org.firstinspires.ftc.teamcode;

// Import mollusc packages.
import org.firstinspires.ftc.teamcode.mollusc.drivetrain.*;
import org.firstinspires.ftc.teamcode.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="SampleTeleOp", group="OpMode")

// `SampleTeleOp` inherits from `MolluscLinearOpMode` which is functionally identical to `LinearOpMode`.
// However, inheriting from `MolluscLinearOpMode` is required for technical reasons.
public class SampleTeleOp extends MolluscLinearOpMode {

    // `config` will be used to retrieve values from `config.txt`.
    private Configuration config;

    // `Drivetrain` is an interface. Please learn about interfaces if you aren't familiar with them.
    private Drivetrain drivetrain;

    // This is the external hardware class from `SampleHardwareClass.java`.
    private SampleHardwareClass sampleHardware;

    // Both Mecanum drives implement the `Drivetrain` interface.
    private MecanumFieldCentric fieldCentricDrive;
    private MecanumRobotCentric robotCentricDrive;
    private double maxDrive, maxStrafe, maxTurn;
    private double maxDriveReduced, maxStrafeReduced, maxTurnReduced;
    private double drive, strafe, turn;

    // See more about enums below.
    private SystemState reducedDrivePowerState = SystemState.OFF;

    // Use `molluscRunOpMode` instead of `runOpMode`.
    @Override
    public void molluscRunOpMode() {
        telemetry.setAutoClear(false);

        // Since retrieving configuration values can throw exceptions, 
        // a convenient utility is provided to gracefully terminate the 
        // OpMode if one is encountered.
        // `Configuration.handle` will request that the OpMode stops 
        // if the lambda function throws an exception.
        // Please learn about lambda functions / expressions if you aren't familiar with them.
        Configuration.handle(() -> { // This is the start of a lambda function.
            // Initializing without specifying a file name will use `assets/mollusc/config.txt` by default.
            config = new Configuration();

            // See `config.txt` for these values.
            boolean fieldCentric = config.getBoolean("field centric on");
            maxDrive = config.getDouble("drive power max");
            maxStrafe = config.getDouble("strafe power max");
            maxTurn = config.getDouble("turn power max");
            maxDriveReduced = config.getDouble("drive power max reduced");
            maxStrafeReduced = config.getDouble("strafe power max reduced");
            maxTurnReduced = config.getDouble("turn power max reduced");

            // Initialize the external hardware class with `config` so that `config` doesn't 
            // have to be initialized twice.
            sampleHardware = new SampleHardwareClass(config, false);

            // See class declarations in mollusc for more details.
            fieldCentricDrive = new MecanumFieldCentric(sampleHardware.base, sampleHardware.imu);
            fieldCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn, Math.PI);
            robotCentricDrive = new MecanumRobotCentric(sampleHardware.base);
            robotCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn);

            if (fieldCentric) {
                drivetrain = fieldCentricDrive;
                telemetry.speak("Driving field centric.");
            } else {
                drivetrain = robotCentricDrive;
                telemetry.speak("Driving robot centric.");
            }
        });

        telemetry.setAutoClear(true);

        waitForStart();

        while (opModeIsActive()) {

            // Quadratic controller sensitivity.
            // The `Controls` class provides various static methods for interpreting 
            // gamepad inputs.
            drive  = Controls.quadratic(-gamepad1.left_stick_y);
            strafe = Controls.quadratic(gamepad1.left_stick_x);
            turn   = Controls.quadratic(gamepad1.right_stick_x);

            drivetrain.drive(drive, strafe, turn);

            // This block allows us to switch between field-centric and robot-centric drive modes.
            // This expression will evaluate to true only on the first press of button X.
            if (Controls.singlePress("toggle drive", gamepad1.x)) {
                if (drivetrain instanceof MecanumFieldCentric) {
                    drivetrain = robotCentricDrive;
                } else {
                    drivetrain = fieldCentricDrive;
                }
            }

            // Similarly, toggling between power modes.
            if (Controls.singlePress("toggle reduce", gamepad1.b)) {
                if (reducedDrivePowerState == SystemState.OFF) {
                    reducedDrivePowerState = SystemState.ON;
                    fieldCentricDrive.setDriveParams(maxDriveReduced, maxStrafeReduced, maxTurnReduced, Math.PI);
                    robotCentricDrive.setDriveParams(maxDriveReduced, maxStrafeReduced, maxTurnReduced);
                } else {
                    reducedDrivePowerState = SystemState.OFF;
                    fieldCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn, Math.PI);
                    robotCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn);
                }
            }
        }
    }

    // This is an enum. It's better to use enums in some places than boolean values.
    // Please learn more about enums if you aren't familiar with them.
    private enum SystemState {
        ON, OFF
    }
}
