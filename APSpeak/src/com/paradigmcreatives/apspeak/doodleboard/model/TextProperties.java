package com.paradigmcreatives.apspeak.doodleboard.model;

public class TextProperties {
	private String textFontStyle;
	private float textFontSize;
	private String text;
	private int textColor;

	public TextProperties() {
		super();
	}

	public TextProperties(String textFontStyle, float textFontSize,
			String text, int textColor) {
		super();
		this.text = text;
		this.textColor = textColor;
		this.textFontSize = textFontSize;
		this.textFontStyle = textFontStyle;
	}

	public String getTextFontStyle() {
		return textFontStyle;
	}

	public void setTextFontStyle(String textFontStyle) {
		this.textFontStyle = textFontStyle;
	}

	public float getTextFontSize() {
		return textFontSize;
	}

	public void setTextFontSize(float textFontSize) {
		this.textFontSize = textFontSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
}
