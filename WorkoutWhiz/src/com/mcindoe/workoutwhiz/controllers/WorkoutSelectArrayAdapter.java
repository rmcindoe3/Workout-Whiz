package com.mcindoe.workoutwhiz.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Workout;
import com.mcindoe.workoutwhiz.views.WorkoutLinearLayout;

public class WorkoutSelectArrayAdapter extends ArrayAdapter<Workout> {
	
	private Context mContext;
	private List<Workout> mWorkouts;
	private View.OnClickListener mListener;

	public WorkoutSelectArrayAdapter(Context context, List<Workout> workouts, View.OnClickListener listener) {
	
		super(context, R.layout.list_item_workout, workouts);
		
		this.mContext = context;
		this.mWorkouts = workouts;
		this.mListener = listener;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View workoutRow = inflater.inflate(R.layout.list_item_workout, parent, false);
		
		//Stores our workout in this view.
		WorkoutLinearLayout layout = (WorkoutLinearLayout)workoutRow.findViewById(R.id.list_item_workout);
		layout.setWorkout(mWorkouts.get(position));
		
		//Sets up the radio button listener. We want clickable to false because we're overriding the default 
		//	way radio buttons are grouped with our custom click listener.
		RadioButton workoutRadioButton = (RadioButton)workoutRow.findViewById(R.id.workout_select_radio_button);
		workoutRadioButton.setClickable(false);

		//Fills in the name of the workout.
		TextView workoutNameTextView = (TextView)workoutRow.findViewById(R.id.workout_name_text_view);
		workoutNameTextView.setText(mWorkouts.get(position).getName());
		
		//Fills in the date of the workout.
		TextView workoutDateTextView = (TextView)workoutRow.findViewById(R.id.workout_date_text_view);
		workoutDateTextView.setText(formatDate(mWorkouts.get(position).getDate()));
		
		//Sets the on click listener for this exercise.
		workoutRow.setOnClickListener(mListener);

		return workoutRow;
	}

	/**
	 * Changes the given date into the format we want for our date text view
	 * @param date - the original formatting of the date
	 * @return - the corrected format of the date
	 */
	public String formatDate(String date) {
		
		//Grab a date object out of the given string.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateObj = new Date();
		try {
			dateObj = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "Date Format Error";
		}
		
		//Change the pattern to what we want and then return the new foramt.
		sdf.applyPattern("yyyy-MM-dd");
		return sdf.format(dateObj);
	}
}
