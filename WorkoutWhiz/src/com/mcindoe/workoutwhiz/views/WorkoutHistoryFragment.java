package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.controllers.WorkoutHistoryArrayAdapter;
import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutHistoryFragment extends Fragment {

	WorkoutHistoryArrayAdapter mWorkoutArrayAdapter;
	ListView mWorkoutsListView;
	
	public interface WorkoutHistoryListener {
		public void showWorkout(Workout workout);
	}
	
	private WorkoutHistoryListener sourceActivity;
	
	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            sourceActivity = (WorkoutHistoryListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement WorkoutHistoryListener");
        }	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View returnView = inflater.inflate(R.layout.fragment_workout_history, container, false);
		
		//Grabs our GUI elements
		mWorkoutsListView = (ListView)returnView.findViewById(R.id.workout_history_list_view);
		
		//Updates our list view.
		updateWorkoutListView();
		
		return returnView;
	}


	/**
	 * updates our workout history list view with the most recent info from the database.
	 */
	public void updateWorkoutListView() {
		
		//Create our list of workouts
		ArrayList<Workout> workouts;
		
		WorkoutDataSource wds = new WorkoutDataSource(this.getActivity());
		wds.open();
		
		//Grabs the 30 most recent workouts to be shown
		workouts = wds.getMostRecentWorkouts(30);

		wds.close();

		//Sets up our list view 
		mWorkoutArrayAdapter = new WorkoutHistoryArrayAdapter(this.getActivity(), workouts, new OnWorkoutHistoryClickListener());
		mWorkoutsListView.setAdapter(mWorkoutArrayAdapter);
	}

	/**
	 * Custom click listener for our list views.
	 */
	private class OnWorkoutHistoryClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			
			//If the view that called this is our list item
			if(v.getId() == R.id.list_item_workout_history) {
				
				WorkoutLinearLayout layout = (WorkoutLinearLayout)v.findViewById(R.id.list_item_workout_history);
				
				sourceActivity.showWorkout(layout.getWorkout());
			}
			//If the view that called this is our options button...
			else if(v.getId() == R.id.list_item_workout_options_button) {

				//TODO: show the options for this list item.
			}
		}
	}
}
