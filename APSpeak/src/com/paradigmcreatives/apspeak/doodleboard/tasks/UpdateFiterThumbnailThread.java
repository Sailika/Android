package com.paradigmcreatives.apspeak.doodleboard.tasks;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.gpuimage.GPUImage;
import com.android.gpuimage.GPUImageFilter;
import com.paradigmcreatives.apspeak.doodleboard.ImageFilterHelper;
import com.paradigmcreatives.apspeak.doodleboard.ImageFilterHelper.FilterType;
import com.paradigmcreatives.apspeak.doodleboard.handlers.UpdateFilterThumbnailHandler;

public class UpdateFiterThumbnailThread implements Runnable {
	private Context context;
	private UpdateFilterThumbnailHandler handler;
	private Bitmap resizedThumbnail;
	private String fileName;

	public UpdateFiterThumbnailThread(Context context,
			UpdateFilterThumbnailHandler handler, Bitmap resizedThumbnail,
			String fileName) {
		this.context = context;
		this.handler = handler;
		this.resizedThumbnail = resizedThumbnail;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		if (context != null && handler != null && resizedThumbnail != null) {

			FilterType filterType;
			if (fileName != null) {
				filterType = FilterType.valueOf(fileName);
			} else {
				filterType = FilterType.BLEND_COLOR;
			}
			GPUImageFilter thumbnailImageFilter = ImageFilterHelper
					.createFilterForType(context, filterType);

			GPUImage gpuImage = new GPUImage(context);
			gpuImage.setImage(resizedThumbnail);
			gpuImage.setFilter(thumbnailImageFilter);
			Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();
			handler.success(bitmap);
		} else {
			handler.failure();
		}

	}
}
