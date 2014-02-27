package com.mcindoe.workoutwhiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.NumberPadController;
import com.mcindoe.workoutwhiz.models.Exercise;

public class ExerciseActivity extends Activity {

	private Exercise mExercise;
	private NumberPadController mNPController;

	private TextView lastRepsTextView;
	private TextView currentRepsTextView;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		
		mExercise = new Exercise("Overhead Press", 40);
		mNPController = new NumberPadController();
		
		lastRepsTextView = (TextView)findViewById(R.id.previous_rep_count_text_view);
		currentRepsTextView = (TextView)findViewById(R.id.current_rep_count_text_view);
		
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

		mButtonOne.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.oneButtonClicked());
		    }
		});
		
		mButtonTwo.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.twoButtonClicked());
		    }
		});
		
		mButtonThree.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.threeButtonClicked());
		    }
		});
		
		mButtonFour.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.fourButtonClicked());
		    }
		});
		
		mButtonFive.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.fiveButtonClicked());
		    }
		});
		
		mButtonSix.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.sixButtonClicked());
		    }
		});
		
		mButtonSeven.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.sevenButtonClicked());
		    }
		});
		
		mButtonEight.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.eightButtonClicked());
		    }
		});
		
		mButtonNine.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.nineButtonClicked());
		    }
		});
		
		mButtonZero.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.zeroButtonClicked());
		    }
		});
		
		mBackspaceButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        currentRepsTextView.setText("" + mNPController.backspaceButtonClicked());
		    }
		});

	}
}
