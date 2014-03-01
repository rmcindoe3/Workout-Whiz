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
	
	/**
	 * Interface that allows us to pass events back to our select exercise activity.
	 */
	public interface WeightDialogListener {
		public void onDialogPositiveClick(Exercise exer);
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
		
		//Set them to the default weight for this exercise.
		mTextView.setText(mExercise.getWeight() + " lbs.");
		mSeekBar.setProgress(mExercise.getWeight());

		//Set up our seek bar listener.
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				//When the seek bar is moved, update the text view with it's new value
				mTextView.setText(progress + " lbs.");
				mExercise.setWeight(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				//We don't need this, but have to override it.
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//We don't need this, but have to override it.
			}
		});
		
		//Finishes creating our dialog builder.
		builder.setView(weightDialogView);

		builder.setTitle("Select Weight for " + mExercise.getName());

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						//When the ok button is pressed, let our source activity know
						//	that the weight has been selected.
						sourceActivity.onDialogPositiveClick(mExercise);
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

	public Exercise getExercise() {
		return mExercise;
	}

	public void setExercise(Exercise mExercise) {
		this.mExercise = mExercise;
	}
}
