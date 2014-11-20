package com.paradigmcreatives.apspeak.app.model;

import android.graphics.drawable.Drawable;

/**
 * Represents share via application details
 * 
 * @author Dileep | neuv
 *
 */
public class ShareAppInfo {
	private String name;
	private Drawable icon;
	private String app_package;

	public ShareAppInfo(String name, Drawable drawable, String pkg) {
		this.name = name;
		this.icon = drawable;
		this.app_package = pkg;

	}

	public String getApp_package() {
		return app_package;
	}

	public void setApp_package(String app_package) {
		this.app_package = app_package;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

}
