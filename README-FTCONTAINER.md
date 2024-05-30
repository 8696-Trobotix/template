# ftcontainer
![image](https://user-images.githubusercontent.com/92550885/232341709-e8e35d97-c334-4ab5-a9e5-e60363a2a2d7.png)
🔨 Build the [FtcRobotController](https://github.com/FIRST-Tech-Challenge/FtcRobotController) in a development container on *practically* any device.

## About
Android Studio is the recommended IDE for developing with the FTC SDK, with OnBot Java being an alternative when Android Studio isn't available.  
However, OnBot Java requires the physical robot to be present in order to build the SDK, and lacks version control.  
The purpose of this project is to provide a [development container](https://docs.github.com/en/codespaces/setting-up-your-project-for-codespaces/adding-a-dev-container-configuration/introduction-to-dev-containers) that **runs in the cloud**, allowing programmers to build their projects anywhere - with or without the robot.  
Additionally, the development container is configured to run on the latest release of the Ubuntu distribution, providing access version control and additional software packages.  
> **Note**
> This is not a robot simulator.  
> Nor does it provide a method of installing the robot controller APK, though you could try to download it and manually push to the phone or Control Hub.

## Getting Started
> **Note**
> This is a guide for using the development container on [GitHub Codespaces](https://github.com/features/codespaces).  
> If you are able to run it on another platform, consider submitting a pull request!  
> Codespaces documentation can be found [here](https://docs.github.com/en/codespaces).
0. Prerequisites:
    - A GitHub account.
1. On this repository's [page](https://github.com/8696-Trobotix/ftcontainer), click on [`<> Code ⯆`].
    - Or click this and skip step two:  
    [![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/8696-Trobotix/ftcontainer?quickstart=1)
2. Navigate to [`Codespaces`].
3. Create a Codespace on main.
    - Initial configuration usually takes a couple minutes.
        - If configuration fails, you will be prompted to view a creation log.
        - Please submit an issue with relevant details from this log.
    - You can access your created Codespaces by navigating to Codespaces menu from the GitHub dashboard.
    - Subsequent startups of a Codespace will take under half a minute.
4. Expand the `FtcRobotController` directory and navigate to `TeamCode/.../teamcode`, or delete the pre-cloned project and clone your own repository.
    - By default and for security, Codespaces are prevented from accessing the contents of private repositories aside from the one they're created in.
    - See [here](https://docs.github.com/en/codespaces/managing-your-codespaces/managing-repository-access-for-your-codespaces) for how to add permissions.
5. Open the README file in `teamcode` or create an OpMode.
6. Use the shortcut <kbd>CTRL</kbd> + <kbd>SHIFT</kbd> + <kbd>B</kbd>, or the build shortcut, to run the build task.
    - Be sure that a file in the project to be built is actively open in the Codespace editor (VSCode), as the build task uses this information to determine which project to build.
    - The initial build and configuring process will take a couple minutes.
    - Subsequent builds will be under half a minute.
    - If the build fails the first time due to the Gradle Daemon "disappearing unexpectedly", try again.
> **Note**
> Codespaces is a free service, but has usage limits. You can view your usage [here](https://github.com/settings/billing).  
> You will not be charged by default if your usage exceeds the amount allotted, assuming no payment information is provided for your account.

## Additional Options
### Build with GUI
If if a GUI is preferred for building, create your Codespace on the [gradle-extension](https://github.com/8696-Trobotix/ftcontainer/tree/gradle-extension) branch.  
Please read the README in this branch for instructions, clarifications, and limitations.
### Others
`.devcontainer/Dockerfile` - The platform-tools and tools Android SDK packages are not installed by default. Uncomment their respective lines here and rebuild the container to add them.  
`.devcontainer/post-create-env.sh` - Configure additional environment variables here, such as adding the path of the packages above to PATH.  
`build.sh` - The build script. By default, Gradle is set to debug build the `TeamCode` of the currently active file in the editor using at most two worker threads. When building a module not named `TeamCode`, that module's name should be used in place of `TeamCode` in the build script and in `.vscode/tasks.json`.

## Tips
- Build performance can possibly be improved by disabling the following extensions:
    - Debugger for Java
    - Language Support for Java...
    - Maven for Java
    - Project Manager for Java
    - Test Runner for Java
- If Codespace storage becomes an issue, run the following command to clean the build: `cd [dir] && ./gradlew clean` (replace [`dir`] with the folder name of the project) or truncate any git commit history to include only the most recent commits.
- Stop the Codespace when it's not being used to prevent incurring unintended usage (click Codespaces in the bottom left corner). Codespaces will automatically timeout after thirty minutes of inactivity.
- To update the container, click the sync button near the bottom-left corner (ensure that the editor is opened to this README file when doing the latter so this container's repository is focused instead of the robot controller's), **or** pull the latest commit with `git pull`.

___

Please consider improving this project by submitting an issue or pull request!
