package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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

		//Sets up and displays our list view.
		updateExerciseListView();

		//Sets the title of the activity.
		mWorkoutTitleTextView.setText(mWorkout.getName());
	}
	
	public void updateExerciseListView() {
		
		//First we need to grab our workout.
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
	public void startExercise(Exercise exer) {

		//set the exercise to the applications current exercise.
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

			//Update the list view with our new exercises
			updateExerciseListView();
			
			mAddExerciseEditText.setText("");
		}
		else if(resultCode == CANCELLED_EXERCISE) {
			//Nothing to do for now.
		}
	}
	
	/**
	 * Custom click listener for our list views.
	 */
	private class OnExerciseClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			
			//If the view that called this is our list item
			if(v.getId() == R.id.list_item_exercise) {
				
				//Show the weight dialog for the exercise that is contained in this layout
				showWeightDialog(extractExercise((ExerciseListItemLinearLayout)v));
			}
			//If the view that called this is our options button...
			else if(v.getId() == R.id.list_item_exercise_options_button) {

				//Shows an exercise options popup menu
				showExerciseOptions(v);
			}
		}
	}
	
	/**
	 * Builds and displays our select weight dialog
	 * @param exer - the exercise we want to select a weight for
	 */
	public void showWeightDialog(Exercise exer) {

		//Fill out a weight dialog and show it.
		WeightDialogFragment weightDialog = new WeightDialogFragment();

		//Extract the exercise from our view and set it in our dialog fragment.
		weightDialog.setExercise(exer);

		weightDialog.show(getFragmentManager(), "weight dialog");
	}
	
	/**
	 * Extracts the exercise component of an ExerciseListItemLinearLayout and returns a copy of it
	 * @param layout - the layout that contains the exercise
	 * @return - the exercise itself
	 */
	public Exercise extractExercise(ExerciseListItemLinearLayout layout) {

		//We create a new exercise and fill it with this exercises information because
		// otherwise it will overwrite the previous exercises information in the case
		// where we are repeating the same exercise in the same workout.
		Exercise exer = new Exercise(layout.getExercise().getName(),layout.getExercise().getWeight());
		exer.setLastReps(layout.getExercise().getLastReps());
		
		return exer;
	}
	
	/**
	 * Shows the exercise options popup menu.
	 * @param anchorView - the view that called this popup
	 */
	public void showExerciseOptions(View anchorView) {

		//Grabs the parent layout of the settings button - the exercise list item linear layout
		final ExerciseListItemLinearLayout srcLayout = ((ExerciseListItemLinearLayout)anchorView.getParent());

		//Inflate our layout
		View exerciseOptions = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_exercise_options, null);
		
		//Create our popup
		final PopupWindow exercisePopup = new PopupWindow(exerciseOptions, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//We want the popup window to be focusable
		exercisePopup.setFocusable(true);
		
		//We also want the popup to be dismissed when a touch is made outside of it.
		exercisePopup.setBackgroundDrawable(new ColorDrawable());
		
		//Adds a listener to our perform exercise button.
		Button performButton = (Button)exerciseOptions.findViewById(R.id.exercise_options_perform_button);
		performButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Extract our exercise from the source layout and pass it to our weight dialog.
				showWeightDialog(extractExercise(srcLayout));
				exercisePopup.dismiss();
			}
		});

		//Initializes our mark as incomplete button
		Button markAsIncompleteButton = (Button)exerciseOptions.findViewById(R.id.exercise_options_mark_as_incomplete_button);
		
		//If the exercise isn't completed, then don't show this option.
		if(!srcLayout.getExercise().isCompleted()) {
			markAsIncompleteButton.setVisibility(View.GONE);
		}
		
		//Sets up the button listener for the mark as incomplete button.
		markAsIncompleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Marks this exercise as incomplete in our workout.
				mWorkout.markAsIncomplete(srcLayout.getExercise());
				updateExerciseListView();
				exercisePopup.dismiss();
			}
		});

		//Adds a listener to our remove exercise button.
		Button removeButton = (Button)exerciseOptions.findViewById(R.id.exercise_options_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Remove this exercise from the list.
				if(srcLayout.getExercise().isCompleted()) {
					mWorkout.removeCompleteExercise(srcLayout.getExercise());
				}
				else {
					mWorkout.removeIncompleteExercise(srcLayout.getExercise());
				}
				updateExerciseListView();
				exercisePopup.dismiss();
			}
		});

		//Show the popup as a drop down from our anchor view
		exercisePopup.showAsDropDown(anchorView);
	}

	/**
	 * Override the default onBackPressed method to provide a confirmation message.
	 */
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Cancel this workout?");
		builder.setMessage("You'll lose current progress!");
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
