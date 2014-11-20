package com.paradigmcreatives.apspeak.doodleboard.layers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;

public class LayerDialogUtil {

    
    /**
     * Launch a confirmation dialog to check if the user really wants to delete the selected layer or not
     * 
     * @param context
     * @param layerID
     * @return
     */
    public static Dialog launchBackgroundChangeDialog(final DoodleView doodleView, final Context context, final int layerID) {
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View emptyCanvasView = inflater.inflate(R.layout.doodle_dialog, null);
	final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

	Button yesButton = (Button) emptyCanvasView.findViewById(R.id.yes_button);
	Button noButton = (Button) emptyCanvasView.findViewById(R.id.no_button);
	TextView dialogTitle = (TextView) emptyCanvasView.findViewById(R.id.dialog_title);
	Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
	Typeface myTypefaceBold = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");

	dialogTitle.setTypeface(myTypefaceBold);
	dialogTitle.setText(context.getText(R.string.delete_addon_message));
	noButton.setTypeface(myTypeface);

	noButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		dialog.dismiss();
	    }
	});
	yesButton.setTypeface(myTypeface);
	yesButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		dialog.dismiss();
		doodleView.removeLayerWithID(layerID);
	    }
	});
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(emptyCanvasView);
	return dialog;
    }

}
