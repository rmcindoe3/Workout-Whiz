package com.mcindoe.workoutwhiz.controllers;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.views.ExerciseLinearLayout;
import com.mcindoe.workoutwhiz.views.SelectExerciseActivity;

public class ExerciseViewArrayAdapter extends ArrayAdapter<Exercise> {
	
	private Context mContext;
	private List<Exercise> mExercises;
	private View.OnClickListener mListener;

	public ExerciseViewArrayAdapter(Context context, List<Exercise> exercises, View.OnClickListener listener) {
	
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

		//Inflate the proper layout
		exerciseRow = inflater.inflate(R.layout.list_item_exercise_view, parent, false);

		//Sets our exercise for this list item.
		ExerciseLinearLayout layout = (ExerciseLinearLayout)exerciseRow.findViewById(R.id.list_item_exercise);
		layout.setExercise(exer);

		//Fills in the name of the exercise.
		TextView exerciseNameTextView = (TextView)exerciseRow.findViewById(R.id.exercise_name_text_view);
		exerciseNameTextView.setText(exer.getName());

		//Fills in the intensity of the exercise.
		TextView exerciseIntensityTextView = (TextView)exerciseRow.findViewById(R.id.exercise_intensity_text_view);
		exerciseIntensityTextView.setText(getExerciseIntensityString(position));

		return exerciseRow;
	}
	
	/**
	 * Creates an appropriate string for the exercise intensity text view
	 * @param position - the position of the exercise in the list.
	 * @return the exercise intensity string
	 */
	public String getExerciseIntensityString(int position) {
		
		String ret = "";
		Exercise exer = mExercises.get(position);
		
		ret += exer.getWeight() + " lbs. - ";

		for(int i = 0; i < exer.getLastReps().size(); i++) {

			ret += exer.getLastReps().get(i) + "";

			if(i != (exer.getLastReps().size()-1)) {
				ret += ", ";
			}
		}
		
		return ret;
	}
	
}
