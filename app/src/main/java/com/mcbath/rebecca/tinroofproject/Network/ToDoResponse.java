package com.mcbath.rebecca.tinroofproject.Network;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by Rebecca McBath
 * on 12/6/18.
 */

public class ToDoResponse implements Parcelable {

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

	public ToDoResponse() {
	}

	public ToDoResponse(Parcel in) {
		if (in.readByte() == 0) {
			userId = null;
		} else {
			userId = in.readInt();
		}
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readInt();
		}
		title = in.readString();
		byte tmpCompleted = in.readByte();
		completed = tmpCompleted == 0 ? null : tmpCompleted == 1;
	}

	public static final Creator<ToDoResponse> CREATOR = new Creator<ToDoResponse>() {
		@Override
		public ToDoResponse createFromParcel(Parcel in) {
			return new ToDoResponse(in);
		}

		@Override
		public ToDoResponse[] newArray(int size) {
			return new ToDoResponse[size];
		}
	};

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
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (userId == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(userId);
		}
		if (id == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(id);
		}
		dest.writeString(title);
		dest.writeByte((byte) (completed == null ? 0 : completed ? 1 : 2));
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
