package com.mcindoe.workoutwhiz.controllers;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.views.ExerciseListItemLinearLayout;
import com.mcindoe.workoutwhiz.views.SelectExerciseActivity;

public class ExerciseArrayAdapter extends ArrayAdapter<Exercise> {
	
	private Context mContext;
	private List<Exercise> mExercises;
	private View.OnClickListener mListener;

	public ExerciseArrayAdapter(Context context, List<Exercise> exercises, View.OnClickListener listener) {
	
		super(context, R.layout.list_item_exercise, exercises);
		
		this.mContext = context;
		this.mExercises = exercises;
		this.mListener = listener;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//Grab which exercise we're currently on.
		Exercise exer = mExercises.get(position);
		
		//Grab our layout inflater
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//Initialize our return.
		View exerciseRow;
		
		//If the exercise we're on is actually the incomplete exercise header title, then add the title
		if(exer.equals(SelectExerciseActivity.INCOMPLETE_EXERCISE_TITLE)) {
			
			//Inflate our row ith the right object
			exerciseRow = inflater.inflate(R.layout.list_item_exercise_header, parent, false);
			
			//Grab the title text view and set it to the correct title.
			TextView tv = (TextView)exerciseRow.findViewById(R.id.list_item_exercise_header_subtitle);
			tv.setText("Incomplete Exercises:");
		}
		//If the exercise we're on is actually the complete exercise header title, then add the title
		else if(exer.equals(SelectExerciseActivity.COMPLETE_EXERCISE_TITLE)) {
			
			//Inflate our row with the right object
			exerciseRow = inflater.inflate(R.layout.list_item_exercise_header, parent, false);
			
			//Grab the title text view and set it to the correct title.
			TextView tv = (TextView)exerciseRow.findViewById(R.id.list_item_exercise_header_subtitle);
			tv.setText("Complete Exercises:");
		}
		//Otherwise, we know we have a real exercise to be filled in
		else {
			
			//If the exercise has completed reps, this is a completed exercise.
			boolean completed = (exer.getReps().size() > 0);
			
			//Inflate the proper layout
			exerciseRow = inflater.inflate(R.layout.list_item_exercise, parent, false);
			
			//Sets our exercise for this list item.
			ExerciseListItemLinearLayout layout = (ExerciseListItemLinearLayout)exerciseRow.findViewById(R.id.list_item_exercise);
			layout.setExercise(exer);

			//Fills in the name of the exercise.
			TextView exerciseNameTextView = (TextView)exerciseRow.findViewById(R.id.exercise_name_text_view);
			exerciseNameTextView.setText(exer.getName());

			//Fills in the intensity of the exercise.
			TextView exerciseIntensityTextView = (TextView)exerciseRow.findViewById(R.id.exercise_intensity_text_view);
			exerciseIntensityTextView.setText(getExerciseIntensityString(position, completed));
			
			//Adds a click listener to our options button.
			ImageButton optionsButton = (ImageButton)exerciseRow.findViewById(R.id.list_item_exercise_options_button);
			optionsButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Shows an exercise options popup menu
					showExerciseOptions(v, (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
				}
			});

			//If this is a completed exercise, color the text green.
			if(completed) {
				exerciseNameTextView.setTextColor(Color.rgb(0, 150, 0));
				exerciseIntensityTextView.setTextColor(Color.rgb(0, 150, 0));
			}
		}
		
		//Sets the on click listener for this exercise.
		exerciseRow.setOnClickListener(mListener);

		return exerciseRow;
	}
	
	public void showExerciseOptions(View anchorView, LayoutInflater inflater) {

		//Inflate our layout
		View exerciseOptions = inflater.inflate(R.layout.popup_exercise_options, null);
		
		//Create our popup
		PopupWindow exercisePopup = new PopupWindow(exerciseOptions, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//Set it up the way we want
		exercisePopup.setFocusable(true);
		exercisePopup.setBackgroundDrawable(new ColorDrawable());
		
		//Adds a listener to our perform exercise button.
		Button performButton = (Button)exerciseOptions.findViewById(R.id.exercise_options_perform_button);
		performButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		//Adds a listener to our edit exercise button.
		Button editButton = (Button)exerciseOptions.findViewById(R.id.exercise_options_edit_button);
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		//Adds a listener to our remove exercise button.
		Button removeButton = (Button)exerciseOptions.findViewById(R.id.exercise_options_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});


		//Show the popup as a drop down from our anchor view
		exercisePopup.showAsDropDown(anchorView);
	}
	
	/**
	 * Creates an appropriate string for the exercise intensity text view
	 * @param position - the position of the exercise in the list.
	 * @return the exercise intensity string
	 */
	public String getExerciseIntensityString(int position, boolean comp) {
		
		String ret = "";
		Exercise exer = mExercises.get(position);
		
		ret += exer.getWeight() + " lbs. - ";

		if(!comp) {
			for(int i = 0; i < exer.getLastReps().size(); i++) {
				
				ret += exer.getLastReps().get(i) + "";
	
				if(i != (exer.getLastReps().size()-1)) {
					ret += ", ";
				}
			}
		}
		else {
			for(int i = 0; i < exer.getReps().size(); i++) {
				
				ret += exer.getReps().get(i) + "";
	
				if(i != (exer.getReps().size()-1)) {
					ret += ", ";
				}
			}
		}
		
		return ret;
	}
	
}
