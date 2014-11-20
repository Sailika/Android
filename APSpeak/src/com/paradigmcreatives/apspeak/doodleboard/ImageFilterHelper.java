package com.paradigmcreatives.apspeak.doodleboard;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.android.gpuimage.GPUImage3x3ConvolutionFilter;
import com.android.gpuimage.GPUImageAddBlendFilter;
import com.android.gpuimage.GPUImageAlphaBlendFilter;
import com.android.gpuimage.GPUImageChromaKeyBlendFilter;
import com.android.gpuimage.GPUImageColorBlendFilter;
import com.android.gpuimage.GPUImageColorBurnBlendFilter;
import com.android.gpuimage.GPUImageColorDodgeBlendFilter;
import com.android.gpuimage.GPUImageColorInvertFilter;
import com.android.gpuimage.GPUImageContrastFilter;
import com.android.gpuimage.GPUImageDarkenBlendFilter;
import com.android.gpuimage.GPUImageDifferenceBlendFilter;
import com.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import com.android.gpuimage.GPUImageDissolveBlendFilter;
import com.android.gpuimage.GPUImageDivideBlendFilter;
import com.android.gpuimage.GPUImageEmbossFilter;
import com.android.gpuimage.GPUImageExclusionBlendFilter;
import com.android.gpuimage.GPUImageExposureFilter;
import com.android.gpuimage.GPUImageFilter;
import com.android.gpuimage.GPUImageFilterGroup;
import com.android.gpuimage.GPUImageGammaFilter;
import com.android.gpuimage.GPUImageGrayscaleFilter;
import com.android.gpuimage.GPUImageHardLightBlendFilter;
import com.android.gpuimage.GPUImageHighlightShadowFilter;
import com.android.gpuimage.GPUImageHueBlendFilter;
import com.android.gpuimage.GPUImageHueFilter;
import com.android.gpuimage.GPUImageLightenBlendFilter;
import com.android.gpuimage.GPUImageLinearBurnBlendFilter;
import com.android.gpuimage.GPUImageLuminosityBlendFilter;
import com.android.gpuimage.GPUImageMonochromeFilter;
import com.android.gpuimage.GPUImageMultiplyBlendFilter;
import com.android.gpuimage.GPUImageNormalBlendFilter;
import com.android.gpuimage.GPUImageOpacityFilter;
import com.android.gpuimage.GPUImageOverlayBlendFilter;
import com.android.gpuimage.GPUImagePixelationFilter;
import com.android.gpuimage.GPUImagePosterizeFilter;
import com.android.gpuimage.GPUImageRGBFilter;
import com.android.gpuimage.GPUImageSaturationBlendFilter;
import com.android.gpuimage.GPUImageSaturationFilter;
import com.android.gpuimage.GPUImageScreenBlendFilter;
import com.android.gpuimage.GPUImageSepiaFilter;
import com.android.gpuimage.GPUImageSharpenFilter;
import com.android.gpuimage.GPUImageSobelEdgeDetection;
import com.android.gpuimage.GPUImageSoftLightBlendFilter;
import com.android.gpuimage.GPUImageSourceOverBlendFilter;
import com.android.gpuimage.GPUImageSubtractBlendFilter;
import com.android.gpuimage.GPUImageTwoInputFilter;
import com.android.gpuimage.GPUImageVignetteFilter;
import com.android.gpuimage.GPUImageWhiteBalanceFilter;
import com.paradigmcreatives.apspeak.R;

public class ImageFilterHelper {
	
	public static final int FILTERS_WITHOUT_ICONS = 15;

