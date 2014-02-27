package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class SelectExerciseActivity extends Activity implements WeightDialogFragment.WeightDialogListener {
	
	private EditText mAddExerciseEditText;
	private ListView mExerciseHistoryListView;
	private TextView mWorkoutTitleTextView;
	
	private Workout mWorkout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_exercise);
		
		//First we need to fill out our workout.
		mWorkout = new Workout("New Workout");
		
		mAddExerciseEditText = (EditText)findViewById(R.id.add_exercise_edit_text);

		mWorkoutTitleTextView = (TextView)findViewById(R.id.workout_title_text_view);
		mWorkoutTitleTextView.setText(mWorkout.getName());
		
		//Sets up our list view of exercises.
		mExerciseHistoryListView = (ListView)findViewById(R.id.exercise_history_list_view);
		mExerciseHistoryListView.setAdapter(new ExerciseArrayAdapter(this, mWorkout.getExercises(), new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Just to make sure that the view object that was clicked is an exercise list item.
				if(v.getId() == R.id.list_item_exercise) {

					//Grab some of it's components.
					TextView exerciseNameTextView = (TextView)v.findViewById(R.id.exercise_name_text_view);
					TextView exerciseIntensityTextView = (TextView)v.findViewById(R.id.exercise_intensity_text_view);
					
					//Fill out a weight dialog and show it.
					WeightDialogFragment weightDialog = new WeightDialogFragment();
					
					//Create a new exercise to pass to the dialog.
					Exercise exer = new Exercise(exerciseNameTextView.getText().toString(), parseInitialWeight(exerciseIntensityTextView.getText().toString()));
					weightDialog.setExercise(exer);

					weightDialog.show(getFragmentManager(), "weight dialog");
				}
			}
		}));

	}

	/**
	 * Called when the user pressed the create new exercise button.
	 */
	public void onAddExerciseButtonClicked(View view) {

		WeightDialogFragment weightDialog = new WeightDialogFragment();

		//If the user does not enter an exercise name, make a toast that tells them to enter one.
		if(mAddExerciseEditText.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please Enter An Exercise Name", Toast.LENGTH_SHORT).show();
			return;
		}
		//If the user did enter an exercise name, then set the name of the weightDialog's exercise.
		else {
			weightDialog.setExercise(new Exercise(mAddExerciseEditText.getText().toString(), 25));
		}

		weightDialog.show(getFragmentManager(), "weight dialog");
	}

	/**
	 * Called after the user has successfully chosen a desired weight.
	 */
	@Override
	public void onDialogPositiveClick(Exercise exer) {
		//We want to start the given exercise.
	}
	
	/**
	 * Parses out the weight of the intensity strings located in the Exercise List Item
	 * @param intensityString - the string from the text view
	 * @return the weight value from the input string.
	 */
	public int parseInitialWeight(String intensityString) {
		return Integer.parseInt(intensityString.substring(0, intensityString.indexOf('/')));
	}
}
