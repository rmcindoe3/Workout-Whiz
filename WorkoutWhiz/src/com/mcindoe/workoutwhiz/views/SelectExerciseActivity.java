package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.ExerciseArrayAdapter;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class SelectExerciseActivity extends Activity implements WeightDialogFragment.WeightDialogListener {
	
	public static final int SUCCESSFUL_EXERCISE = 0x239F;
	public static final int CANCELLED_EXERCISE = 0x38AB;
	
	public static final Exercise INCOMPLETE_EXERCISE_TITLE = new Exercise("18fj9x0jd02k3j9");
	public static final Exercise COMPLETE_EXERCISE_TITLE = new Exercise("0219j381s0dk3nx0");

	private EditText mAddExerciseEditText;
	private TextView mWorkoutTitleTextView;

	private ListView mExercisesListView;
	private ExerciseArrayAdapter mExercisesArrayAdapter;
	
	private Workout mWorkout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_exercise);

		//Grabs our GUI elements.
		mAddExerciseEditText = (EditText)findViewById(R.id.add_exercise_edit_text);
		mWorkoutTitleTextView = (TextView)findViewById(R.id.workout_title_text_view);
		mExercisesListView = (ListView)findViewById(R.id.incomplete_exercises_list_view);
		
		//First we need to grab our workout.
		mWorkout = ((WorkoutWhizApplication)getApplication()).getCurrentWorkout();

		//Sets the title of the activity.
		mWorkoutTitleTextView.setText(mWorkout.getName());
		
		//Formats our list of exercises the way the array adapter will understand it.
		ArrayList<Exercise> listOfExercises = new ArrayList<Exercise>();
		listOfExercises.add(INCOMPLETE_EXERCISE_TITLE);
		listOfExercises.addAll(mWorkout.getIncompleteExercises());
		listOfExercises.add(COMPLETE_EXERCISE_TITLE);
		listOfExercises.addAll(mWorkout.getCompleteExercises());

		//Sets up our list view 
		mExercisesArrayAdapter = new ExerciseArrayAdapter(this, listOfExercises, new OnExerciseClickListener());
		mExercisesListView.setAdapter(mExercisesArrayAdapter);
	}
	
	@Override
	protected void onPause() {
		//Any time this activity pauses we want to make sure our current workout is saved to the application.
		((WorkoutWhizApplication)getApplication()).setCurrentWorkout(mWorkout);
		super.onPause();
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
		//If the exercise name is too long, let the user know.
		else if(mAddExerciseEditText.getText().length() > 20) {
			Toast.makeText(getApplicationContext(), "Exercise Name Too Long\nMaximum Letters: 20", Toast.LENGTH_SHORT).show();
			return;
		}
		//If the user did enter an exercise name, then set the name of the weightDialog's exercise.
		else {
			weightDialog.setExercise(new Exercise(Exercise.capitalizeLetters(mAddExerciseEditText.getText().toString()), 25));
		}

		weightDialog.show(getFragmentManager(), "weight dialog");
	}

	/**
	 * Called when the user presses the end workout button.
	 */
	public void onEndWorkoutButtonClicked(View view) {
		
		//If they have more than 3 exercises, let them finish the workout.
		if(mWorkout.getCompleteExercises().size() >= 3) {

			//Store our workout in the database.
			WorkoutDataSource wds = new WorkoutDataSource(this);
			wds.open();
			wds.insertWorkout(mWorkout);
			wds.close();

			setResult(MainActivity.SUCCESSFUL_WORKOUT);
			Toast.makeText(getApplicationContext(), "Workout saved!", Toast.LENGTH_LONG).show();
			finish();
		}
		//If they haven't completed at least 3 exercises, don't let them finish the workout.
		else {
			Toast.makeText(getApplicationContext(), "You must complete at least 3 exercises to finish your workout!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Called after the user has successfully chosen a desired weight.
	 */
	@Override
	public void onDialogPositiveClick(Exercise exer) {

		//first we need to set it's current reps to empty
		exer.setReps(new ArrayList<Integer>());
		
		//Then set it to the applications current exercise.
		((WorkoutWhizApplication)getApplication()).setCurrentExercise(exer);

		//Now start the exercise activity for result
		Intent intent = new Intent(this, ExerciseActivity.class);
		startActivityForResult(intent, 0);
	}
	
	/**
	 * Called when the exercise activity returns.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == SUCCESSFUL_EXERCISE) {

			mWorkout = ((WorkoutWhizApplication)getApplication()).getCurrentWorkout();

			//Formats our list of exercises the way the array adapter will understand it.
			ArrayList<Exercise> listOfExercises = new ArrayList<Exercise>();
			listOfExercises.add(INCOMPLETE_EXERCISE_TITLE);
			listOfExercises.addAll(mWorkout.getIncompleteExercises());
			listOfExercises.add(COMPLETE_EXERCISE_TITLE);
			listOfExercises.addAll(mWorkout.getCompleteExercises());

			//Sets up our list view 
			mExercisesArrayAdapter = new ExerciseArrayAdapter(this, listOfExercises, new OnExerciseClickListener());
			mExercisesListView.setAdapter(mExercisesArrayAdapter);
			
			mAddExerciseEditText.setText("");
			
		}
		else if(resultCode == CANCELLED_EXERCISE) {
			//Nothing to do for now.
		}
	}

	/**
	 * Parses out the weight of the intensity strings located in the Exercise List Item
	 * @param intensityString - the string from the text view
	 * @return the weight value from the input string.
	 */
	public int parseInitialWeight(String intensityString) {
		return Integer.parseInt(intensityString.substring(0, intensityString.indexOf('/')));
	}
	
	/**
	 * Custom click listener for our list views.
	 */
	private class OnExerciseClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			//Just to make sure that the view object that was clicked is an exercise list item.
			if(v.getId() == R.id.list_item_exercise) {
				
				//Grabs the exercise from this list item
				ExerciseListItemLinearLayout layout = (ExerciseListItemLinearLayout)v;
				
				//Fill out a weight dialog and show it.
				WeightDialogFragment weightDialog = new WeightDialogFragment();
				
				weightDialog.setExercise(layout.getExercise());

				weightDialog.show(getFragmentManager(), "weight dialog");
			}
		}
	}

	/**
	 * Override the default onBackPressed method to provide a confirmation message.
	 */
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you sure?");
		builder.setMessage("Continuing will cancel this workout!");
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//Set the result for this activity to indicate we cancelled the exercise and finish it.
		    	setResult(MainActivity.CANCELLED_WORKOUT);
		    	finish();
			}
		});
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//The dialog will be dismissed and nothing happens.
			}
		});
		AlertDialog dialog = builder.create();
		
		dialog.show();
	}
}
