# ScreenShot
A better library to help you listen to the screen capture.

How to use

in root build.gradle

    allprojects {
	 repositories {
	     ...
	     maven { url 'https://jitpack.io' }
	 }
    }

in app build.gradle
    
    dependencies {
       ...
       implementation 'com.github.evansherry:ScreenShot:1.0.0'
    }

You should Activity associated life cycle,in Activity onDestroy() destroy ShotScreenHelper object.

Please refer to app MainActivity.


<img src="https://github.com/evansherry/ScreenShot/blob/master/screenshots/screenshot_01.jpg" style="zoom:50%" />

