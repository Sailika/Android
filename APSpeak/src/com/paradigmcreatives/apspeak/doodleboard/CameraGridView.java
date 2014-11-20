package com.paradigmcreatives.apspeak.doodleboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class CameraGridView extends View {
	Paint paint = new Paint();
	public CameraGridView(Context context) {
		super(context);
		paint.setColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
			// Find Screen size first
			DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
			int screenWidth = metrics.widthPixels;
			int screenHeight = screenWidth;

			// Set paint options
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.argb(255, 255, 255, 255));

			canvas.drawLine((screenWidth / 3) * 2, 0, (screenWidth / 3) * 2,
					screenHeight, paint);
			canvas.drawLine((screenWidth / 3), 0, (screenWidth / 3),
					screenHeight, paint);
			canvas.drawLine(0, (screenHeight / 3) * 2, screenWidth,
					(screenHeight / 3) * 2, paint);
			canvas.drawLine(0, (screenHeight / 3), screenWidth,
					(screenHeight / 3), paint);
		
}
}
