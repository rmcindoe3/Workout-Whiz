package com.mcindoe.workoutwhiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mcindoe.workoutwhiz.R;

public class SelectExerciseActivity extends Activity implements WeightDialogFragment.WeightDialogListener {
	
	private EditText mAddExerciseEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_exercise);
		
		mAddExerciseEditText = (EditText)findViewById(R.id.add_exercise_edit_text);
	}

	/**
	 * Called when the user pressed the create new exercise button.
	 */
	public void onAddExerciseButtonClicked(View view) {

		WeightDialogFragment weightDialog = new WeightDialogFragment();
		weightDialog.setInitialWeight(25);
		weightDialog.show(getFragmentManager(), "weight dialog");
	}

	/**
	 * Called after the user has successfully chosen a desired weight.
	 */
	@Override
	public void onDialogPositiveClick(int weight) {
	}
}
