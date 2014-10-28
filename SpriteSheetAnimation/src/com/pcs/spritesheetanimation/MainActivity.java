package com.pcs.spritesheetanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	public boolean go;;
	public SpriteView spriteView;
	public Button startAnimation_Btn,stopAnimation_Btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		startAnimation_Btn = (Button)findViewById(R.id.startAnimation_btn);
		stopAnimation_Btn = (Button)findViewById(R.id.stopAnimation_btn);
		spriteView = (SpriteView)findViewById(R.id.spriteView);

		startAnimation_Btn.setOnClickListener(this);
		stopAnimation_Btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.startAnimation_btn:
			spriteView.startAnimation();
			break;
			
		case R.id.stopAnimation_btn:
			spriteView.stopAnimation();
			break;

		}
	}

}
