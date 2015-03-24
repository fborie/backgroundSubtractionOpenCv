package com.example.francisco.drowningdetector;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFramesGalleryFragment extends Fragment {

    public static final String TAG = "VideoFramesGalleryFragment";

    private GridView mVideoFramesGallery;

    public VideoFramesGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_frames_gallery, container, false);
        setGridView(view);
        setGridViewAdapter();
        return view;
    }

    private void setGridView(View view){
        mVideoFramesGallery = (GridView) view.findViewById(R.id.videoFramesGallery);
    }

    private void setGridViewAdapter(){
        ArrayList<Bitmap> videoFrames = FrameVideoGetterService.videoFrames;
        VideoFrameAdapter videoFrameAdapter = new VideoFrameAdapter(getActivity(),videoFrames);
        mVideoFramesGallery.setAdapter(videoFrameAdapter);
    }


}
