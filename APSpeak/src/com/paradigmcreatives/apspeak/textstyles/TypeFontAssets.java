package com.paradigmcreatives.apspeak.textstyles;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFontAssets {
	
	public Typeface thinFont;
	public Typeface boldFont;
	public Typeface regularFont;
	public Typeface lightFont;
	
	

	public TypeFontAssets(Context context)
	{
		
		regularFont = Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
		boldFont = Typeface.createFromAsset(context.getAssets(),"Roboto-Bold.ttf");
		lightFont = Typeface.createFromAsset(context.getAssets(),"Roboto-Light.ttf");
	}
}
