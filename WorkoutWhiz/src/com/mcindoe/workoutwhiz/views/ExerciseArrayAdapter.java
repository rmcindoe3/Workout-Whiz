package com.mcindoe.workoutwhiz.views;

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

public class ExerciseArrayAdapter extends ArrayAdapter<Exercise> {
	
	private Context mContext;
	private List<Exercise> mExercises;
	private View.OnClickListener mListener;
	private boolean completed;

	public ExerciseArrayAdapter(Context context, boolean completed, List<Exercise> exercises, View.OnClickListener listener) {
	
		super(context, R.layout.list_item_exercise, exercises);
		
		this.mContext = context;
		this.mExercises = exercises;
		this.mListener = listener;
		this.completed = completed;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View exerciseRow = inflater.inflate(R.layout.list_item_exercise, parent, false);

		//Fills in the name of the exercise.
		TextView exerciseNameTextView = (TextView)exerciseRow.findViewById(R.id.exercise_name_text_view);
		exerciseNameTextView.setText(mExercises.get(position).getName());
		
		//Fills in the intensity of the exercise.
		TextView exerciseIntensityTextView = (TextView)exerciseRow.findViewById(R.id.exercise_intensity_text_view);
		exerciseIntensityTextView.setText(getExerciseIntensityString(position, completed));
		
		if(completed) {
			exerciseNameTextView.setTextColor(Color.rgb(0, 150, 0));
			exerciseIntensityTextView.setTextColor(Color.rgb(0, 150, 0));
		}
		
		//Sets the on click listener for this exercise.
		exerciseRow.setOnClickListener(mListener);

		return exerciseRow;
	}
	
	public void updateExerciseList(List<Exercise> exers) {
		mExercises.clear();
		mExercises.addAll(exers);
		this.notifyDataSetChanged();
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
