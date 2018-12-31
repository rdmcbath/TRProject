package com.mcbath.rebecca.tinroofproject.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.mcbath.rebecca.tinroofproject.Adapters.DetailAdapter;
import com.mcbath.rebecca.tinroofproject.Network.ToDoClient;
import com.mcbath.rebecca.tinroofproject.Network.ToDoInterface;
import com.mcbath.rebecca.tinroofproject.Network.ToDoResponse;
import com.mcbath.rebecca.tinroofproject.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */
public class DetailActivity extends AppCompatActivity {
	private static final String TAG = DetailActivity.class.getName();

	private RecyclerView mRecyclerView;
	private DetailAdapter mAdapter;
	private LinearLayoutManager mLayoutManager;
	private ToDoResponse toDoResponse;
	public static List<ToDoResponse> toDoResponseList = new ArrayList<>();
	private Parcelable mListState;
	private String STATE_KEY = "list_state";
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Intent intent = getIntent();
		if (intent != null) {
			try {
				String mUserId = intent.getStringExtra("userId");
				userId = Integer.valueOf(mUserId);
			} catch (NumberFormatException e){
				Log.d(TAG, "NumberFormatException" + e.getMessage());
				userId = 1; //use default UserId in case of null
			}
		}

		Objects.requireNonNull(getSupportActionBar()).setTitle("ToDo List for UserId " + userId);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mRecyclerView = findViewById(R.id.detail_recyclerView);

		mLayoutManager = new LinearLayoutManager(this);

		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new DetailAdapter(toDoResponseList);
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
			Toast.makeText(this, "No Network", Toast.LENGTH_SHORT).show();
		}
	}

	private void loadToDoList() {
		ToDoInterface toDoInterface = ToDoClient.getClient().create(ToDoInterface.class);
		Call<List<ToDoResponse>> call = toDoInterface.getTodosByUserId(userId);
		Log.d(TAG, "ToDoInterface - GetResponse Called");

		call.enqueue(new Callback<List<ToDoResponse>>() {
			@Override
			public void onResponse(Call<List<ToDoResponse>> call, Response<List<ToDoResponse>> response) {
				if (response.isSuccessful()) {
					List<ToDoResponse> toDoList = response.body();

					mAdapter = new DetailAdapter(toDoList);
					mRecyclerView.setAdapter(mAdapter);
					mLayoutManager.onRestoreInstanceState(mListState);

				} else {
					System.out.println(response.errorBody());
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
