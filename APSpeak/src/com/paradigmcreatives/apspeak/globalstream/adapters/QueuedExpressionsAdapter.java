package com.paradigmcreatives.apspeak.globalstream.adapters;

import java.io.File;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.paradigmcreatives.apspeak.app.database.expressionsdb.ExpressionsSubmitQueueDAO;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.model.SUBMISSION_STATUS;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.autosend.AutoSendManager;

/**
 * Adapter to hold Failed Expressions
 * 
 * @author Dileep | neuv
 * 
 */
public class QueuedExpressionsAdapter extends BaseAdapter {

	private Fragment mFragment;
	private ArrayList<ExpressionSubmitQueueBean> mFailedExpressions;
	private ProgressDialog mSubmissionDialog;
	private DisplayImageOptions options;

	public QueuedExpressionsAdapter(final Fragment fragment,
			ArrayList<ExpressionSubmitQueueBean> failedExpressions) {
		this.mFragment = fragment;
		this.mFailedExpressions = failedExpressions;
		this.mSubmissionDialog = new ProgressDialog(mFragment.getActivity(),
					ProgressDialog.THEME_HOLO_LIGHT);
		this.mSubmissionDialog.setTitle("Just a moment");

		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this.mFragment
						.getActivity()));
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (mFragment == null) {
			return null;
		}
		if (convertView == null) {
			convertView = LayoutInflater.from(mFragment.getActivity()).inflate(
					R.layout.failedexpression_list_item, null);
		}
		view = convertView;
		if (view != null) {
			// Initialize view with failed expression
			final ExpressionSubmitQueueBean expression = (ExpressionSubmitQueueBean) getItem(position);
			if (expression != null) {
				final ImageView expressionImage = (ImageView) convertView
						.findViewById(R.id.expressionIcon);
				final TextView expressionStatus = (TextView) convertView
						.findViewById(R.id.expression_status);
				if(expression.getSubmissionStatus() != null){
					if(expression.getSubmissionStatus() == SUBMISSION_STATUS.PENDING){
						expressionStatus.setText(R.string.pending);
						expressionStatus.setTextColor(Color.BLACK);
					}else if(expression.getSubmissionStatus() == SUBMISSION_STATUS.FAILED){
						expressionStatus.setText(R.string.failed);
						expressionStatus.setTextColor(Color.RED);
					}
				}
				
				final ImageView retry = (ImageView) convertView
						.findViewById(R.id.retry);
				final ImageView delete = (ImageView) convertView
						.findViewById(R.id.delete);

				retry.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if(Util.isOnline(mFragment.getActivity())){
							// Retry expression submission
							AutoSendManager.getInstance(mFragment.getActivity()).initializeSubmitDialog(mSubmissionDialog);
							AutoSendManager.getInstance(mFragment.getActivity()).interruptAndStartSending(expression.getID());
						}else{
							Toast.makeText(mFragment.getActivity(),
									mFragment.getResources().getString(R.string.no_network),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

				delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// Delete failed expression from DB
						if (deleteExpressionFromDB(expression.getID())) {
							delete.setVisibility(View.INVISIBLE);
							if(expressionStatus != null){
								expressionStatus.setText(R.string.deleted);
								expressionStatus.setTextColor(Color.BLACK);
							}
							if(retry != null){
								retry.setVisibility(View.INVISIBLE);
							}
						}
					}
				});

				//setExpressionImage(expression, expressionImage, null);
				if(!TextUtils.isEmpty(expression.getFilePath())){
					loadExpressionImage(expression.getFilePath(), expressionImage);
				}
			}
		}
		return view;
	}

	@Override
	public int getCount() {
		if (mFailedExpressions != null) {
			return mFailedExpressions.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mFailedExpressions != null && position >= 0
				&& position < mFailedExpressions.size()) {
			return mFailedExpressions.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mFailedExpressions != null && position >= 0
				&& position < mFailedExpressions.size()) {
			return position;
		}
		return -1;
	}

	/**
	 * Appends next batch of assets to existing list
	 * 
	 * @param nextBatchAssets
	 */
	/*
	 * public void appendNextBatchAssets(ArrayList<StreamAsset> nextBatchAssets)
	 * { if (nextBatchAssets != null && nextBatchAssets.size() > 0) {
	 * this.mAssetsList.addAll(nextBatchAssets); notifyDataSetChanged(); } }
	 */

	/**
	 * Clears all items from the adapter
	 */
	public void clearAll() {
		if (mFailedExpressions != null) {
			mFailedExpressions.clear();
			notifyDataSetChanged();
		}
	}

	/**
	 * Sets asset's image
	 * 
	 * @param position
	 * @param assetImage
	 */
	public void setExpressionImage(final ExpressionSubmitQueueBean expression,
			final ImageView expressionImage, final ProgressWheel progresswheel) {
		if (expressionImage == null) {
			return;
		}
		// assetImage.setImageResource(R.drawable.doodle);
		if (expression != null) {
			expressionImage.setVisibility(View.INVISIBLE);
			String url = expression.getFilePath();
			ImageLoader.getInstance().displayImage(url, expressionImage,
					options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

							if (progresswheel != null) {
								progresswheel.incrementProgress(0);
								progresswheel.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if (progresswheel != null)
								progresswheel.setVisibility(View.GONE);
							expressionImage.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {

						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							if (progresswheel != null)
								progresswheel.incrementProgress((current * 360)
										/ total);
						}
					});

		}
	}

	/**
	 * Deletes expressions from database
	 * @param id
	 * @return
	 */
	private boolean deleteExpressionFromDB(int id) {
		boolean isDeleted = false;
		if (id != -1 && mFragment != null) {
			ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
					mFragment.getActivity());
			if (dao != null) {
				isDeleted = dao.deleteExpressionFromDB(id);
			}
		}
		return isDeleted;
	}
	
	private void loadExpressionImage(String filePath, ImageView imageView){
		if (!TextUtils.isEmpty(filePath) && imageView != null) {
			File file = new File(filePath);
			if (file != null && file.exists()) {
				try {
					Bitmap expressionBitmap = BitmapFactory
							.decodeFile(file
									.getAbsolutePath());
					imageView
							.setImageBitmap(expressionBitmap);
				} catch (Exception e) {

				}
			} else {
				imageView
						.setImageResource(R.drawable.announcement);
			}
		}
	}
	
	
}
