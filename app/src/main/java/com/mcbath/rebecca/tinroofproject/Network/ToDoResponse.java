package com.mcbath.rebecca.tinroofproject.Network;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */

public class ToDoResponse {

	@SerializedName("userId")
	@Expose
	private Integer userId;
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("completed")
	@Expose
	private Boolean completed;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ToDoResponse)) return false;
		ToDoResponse that = (ToDoResponse) o;
		return Objects.equals(getUserId(), that.getUserId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId());
	}
}