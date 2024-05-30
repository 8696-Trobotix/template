package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.mollusc.auto.interpreter.*;
import org.firstinspires.ftc.teamcode.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;
import org.firstinspires.ftc.teamcode.mollusc.auto.*;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

// This Autonomous sample uses the manual method of using EasyOpenCV.
// Newer versions of the SDK provide a `VisionPortal` API that simplifies 
// this process.
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCamera;

@Autonomous(name="SampleAutonomous", group="OpMode")

// `SampleAutonomous` inherits from `MolluscLinearOpMode` which is functionally identical to `LinearOpMode`.
// However, inheriting from `MolluscLinearOpMode` is required for technical reasons.
public class SampleAutonomous extends MolluscLinearOpMode {

    // `config` will be used to retrieve values from `config.txt`.
    private Configuration config;

    // `Auto` is an interface. Please learn about interfaces if you aren't familiar with them.
    private Auto auto;

    // This is the external hardware class from `SampleHardwareClass.java`.
    private SampleHardwareClass sampleHardware;

    // `interpreter` will be used to execute commands from the autonomous script.
    private Interpreter interpreter;

    // Say that red is true and blue is false 
    private boolean alliance = true;

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
        Configuration.handle(() -> {
            // Initializing without specifying a file name will use `assets/mollusc/config.txt` by default.
            config = new Configuration();

            // `inputBoolean` prompts the driver to select between 
            // two options ("Red" and "Blue") in the driver station 
            // using controller A.
            alliance = Configuration.inputBoolean("Alliance", "Red", "Blue", alliance);

            // Initialize the external hardware class with `config` so that `config` doesn't 
            // have to be initialized twice.
            sampleHardware = new SampleHardwareClass(config, true);

            // Initialize the interpreter with the script located at `assets/sample.txt`.
            interpreter = new Interpreter(new Asset("sample.txt"));

            // This function is defined below in this file.
            // It specifies custom commands that can be used with the interpreter.
            register();

            // See `config.txt` for these values.
            double maxDrivePower = config.getDouble("autonomous drive power");

            // See class declarations in mollusc for more details.
            auto = new MecanumAutoII(
                sampleHardware.base, 
                sampleHardware.deadWheels, 
                interpreter, 
                new PIDF(0.005, 0, 0, 0.15, 0.25, 1), // Three PIDF controllers, one per driving, strafing, and turning.
                new PIDF(0.009, 0, 0, 0.2, 0.25, 1),
                new PIDF(1.4, 0.00, 0.00, 0.15, 0.25, 1),
                maxDrivePower, 
                30,
                6
            );

            // This method must be called for the standard drive and wait_seconds 
            // commands to be configured for use in the autonomous script.
            auto.register();
        });

        telemetry.setAutoClear(true);

        // Initializing a webcame connected via USB 2.0 or USB 3.0 for EasyOpenCV. 
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {
            }
        });

        // If you want to use a vision pipeline, initialize it and pass it to the webcame here.

        // webcam.setPipeline(insertYourPipelineHere);

        // You may want to use the now built-in `VisionPortal` API instead of all of the above.

        // This enables automatic bulk caching for I2C devices, which can speed up cycle times.
        PIDF.bulkMode();

        waitForStart();

        webcam.pauseViewport();

        // Since the running script can throw an exception, 
        // a convenient utility is provided to gracefully terminate the 
        // OpMode if one is encountered.
        // `Configuration.handle` will request that the OpMode stops 
        // if the lambda function throws an exception.
        // Please learn about lambda functions / expressions if you aren't familiar with them.
        Configuration.handle(() -> { // This is the start of a lambda function.
            // Running the script with debug messages enabled (true).
            interpreter.run(true);
        });
    }

    // The aforementioned `register` function.
    private void register() {
        // Specifies a command called "Nothing" that does nothing.
        // Such commands can be useful for conditional logic.
        interpreter.register("Nothing", Interpreter.noneAction);

        // Specifies a command called "SaySomethingNTimes" that uses TTS to speak 
        // a string passed to the command an N amount of times.
        interpreter.register("SaySomethingNTimes", (Object[] args) -> { // Lambda function.
            for (int i = 0; i < (Integer)args[1]; ++i) {
                telemetry.speak((String)args[0]); // You must cast the objects to the required types before use.
            }
        }, String.class, Integer.class); // You must specify how many and what types of arguments are expected.
        // See mollusc for more details on advanced interpreter usage.
    }
}
