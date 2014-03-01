package com.mcindoe.workoutwhiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutListItemLinearLayout extends LinearLayout {
	
	private Workout mWorkout;

	public WorkoutListItemLinearLayout(Context context) {
		super(context);
	}

	public WorkoutListItemLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WorkoutListItemLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Workout getWorkout() {
		return mWorkout;
	}

	public void setWorkout(Workout mWorkout) {
		this.mWorkout = mWorkout;
	}

}
