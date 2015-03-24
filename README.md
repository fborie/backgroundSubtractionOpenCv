# backgroundSubtractionOpenCv
Android App. that utilize the openCv library to detect objects in motion though the method of background subtraction.

[Config Steps]
  1.- Download to your PC the OpenCvLibrary for android (i'm currently using the 2.4.10)
  2.- Copy the folders that your emulator or mobile device need (depending of your processor architecure)
    from the folder /path where you unzipped the the OpenCvLibrary/OpenCvLibrary/sdk/native/libs/ to your folder jniLibs of the android 
    application.
    In My case C:\Program Files (x86)\Android\OpenCV-2.4.10-android-sdk\OpenCV-2.4.10-android-sdk\sdk\native\libs\ armeabi-v7a
    to  \project_path\app\src\main\jniLibs
  3.- Re-Build
