package com.pcs.animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {

	private ImageView first;
	private ImageView middle;
	private ImageView last;
	private Animator zoom;
	private Animator dropdown;
	private Animator dropside;
	private Animator moveup;
	private Animator moveside;

	private AnimatorSet set;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		first = (ImageView) findViewById(R.id.firstImage);
		middle = (ImageView) findViewById(R.id.middleImage);
		last = (ImageView) findViewById(R.id.lastImage);

		first.setOnClickListener(this);
		middle.setOnClickListener(this);
		last.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.firstImage:
			
			zoom=(Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.zoom_in_out);
			zoom.setTarget(first);
			dropdown = (Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.dropdown);
			dropdown.setTarget(middle);
			dropside=(Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.dropside);
			dropside.setTarget(last);
			set= new AnimatorSet();
			((AnimatorSet)set).playTogether(zoom,dropdown,dropside);
			set.start();

			
			break;
		case R.id.middleImage:
			zoom=(Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.zoom_in_out);
			zoom.setTarget(middle);
			dropdown = (Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.dropdown);
			dropdown.setTarget(last);
			moveup=(Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.moveup);
			moveup.setTarget(first);
			set= new AnimatorSet();
			((AnimatorSet)set).playTogether(zoom,dropdown,moveup);
			set.start();


			break;
		case R.id.lastImage:
			zoom=(Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.zoom_in_out);
			zoom.setTarget(last);
			moveup = (Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.moveup);
			moveup.setTarget(middle);
			moveside=(Animator)AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.moveside);
			moveside.setTarget(first);
			set= new AnimatorSet();
			((AnimatorSet)set).playTogether(zoom,moveup,moveside);
			set.start();

			break;

		default:
			break;
		}

	}

}
