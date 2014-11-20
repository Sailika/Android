package com.paradigmcreatives.apspeak.doodleboard.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.database.expressionsdb.ExpressionsSubmitQueueDAO;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView.PlayState;
import com.paradigmcreatives.apspeak.doodleboard.background.text.TextHelper;
import com.paradigmcreatives.apspeak.doodleboard.handlers.SaveAndSubmitAssetHandler;
import com.paradigmcreatives.apspeak.doodleboard.listeners.CanvasActivityListeners;
import com.paradigmcreatives.apspeak.doodleboard.send.AssetSubmitHelper;
import com.paradigmcreatives.apspeak.doodleboard.tasks.SaveAndSubmitAssetTask;
import com.paradigmcreatives.apspeak.doodleboard.tasks.SaveAndSubmitAssetTask.TYPE;

public class ImagePreviewFragment extends Fragment {
	private View view;
	private LinearLayout mColorsLayout;
	private LinearLayout mTextStylesLayout;
	private TextView mAddText;
	private Bitmap selectedBitmap;
	private String cueId;
	private DoodleView doodleView;
	private ImageView nextView;

	public ImagePreviewFragment(Bitmap selectedBitmap, String cueId) {
		this.selectedBitmap = selectedBitmap;
		this.cueId = cueId;

	}

	public ImagePreviewFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.preview_layout, container, false);
		initUi();
		return view;

	}

	private void initUi() {
		if (view != null) {
			String[] fontsList = getResources().getStringArray(
					R.array.text_style_items);
			String[] fontTtfList = getResources().getStringArray(
					R.array.text_style_ttf);
			String[] colorsList = getResources().getStringArray(
					R.array.color_items);
			mColorsLayout = (LinearLayout) view
					.findViewById(R.id.colors_layout);
			mTextStylesLayout = (LinearLayout) view
					.findViewById(R.id.text_layout);
			mAddText = (TextView) view.findViewById(R.id.add_text);
			doodleView = (DoodleView) view.findViewById(R.id.doodle_view);
			nextView = (ImageView) view.findViewById(R.id.next_view);
			if (selectedBitmap != null) {
				// Stop drawing
				doodleView.setBackgroundBitmap(selectedBitmap);
				doodleView.setPlayState(PlayState.LAYERS);
			}
			if (fontsList != null && fontsList.length > 0
					&& fontTtfList != null && fontTtfList.length > 0
					&& colorsList != null && colorsList.length > 0) {
				for (int i = 0; i < fontsList.length; i++) {
					TextView textview = new TextView(this.getActivity());
					textview.setPadding(10, 0, 10, 0);
					textview.setText(fontsList[i]);
					Typeface font = Typeface.createFromAsset(getActivity()
							.getAssets(), fontTtfList[i]);
					textview.setTypeface(font);
					int size = colorsList.length - (i + 1);
					textview.setTextColor(Color.parseColor(colorsList[size]));

					textview.setTextSize(30);
					textview.setTag(i);
					mTextStylesLayout.addView(textview);

				}
			}

			if (colorsList != null && colorsList.length > 0) {
				for (int i = 0; i < colorsList.length; i++) {
					ImageView imageView = new ImageView(this.getActivity());
					imageView.setImageResource(R.drawable.color);
					LinearLayout layout = new LinearLayout(this.getActivity());

					layout.setBackgroundColor(Color.parseColor(colorsList[i]));
					layout.addView(imageView);
					layout.setTag(i);
					mColorsLayout.addView(layout);

				}
			}
			nextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					Animation bounceInAnim = AnimationUtils.loadAnimation(
							ImagePreviewFragment.this.getActivity(), R.anim.bounce_in);
					v.startAnimation(bounceInAnim);
					bounceInAnim.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							Animation bounceOutAnim = AnimationUtils.loadAnimation(
									ImagePreviewFragment.this.getActivity(),
									R.anim.bounce_out_anim);
							v.startAnimation(bounceOutAnim);
							bounceOutAnim.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
								}

								public void onAnimationRepeat(Animation animation) {
								}

								public void onAnimationEnd(Animation animation) {
									addToQueue();
								}
							});
						}
					});					
				}
			});
			mAddText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					Animation bounceInAnim = AnimationUtils.loadAnimation(
							ImagePreviewFragment.this.getActivity(), R.anim.bounce_in);
					v.startAnimation(bounceInAnim);
					bounceInAnim.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							Animation bounceOutAnim = AnimationUtils.loadAnimation(
									ImagePreviewFragment.this.getActivity(),
									R.anim.bounce_out_anim);
							v.startAnimation(bounceOutAnim);
							bounceOutAnim.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
								}

								public void onAnimationRepeat(Animation animation) {
								}

								public void onAnimationEnd(Animation animation) {
									TextHelper textHelper = new TextHelper(
											ImagePreviewFragment.this);
									textHelper.backgroundTextDialog();
								}
							});
						}
					});	
					
				

				}
			});
		}

	}

	private class TextColorListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int position = 0;
			if (v.getTag() instanceof Integer) {
				position = (Integer) v.getTag();
			}

		}

	}

	private class TextStyleListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int position = 0;
			if (v.getTag() instanceof Integer) {
				position = (Integer) v.getTag();
			}

		}

	}

	private class EditTextKeyListener implements OnKeyListener {
		private EditText editText;

		private EditTextKeyListener(EditText editText) {
			this.editText = editText;
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if ((event.getAction() == KeyEvent.ACTION_DOWN)
					&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

				return true;
			}
			return false;
		}

	}

	public void startSubmit() {
		if (doodleView != null) {
			doodleView.stopLayerSelectionAndRedraw();
			SaveAndSubmitAssetHandler handler = new SaveAndSubmitAssetHandler(
					new CanvasActivityListeners(getActivity(), this));
			SaveAndSubmitAssetTask task = new SaveAndSubmitAssetTask(
					getActivity(), handler, doodleView, cueId, TYPE.SAVE_AND_SUBMIT);
			Thread t = new Thread(task);
			t.start();
		} else {
			// TODO
		}
	}
	
	/**
	 * Adds expression to Submission Queue
	 */
	public void addToQueue(){
		if(doodleView != null){
			doodleView.stopLayerSelectionAndRedraw();
			SaveAndSubmitAssetHandler handler = new SaveAndSubmitAssetHandler(
					new CanvasActivityListeners(getActivity(), this));
			SaveAndSubmitAssetTask task = new SaveAndSubmitAssetTask(
					getActivity(), handler, doodleView, cueId, TYPE.SAVE);
			Thread t = new Thread(task);
			t.start();
		}
	}

	public DoodleView getDoodleView() {
		return doodleView;
	}
}
