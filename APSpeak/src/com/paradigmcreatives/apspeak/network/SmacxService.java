package com.paradigmcreatives.apspeak.network;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;
import com.paradigmcreatives.apspeak.feedback.FeedBack;

public interface SmacxService {
	static final String HEADER_KEY = "J290EeGRFyIYRdXES7outLUbZKr";
	static final String HEADER_VALUE = "l0FQ5cmpRcADmREyUY4DKwH3CnxejQtpb1cM";

	@POST("/api/v1/feedback/create")
	@Headers({ "Content-Type:application/json", HEADER_KEY + ":" + HEADER_VALUE })
	void postFeedBack(@Body FeedBack feedback, Callback<Response> callback);

	@Multipart
	@POST("/api/v1/asset/put")
	@Headers({ HEADER_KEY + ":" + HEADER_VALUE })
	SubmitResultBean submit(@Part("user_id") String user_id,
			@Part("content") TypedFile content,
			@Part("group_id") String group_id, @Part("type") String type,
			@Part("description") String description, @Part("tags") String tags,
			@Part("created_at") String created_at, @Part("label") String label,
			@Part("categories") String categories);

}