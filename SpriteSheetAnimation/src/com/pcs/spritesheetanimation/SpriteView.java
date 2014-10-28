package com.pcs.spritesheetanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class SpriteView extends View{

	public SpriteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public Context context;
	public int frameWidth;
	public int frameHeight;

	Bitmap spriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheetb);

	Rect source = new Rect();
	Rect destination = new Rect();

	public int x,y;
	public boolean go;
	public SpriteThread spriteThread;

	

	private void init() {

		frameWidth = spriteSheet.getWidth()/2;
		frameHeight = spriteSheet.getHeight()/4;
		destination.left = destination.top = 0;
		destination.right = frameWidth;
		destination.bottom = frameHeight;
	}

	public void startAnimation() {
		go = true;
		spriteThread = new SpriteThread();
		spriteThread.start();

	}

	public void stopAnimation() {

		go=false;
		try{
			spriteThread.join();
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(spriteSheet, source, destination, null);
	}

	public class SpriteThread extends Thread{

		public int screenWidth,screenHeight;

		int speed=5;

		@Override
		public void run() {
			screenWidth = getWidth();
			screenHeight = getHeight();

			while(go) {
				for(int rows=0;rows<2;rows++) {
					for(int columns=0;columns<4;columns++) {
						source.left = columns*frameWidth;
						source.top = rows*frameHeight;
						source.right = source.left+frameWidth;
						source.bottom = source.top+frameHeight;

						destination.left = x;
						destination.top = 0;
						destination.right = destination.left+frameWidth;
						destination.bottom = destination.top+frameHeight;
						postInvalidate();
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}// end of inner for()
				}// end of outer for()
			}// end of while()
		}// end of run()
	}
}
