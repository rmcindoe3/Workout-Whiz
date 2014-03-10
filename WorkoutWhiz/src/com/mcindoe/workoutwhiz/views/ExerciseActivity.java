package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.NumberPadController;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class ExerciseActivity extends Activity {
	
	public static final int MAX_NUM_SETS = 5;
	public static final int MIN_NUM_SETS = 1;

	private Exercise mExercise;
	private NumberPadController mNPController;

	private TextView mLastRepsTextView;
	private TextView mCurrentRepsTextView;
	private TextView mExerciseTitleTextView;

	private Button mButtonOne;
	private Button mButtonTwo;
	private Button mButtonThree;
	private Button mButtonFour;
	private Button mButtonFive;
	private Button mButtonSix;
	private Button mButtonSeven;
	private Button mButtonEight;
	private Button mButtonNine;
	private Button mButtonZero;
	private ImageButton mBackspaceButton;
	
	private Button mNextSetButton;
	private Button mFinishExerciseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exercise);

		//Grab all our views
		mExerciseTitleTextView = (TextView)findViewById(R.id.exercise_title_text_view);
		mLastRepsTextView = (TextView)findViewById(R.id.previous_rep_count_text_view);
		mCurrentRepsTextView = (TextView)findViewById(R.id.current_rep_count_text_view);
		mNextSetButton = (Button)findViewById(R.id.next_set_button);
		mFinishExerciseButton = (Button)findViewById(R.id.finish_exercise_button);
		mButtonOne = (Button)findViewById(R.id.exercise_one_button);
		mButtonTwo = (Button)findViewById(R.id.exercise_two_button);
		mButtonThree = (Button)findViewById(R.id.exercise_three_button);
		mButtonFour = (Button)findViewById(R.id.exercise_four_button);
		mButtonFive = (Button)findViewById(R.id.exercise_five_button);
		mButtonSix = (Button)findViewById(R.id.exercise_six_button);
		mButtonSeven = (Button)findViewById(R.id.exercise_seven_button);
		mButtonEight = (Button)findViewById(R.id.exercise_eight_button);
		mButtonNine = (Button)findViewById(R.id.exercise_nine_button);
		mButtonZero = (Button)findViewById(R.id.exercise_zero_button);
		mBackspaceButton = (ImageButton)findViewById(R.id.exercise_backspace_button);

		//Grabs our current exercise.
		mExercise = ((WorkoutWhizApplication)getApplication()).getCurrentExercise();

		//Setup our number pad controller.
		mNPController = new NumberPadController();
		
		//Grabs the title to this activity and sets it.
		mExerciseTitleTextView.setText(mExercise.getName() + " (" + mExercise.getNewWeight() + " lbs.)");
		
		//If we have a previously recorded rep for this exercise and set, then set the previous reps text box.
		if(mExercise.getLastReps().size() > 0) {
			mLastRepsTextView.setText(createRepsString(mExercise.getLastReps()));
		}
		//Otherwise, just throw in a 0 for reps completed last time.
		else {
			mLastRepsTextView.setText("N/A");
		}

		//Here are the button listeners for each button on the screen.
		mButtonOne.setOnClickListener(new OnNumberPadClickListener());
		mButtonTwo.setOnClickListener(new OnNumberPadClickListener());
		mButtonThree.setOnClickListener(new OnNumberPadClickListener());
		mButtonFour.setOnClickListener(new OnNumberPadClickListener());
		mButtonFive.setOnClickListener(new OnNumberPadClickListener());
		mButtonSix.setOnClickListener(new OnNumberPadClickListener());
		mButtonSeven.setOnClickListener(new OnNumberPadClickListener());
		mButtonEight.setOnClickListener(new OnNumberPadClickListener());
		mButtonNine.setOnClickListener(new OnNumberPadClickListener());
		mButtonZero.setOnClickListener(new OnNumberPadClickListener());
		mBackspaceButton.setOnClickListener(new OnNumberPadClickListener());
		
		//If the exercise has zero reps already completed, don't let the user hit mNextSetButton
		if(mExercise.getReps().size() == 0) {
			mNextSetButton.setEnabled(false);
		}
		mNextSetButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//Adds the completed rep to our exercise object
		    	mExercise.addRep(mNPController.getNumber());
		    	
		    	//Clears the text view and number pad controller
		    	mCurrentRepsTextView.setText(createRepsString(mExercise.getReps()) + ", " + mNPController.clearNumber());
		    	
		    	//If the current number of reps is higher than the minimum, enable the finish exercise button.
		    	if(mExercise.getReps().size() >= MIN_NUM_SETS) {
		    		mFinishExerciseButton.setEnabled(true);
		    	}
		    	
		    	//Disable this button until the user presses the next rep buttons.
		    	mNextSetButton.setEnabled(false);
		    }
		});
		
		mFinishExerciseButton.setEnabled(false);
		mFinishExerciseButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//Finish this exercise and go back to the previous screen.

		    	//Adds the completed rep to our exercise object
		    	if(mNPController.getNumber() != 0) {
		    		mExercise.addRep(mNPController.getNumber());
		    	}
		    	
		    	//Grab the current workout.
		    	Workout workout = ((WorkoutWhizApplication)getApplication()).getCurrentWorkout();
		    	
		    	//If there is only one complete exercise for the current workout,
		    	// check to see if it's the "None..." indicator.  If it is, then
		    	// remove it from the list.
		    	if(workout.getCompleteExercises().size() == 1) {
		    		if(workout.getCompleteExercises().get(0).getName().equals(Exercise.NO_EXERCISES)) {
		    			workout.getCompleteExercises().remove(0);
		    		}
		    	}
		    	
		    	//Complete our exercise.
		    	workout.completeExercise(mExercise);
		    	
		    	//Set our workout again.
		    	((WorkoutWhizApplication)getApplication()).setCurrentWorkout(workout);
		    	
		    	setResult(SelectExerciseActivity.SUCCESSFUL_EXERCISE);
		    	finish();
		    	overridePendingTransition(R.animator.enter_previous_activity, R.animator.exit_next_activity);
		    }
		});
	}
	
	/**
	 * Creates a string of reps from a given array of integers.
	 * @param reps - the arraylist object from our exercise that contains rep information
	 * @return - a string formatted the way we want to display it on the screen.
	 */
	public String createRepsString(ArrayList<Integer> reps) {
		String ret = "";
		
		for(int i = 0; i < reps.size(); i++) {
			ret += "" + reps.get(i);
			if(i != (reps.size()-1)) {
				ret += ", ";
			}
		}
		
		return ret;
	}
	
	private class OnNumberPadClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			int oldValue = mNPController.getNumber();
			int newValue = -1;
			
			//Sets newValue according to which button is pressed.
			if(v.getId() == R.id.exercise_one_button) {
			    newValue = mNPController.oneButtonClicked();
			}
			else if(v.getId() == R.id.exercise_two_button) {
			    newValue = mNPController.twoButtonClicked();
			}
			else if(v.getId() == R.id.exercise_three_button) {
			    newValue = mNPController.threeButtonClicked();
			}
			else if(v.getId() == R.id.exercise_four_button) {
			    newValue = mNPController.fourButtonClicked();
			}
			else if(v.getId() == R.id.exercise_five_button) {
			    newValue = mNPController.fiveButtonClicked();
			}
			else if(v.getId() == R.id.exercise_six_button) {
			    newValue = mNPController.sixButtonClicked();
			}
			else if(v.getId() == R.id.exercise_seven_button) {
			    newValue = mNPController.sevenButtonClicked();
			}
			else if(v.getId() == R.id.exercise_eight_button) {
			    newValue = mNPController.eightButtonClicked();
			}
			else if(v.getId() == R.id.exercise_nine_button) {
			    newValue = mNPController.nineButtonClicked();
			}
			else if(v.getId() == R.id.exercise_zero_button) {
			    newValue = mNPController.zeroButtonClicked();
			}
			else if(v.getId() == R.id.exercise_backspace_button) {
				newValue = mNPController.backspaceButtonClicked();
			}

			//If the old value was zero and we tried to backspace again, delete the last rep from our list.
			if(oldValue == 0 && newValue == 0 && mExercise.getReps().size() != 0) {
				
				//Removes and updates the numbers.
				newValue = mExercise.getReps().remove(mExercise.getReps().size()-1);
				mNPController.setNumber(newValue);

				//If the number of reps is now below the minimum to finish, disable finish button
				if(mExercise.getReps().size() < MIN_NUM_SETS) {
					mFinishExerciseButton.setEnabled(false);
				}
			}
			
			//Only allow the user to move on to the next set if the value isn't zero.
			if(newValue == 0 && mNextSetButton.isEnabled()) {
				mNextSetButton.setEnabled(false);
			}
			else if(newValue != 0 && !mNextSetButton.isEnabled() && mExercise.getReps().size() < MAX_NUM_SETS) {
				mNextSetButton.setEnabled(true);
			}
			
			//Only allow the user to finish the exercise if they have entered at least the minimum values.
			if(newValue == 0 && mFinishExerciseButton.isEnabled() && mExercise.getReps().size() < MIN_NUM_SETS) {
				mFinishExerciseButton.setEnabled(false);
			}
			else if(newValue != 0 && !mFinishExerciseButton.isEnabled() && mExercise.getReps().size() >= MIN_NUM_SETS - 1) {
				mFinishExerciseButton.setEnabled(true);
			}

	    	//Updates and formats the screen with the most recent number.
	    	if(mExercise.getReps().size() != 0) {
	    		mCurrentRepsTextView.setText(createRepsString(mExercise.getReps()) + ", " + newValue);
	    	}
	    	else {
	    		mCurrentRepsTextView.setText("" + newValue);
	    	}
		}
	}

	/**
	 * Override the default onBackPressed method to provide a confirmation message.
	 */
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Cancel this exercise?");
		builder.setMessage("You'll lose current progress!");
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//Set the result for this activity to indicate we cancelled the exercise and finish it.
		    	setResult(SelectExerciseActivity.CANCELLED_EXERCISE);
		    	finish();
		    	overridePendingTransition(R.animator.enter_previous_activity, R.animator.exit_next_activity);
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
