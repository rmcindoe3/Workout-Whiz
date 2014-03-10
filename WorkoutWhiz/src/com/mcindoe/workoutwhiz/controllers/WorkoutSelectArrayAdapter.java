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

        View workoutRow;

        //If the workout we are inflating is really an indicator that we don't have a workout...
        if(mWorkouts.get(position).getName().equals(Workout.NO_WORKOUTS)) {

            //Inflate the empty list view.
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workoutRow = inflater.inflate(R.layout.list_item_empty, parent, false);
        }
        //Otherwise just treat it like a normal workout.
        else {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workoutRow = inflater.inflate(R.layout.list_item_workout, parent, false);

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
            workoutDateTextView.setText(Workout.formatDate(mWorkouts.get(position).getDate()));

            //Sets the on click listener for this exercise.
            workoutRow.setOnClickListener(mListener);
        }

        return workoutRow;
    }
}
