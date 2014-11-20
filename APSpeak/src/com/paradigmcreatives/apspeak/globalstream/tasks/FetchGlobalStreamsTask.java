package com.paradigmcreatives.apspeak.globalstream.tasks;

import java.util.ArrayList;

import android.content.Context;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.globalstream.tasks.helpers.FetchGlobalStreamsHelper;
import com.paradigmcreatives.apspeak.stream.handlers.GetStreamHandler;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

/**
 * Fetches global/personal stream from server based on given stream type and
 * parameters
 * 
 * @author Dileep | neuv
 * 
 */
public class FetchGlobalStreamsTask extends Thread {

	private Context mContext;
	private GetStreamHandler mHandler;
	private String mUserId;
	private String mGroupId;
	private String mCueId;
	private STREAM_TYPE mStreamType;
	private int mOffset;
	private int mCount;

	public FetchGlobalStreamsTask(Context context, GetStreamHandler handler,
			String userId, String groupId, String cueId,
			STREAM_TYPE streamType, int startIndex, int limit) {
		super();
		this.mContext = context;
		this.mHandler = handler;
		this.mUserId = userId;
		this.mGroupId = groupId;
		this.mCueId = cueId;
		this.mStreamType = streamType;
		this.mOffset = startIndex;
		this.mCount = limit;
	}

	@Override
	public void run() {
		FetchGlobalStreamsHelper helper = new FetchGlobalStreamsHelper(
				mContext, mUserId, mGroupId, mCueId, mStreamType, mOffset,
				mCount);
		ArrayList<StreamAsset> streamAssets = helper.execute();
		if (mHandler != null) {
			if (streamAssets != null && streamAssets.size() > 0) {
				mHandler.didBatchFetchComplete(streamAssets, mOffset, mCount,
						mStreamType);
			} else {
				String message;
				if (mStreamType == STREAM_TYPE.ALLCOLLEGES) {
					message = mContext.getString(R.string.all_colleges_empty);
				} else if (mStreamType == STREAM_TYPE.COLLEGE) {
					message = mContext.getString(R.string.my_college_empty);
				} else if (mStreamType == STREAM_TYPE.FRIENDS) {
					message = mContext.getString(R.string.friends_empty);
				} else {
					message = mContext.getString(R.string.default_empty);
				}
				mHandler.failed(-1, -1, message);
			}
		}
		super.run();
	}
}
