package com.paradigmcreatives.apspeak.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import retrofit.client.Response;

import com.google.gson.Gson;

public class ResponseParser {

    /**
     * Parses the response.
     * @param <O> the generic type
     * @param response the response
     * @param type the type
     * @return the o
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static <O> O parseResponse(Response response, final Class<O> type) throws IOException {
        InputStream in = response.getBody().in();
        Reader reader = new InputStreamReader(in);
        Gson g = new Gson();
        return (O) g.fromJson(reader, type);
    }

}
