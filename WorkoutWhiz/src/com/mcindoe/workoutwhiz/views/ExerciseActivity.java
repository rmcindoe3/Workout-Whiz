package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.NumberPadController;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class ExerciseActivity extends Activity {

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
	private Button mBackspaceButton;
	
	private Button mNextSetButton;
	private Button mFinishExerciseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		mBackspaceButton = (Button)findViewById(R.id.exercise_backspace_button);

		//Grabs our current exercise.
		mExercise = ((WorkoutWhizApplication)getApplication()).getCurrentExercise();

		//Setup our number pad controller.
		mNPController = new NumberPadController();
		
		//Grabs the title to this activity and sets it.
		mExerciseTitleTextView.setText(mExercise.getName() + " (" + mExercise.getWeight() + ")");
		
		//If we have a previously recorded rep for this exercise and set, then set the previous reps text box.
		if(mExercise.getLastReps().size() > 0) {
			mLastRepsTextView.setText(createRepsString(mExercise.getLastReps()));
		}
		//Otherwise, just throw in a 0 for reps completed last time.
		else {
			mLastRepsTextView.setText("0");
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
		
		mNextSetButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	//Adds the completed rep to our exercise object
		    	mExercise.addRep(mNPController.getNumber());
		    	
		    	//Clears the text view and number pad controller
		    	mCurrentRepsTextView.setText(createRepsString(mExercise.getReps()) + ", " + mNPController.clearNumber());
		    }
		});
		
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
		    	
		    	//Complete our exercise.
		    	workout.completeExercise(mExercise);
		    	
		    	//Set our workout again.
		    	((WorkoutWhizApplication)getApplication()).setCurrentWorkout(workout);
		    	
		    	setResult(SelectExerciseActivity.SUCCESSFUL_EXERCISE);
		    	finish();
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

	    	//Updates and formats the screen with the most recent number.
	    	if(mExercise.getReps().size() != 0) {
	    		mCurrentRepsTextView.setText(createRepsString(mExercise.getReps()) + ", " + newValue);
	    	}
	    	else {
	    		mCurrentRepsTextView.setText("" + newValue);
	    	}
		}
		
	}
}
