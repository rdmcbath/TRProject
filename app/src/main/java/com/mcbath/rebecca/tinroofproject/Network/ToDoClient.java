package com.mcbath.rebecca.tinroofproject.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */
public class ToDoClient {

	public static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

	private static Retrofit retrofit = null;
	public static Retrofit getClient() {
		if (retrofit == null) {

			retrofit = new Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}
}
