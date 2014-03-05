package com.mcindoe.workoutwhiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.models.Exercise;

public class ExerciseLinearLayout extends LinearLayout {
	
	private Exercise mExercise;

	public ExerciseLinearLayout(Context context) {
		super(context);
	}
	
	public ExerciseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExerciseLinearLayout(Context context, AttributeSet attrs, int defStyles) {
		super(context, attrs, defStyles);
	}

	public Exercise getExercise() {
		return mExercise;
	}

	public void setExercise(Exercise mExercise) {
		this.mExercise = mExercise;
	}
}
