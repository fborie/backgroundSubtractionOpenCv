package com.example.francisco.drowningdetector;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Francisco on 16-03-2015.
 */
public class ServiceReceiver extends ResultReceiver {

    private Receiver  mReceiver;

    public ServiceReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver){
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData){
        if(mReceiver != null){
            mReceiver.onReceiveResult(resultCode,resultData);
        }
    }
}
