package com.mcindoe.workoutwhiz.views;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.models.Workout;

public class MainActivity extends Activity {
	
	public static final int SUCCESSFUL_WORKOUT = 0x03;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Called when the Workout! button is clicked in the main activity.
	 */
	public void onWorkoutButtonClicked(View view) {

		WorkoutDataSource wds = new WorkoutDataSource(this);
		wds.open();
		Workout workout = wds.getMostRecentWorkout();
		wds.close();

		//Creates a new workout object for this applications instance.
		if(workout != null) {
			((WorkoutWhizApplication)getApplication()).setCurrentWorkout(workout);
		}
		else {
			((WorkoutWhizApplication)getApplication()).setCurrentWorkout(new Workout("New Workout"));
		}
		
		Intent intent = new Intent(this, SelectExerciseActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * Called when the exercise activity returns.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == SUCCESSFUL_WORKOUT) {
			//Workout went well.
		}
		else {
			//Workout didn't end successfully.
		}

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
