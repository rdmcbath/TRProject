package com.mcbath.rebecca.tinroofproject.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mcbath.rebecca.tinroofproject.Adapters.MainAdapter;
import com.mcbath.rebecca.tinroofproject.Network.ToDoClient;
import com.mcbath.rebecca.tinroofproject.Network.ToDoInterface;
import com.mcbath.rebecca.tinroofproject.Network.ToDoResponse;
import com.mcbath.rebecca.tinroofproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getName();

	private RecyclerView mRecyclerView;
	private MainAdapter mAdapter;
	public LinearLayoutManager mLayoutManager;
	private ToDoResponse toDoResponse;
	public static List<ToDoResponse> toDoArrayList = new ArrayList<>();
	private Parcelable mListState;
	private String STATE_KEY = "list_state";
	private static String USER_ID_KEY = "userId";

	private List<ToDoResponse> userIds = new ArrayList<>();
	private List<ToDoResponse> totalTodos = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.main_toolbar_title));

		mRecyclerView = findViewById(R.id.main_recyclerView);
		mLayoutManager = new LinearLayoutManager(this);

		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new MainAdapter(toDoArrayList, MainActivity.this, totalTodos);
		mRecyclerView.setAdapter(mAdapter);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
				DividerItemDecoration.VERTICAL);
		mRecyclerView.addItemDecoration(dividerItemDecoration);

		ConnectivityManager connMgr = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connMgr != null) {
			networkInfo = connMgr.getActiveNetworkInfo();
		}

		if (networkInfo != null && networkInfo.isConnected()) {

			loadToDoList();

		} else {
			Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}
	}

	private void loadToDoList() {

		final ToDoInterface toDoInterface = ToDoClient.getClient().create(ToDoInterface.class);
		Call<List<ToDoResponse>> call = toDoInterface.getIncompleteTodos(false);
		Log.d(TAG, "ToDoInterface - GetResponse Called");

		call.enqueue(new Callback<List<ToDoResponse>>() {
			             @Override
			             public void onResponse(Call<List<ToDoResponse>> call, Response<List<ToDoResponse>> response) {
				             Log.d("DataCheck", new Gson().toJson(response.body()));
				             if (response.isSuccessful()) {
					             List<ToDoResponse> toDoList = response.body();
					             Log.d(TAG, "ResponseBodyRetrofit Called");

					             toDoArrayList = toDoList;

					             for (int j = 0; j < toDoArrayList.size(); j++) {
						             ToDoResponse toDoResponse = new ToDoResponse();

					             	 toDoResponse.setId(j+1);
					             	 totalTodos.add(toDoResponse);
					             }
					             toDoArrayList = toDoList.stream().distinct().collect(Collectors.<ToDoResponse>toList());

					mAdapter = new MainAdapter(toDoArrayList, MainActivity.this, totalTodos);
					mRecyclerView.setAdapter(mAdapter);
					mLayoutManager.onRestoreInstanceState(mListState);

				}
			}

			@Override
			public void onFailure(Call<List<ToDoResponse>> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		mListState = mLayoutManager.onSaveInstanceState();
		outState.putParcelable(STATE_KEY, mListState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {
			mListState = savedInstanceState.getParcelable(STATE_KEY);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mListState != null) {
			mLayoutManager.onRestoreInstanceState(mListState);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		finish();
		startActivity(getIntent());
		mAdapter.notifyDataSetChanged();
	}
}





