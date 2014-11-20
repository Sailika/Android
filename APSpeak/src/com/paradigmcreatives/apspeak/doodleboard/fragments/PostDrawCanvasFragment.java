package com.paradigmcreatives.apspeak.doodleboard.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.handlers.SaveAndSubmitAssetHandler;
import com.paradigmcreatives.apspeak.doodleboard.listeners.CanvasActivityListeners;
import com.paradigmcreatives.apspeak.doodleboard.tasks.SaveAndSubmitAssetTask;
import com.paradigmcreatives.apspeak.doodleboard.tasks.SaveAndSubmitAssetTask.TYPE;
import com.paradigmcreatives.apspeak.logging.Logger;

public class PostDrawCanvasFragment extends Fragment {

	private DoodleView doodleView;
	private static final String TAG = PostDrawCanvasFragment.class
			.getSimpleName();

	public PostDrawCanvasFragment(DoodleView doodleView) {
		this.doodleView = doodleView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.post_draw_canvas_layout,
				container, false);
		Log.d(TAG, "Post draw canvas activity started.");

		Bitmap bitmap = doodleView.getDoodleBitmap();
		int bitmapWidth = -1;
		if (bitmap != null) {
			bitmapWidth = (int) (bitmap.getWidth() * 0.4);
			ImageView doodleBitmap = (ImageView) view
					.findViewById(R.id.post_draw_bitmap);
			doodleBitmap.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
					bitmapWidth, bitmapWidth, false));
		}

		FrameLayout postDrawTagsLayout = (FrameLayout) view
				.findViewById(R.id.post_draw_tags_layout);
		postDrawTagsLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, bitmapWidth));
		TextView primaryTagTextView = (TextView) view
				.findViewById(R.id.post_canvas_primary_tag);
		primaryTagTextView.setText(formatString(" TAGGED IN #MUSIC"));

		return view;
	}// end of onCreateView()

	private SpannableStringBuilder formatString(String str) {
		SpannableStringBuilder sb = new SpannableStringBuilder(str);
		sb.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.tag_text_color)), 0, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.blue)), 10, str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 9,
				str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return sb;
	}

	/**
	 * Starts submitting the Doodle to the user stream.
	 */
	public void startSubmit() {
		if (doodleView != null) {
			SaveAndSubmitAssetHandler handler = new SaveAndSubmitAssetHandler(new CanvasActivityListeners(getActivity(), this));
			SaveAndSubmitAssetTask task = new SaveAndSubmitAssetTask(getActivity(), handler, doodleView, "", TYPE.SAVE_AND_SUBMIT);
			Thread t = new Thread(task);
			t.start();
		} else {
			Logger.warn(TAG, "Empty doodle view");
		}
	}// end of startSubmit()

	public DoodleView getDoodleView() {
		return doodleView;
	}

	public void setDoodleView(DoodleView doodleView) {
		this.doodleView = doodleView;
	}
}// end of class