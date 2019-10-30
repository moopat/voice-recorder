# Voice Recorder

This simple voice recorder application allows you to record audio with your
Android smartphone.

## System Requirements

 * A microphone
 * Android 7.0+
 
## Limitations

 * On my test devices, the audio quality was poor
 * Right now, there's now way to export or share recordings
 * The player should be improved (to allow skipping or pausing)
 
## Building

The app is built using `targetSdk` 29, so make sure to install it on
your system. After that, just check out this repository using Android Studio 
and everything should work out of the box. 

### Running tests

To run the tests, connect a compatible device (or start an emulator) and run
the `connectedCheck` Gradle task. To get a coverage report, 
run `createDebugCoverageReport` which runs the tests as well.
