package com.mcbath.rebecca.tinroofproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
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

import java.util.List;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {
	private static final String TAG = DetailAdapter.class.getSimpleName();

		private List<ToDoResponse> toDoList;

		public DetailAdapter(List<ToDoResponse> toDoList) {
			this.toDoList = toDoList;
		}

		@NonNull
		@Override
		public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_detail, parent, false);
			return new DetailAdapter.ViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder holder, int position) {

			holder.titleTextVIew.setText(String.valueOf(toDoList.get(position).getTitle()));

			if (toDoList.get(position).getCompleted()) {
				holder.completedTextView.setText(R.string.detail_adapter_done);
			} else {
				holder.completedTextView.setText(R.string.detail_adapter_incomplete);
			}
		}

		@Override
		public int getItemCount() {
			return toDoList.size();
		}

	class ViewHolder extends RecyclerView.ViewHolder {

			TextView titleTextVIew, completedTextView;

			ViewHolder(View itemView) {
				super(itemView);

				titleTextVIew = itemView.findViewById(R.id.title_textView);
				completedTextView = itemView.findViewById(R.id.completed_textView);
			}
		}
	}

