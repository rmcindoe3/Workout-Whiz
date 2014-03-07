package com.mcindoe.workoutwhiz.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.ExerciseViewArrayAdapter;
import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutViewFragment extends Fragment {

	private ExerciseViewArrayAdapter mExerciseViewArrayAdapter;
	private ListView mWorkoutListView;

	private Workout mWorkout;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_workout_view, container, false);
        
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