	public static GPUImageFilter createFilterForType(final Context context,
			final FilterType type) {
		switch (type) {
		case CONTRAST:
			return new GPUImageContrastFilter(2.0f);
		case GAMMA:
			return new GPUImageGammaFilter(2.0f);
		case INVERT:
			return new GPUImageColorInvertFilter();
		case PIXELATION:
			return new GPUImagePixelationFilter();
		case HUE:
			return new GPUImageHueFilter(90.0f);
		case GRAYSCALE:
			return new GPUImageGrayscaleFilter();
		case SEPIA:
			return new GPUImageSepiaFilter();
		case SHARPEN:
			GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
			sharpness.setSharpness(2.0f);
			return sharpness;
		case SOBEL_EDGE_DETECTION:
			return new GPUImageSobelEdgeDetection();
		case THREE_X_THREE_CONVOLUTION:
			GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
			convolution.setConvolutionKernel(new float[] { -1.0f, 0.0f, 1.0f,
					-2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f });
			return convolution;
		case EMBOSS:
			return new GPUImageEmbossFilter();
		case POSTERIZE:
			return new GPUImagePosterizeFilter();
		case FILTER_GROUP:
			List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
			filters.add(new GPUImageContrastFilter());
			filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
			filters.add(new GPUImageGrayscaleFilter());
			return new GPUImageFilterGroup(filters);
		case SATURATION:
			return new GPUImageSaturationFilter(1.0f);
		case EXPOSURE:
			return new GPUImageExposureFilter(0.0f);
		case HIGHLIGHT_SHADOW:
			return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
		case MONOCHROME:
			return new GPUImageMonochromeFilter(1.0f, new float[] { 0.6f,
					0.45f, 0.3f, 1.0f });
		case OPACITY:
			return new GPUImageOpacityFilter(1.0f);
		case RGB:
			return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
		case WHITE_BALANCE:
			return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
		case VIGNETTE:
			PointF centerPoint = new PointF();
			centerPoint.x = 0.5f;
			centerPoint.y = 0.5f;
			return new GPUImageVignetteFilter(centerPoint, new float[] { 0.0f,
					0.0f, 0.0f }, 0.3f, 0.75f);
		case BLEND_DIFFERENCE:
			return createBlendFilter(context,
					GPUImageDifferenceBlendFilter.class);
		case BLEND_SOURCE_OVER:
			return createBlendFilter(context,
					GPUImageSourceOverBlendFilter.class);
		case BLEND_COLOR_BURN:
			return createBlendFilter(context,
					GPUImageColorBurnBlendFilter.class);
		case BLEND_COLOR_DODGE:
			return createBlendFilter(context,
					GPUImageColorDodgeBlendFilter.class);
		case BLEND_DARKEN:
			return createBlendFilter(context, GPUImageDarkenBlendFilter.class);
		case BLEND_DISSOLVE:
			return createBlendFilter(context, GPUImageDissolveBlendFilter.class);
		case BLEND_EXCLUSION:
			return createBlendFilter(context,
					GPUImageExclusionBlendFilter.class);

		case BLEND_HARD_LIGHT:
			return createBlendFilter(context,
					GPUImageHardLightBlendFilter.class);
		case BLEND_LIGHTEN:
			return createBlendFilter(context, GPUImageLightenBlendFilter.class);
		case BLEND_ADD:
			return createBlendFilter(context, GPUImageAddBlendFilter.class);
		case BLEND_DIVIDE:
			return createBlendFilter(context, GPUImageDivideBlendFilter.class);
		case BLEND_MULTIPLY:
			return createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
		case BLEND_OVERLAY:
			return createBlendFilter(context, GPUImageOverlayBlendFilter.class);
		case BLEND_SCREEN:
			return createBlendFilter(context, GPUImageScreenBlendFilter.class);
		case BLEND_ALPHA:
			return createBlendFilter(context, GPUImageAlphaBlendFilter.class);
		case BLEND_COLOR:
			return createBlendFilter(context, GPUImageColorBlendFilter.class);
		case BLEND_HUE:
			return createBlendFilter(context, GPUImageHueBlendFilter.class);
		case BLEND_SATURATION:
			return createBlendFilter(context,
					GPUImageSaturationBlendFilter.class);
		case BLEND_LUMINOSITY:
			return createBlendFilter(context,
					GPUImageLuminosityBlendFilter.class);
		case BLEND_LINEAR_BURN:
			return createBlendFilter(context,
					GPUImageLinearBurnBlendFilter.class);
		case BLEND_SOFT_LIGHT:
			return createBlendFilter(context,
					GPUImageSoftLightBlendFilter.class);
		case BLEND_SUBTRACT:
			return createBlendFilter(context, GPUImageSubtractBlendFilter.class);
		case BLEND_CHROMA_KEY:
			return createBlendFilter(context,
					GPUImageChromaKeyBlendFilter.class);
		case BLEND_NORMAL:
			return createBlendFilter(context, GPUImageNormalBlendFilter.class);

		default:
			throw new IllegalStateException("No filter of that type!");
		}

	}

	private static GPUImageFilter createBlendFilter(Context context,
			Class<? extends GPUImageTwoInputFilter> filterClass) {
		try {
			GPUImageTwoInputFilter filter = filterClass.newInstance();
			filter.setBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher));
			return filter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public enum FilterType {
		CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION,
		THREE_X_THREE_CONVOLUTION, 
		FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, 
		INVERT, HUE, PIXELATION, SATURATION, EXPOSURE, 
		HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE,
		VIGNETTE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, 
		BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION,
		BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, 
		BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, 
		BLEND_SCREEN, BLEND_ALPHA, BLEND_COLOR, BLEND_HUE, 
		BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, 
		BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL
	}

	public static final String filternames[] = { "CONTRAST", "GRAYSCALE", "SHARPEN", "SEPIA",
			"SOBEL_EDGE_DETECTION", "THREE_X_THREE_CONVOLUTION",
			"FILTER_GROUP", "EMBOSS", "POSTERIZE", "GAMMA", "INVERT", "HUE",
			"PIXELATION", "SATURATION", 
			"OPACITY",  "WHITE_BALANCE", "VIGNETTE",
			"BLEND_COLOR_BURN", "BLEND_COLOR_DODGE", "BLEND_DARKEN",
			"BLEND_DIFFERENCE", "BLEND_DISSOLVE", "BLEND_EXCLUSION",
			"BLEND_SOURCE_OVER", "BLEND_HARD_LIGHT", "BLEND_LIGHTEN",
			"BLEND_ADD", "BLEND_DIVIDE", "BLEND_MULTIPLY", "BLEND_OVERLAY",
			"BLEND_SCREEN", "BLEND_ALPHA", "BLEND_COLOR", "BLEND_HUE",
			"BLEND_SATURATION", "BLEND_LUMINOSITY", "BLEND_LINEAR_BURN",
			"BLEND_SOFT_LIGHT", "BLEND_SUBTRACT", " BLEND_CHROMA_KEY",
			"BLEND_NORMAL" };
}
