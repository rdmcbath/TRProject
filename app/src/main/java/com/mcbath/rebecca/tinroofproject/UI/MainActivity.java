package com.mcbath.rebecca.tinroofproject.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;
import com.mcbath.rebecca.tinroofproject.Network.ToDoClient;
import com.mcbath.rebecca.tinroofproject.Network.ToDoInterface;
import com.mcbath.rebecca.tinroofproject.Network.ToDoResponse;
import com.mcbath.rebecca.tinroofproject.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getName();

	private SimpleAdapter adapter;
	public static List<ToDoResponse> toDoArrayList = new ArrayList<>();
	private Parcelable mListState;
	private String STATE_KEY = "list_state";
	private ArrayList<HashMap<String, String>> arrayList;
	private HashMultimap<Integer, Integer> responseMap;
	private int todoValue;
	private int userIdValue;
	private HashMap<String, String> viewMap;
	private ListView simpleListView;
	private String[] from;
	private int[] to;
	private String userIdNum;
	private String todoCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.main_toolbar_title));

		simpleListView = findViewById(R.id.main_listView);

		arrayList = new ArrayList<>();

		to = new int[]{R.id.userId_textView, R.id.incomplete_todo_number_textView};

		adapter = new SimpleAdapter(this, arrayList, R.layout.row_item_main, from, to);
		simpleListView.setAdapter(adapter);

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

	// Assignment:
	// Show list with one row for each unique "userId"
	// In each row there should be a count for the number of incomplete todos (determined by "completed": false)
	// The rows should be sorted descending by the number of incomplete todos

	private void loadToDoList() {

		final ToDoInterface toDoInterface = ToDoClient.getClient().create(ToDoInterface.class);
		Call<List<ToDoResponse>> call = toDoInterface.getIncompleteTodos(false);
		Log.d(TAG, "ToDoInterface - GetResponse Called");

		call.enqueue(new Callback<List<ToDoResponse>>() {
			@Override
			public void onResponse(Call<List<ToDoResponse>> call, Response<List<ToDoResponse>> response) {
				Log.d("DataCheck", new Gson().toJson(response.body()));
				if (response.isSuccessful()) {
					final List<ToDoResponse> toDoList = response.body();

					toDoArrayList = toDoList;

					// create the initial map that will hold all the userIds and todoIds
					responseMap = HashMultimap.create();

					for (int i = 0; i < toDoArrayList.size(); i++) { //iterate over the response of all to-dos
						userIdValue = toDoList.get(i).getUserId();//single userId
						todoValue = toDoList.get(i).getId();//single todoId
						// populate the initial map by key (userId) and value (todoId)
						responseMap.get(userIdValue).add(todoValue);
					}

					// create map of each unique userId and the collection of incomplete todoIds assigned to them
					// value.size is the total count of each todolist by user
					Map<Integer, Collection<Integer>> finalMap = responseMap.asMap();

					for (final Map.Entry<Integer, Collection<Integer>> entry : finalMap.entrySet()) {
						final Integer key = entry.getKey();
						Collection<Integer> value = finalMap.get(key);
						Log.d(TAG, "UserId: " + key + " Todos: " + value + " Total Todo Count: " + value.size());

						// create hashmap which contains the userId and the number of associated incomplete to-dos put into Arraylist for adapter
						userIdNum = String.valueOf(key);
						todoCount = String.valueOf(value.size());

						HashMap<String,String> viewMap = new HashMap<>();
						viewMap.put("userIdNum", userIdNum);
						viewMap.put("todoCount", todoCount);
						arrayList.add(viewMap); //add the hashmap into arrayList

						// sort by to-dos in descending order
						Comparator<HashMap<String, String>> todosComparator = new Comparator<HashMap<String,String>>() {
							@Override
							public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
								Integer todos1 = Integer.parseInt(o1.get("todoCount"));
								Integer todos2 = Integer.parseInt(o2.get("todoCount"));

								return todos2.compareTo(todos1);
							}
						};
						Collections.sort(arrayList, todosComparator);
					}

					from = new String[]{"userIdNum", "todoCount"};
					adapter = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.row_item_main, from, to);
					simpleListView.setAdapter(adapter);

					adapter.notifyDataSetChanged();

					simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
							Object clickItemObj = adapterView.getAdapter().getItem(position);
							HashMap clickItemMap = (HashMap)clickItemObj;
							String userIdClicked = (String)clickItemMap.get("userIdNum");
							Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
							intent.putExtra("userId", userIdClicked);
							startActivity(intent);
						}
					});
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
		outState.putParcelable(STATE_KEY, mListState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		finish();
		startActivity(getIntent());
	}
}
