package com.example.francisco.drowningdetector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Francisco on 10-03-2015.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    //factory method for every concrete SingleFragmentActivity subclass
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }
    }
}
