package com.paradigmcreatives.apspeak.network;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

import com.paradigmcreatives.apspeak.feedback.FeedBack;

public interface SmacxService {
	static final String HEADER_KEY = "J290EeGRFyIYRdXES7outLUbZKr";
	static final String HEADER_VALUE = "l0FQ5cmpRcADmREyUY4DKwH3CnxejQtpb1cM";

	@POST("/api/v1/feedback/create")
	@Headers({ "Content-Type:application/json", HEADER_KEY + ":" + HEADER_VALUE })
	void postFeedBack(@Body FeedBack feedback,
			Callback<Response> callback);

	}
