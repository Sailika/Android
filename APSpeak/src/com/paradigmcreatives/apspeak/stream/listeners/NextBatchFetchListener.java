package com.paradigmcreatives.apspeak.stream.listeners;

/**
 * Interface used to notify listeners with stream batch details, that needs to be fetched
 * 
 * @author Dileep | neuv
 * 
 */
public interface NextBatchFetchListener {
    public void fetchNextBatch(int startIndex, int limit, boolean isPullToRefresh);
}
