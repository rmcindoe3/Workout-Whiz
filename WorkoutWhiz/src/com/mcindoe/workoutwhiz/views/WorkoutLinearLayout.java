package com.mcindoe.workoutwhiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutLinearLayout extends LinearLayout {

    private Workout mWorkout;

    public WorkoutLinearLayout(Context context) {
        super(context);
    }

    public WorkoutLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkoutLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Workout getWorkout() {
        return mWorkout;
    }

    public void setWorkout(Workout mWorkout) {
        this.mWorkout = mWorkout;
    }

}
