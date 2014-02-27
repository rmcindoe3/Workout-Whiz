package com.mcindoe.workoutwhiz.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Workout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Called when the Workout! button is clicked in the main activity.
	 */
	public void onWorkoutButtonClicked(View view) {

		//Creates a new workout object for this applications instance.
		((WorkoutWhizApplication)getApplication()).setCurrentWorkout(new Workout("New Workout"));
		
		Intent intent = new Intent(this, SelectExerciseActivity.class);
		startActivity(intent);
	}

	/**
	 * Called when the History button is clicked in the main activity.
	 */
	public void onHistoryButtonClicked(View view) {
		//TODO: Open history activity.
	}

	/**
	 * Called when the Settings button is clicked in the main activity.
	 */
	public void onSettingsButtonClicked(View view) {
		//TODO: Open settings activity.
	}
}
