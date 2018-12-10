package com.mcbath.rebecca.tinroofproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mcbath.rebecca.tinroofproject.Network.ToDoResponse;
import com.mcbath.rebecca.tinroofproject.R;
import com.mcbath.rebecca.tinroofproject.UI.DetailActivity;
import com.mcbath.rebecca.tinroofproject.UI.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
	private static final String TAG = MainAdapter.class.getSimpleName();

	private List<ToDoResponse> toDoList;
	private List<ToDoResponse> totalTodos;
	private Context context;


	public MainAdapter(List<ToDoResponse> toDoList, Context context, List<ToDoResponse> totalTodos) {
		this.toDoList = toDoList;
		this.context = context;
		this.totalTodos = totalTodos;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_main, parent, false);
		return new ViewHolder(itemView);


	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

			holder.userIdTextVIew.setText(String.valueOf(toDoList.get(position).getUserId()));

			holder.incompleteNumberTextView.setText(String.valueOf(getTodoItemCount()));

	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount = " + toDoList.size());
		return toDoList.size();
	}

	public int getTodoItemCount() {

		for (int j = 0; j < MainActivity.toDoArrayList.size(); j++) {

				ToDoResponse toDoResponse = new ToDoResponse();
				toDoResponse.setId(j + 1);
				totalTodos.add(toDoResponse);
			}

		Log.d(TAG, "getToDoItemCount = " + totalTodos.size());
		return totalTodos.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		TextView userIdTextVIew, incompleteNumberTextView;

		ViewHolder(View itemView) {
			super(itemView);

			userIdTextVIew = itemView.findViewById(R.id.userId_textView);
			incompleteNumberTextView = itemView.findViewById(R.id.incomplete_todo_number_textView);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int userId = toDoList.get(getAdapterPosition()).getUserId();
					Intent intent = new Intent(context, DetailActivity.class);
					intent.putExtra("userId", userId);
					Log.d(TAG, "UserId = " + userId);
					context.startActivity(intent);
				}
			});
		}
	}
}
