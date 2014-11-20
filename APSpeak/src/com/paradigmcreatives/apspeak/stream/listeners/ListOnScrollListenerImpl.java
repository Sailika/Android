package com.paradigmcreatives.apspeak.stream.listeners;

import android.support.v4.app.Fragment;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.featured.fragments.FeaturedFragment;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;

/**
 * Implementation class to listen for view scroll
 * 
 * @author Dileep | neuv
 * 
 */
public class ListOnScrollListenerImpl implements OnScrollListener {

    private Fragment mFragment;
    private boolean mShouldLoadMore = true;

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    public ListOnScrollListenerImpl(final Fragment fragment, boolean isPersonalStream) {
	super();
	mFragment = fragment;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	this.firstVisibleItem = firstVisibleItem;
	this.visibleItemCount = visibleItemCount;
	this.totalItemCount = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
	switch (scrollState) {
	case SCROLL_STATE_FLING:
	    // shouldLoadMore = true;
	    break;

	case SCROLL_STATE_TOUCH_SCROLL:
		 if (mFragment!=null&&mFragment instanceof GlobalStreamsFragment) {
		    	GlobalStreamsFragment fragment = (GlobalStreamsFragment) mFragment;
		    	if(fragment.isGridViewInUse()){
		    		//fragment.removeLongPressView();
		    	}
		 }
	    break;

	case SCROLL_STATE_IDLE:
	    if (mFragment != null
		    && (firstVisibleItem + visibleItemCount - Constants.BATCH_FETCHLIMIT) >= (totalItemCount - Constants.BATCH_FETCHLIMIT)
		    && mShouldLoadMore) {
		mShouldLoadMore = false;
		if (mFragment != null) {
		    if (mFragment instanceof UserStreamFragment) {
			((UserStreamFragment) mFragment).fetchNextBatch(totalItemCount, Constants.BATCH_FETCHLIMIT, false);
		    } else if (mFragment instanceof GlobalStreamsFragment) {
		    	GlobalStreamsFragment fragment = (GlobalStreamsFragment) mFragment;
		    	if(!fragment.isGridViewInUse()){
		    		// Trigger next batch fetch only if the current mode is ListView
		    		fragment.fetchNextBatch(totalItemCount, Constants.BATCH_FETCHLIMIT, false);
		    	}
		    } else if (mFragment instanceof UserNetworkFragment) {
			((UserNetworkFragment) mFragment).fetchNextBatch(totalItemCount, Constants.BATCH_FETCHLIMIT, false);
		    } else if (mFragment instanceof FeaturedFragment) {
			//((FeaturedFragment) mFragment).fetchNextBatch(totalItemCount, Constants.BATCH_FETCHLIMIT, false);
		    }
		}
	    }
	    break;
	default:
	    break;
	}
    }

    public void setShouldLoadMoreFlag(boolean loadMore) {
	this.mShouldLoadMore = loadMore;
    }
}
