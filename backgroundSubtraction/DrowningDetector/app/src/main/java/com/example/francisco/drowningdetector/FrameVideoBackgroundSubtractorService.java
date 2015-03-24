package com.example.francisco.drowningdetector;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;

import java.util.Iterator;

/**
 * Created by Francisco on 17-03-2015.
 */
public class FrameVideoBackgroundSubtractorService extends IntentService {

    private static final String TAG = "FrameVideoBackgroundSubtractorService";

    private ResultReceiver mResultReceiver;
    private BackgroundSubtractorMOG backgoundSubtractorMog;
    private Bundle resultData;

    public FrameVideoBackgroundSubtractorService(){
        super(TAG);
        resultData = new Bundle();
        backgoundSubtractorMog = new BackgroundSubtractorMOG();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setResultReceiver(intent);
        doBackgroundSubtraction();
        notifyResultReceiver(resultData,-1,3);
    }

    private void doBackgroundSubtraction(){
        Mat currentFrame = new Mat();
        Mat fgMaskMog = new Mat();
        Iterator<Bitmap> frameIterator = getVideoFrameIterator();
        iterateOverFramesToApplyBackgroundSubtractor(frameIterator, currentFrame,fgMaskMog);
    }

    private void iterateOverFramesToApplyBackgroundSubtractor(Iterator<Bitmap> frameIterator, Mat currentFrameMat, Mat fgMaskMog){
        int index = 0;
        for(Iterator<Bitmap> it = frameIterator; frameIterator.hasNext();){
            Bitmap nextFrame = it.next();
            convertBitmapToMat(nextFrame,currentFrameMat);
            Mat convertedCurrentFrameMat = new Mat();
            convertCurrentFrame(currentFrameMat,convertedCurrentFrameMat);
            applyBackgroundSubtractorMethod(convertedCurrentFrameMat, fgMaskMog);
            Bitmap videoFrame = getBitmapFromVideoFrames(index);
            convertMatToBitmap(fgMaskMog,videoFrame);
            setBackgrundSustractorBitmapOnVideoFrames(videoFrame, index);
            notifyResultReceiver(resultData, index, 4);
            index++;
        }
    }

    private void applyBackgroundSubtractorMethod(Mat frame, Mat fgMaskMog){
        backgoundSubtractorMog.apply(frame,fgMaskMog,0.3);
    }

    private void convertCurrentFrame(Mat currentFrame, Mat futureFrame){
        Imgproc.cvtColor(currentFrame, futureFrame, Imgproc.COLOR_BGRA2RGB);
    }

    private Bitmap getBitmapFromVideoFrames(int index){
        return FrameVideoGetterService.videoFrames.get(index);
    }

    private void setBackgrundSustractorBitmapOnVideoFrames(Bitmap frame, int index){
        FrameVideoGetterService.videoFrames.set(index,frame);
    }

    private void convertMatToBitmap(Mat mat, Bitmap bm){
        Utils.matToBitmap(mat,bm);
    }

    private void convertBitmapToMat(Bitmap bm, Mat mat){
        Utils.bitmapToMat(bm, mat);
    }

    private Iterator<Bitmap> getVideoFrameIterator(){
        return FrameVideoGetterService.videoFrames.iterator();
    }

    private void setResultReceiver(Intent intent){
        mResultReceiver = intent.getParcelableExtra("receiverTag");
    }

    private void notifyResultReceiver(Bundle bundle, int value, int resultCode){
        bundle.putString("key", String.valueOf(value));
        mResultReceiver.send(resultCode,bundle);
    }
}
