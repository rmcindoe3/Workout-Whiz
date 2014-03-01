package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutSelectArrayAdapter;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutSelectDialogFragment extends DialogFragment {
	
	private ListView mRecentWorkoutsListView;
	private RadioButton mCreateNewWorkoutRadioButton;
	private EditText mCreateNewWorkoutEditText;
	private ArrayList<Workout> mRecentWorkouts;
	
	private static final String INVALID_WORKOUT_NAME = "0v329jjc9222nk3lj235h35";
	
	private Context mContext;

	/**
	 * Interface that allows us to pass events back to our main activity.
	 */
	public interface WorkoutSelectDialogListener {
		public void onDialogPositiveClick(Workout workout);
	}
	
	private WorkoutSelectDialogListener sourceActivity;
	
	@Override
	public void onAttach(Activity activity) {

        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            sourceActivity = (WorkoutSelectDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement WorkoutSelectDialogListener");
        }	
        
        //Sets the context of this dialog.
        mContext = activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		// Get the Layout Inflater.
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View selectWorkoutDialogView = inflater.inflate(R.layout.dialog_workout_select, null);
		
		//Initialize our custom dialog elements.
		mCreateNewWorkoutEditText = (EditText)selectWorkoutDialogView.findViewById(R.id.create_new_workout_edit_text);
		mCreateNewWorkoutRadioButton = (RadioButton)selectWorkoutDialogView.findViewById(R.id.create_new_workout_radio_button);
		mRecentWorkoutsListView = (ListView)selectWorkoutDialogView.findViewById(R.id.recent_workouts_list_view);
		
		//Sets up the recent workouts list view
		mRecentWorkoutsListView.setAdapter(new WorkoutSelectArrayAdapter(mContext, mRecentWorkouts, new OnRecentWorkoutClickListener()));

		//Since we're overriding default radio button controllers, turn off clickable
		mCreateNewWorkoutRadioButton.setClickable(false);
		
		//Sets up the on click listener for our create new workout layout to work with new radio button usage.
		((LinearLayout)selectWorkoutDialogView.findViewById(R.id.create_new_workout_linear_layout)).setOnClickListener(new OnRecentWorkoutClickListener());
		
		//Adds a focus change listener to our create workout edit text that acts properly with our radio buttons.
		mCreateNewWorkoutEditText.setOnFocusChangeListener(new OnCreateWorkoutEditTextFocusChangeListener());
		
		//Finishes creating our dialog builder.
		builder.setView(selectWorkoutDialogView);

		builder.setTitle(R.string.select_workout_dialog_title);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						//When the ok button is pressed, let our source activity know
						//	that the workout has been selected.
						Workout workout = determineSelectedWorkout();
						
						//If they did select a workout...
						if(workout != null) {

							//If the workout name was invalid, let the user know.
							if(workout.getName().equals(INVALID_WORKOUT_NAME)) {
								Toast.makeText(mContext, "Please Enter A Valid Workout Name\nMinimum Length: 6 Letters", Toast.LENGTH_LONG).show();
							}
							//If the workout name is too long, let the user know.
							else if(workout.getName().length() > 20) {
								Toast.makeText(mContext, "Please Enter A Valid Workout Name\nMaximum Length: 20 Letters", Toast.LENGTH_LONG).show();
							}
							//If the workout does have a valid name then return to the main activity.
							else {
								sourceActivity.onDialogPositiveClick(workout);
							}
						}
						//If they didn't select a workout then let them know and do nothing.
						else {
							Toast.makeText(mContext, "Please Select A Workout", Toast.LENGTH_LONG).show();
						}
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//Do nothing.
					}
				});

		//Create the dialog.
		return builder.create();
	}

	public ArrayList<Workout> getRecentWorkouts() {
		return mRecentWorkouts;
	}

	public void setRecentWorkouts(ArrayList<Workout> mRecentWorkouts) {
		this.mRecentWorkouts = mRecentWorkouts;
	}
	
	/**
	 * Figures out which workout was selected and returns it.
	 * @return - the workout that has been selected by this fragment
	 */
	public Workout determineSelectedWorkout() {
		
		Workout ret = null;

		//If the create new workout radio button is checked, create a new workout to be returned.
		if(mCreateNewWorkoutRadioButton.isChecked()) {

			//If the edit text has text, name the workout accordingly.
			if(mCreateNewWorkoutEditText.length() >= 6) {
				ret = new Workout(Exercise.capitalizeLetters(mCreateNewWorkoutEditText.getText().toString()));
			}
			//If the edit text was empty, set the return workout to the invalid workout name string.
			else {
				ret = new Workout(INVALID_WORKOUT_NAME);
			}
		}
		//Otherwise it's one of the previously performed workouts.
		else {
			
			//Find which one it is and get it's workout to be returnd.
			for(int i = 0; i < mRecentWorkoutsListView.getChildCount(); i++) {
				WorkoutListItemLinearLayout recentWorkoutsListItem = (WorkoutListItemLinearLayout)mRecentWorkoutsListView.getChildAt(i);
				RadioButton rb = (RadioButton)recentWorkoutsListItem.findViewById(R.id.workout_select_radio_button);
				
				//If the radio button is checked, we have found the checked radio button
				if(rb.isChecked()) {
					ret = recentWorkoutsListItem.getWorkout();
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Custom on click listener for our dialog fragment views that simulates
	 * a working RadioGroup for the different components of the screen.
	 */
	private class OnRecentWorkoutClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			//Set all the radio buttons to false.
			mCreateNewWorkoutRadioButton.setChecked(false);
			for(int i = 0; i < mRecentWorkoutsListView.getChildCount(); i++) {
				View recentWorkoutsListItem = mRecentWorkoutsListView.getChildAt(i);
				RadioButton rb = (RadioButton)recentWorkoutsListItem.findViewById(R.id.workout_select_radio_button);
				rb.setChecked(false);
			}
			
			//Now set the clicked radio button to true.
			if(v.getId() == R.id.list_item_workout) {
				
				//Clear focus from the edit text since we selected a previously done workout
				mCreateNewWorkoutEditText.clearFocus();
				
				//Set the radio button to checked
				RadioButton rb = (RadioButton)v.findViewById(R.id.workout_select_radio_button);
				rb.setChecked(true);
			}
			//If the view that was clicked isn't a workout list item, then we know it was our
			// 	create new workout view.
			else {
				mCreateNewWorkoutRadioButton.setChecked(true);
			}
		}
	}
	
	/**
	 * Custom focus changed listener for our create new exercise edit text that 
	 * simulates one RadioGroup for all the different components of our fragment.
	 */
	private class OnCreateWorkoutEditTextFocusChangeListener implements OnFocusChangeListener {
		
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//If the edit text just gained focus.
				if(hasFocus) {
					//Set all the radio buttons to false except the one for creating a new workout
					mCreateNewWorkoutRadioButton.setChecked(true);
					for(int i = 0; i < mRecentWorkoutsListView.getChildCount(); i++) {
						View recentWorkoutsListItem = mRecentWorkoutsListView.getChildAt(i);
						RadioButton rb = (RadioButton)recentWorkoutsListItem.findViewById(R.id.workout_select_radio_button);
						rb.setChecked(false);
					}
				}
			}
	}

}
