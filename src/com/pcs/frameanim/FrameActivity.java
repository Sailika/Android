package com.pcs.frameanim;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;


public class FrameActivity extends Activity {
private ImageView frameView;
private AnimationDrawable frameAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        frameView=(ImageView)findViewById(R.id.frameView);
        frameView.setBackgroundResource(R.drawable.frames);
        frameAnimation = (AnimationDrawable) frameView.getBackground();
        }
        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        	//Start Animation
        frameAnimation.start();
        else
        	//Igf not in focus stop Animation
        frameAnimation.stop();
        }
        }
    


   

