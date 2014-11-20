package com.paradigmcreatives.apspeak.app.util.customcontrols;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {

	private final Typeface newTypeface;

	public CustomTypefaceSpan(String family, Typeface typeface) {
		super(family);
		this.newTypeface = typeface;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		applyCustomTypeFace(ds, newTypeface);
	}

	@Override
	public void updateMeasureState(TextPaint paint) {
		applyCustomTypeFace(paint, newTypeface);
	}

	private static void applyCustomTypeFace(Paint paint, Typeface tf) {
		int oldStyle;
		Typeface old = paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}

		int fake = oldStyle & ~tf.getStyle();
		if ((fake & Typeface.BOLD) != 0) {
			paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			paint.setTextSkewX(-0.25f);
		}

		paint.setTypeface(tf);
	}
}