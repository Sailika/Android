package com.paradigmcreatives.apspeak.network;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import android.content.Context;

public class RestClient implements RequestInterceptor {
	private static SmacxService smacxService;
	private static RestClient restClient;

	public static RestClient getInstance() {
		if (restClient == null) {
			restClient = new RestClient();
		}
		return restClient;
	}

	public SmacxService getRestClient(Context context) {
		if (smacxService == null) {
			getSmackxObj(context);
		}
		return smacxService;
	}

	private void getSmackxObj(Context context) {
		String BASE_URL = "http://smacx-node-server-dev.herokuapp.com";
		RestAdapter smacxRestAdapter = new RestAdapter.Builder()
				.setEndpoint(BASE_URL).setLogLevel(LogLevel.FULL)
				.setRequestInterceptor(this)
				.setClient(new retrofit.client.UrlConnectionClient()).build();
		smacxService = smacxRestAdapter.create(SmacxService.class);

	}

	@Override
	public void intercept(RequestFacade arg0) {

	}
}
