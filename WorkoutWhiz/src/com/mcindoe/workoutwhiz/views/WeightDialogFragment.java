package com.mcindoe.workoutwhiz.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Exercise;

public class WeightDialogFragment extends DialogFragment {
	
	private SeekBar mSeekBar;
	private TextView mTextView;
	private Exercise mExercise;
	private int mExerciseOriginalWeight;
	
	/**
	 * Interface that allows us to pass events back to our select exercise activity.
	 */
	public interface WeightDialogListener {
		public void startExercise(Exercise exer);
	}
	
	private WeightDialogListener sourceActivity;
	
	@Override
	public void onAttach(Activity activity) {

        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            sourceActivity = (WeightDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement WeightDialogListener");
        }	
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		// Get the Layout Inflater.
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View weightDialogView = inflater.inflate(R.layout.dialog_weight, null);
		
		//Initialize our custom dialog elements.
		mSeekBar = (SeekBar)weightDialogView.findViewById(R.id.weight_input_seek_bar);
		mTextView = (TextView)weightDialogView.findViewById(R.id.weight_input_text_view);
		
		//Store the initial weight in case the user cancels this dialog.
		mExerciseOriginalWeight = mExercise.getWeight();
		
		//Set them to the default weight for this exercise.
		mTextView.setText(mExercise.getWeight() + " lbs.");
		
		//Set the max of the seekbar to 20
		mSeekBar.setMax(20);
		
		//Set the progress of the seekbar to the middle.
		mSeekBar.setProgress(10);

		//Set up our seek bar listener.
		mSeekBar.setOnSeekBarChangeListener(new WeightSeekBarChangeListener(mExercise.getWeight()));
		
		//Finishes creating our dialog builder.
		builder.setView(weightDialogView);

		builder.setTitle("Select weight for " + mExercise.getName());

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						//When the ok button is pressed, let our source activity know
						//	that the weight has been selected.
						sourceActivity.startExercise(mExercise);
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						//Revert the exercise back to it's original weight.
						mExercise.setWeight(mExerciseOriginalWeight);
					}
				});

		//Create the dialog.
		return builder.create();
	}
	
	/**
	 * Custom seek bar change listener that turns this seekbar into a 
	 * "sliding window" seekbar.
	 */
	private class WeightSeekBarChangeListener implements OnSeekBarChangeListener {
		
		private int weightOffset;
		private int maxChangedCounter;
		private int minChangedCounter;
		
		public WeightSeekBarChangeListener(int startingWeight) {
			this.weightOffset = startingWeight - 10;
			this.maxChangedCounter = 0;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			//If the seekbar is moved to maximum range and the maximum has not changed recently
			// then shift the weight offset by 10.
			if(progress == mSeekBar.getMax() && maxChangedCounter == 0) {
				weightOffset += 10;
				maxChangedCounter = 5;
			}
			else if(progress != mSeekBar.getMax() && maxChangedCounter != 0) {
				maxChangedCounter--;
			}
			
			//If the seekbar is moved to minimum range and the minimum has not changed recently
			// then shift the weight offset by 10.
			if(progress == 0 && minChangedCounter == 0 && weightOffset > 10) {
				weightOffset -= 10;
				if(weightOffset < 10) {
					weightOffset = 10;
				}
				minChangedCounter = 5;
			}
			else if(progress != 0 && minChangedCounter != 0) {
				minChangedCounter--;
			}

			//When the seek bar is moved, update the text view with it's new value
			mTextView.setText(progress + weightOffset + " lbs.");
			mExercise.setWeight(progress + weightOffset);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			//Do nothing...
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			//Do nothing...
		}
		
	}

	public Exercise getExercise() {
		return mExercise;
	}

	public void setExercise(Exercise mExercise) {
		this.mExercise = mExercise;
	}
}
