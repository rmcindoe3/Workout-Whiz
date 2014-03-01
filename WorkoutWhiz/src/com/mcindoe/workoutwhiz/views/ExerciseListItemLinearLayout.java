package com.mcindoe.workoutwhiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.models.Exercise;

public class ExerciseListItemLinearLayout extends LinearLayout {
	
	private Exercise mExercise;

	public ExerciseListItemLinearLayout(Context context) {
		super(context);
	}
	
	public ExerciseListItemLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExerciseListItemLinearLayout(Context context, AttributeSet attrs, int defStyles) {
		super(context, attrs, defStyles);
	}

	public Exercise getExercise() {
		return mExercise;
	}

	public void setExercise(Exercise mExercise) {
		this.mExercise = mExercise;
	}
}
