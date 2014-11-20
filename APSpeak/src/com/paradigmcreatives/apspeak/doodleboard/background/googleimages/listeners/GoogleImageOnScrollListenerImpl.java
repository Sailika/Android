package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners;

import android.widget.AbsListView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.GoogleImageActivity;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.tasks.GoogleImageSearchThread;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

public class GoogleImageOnScrollListenerImpl extends PauseOnScrollListener {

    private static final String TAG = "GoogleImageOnScrollListenerImpl";

    private BackgroundFragment fragment;

    public GoogleImageOnScrollListenerImpl( BackgroundFragment fragment, ImageLoader imageLoader,
	    boolean pauseOnScroll, boolean pauseOnFling) {
	super(imageLoader, pauseOnScroll, pauseOnFling);
	this.fragment = fragment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nostra13.universalimageloader.core.assist.PauseOnScrollListener#onScroll(android.widget.AbsListView,
     * int, int, int)
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	Logger.info(TAG, "on scroll called");
	if (fragment != null) {
	    if (!fragment.isLoadingMore() && (firstVisibleItem + visibleItemCount) >= totalItemCount - 2) {
		loadMore();
	    } else {
		Logger.warn(TAG, "Condition failed for load more");
	    }
	} else {
	    Logger.warn(TAG, "Activity is null");
	}
	super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    private void loadMore() {
	Logger.warn(TAG, "Load more called");
	if (fragment != null) {
	    if (fragment.getSearchHelper() != null) {
		if (Util.isOnline(fragment.getActivity())) {
		    try {
			(new GoogleImageSearchThread(fragment, fragment.getSearchHelper(), true)).start();
			fragment.setLoadingMore(true);
		    } catch (IllegalThreadStateException itse) {
			Logger.warn(TAG, itse.getLocalizedMessage());
		    } catch (Exception e) {
			Logger.warn(TAG, e.getLocalizedMessage());
			e.printStackTrace();
		    }
		} else {
		    Util.displayToast(fragment.getActivity(), fragment.getString(R.string.no_internet));
		}
	    } else {
		Logger.warn(TAG, "Search helper is null");
	    }
	} else {
	    Logger.warn(TAG, "Activity is null");
	}
    }

}
