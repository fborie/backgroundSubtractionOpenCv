package com.example.francisco.drowningdetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Francisco on 17-03-2015.
 */
public class VideoFrameAdapter extends ArrayAdapter<Bitmap> {

    private Context mContext;
    private ArrayList<Bitmap> mVideoFrames;

    public VideoFrameAdapter(Context context,ArrayList<Bitmap> videoFrames){
        super(context,0, videoFrames);
        mContext = context;
        mVideoFrames = videoFrames;
    }

    @Override
    public int getCount() {
        return mVideoFrames.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.video_frame_item, null);
        }

        Bitmap videoFrame = getItem(position);
        ImageView videoFrameImageView = (ImageView) convertView.findViewById(R.id.videoFrameImageView);
        videoFrameImageView.setImageBitmap(videoFrame);

        return convertView;
    }
}
