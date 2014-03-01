package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.models.Workout;

public class MainActivity extends Activity implements WorkoutSelectDialogFragment.WorkoutSelectDialogListener {
	
	public static final int SUCCESSFUL_WORKOUT = 0x9271;
	public static final int CANCELLED_WORKOUT = 0x843D;
	
	private ArrayList<Workout> mWorkouts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateWorkouts();
	}

	/**
	 * Called when the Workout! button is clicked in the main activity.
	 */
	public void onWorkoutButtonClicked(View view) {

		//Create our workout select dialog fragment and then show it.
		WorkoutSelectDialogFragment workoutSelectDialog = new WorkoutSelectDialogFragment();
		workoutSelectDialog.setRecentWorkouts(mWorkouts);
		workoutSelectDialog.show(getFragmentManager(), "workout select dialog");
	}

	@Override
	public void onDialogPositiveClick(Workout workout) {

		//Sets the application's workout to what the user has selected.
		((WorkoutWhizApplication)getApplication()).setCurrentWorkout(workout);
		
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
	 * Update our workouts variable to the most recent workouts in the database.
	 */
	public void updateWorkouts() {

		WorkoutDataSource wds = new WorkoutDataSource(this);
		wds.open();
		mWorkouts = wds.getMostRecentWorkouts();
		wds.close();
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
		WorkoutDataSource wds = new WorkoutDataSource(this);
		wds.open();
		wds.clearDatabase();
		wds.close();
	}
}
