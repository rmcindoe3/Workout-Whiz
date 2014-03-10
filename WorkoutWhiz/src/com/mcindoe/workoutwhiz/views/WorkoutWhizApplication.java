package com.mcindoe.workoutwhiz.views;

import android.app.Application;

import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutWhizApplication extends Application {

    private Workout currentWorkout;
    private Exercise currentExercise;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Exercise getCurrentExercise() {
        return currentExercise;
    }

    public void setCurrentExercise(Exercise currentExercise) {
        this.currentExercise = currentExercise;
    }

    public Workout getCurrentWorkout() {
        return currentWorkout;
    }

    public void setCurrentWorkout(Workout currentWorkout) {
        this.currentWorkout = currentWorkout;
    }
}
