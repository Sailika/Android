package com.paradigmcreatives.apspeak.doodleboard.layers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.doodleboard.background.text.TextHelper;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImagePreviewFragment;
import com.paradigmcreatives.apspeak.doodleboard.model.TextProperties;

public class TextLayer extends Layer {

	private TextProperties textProperties;

	public TextLayer(DoodleView doodleView, Bitmap bitmap, float x, float y,
			String thumbnailURL, TextProperties textProperties) {
		super(doodleView, bitmap, x, y, LayerType.TEXT, thumbnailURL);
		this.textProperties = textProperties;
		addControls();
	}

	private void addControls() {
		ImageButton edit = new ImageButton(getDoodleView().getContext());
		edit.setBackgroundResource(R.drawable.edit_control);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getDoodleView().getContext();
				if (context instanceof ImageSelectionFragmentActivity) {
					ImageSelectionFragmentActivity activity = ((ImageSelectionFragmentActivity) context);
					Fragment fragment = activity.getSupportFragmentManager()
							.findFragmentByTag(
									ImageSelectionFragmentActivity.CANVAS_TAG);

					if (fragment != null
							&& fragment instanceof ImagePreviewFragment) {
						TextHelper textHelper = new TextHelper(
								(ImagePreviewFragment) fragment, textProperties, getID());
						textHelper.backgroundTextDialog();
					}
				}
			}
		});
		addControl(new LayerControl(edit, LayerControl.Gravity.LEFT,
				LayerControl.Gravity.BOTTOM));

	}

	public TextProperties getTextProperties() {
		return textProperties;
	}

	public void setTextProperties(TextProperties textProperties) {
		this.textProperties = textProperties;
	}

}
