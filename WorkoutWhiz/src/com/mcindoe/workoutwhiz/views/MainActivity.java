package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.models.Workout;

public class MainActivity extends Activity implements WorkoutSelectDialogFragment.WorkoutSelectDialogListener {
	
	public static final int SUCCESSFUL_WORKOUT = 0x9271;
	public static final int CANCELLED_WORKOUT = 0x843D;
	public static final int PERFORM_WORKOUT = 0x28B1;
	
	private ArrayList<Workout> mWorkouts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		else if(resultCode == CANCELLED_WORKOUT) {
			//Workout didn't end successfully.
		}
		else if(resultCode == PERFORM_WORKOUT) {

			//We received a request to start a new workout.
			// This is set up so the applications current workout is already set.
			// So all we need to do is start the SelectExerciseActivity
			Intent intent = new Intent(this, SelectExerciseActivity.class);
			startActivityForResult(intent, 0);
		}

	}
	
	/**
	 * Update our workouts variable to the most recent workouts in the database.
	 */
	public void updateWorkouts() {

		WorkoutDataSource wds = new WorkoutDataSource(this);
		wds.open();
		
		//Grabs the 5 most recent workouts to be shown
		mWorkouts = wds.getMostRecentWorkouts(5);

		wds.close();
	}

	/**
	 * Called when the History button is clicked in the main activity.
	 */
	public void onHistoryButtonClicked(View view) {
		
		//Open history activity.
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * Called when the Settings button is clicked in the main activity.
	 */
	public void onSettingsButtonClicked(View view) {
		//TODO: Open settings activity.
	}

	/**
	 * Override the default onBackPressed method to do nothing.  We don't
	 * want the back button to exit the app accidentally.  User can still press
	 * the HOME button to go to the home screen.
	 */
	@Override
	public void onBackPressed() {
		//Do nothing.
	}
}
