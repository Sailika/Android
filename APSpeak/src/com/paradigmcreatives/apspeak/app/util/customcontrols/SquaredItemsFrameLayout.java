package com.paradigmcreatives.apspeak.app.util.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquaredItemsFrameLayout extends FrameLayout{
    
    public SquaredItemsFrameLayout(Context context){
	super(context);
    }
    
    public SquaredItemsFrameLayout(Context context, AttributeSet attrs){
	super(context, attrs);
    }
    
    public SquaredItemsFrameLayout(Context context, AttributeSet attrs, int defStyle){
	super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
