package com.mcindoe.workoutwhiz.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.ExerciseViewArrayAdapter;
import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutViewFragment extends Fragment {

	private ExerciseViewArrayAdapter mExerciseViewArrayAdapter;
	private ListView mWorkoutListView;
	
	private Button eraseWorkoutButton;
	private Button favoriteWorkoutButton;
	private Button performWorkoutButton;

	private Workout mWorkout;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_workout_view, container, false);

        //Grab our buttons
        eraseWorkoutButton = (Button)fragmentView.findViewById(R.id.erase_workout_button);
        favoriteWorkoutButton = (Button)fragmentView.findViewById(R.id.favorite_workout_button);
        performWorkoutButton = (Button)fragmentView.findViewById(R.id.perform_workout_button);

        //Setup our erase button listener
        eraseWorkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO: setup erase button event
				Log.d("Button Event", "Erase");
			}
        });

        //Setup our favorite button listener
        favoriteWorkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO: setup favorite button event
				Log.d("Button Event", "Favorite");
			}
        });

        //Setup our perform button listener
        performWorkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO: setup perform button event
				Log.d("Button Event", "Perform");
			}
        });
        
        //Sets the title of the workout to the view.
        TextView title = (TextView)fragmentView.findViewById(R.id.workout_view_title_text_view);
        title.setText(mWorkout.getName());
        
        //Fills in our list view.
        mWorkoutListView = (ListView)fragmentView.findViewById(R.id.workout_view_list_view);
        mExerciseViewArrayAdapter = new ExerciseViewArrayAdapter(this.getActivity(), mWorkout.getIncompleteExercises(), null);
        mWorkoutListView.setAdapter(mExerciseViewArrayAdapter);
        
        return fragmentView;
    }

	public Workout getWorkout() {
		return mWorkout;
	}

	public void setWorkout(Workout mWorkout) {
		this.mWorkout = mWorkout;
	}

}
