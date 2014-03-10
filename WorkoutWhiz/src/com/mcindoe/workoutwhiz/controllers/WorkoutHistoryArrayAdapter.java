package com.mcindoe.workoutwhiz.controllers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.models.Workout;
import com.mcindoe.workoutwhiz.views.WorkoutLinearLayout;

public class WorkoutHistoryArrayAdapter extends ArrayAdapter<Workout> {

    private Context mContext;
    private List<Workout> mWorkouts;
    private View.OnClickListener mListener;

    public WorkoutHistoryArrayAdapter(Context context, List<Workout> workouts, View.OnClickListener listener) {

        super(context, R.layout.list_item_workout_history, workouts);

        this.mContext = context;
        this.mWorkouts = workouts;
        this.mListener = listener;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Workout workout = mWorkouts.get(position);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate the proper layout
        View workoutRow = inflater.inflate(R.layout.list_item_workout_history, parent, false);

        //Sets our workout for this list item.
        WorkoutLinearLayout layout = (WorkoutLinearLayout)workoutRow.findViewById(R.id.list_item_workout_history);
        layout.setWorkout(workout);

        //Fills in the name of the workout.
        TextView workoutNameTextView = (TextView)workoutRow.findViewById(R.id.workout_name_text_view);
        workoutNameTextView.setText(workout.getName());

        //Fills in the date of the workout.
        TextView workoutDateTextView = (TextView)workoutRow.findViewById(R.id.workout_date_text_view);
        workoutDateTextView.setText(Workout.formatDate(workout.getDate()));

        //If this workout is a favorite, draw the star on our favorite star image view.
        if(workout.getFavorite() != 0) {
            ImageView favoriteStarView = (ImageView)workoutRow.findViewById(R.id.favorite_star_image_view);
            favoriteStarView.setImageResource(R.drawable.favorite_star);
        }

        //Adds a click listener to our options button.
        ImageButton optionsButton = (ImageButton)workoutRow.findViewById(R.id.list_item_workout_options_button);
        optionsButton.setOnClickListener(mListener);

        //Sets the on click listener for this workout.
        workoutRow.setOnClickListener(mListener);

        return workoutRow;
    }

}
