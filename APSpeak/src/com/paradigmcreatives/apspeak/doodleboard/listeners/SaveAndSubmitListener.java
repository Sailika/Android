package com.paradigmcreatives.apspeak.doodleboard.listeners;

import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;


/**
 * Defining the behavior of save and submit process of assets
 * 
 * @author robin
 * 
 */
public interface SaveAndSubmitListener {

	/**
	 * Fired when saving of the asset starts
	 */
	public void onSaveStart();

	/**
	 * Fired when asset is saved
	 * 
	 * @return
	 */
	public void onSaved(String path);

	/**
	 * Fired if there is a problem while saving the asset
	 * 
	 * @param error
	 */
	public void onSaveError(String error);

	/**
	 * Fired on the start of submit web-service
	 */
	public void onSubmitStart();

	/**
	 * Fired if the submit web-service is successful
	 * 
	 * @return
	 */
	public void onSubmit(SubmitResultBean bean);

	/**
	 * Fired if there is some error while submission
	 * 
	 * @param error
	 */
	public void onSubmitError(String error, int errorCode);

}
