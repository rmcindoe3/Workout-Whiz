package com.mcindoe.workoutwhiz.controllers;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.views.ExerciseListItemLinearLayout;
import com.mcindoe.workoutwhiz.views.SelectExerciseActivity;

public class ExerciseArrayAdapter extends ArrayAdapter<Exercise> {
	
	private Context mContext;
	private List<Exercise> mExercises;
	private View.OnClickListener mListener;
	private boolean completed;

	public ExerciseArrayAdapter(Context context, List<Exercise> exercises, View.OnClickListener listener) {
	
		super(context, R.layout.list_item_exercise, exercises);
		
		this.mContext = context;
		this.mExercises = exercises;
		this.mListener = listener;
		this.completed = false;
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
			
			//Indicates to this adapter that we're now adding completed exercises.
			completed = true;
			
			//Inflate our row with the right object
			exerciseRow = inflater.inflate(R.layout.list_item_exercise_header, parent, false);
			
			//Grab the title text view and set it to the correct title.
			TextView tv = (TextView)exerciseRow.findViewById(R.id.list_item_exercise_header_subtitle);
			tv.setText("Complete Exercises:");
		}
		//Otherwise, we know we have a real exercise to be filled in
		else {
			
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
	
	/**
	 * Creates an appropriate string for the exercise intensity text view
	 * @param position - the position of the exercise in the list.
	 * @return the exercise intensity string
	 */
	public String getExerciseIntensityString(int position, boolean comp) {
		
		String ret = "";
		Exercise exer = mExercises.get(position);
		
		ret += exer.getWeight() + "/";

		if(!comp) {
			for(int i = 0; i < exer.getLastReps().size(); i++) {
				
				ret += exer.getLastReps().get(i) + "";
	
				if(i != (exer.getLastReps().size()-1)) {
					ret += ",";
				}
			}
		}
		else {
			for(int i = 0; i < exer.getReps().size(); i++) {
				
				ret += exer.getReps().get(i) + "";
	
				if(i != (exer.getReps().size()-1)) {
					ret += ",";
				}
			}
		}
		
		return ret;
	}
	
}
