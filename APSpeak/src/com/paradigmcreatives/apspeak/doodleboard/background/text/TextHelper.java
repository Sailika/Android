package com.paradigmcreatives.apspeak.doodleboard.background.text;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImagePreviewFragment;
import com.paradigmcreatives.apspeak.doodleboard.model.TextProperties;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class contains methods to set text as background for a doodle
 * 
 * @author Vineela | Neuv
 * 
 */
public class TextHelper {
	private final String TAG = "TextActivity";
	private EditText editText;
	private TextView textView;
	private ImageButton acceptButton;
	private ImageButton cancelButton;
	private HorizontalScrollView optionsScrollLayout;
	private ImageView textIncreaseSizeButton;
	private ImageView textDecreaseSizeButton;
	private ImageView textColorButton;
	private ImageView textTypesButton;
	private ImagePreviewFragment fragment;
	private TextProperties textProperties;
	private String layerID;
	private LinearLayout mColorsLayout;
	private LinearLayout mTextStylesLayout;
	private Dialog dialog;
	private String[] fontsList;
	private String[] fontTtfList;
	private String[] colorsList;

	public TextHelper(ImagePreviewFragment fragment) {
		this.fragment = fragment;
	}

	public TextHelper(ImagePreviewFragment fragment,
			TextProperties textProperties, String layerID) {
		this.fragment = fragment;
		this.textProperties = textProperties;
		this.layerID = layerID;
	}

