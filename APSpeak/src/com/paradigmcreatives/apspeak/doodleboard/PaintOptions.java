package com.paradigmcreatives.apspeak.doodleboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;

/**
 * This class manages the layout of brushes and colors along with changing the properties of doodle view when an option
 * is changed
 */
public class PaintOptions {
	private static final String TAG = "PaintOptions";

	private static final int BRUSH_IMAGE_SIZE = 60; // dip

	private DoodleView doodleView;

	// private ImageView brushSize1;
	// private ImageView brushSize2;
	// private ImageView brushSize3;
	// private ImageView brushSize4;
	// private ImageView brushSize5;
	// private ImageView brushSize6;

	private View paintOptionsView;
	private SeekBar brushSizeBar;
	private ImageView brushSizeImageView;

	private ImageView brushColor1;
	private ImageView brushColor2;
	private ImageView brushColor3;
	private ImageView brushColor4;
	private ImageView brushColor5;
	private ImageView brushColor6;
	private ImageView brushColor7;
	private ImageView brushColor8;
	private ImageView brushColor9;
	private ImageView brushColor10;
	private ImageView brushColor11;
	private ImageView brushColor12;
	private ImageView brushColor13;
	private ImageView brushColor14;
	private ImageView brushColor15;
	private ImageView brushColor16;
	private ImageView brushColor17;
	private ImageView brushColor18;
	private ImageView brushColor19;
	private ImageView brushColor20;

	// private LinearLayout brushSize1Layout;
	// private LinearLayout brushSize2Layout;
	// private LinearLayout brushSize3Layout;
	// private LinearLayout brushSize4Layout;
	// private LinearLayout brushSize5Layout;
	// private LinearLayout brushSize6Layout;

	private int selectedColor;
	private int selectedSize;

	/**
	 * This variable should be changed only via <code>setBrushProperties(int color, int size)</code> method
	 */
	private int color;

	/**
	 * This variable should be changed only via <code>setBrushProperties(int color, int size)</code> method
	 */
	private int size;

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	public PaintOptions(DoodleView doodleView) {
		this.doodleView = doodleView;
		initDoodleProperties();
	}

	/**
	 * Initializes the default doodle properties
	 */
	private void initDoodleProperties() {
		this.color = Util.returnColorValue(Constants.DEFAULT_BRUSH_COLOR);
		this.size = Constants.DEFAULT_BRUSH_SIZE;
	}

	/**
	 * Launches the brush and color selection palette
	 * 
	 * @param context
	 * @param helpOverlayUtility
	 * @return
	 */
	public Dialog brushSizeAndColorDialog(final Context context/* , final HelpOverlayUtil helpOverlayUtility */) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// paintOptionsView = inflater.inflate(R.layout.brush_size_color_layout, null);
		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

		// Button okayButton = (Button) paintOptionsView.findViewById(R.id.okay_button);
		// Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
		// okayButton.setTypeface(myTypeface);

		/*
		 * Below is the piece of code can be used with minor modifications, in order to show help overlay when user
		 * clicks on "Brush" from canvas options
		 */
		/*
		 * //Initialise help overlay for Paint Brush Dialog if(context != null && helpOverlayUtility != null){ ImageView
		 * helpOverlay = (ImageView) view.findViewById(R.id.helpOverlay_canvasbrush);
		 * helpOverlayUtility.initHelpOverlay(context, helpOverlay, HelpOverlayUtil.HELPOVERLAY_CANVASBRUSH); }
		 */

		// brushSizeBar = (SeekBar) paintOptionsView.findViewById(R.id.brush_size_seekbar);
		// brushSizeImageView = (ImageView) paintOptionsView.findViewById(R.id.brush_size_transparent_image);
		int imageSize = ImageUtil.getPxFromDip(context, BRUSH_IMAGE_SIZE);
		brushSizeImageView.setImageBitmap(ImageUtil.getImageWithCircularWindow(imageSize, Constants.BRUSH_SIZE6));

