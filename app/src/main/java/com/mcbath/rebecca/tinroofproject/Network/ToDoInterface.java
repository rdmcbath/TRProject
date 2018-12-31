package com.mcbath.rebecca.tinroofproject.Network;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */
public interface ToDoInterface {

	@GET("todos")
	Call<List<ToDoResponse>> getIncompleteTodos(
			@Query("completed") Boolean isComplete

	);

	@GET("todos")
	Call<List<ToDoResponse>> getTodosByUserId(
			@Query("userId") Integer userId
	);

	@GET("todos?&_sort=userId&completed=false")
	Call<Map<Integer, Integer>> getTotalToDosByUserId();

}
