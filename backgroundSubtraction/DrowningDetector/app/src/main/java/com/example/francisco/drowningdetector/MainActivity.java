package com.example.francisco.drowningdetector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2, Receiver {

    public static final String TAG = "MainActivity";
    public static final String SERVICE_RECEIVER_TAG = "Receiver";
    public static final int SELECT_VIDEO = 1;

    //this static line is for include directly the opencv library and not use the open cv manager
    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    private JavaCameraView mOpenCvCameraView;

    private TextView txtView;
    private TextView taskName;
    private ImageView imageView;
    private ProgressBar mProgressBar;

    private ServiceReceiver mServiceReceiver;

    //this is for using the open cv manager app for android
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.frameView);
        txtView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarVideoLoader);
        taskName = (TextView) findViewById(R.id.textTask);
        mServiceReceiver = new ServiceReceiver(new Handler());
        mServiceReceiver.setReceiver(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //mOpenCvCameraView.enableView();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback); opencv manager
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_choose_video){
            Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(videoIntent, SELECT_VIDEO);
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VIDEO)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedVideo = data.getData();
                Intent videoFrameGetterIntent = getVideoFrameGetterIntent(selectedVideo);
                startService(videoFrameGetterIntent);
                setProgressBarVisible();
                taskName.setText("Getting Frames");
            }
    }

    private void setProgressBarVisible(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private Intent getVideoFrameGetterIntent(Uri filePath){
        Intent videoFrameGetterIntent = new Intent(this, FrameVideoGetterService.class);
        videoFrameGetterIntent.setData(filePath);
        videoFrameGetterIntent.putExtra("receiverTag",mServiceReceiver);
        return videoFrameGetterIntent;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        txtView.setText(resultData.getString("key"));
        if(resultCode == 2){
            resetServiceReceiver();
            setProgressBarInvisible();
            startServiceForBackgroundSustraction();
        }
        if(resultCode == 3){
            setProgressBarInvisible();
            txtView.setText("Listo!");
            showVideoFrames();
        }
    }

    private void showVideoFrames(){
        Intent intent = new Intent(this, VideoFramesGalleryActivity.class);
        startActivity(intent);
    }

    private void setProgressBarInvisible(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void startServiceForBackgroundSustraction(){
        Intent backgroundSubtractionIntent = getBackgrounsSustractionIntent();
        startService(backgroundSubtractionIntent);
        taskName.setText("Doing Background Sustraction");
    }

    private void resetServiceReceiver(){
        mServiceReceiver = new ServiceReceiver(new Handler());
        mServiceReceiver.setReceiver(this);
    }

    private Intent getBackgrounsSustractionIntent(){
        Intent intent = new Intent(this, FrameVideoBackgroundSubtractorService.class);
        intent.putExtra("receiverTag", mServiceReceiver);
        return intent;
    }
}