		// brushSize1 = (ImageView) paintOptionsView.findViewById(R.id.brush_size1_imageview);
		// brushSize2 = (ImageView) paintOptionsView.findViewById(R.id.brush_size2_imageview);
		// brushSize3 = (ImageView) paintOptionsView.findViewById(R.id.brush_size3_imageview);
		// brushSize4 = (ImageView) paintOptionsView.findViewById(R.id.brush_size4_imageview);
		// brushSize5 = (ImageView) paintOptionsView.findViewById(R.id.brush_size5_imageview);
		// brushSize6 = (ImageView) paintOptionsView.findViewById(R.id.brush_size6_imageview);
		// brushSize1Layout = (LinearLayout) paintOptionsView.findViewById(R.id.brush_size1_layout);
		// brushSize2Layout = (LinearLayout) paintOptionsView.findViewById(R.id.brush_size2_layout);
		// brushSize3Layout = (LinearLayout) paintOptionsView.findViewById(R.id.brush_size3_layout);
		// brushSize4Layout = (LinearLayout) paintOptionsView.findViewById(R.id.brush_size4_layout);
		// brushSize5Layout = (LinearLayout) paintOptionsView.findViewById(R.id.brush_size5_layout);
		// brushSize6Layout = (LinearLayout) paintOptionsView.findViewById(R.id.brush_size6_layout);

		// brushColor1 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_1);
		// brushColor2 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_2);
		// brushColor3 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_3);
		// brushColor4 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_4);
		// brushColor5 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_5);
		// brushColor6 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_6);
		// brushColor7 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_7);
		// brushColor8 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_8);
		//
		// brushColor9 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_9);
		// brushColor10 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_10);
		// brushColor11 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_11);
		// brushColor12 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_12);
		// brushColor13 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_13);
		// brushColor14 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_14);
		// brushColor15 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_15);
		// brushColor16 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_16);
		// brushColor17 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_17);
		// brushColor18 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_18);
		// brushColor19 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_19);
		// brushColor20 = (ImageView) paintOptionsView.findViewById(R.id.brush_color_20);

		// Update the brush layout and doodle properties
		setBrushProperties(color, size);
		selectedColor = color;
		selectedSize = size;

		brushSizeBar.setOnSeekBarChangeListener(this.new BrushSiskeSeekBarChangeListener());

		// BrushSizeChangeListener brushSizeChangeListener = this.new BrushSizeChangeListener();
		// brushSize1.setOnClickListener(brushSizeChangeListener);
		// brushSize2.setOnClickListener(brushSizeChangeListener);
		// brushSize3.setOnClickListener(brushSizeChangeListener);
		// brushSize4.setOnClickListener(brushSizeChangeListener);
		// brushSize5.setOnClickListener(brushSizeChangeListener);
		// brushSize6.setOnClickListener(brushSizeChangeListener);

		BrushColorChangeListener brushColorChangeListener = this.new BrushColorChangeListener();
		brushColor1.setOnClickListener(brushColorChangeListener);
		brushColor2.setOnClickListener(brushColorChangeListener);
		brushColor3.setOnClickListener(brushColorChangeListener);
		brushColor4.setOnClickListener(brushColorChangeListener);
		brushColor5.setOnClickListener(brushColorChangeListener);
		brushColor6.setOnClickListener(brushColorChangeListener);
		brushColor7.setOnClickListener(brushColorChangeListener);
		brushColor8.setOnClickListener(brushColorChangeListener);
		brushColor9.setOnClickListener(brushColorChangeListener);
		brushColor10.setOnClickListener(brushColorChangeListener);
		brushColor11.setOnClickListener(brushColorChangeListener);
		brushColor12.setOnClickListener(brushColorChangeListener);
		brushColor13.setOnClickListener(brushColorChangeListener);
		brushColor14.setOnClickListener(brushColorChangeListener);
		brushColor15.setOnClickListener(brushColorChangeListener);
		brushColor16.setOnClickListener(brushColorChangeListener);
		brushColor17.setOnClickListener(brushColorChangeListener);
		brushColor18.setOnClickListener(brushColorChangeListener);
		brushColor19.setOnClickListener(brushColorChangeListener);
		brushColor20.setOnClickListener(brushColorChangeListener);

		// okayButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// if (doodleView != null) {
		// doodleView.setColor(selectedColor);
		// doodleView.setStrokeSize(selectedSize);
		// }
		// dialog.dismiss();
		// }
		// });
		dialog.setContentView(paintOptionsView);
		return dialog;
	}

	/**
	 * Update the brush's layout with the selection as well as the doodle properties in doodle view
	 */
	public void setBrushProperties(int color, int size) {
		// 1 - Update the state of color and brush
		this.selectedColor = color;
		this.selectedSize = size;

		// 2 - Update the layout
		resetBrushesView();
		updateBrushView(size, color);
	}

	private void updateBrushView(int size, int color) {
		int imageSize = ImageUtil.getPxFromDip(doodleView.getContext(), BRUSH_IMAGE_SIZE);
		brushSizeImageView.setImageBitmap(ImageUtil.getImageWithCircularWindow(imageSize, size));
		brushSizeImageView.setBackgroundColor(color);
		brushSizeBar.setProgress(size);

		// LinearLayout brushToUpdate = null;
		// switch (size) {
		// case Constants.BRUSH_SIZE1:
		// brushToUpdate = brushSize1Layout;
		// break;
		// case Constants.BRUSH_SIZE2:
		// brushToUpdate = brushSize2Layout;
		// break;
		// case Constants.BRUSH_SIZE3:
		// brushToUpdate = brushSize3Layout;
		// break;
		// case Constants.BRUSH_SIZE4:
		// brushToUpdate = brushSize4Layout;
		// break;
		// case Constants.BRUSH_SIZE5:
		// brushToUpdate = brushSize5Layout;
		// break;
		// case Constants.BRUSH_SIZE6:
		// brushToUpdate = brushSize6Layout;
		// break;
		// }
		//
		// if (brushToUpdate != null) {
		// brushToUpdate.setBackgroundColor(color);
		// }
	}

	/**
	 * Resets the all the brushes in the view
	 */
	private void resetBrushesView() {
		// Resetting sizes

		brushSizeImageView.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));

		// brushSize1Layout.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));
		// brushSize2Layout.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));
		// brushSize3Layout.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));
		// brushSize4Layout.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));
		// brushSize5Layout.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));
		// brushSize6Layout.setBackgroundColor(Color.parseColor(Constants.DEFAULT_BRUSH_BACKGROUND));

	}

	/**
	 * The two states of brush size and color layout's visibility
	 * 
	 * @author robin
	 * 
	 */
	enum ViewState {
		VISIBLE, INVISIBLE
	}

	class BrushSiskeSeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			progress = (progress < Constants.BRUSH_SIZE1) ? Constants.BRUSH_SIZE1 : progress;
			selectedSize = progress;
			setBrushProperties(selectedColor, selectedSize);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do nothing
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Do nothing
		}

	}

	/**
	 * Listener for brush size buttons
	 * 
	 * @author robin
	 * 
	 */
	class BrushSizeChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			// case R.id.brush_size1_imageview:
			// selectedSize = Constants.BRUSH_SIZE1;
			// break;
			// case R.id.brush_size2_imageview:
			// selectedSize = Constants.BRUSH_SIZE2;
			// break;
			// case R.id.brush_size3_imageview:
			// selectedSize = Constants.BRUSH_SIZE3;
			// break;
			// case R.id.brush_size4_imageview:
			// selectedSize = Constants.BRUSH_SIZE4;
			// break;
			// case R.id.brush_size5_imageview:
			// selectedSize = Constants.BRUSH_SIZE5;
			// break;
			// case R.id.brush_size6_imageview:
			// selectedSize = Constants.BRUSH_SIZE6;
			// break;
			// default:
			// selectedSize = Constants.DEFAULT_BRUSH_SIZE;
			}

			// Update the layout and the properties
			setBrushProperties(selectedColor, selectedSize);
		}

	}

	/**
	 * Listener for brush colors
	 * 
	 * @author robin
	 * 
	 */
	class BrushColorChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			// switch (v.getId()) {
			// case R.id.brush_color_1:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_1);
			// break;
			// case R.id.brush_color_2:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_2);
			// break;
			// case R.id.brush_color_3:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_3);
			// break;
			// case R.id.brush_color_4:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_4);
			// break;
			// case R.id.brush_color_5:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_5);
			// break;
			// case R.id.brush_color_6:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_6);
			// break;
			// case R.id.brush_color_7:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_7);
			// break;
			// case R.id.brush_color_8:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_8);
			// break;
			// case R.id.brush_color_9:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_9);
			// break;
			// case R.id.brush_color_10:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_10);
			// break;
			// case R.id.brush_color_11:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_11);
			// break;
			// case R.id.brush_color_12:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_12);
			// break;
			// case R.id.brush_color_13:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_13);
			// break;
			// case R.id.brush_color_14:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_14);
			// break;
			// case R.id.brush_color_15:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_15);
			// break;
			// case R.id.brush_color_16:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_16);
			// break;
			// case R.id.brush_color_17:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_17);
			// break;
			// case R.id.brush_color_18:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_18);
			// break;
			// case R.id.brush_color_19:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_19);
			// break;
			// case R.id.brush_color_20:
			// selectedColor = Color.parseColor(Constants.BRUSH_COLOR_20);
			// break;
			// default:
			// selectedColor = Color.parseColor(Constants.DEFAULT_BRUSH_COLOR);
			// }
			//
			// // Update the color
			// setBrushProperties(selectedColor, selectedSize);
		}
	}
}// end of class