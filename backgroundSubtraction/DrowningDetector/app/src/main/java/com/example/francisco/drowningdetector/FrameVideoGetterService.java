package com.example.francisco.drowningdetector;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.opencv.core.Mat;

import java.util.ArrayList;

/**
 * Created by Francisco on 16-03-2015.
 */
public class FrameVideoGetterService extends IntentService {

    private static final String TAG = "FrameVideoGetterService";

    public static ArrayList<Bitmap> videoFrames;

    private ResultReceiver mResultReceiver;
    private MediaMetadataRetriever mRetriever;

    public FrameVideoGetterService(){
        super(TAG);
        videoFrames = new ArrayList<Bitmap>();
        mRetriever = new MediaMetadataRetriever();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setResultReceiver(intent);
        Uri selectedVideoUri = intent.getData();
        setVideoPath(selectedVideoUri);
        int videoDuration = getVideoDuration()*1000; //microseconds
        int count = 1;
        Bundle bundle = new Bundle(); //use the same bundle structure to minimize memory allocation
        setVideoFramesFromMedia(videoDuration,bundle);;
        notifyResultReceiver(bundle,-1,2);
    }

    private void setVideoFramesFromMedia(int videoDuration, Bundle bundle){
        int count = 1;
        for(int time=0; time<videoDuration; time+=670000){
            notifyResultReceiver(bundle,count,1);
            count++;
            Bitmap currentFrame = mRetriever.getFrameAtTime(time);
            videoFrames.add(currentFrame);
        }
    }

    private void notifyResultReceiver(Bundle bundle, int value, int resultCode){
        bundle.putString("key", String.valueOf(value));
        mResultReceiver.send(resultCode,bundle);
    }

    private void setVideoPath(Uri filePath){
        mRetriever.setDataSource(getApplicationContext(), filePath);
    }

    private int getVideoDuration(){
        String videoDuration = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(videoDuration);
    }

    private void setResultReceiver(Intent intent){
        mResultReceiver = intent.getParcelableExtra("receiverTag");

    }
}