	/**
	 * This method is used to initialize layout components
	 */
	public void backgroundTextDialog() {
		Logger.info(TAG, "Initializing activity components...");
		if (fragment != null) {
			fontsList = fragment.getResources().getStringArray(
					R.array.text_style_items);
			fontTtfList = fragment.getResources().getStringArray(
					R.array.text_style_ttf);
			colorsList = fragment.getResources().getStringArray(
					R.array.color_items);

			LayoutInflater inflater = (LayoutInflater) fragment.getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.background_text_dialog, null);
			dialog = new Dialog(fragment.getActivity(),
					android.R.style.Theme_Light_NoTitleBar_Fullscreen);

			mColorsLayout = (LinearLayout) view
					.findViewById(R.id.text_color_layout);
			optionsScrollLayout = (HorizontalScrollView) view
					.findViewById(R.id.horizontal_scroll);
			mTextStylesLayout = (LinearLayout) view
					.findViewById(R.id.text_style_layout);
			editText = (EditText) view.findViewById(R.id.enter_text);
			textView = (TextView) view.findViewById(R.id.text_text);
			acceptButton = (ImageButton) view.findViewById(R.id.accept_button);
			textColorButton = (ImageView) view
					.findViewById(R.id.text_color_button);
			textTypesButton = (ImageView) view
					.findViewById(R.id.text_types_button);
			cancelButton = (ImageButton) view.findViewById(R.id.cancel_button);
			textIncreaseSizeButton = (ImageView) view
					.findViewById(R.id.text_size_increase);
			textDecreaseSizeButton = (ImageView) view
					.findViewById(R.id.text_size_decrease);

			editText.addTextChangedListener(new EnteredTextListener());
			editText.setFilters(new InputFilter[] { new TextConstraintFiler() });

			acceptButton
					.setOnClickListener(this.new AcceptListener(/* textLayer */));
			cancelButton.setOnClickListener(new CancelListener());
			textColorButton.setOnClickListener(this.new TextColorListener());
			textTypesButton.setOnClickListener(this.new TextTypesListener());

			textIncreaseSizeButton
					.setOnClickListener(this.new TextSizeChangeListener());
			textDecreaseSizeButton
					.setOnClickListener(this.new TextSizeChangeListener());
			// Set the text properties if they exists
			applyTextProperties();
			final RelativeLayout helpLayout = (RelativeLayout) view
					.findViewById(R.id.help_overlay_layout);
			ImageView welcomeClose = (ImageView) view
					.findViewById(R.id.help_close_view);
			if (!AppPropertiesUtil.getTextHelpOverlayStatus(fragment
					.getActivity())) {
				fragment.getActivity()
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				helpLayout.setVisibility(View.VISIBLE);
				AppPropertiesUtil.setTextHelpOverlayStatus(
						fragment.getActivity(), true);

			} else {
				helpLayout.setVisibility(View.GONE);
				makeKeyboardVisible();

			}
			welcomeClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					helpLayout.setVisibility(View.GONE);
					makeKeyboardVisible();

				}
			});
			helpLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					helpLayout.setVisibility(View.GONE);
					makeKeyboardVisible();

				}
			});
			if (fontsList != null && fontsList.length > 0
					&& fontTtfList != null && fontTtfList.length > 0
					&& colorsList != null && colorsList.length > 0) {
				for (int i = 0; i < fontsList.length; i++) {
					TextView textview = new TextView(fragment.getActivity());
					textview.setText(fontsList[i]);
					Typeface font = Typeface.createFromAsset(fragment
							.getActivity().getAssets(), fontTtfList[i]);
					textview.setTypeface(font);
					textview.setPadding(5, 0, 5, 0);
					int color = getColorForFontStyle(i)/*
														 * colorsList.length -
														 * (i + 1)
														 */;
					textview.setTextColor(color);
					textview.setTextSize(30);
					textview.setTag(i);
					textview.setOnClickListener(new TextTypeChangeListener());
					mTextStylesLayout.addView(textview);

				}
			}

			if (colorsList != null && colorsList.length > 0) {
				for (int i = 0; i < colorsList.length; i++) {
					ImageView imageView = new ImageView(fragment.getActivity());
					imageView.setImageResource(R.drawable.color);
					LinearLayout layout = new LinearLayout(
							fragment.getActivity());

					layout.setBackgroundColor(Color.parseColor(colorsList[i]));
					layout.addView(imageView);
					layout.setTag(i);
					layout.setOnClickListener(new TextColorChangeListener());
					mColorsLayout.addView(layout);

				}
			}

			dialog.setContentView(view);
			dialog.show();
		}
	}

	private void applyTextProperties() {
		Typeface myTypeface;
		if (textProperties != null) {
			if (textProperties.getTextFontSize() > 0) {

				// No need to scale the font here because it would already be
				// scaled as per density
				textView.setTextSize(textProperties.getTextFontSize());
				editText.setTextSize(textProperties.getTextFontSize());
			}

			if (!TextUtils.isEmpty(textProperties.getTextFontStyle())) {
				myTypeface = Typeface.createFromAsset(fragment.getActivity()
						.getAssets(), textProperties.getTextFontStyle());
			} else {
				myTypeface = Typeface.createFromAsset(fragment.getActivity()
						.getAssets(),
						fontTtfList[Constants.DEFAULT_TEXT_FONT_INDEX]);

			}

			if (!TextUtils.isEmpty(textProperties.getText())) {
				String text = textProperties.getText();
				textView.setText(text);
				editText.setText(text);
				// Enable accept button
				acceptButton.setEnabled(true);
			} else {
				// Disable it
				acceptButton.setEnabled(false);
			}

			textView.setTextColor(textProperties.getTextColor());
			editText.setTextColor(textProperties.getTextColor());

		} else {
			textProperties = new TextProperties(
					fontTtfList[Constants.DEFAULT_TEXT_FONT_INDEX],
					Constants.DEFAULT_TEXT_SIZE, "",
					Color.parseColor(Constants.DEFAULT_TEXT_COLOR));
			myTypeface = Typeface.createFromAsset(fragment.getActivity()
					.getAssets(),
					fontTtfList[Constants.DEFAULT_TEXT_FONT_INDEX]);

			// Text size textView.setTextSize(Constants.DEFAULT_TEXT_SIZE);
			editText.setTextSize(getScaledSize(Constants.DEFAULT_TEXT_SIZE));
			textView.setTextSize(getScaledSize(Constants.DEFAULT_TEXT_SIZE));

			// No text properties supplied here. Disable the accept button
			acceptButton.setEnabled(false);
		}
		editText.setTypeface(myTypeface);
		textView.setTypeface(myTypeface);

	}

	/**
	 * Captures the current text view and creates a bitmap out of it
	 * 
	 * @return Bitmap, returns the current map view in the form of Bitmap
	 */
	private Bitmap getTextImage() {
		Bitmap resultBmp = null;
		if (textView != null) {
			Logger.info(TAG, "getTextImage");
			/* Capture drawing cache as bitmap */
			textView.setDrawingCacheEnabled(true);
			resultBmp = Bitmap.createBitmap(textView.getDrawingCache());
			textView.setDrawingCacheEnabled(false);
		} else {
			Util.displayToast(fragment.getActivity(),
					"Text view not loaded yet");
		}
		return resultBmp;
	}

	private float getScaledSize(float size) {
		float scaledDensity = fragment.getActivity().getResources()
				.getDisplayMetrics().scaledDensity;
		float scaledSelectedSize = size / scaledDensity;
		return scaledSelectedSize;

	}

	/**
	 * Listener for change of text colors
	 * 
	 * @author Vineela | Paradigm Creatives
	 * 
	 */
	class TextColorChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String selectedColor = Constants.DEFAULT_TEXT_COLOR;
			int position = 0;
			if (v.getTag() instanceof Integer) {
				position = (Integer) v.getTag();
				if (colorsList != null && colorsList.length > 0) {
					selectedColor = colorsList[position];
				}
			}
			if (optionsScrollLayout != null
					&& optionsScrollLayout.getVisibility() == View.VISIBLE) {
				optionsScrollLayout.setVisibility(View.INVISIBLE);
			}
			textView.setTextColor(Color.parseColor(selectedColor));
			editText.setTextColor(Color.parseColor(selectedColor));
			textProperties.setTextColor(Color.parseColor(selectedColor));
		}
	}

	class TextTypeChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String fontName = fontTtfList[Constants.DEFAULT_TEXT_FONT_INDEX];
			int position = 0;
			if (v.getTag() instanceof Integer) {
				position = (Integer) v.getTag();
				if (fontTtfList != null && fontTtfList.length > 0) {
					fontName = fontTtfList[position];
				}
			}
			if (optionsScrollLayout != null
					&& optionsScrollLayout.getVisibility() == View.VISIBLE) {
				optionsScrollLayout.setVisibility(View.INVISIBLE);
			}
			Typeface myTypeface = Typeface.createFromAsset(fragment
					.getActivity().getAssets(), fontName);
			textView.setTypeface(myTypeface);
			editText.setTypeface(myTypeface);
			textProperties.setTextFontStyle(fontName);
		}

	}

	/**
	 * Listener for change of text sizes
	 * 
	 * @author Vineela | Paradigm Creatives
	 * 
	 */
	class TextSizeChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			optionsScrollLayout.setVisibility(View.INVISIBLE);

			float selectedSize = textView.getTextSize();
			float scaledDensity = fragment.getActivity().getResources()
					.getDisplayMetrics().scaledDensity;
			float scaledSelectedSize = selectedSize / scaledDensity;
			System.out.println("text size" + selectedSize);
			switch (v.getId()) {
			case R.id.text_size_increase:
				if (scaledSelectedSize < Constants.MAX_TEXT_SIZE) {
					scaledSelectedSize = scaledSelectedSize + 5;
				}
				break;
			case R.id.text_size_decrease:
				if (scaledSelectedSize > Constants.MIN_TEXT_SIZE) {
					scaledSelectedSize = scaledSelectedSize - 5;
				}
				break;

			default:
				scaledSelectedSize = Constants.DEFAULT_TEXT_SIZE;
				break;
			}
			textProperties.setTextFontSize(scaledSelectedSize);
			textView.setTextSize(scaledSelectedSize);
			editText.setTextSize(scaledSelectedSize);

		}
	}

	/**
	 * Text watcher class
	 * 
	 * @author Vineela | Paradigm Creatives
	 * 
	 */
	private class EnteredTextListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// unused

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// unused

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!TextUtils.isEmpty(editText.getText().toString())) {
				acceptButton.setEnabled(true);
				textView.setText(editText.getText().toString());
			} else {
				acceptButton.setEnabled(false);
			}

		}

	}

	/**
	 * Listener for Accept button
	 * 
	 * @author Vineela | Paradigm Creatives
	 * 
	 */
	private class AcceptListener implements OnClickListener {

		// private TextLayer textLayer;

		AcceptListener(/* TextLayer layer */) {
			// this.textLayer = layer;
		}

		@Override
		public void onClick(View v) {
			Bitmap bitmap = getTextImage();
			if (bitmap != null) {
				if (fragment != null && editText != null) {
					InputMethodManager imm = (InputMethodManager) fragment
							.getActivity().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				}
				dialog.dismiss();
				if (bitmap != null) {
					DoodleView doodleView = fragment.getDoodleView();
					if (doodleView != null) {
						if (TextUtils.isEmpty(layerID)) {
							if (!TextUtils.isEmpty(editText.getText())) {
								textProperties.setText(editText.getText()
										.toString());
							}

							doodleView.addTextLayer(bitmap, textProperties);
						} else {
							doodleView.replaceBitmapOfLayerViaID(layerID,
									bitmap);
						}
					}
				}
			}

		}
	}

	/**
	 * Listener for cancel button
	 * 
	 * @author Vineela | Neuv
	 * 
	 */
	private class CancelListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (fragment != null && editText != null) {
				InputMethodManager imm = (InputMethodManager) fragment
						.getActivity().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			}
			dialog.dismiss();
			/*
			 * ((CanvasActivity) canvasActivity)
			 * .setTextProperties(getTextProperties());
			 */

		}

	}

	/**
	 * Listener for text color button
	 * 
	 * @author Vineela | Paradigm Creatives
	 * 
	 */
	private class TextColorListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mTextStylesLayout.getVisibility() == View.VISIBLE) {
				optionsScrollLayout.setVisibility(View.VISIBLE);
			} else {
				if (optionsScrollLayout.getVisibility() == View.VISIBLE) {
					optionsScrollLayout.setVisibility(View.INVISIBLE);
				} else {
					optionsScrollLayout.setVisibility(View.VISIBLE);
				}
			}
			mColorsLayout.setVisibility(View.VISIBLE);
			mTextStylesLayout.setVisibility(View.GONE);
		}

	}

	private class TextTypesListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mColorsLayout.getVisibility() == View.VISIBLE) {
				optionsScrollLayout.setVisibility(View.VISIBLE);
			} else {
				if (optionsScrollLayout.getVisibility() == View.VISIBLE) {
					optionsScrollLayout.setVisibility(View.INVISIBLE);
				} else {
					optionsScrollLayout.setVisibility(View.VISIBLE);
				}

			}
			mColorsLayout.setVisibility(View.GONE);
			mTextStylesLayout.setVisibility(View.VISIBLE);
		}
	}

	private class TextConstraintFiler implements InputFilter {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			/*
			 * if (isTextAboutToCrossTheAvailableHeight()) {
			 * Toast.makeText(canvasActivity, R.string.err_max_text_limit,
			 * Toast.LENGTH_SHORT).show(); return "";
			 * 
			 * }
			 */
			return null;
		}

	}

	private int getColorForFontStyle(int i) {
		String colorCode = "#2f4f4f";
		switch (i) {
		case 0:
			colorCode = "#2f4f4f";
			break;
		case 1:
			colorCode = "#add8e6";

			break;
		case 2:

			colorCode = "#4a8f05";

			break;
		case 3:

			colorCode = "#30cf99";

			break;
		case 4:

			colorCode = "#1ab0d4";

			break;
		case 5:

			colorCode = "#a52a2a";

			break;
		case 6:

			colorCode = "#d2691e";

			break;
		case 7:

			colorCode = "#a0522d";

			break;
		case 8:

			colorCode = "#1254a3";

			break;
		case 9:
			colorCode = "#f0fff0";

			break;

		case 10:
			colorCode = "#fced21";

			break;
		case 11:
			colorCode = "#40e0d0";

			break;
		case 12:
			colorCode = "#fa2ba8";
			break;

		case 13:
			colorCode = "#7fffd4";
			break;

		case 14:
			colorCode = "#fa2ba8";

			break;

		case 15:
			colorCode = "#a17aab";

			break;

		case 16:
			colorCode = "#a52a2a";

			break;

		case 17:
			colorCode = "#40e0d0";
			break;

		default:
			break;

		}
		return Color.parseColor(colorCode);
	}

	private void makeKeyboardVisible() {
		editText.requestFocus();
		InputMethodManager imm = (InputMethodManager) fragment.getActivity()
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		 imm.showSoftInput(editText, 0);

	}
}
