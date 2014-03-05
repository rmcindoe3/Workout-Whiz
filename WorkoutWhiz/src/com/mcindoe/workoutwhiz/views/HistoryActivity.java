package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.controllers.WorkoutHistoryArrayAdapter;
import com.mcindoe.workoutwhiz.models.Workout;

public class HistoryActivity extends Activity {
	
	WorkoutHistoryArrayAdapter mWorkoutArrayAdapter;
	ListView mWorkoutsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_history);
		
		//Grabs our GUI elements
		mWorkoutsListView = (ListView)findViewById(R.id.workout_history_list_view);
		
		//Updates our list view.
		updateWorkoutListView();
	}
	
	/**
	 * Called when the user pressed the clear workouts button in the interface.
	 * @param view - the view that called this method
	 */
	public void onClearWorkoutsButtonClicked(View view) {
		
		//TODO: clear workout history here.
	}
	
	/**
	 * Called when the user pressed the export workouts button in the interface.
	 * @param view - the view that called this method
	 */
	public void onExportWorkoutsButtonClicked(View view) {
		
		//TODO: implement exporting of workouts here.
	}

	/**
	 * updates our workout history list view with the most recent info from the database.
	 */
	public void updateWorkoutListView() {
		
		//Create our list of workouts
		ArrayList<Workout> workouts;
		
		WorkoutDataSource wds = new WorkoutDataSource(this);
		wds.open();
		
		//Grabs the 30 most recent workouts to be shown
		workouts = wds.getMostRecentWorkouts(30);

		wds.close();

		//Sets up our list view 
		mWorkoutArrayAdapter = new WorkoutHistoryArrayAdapter(this, workouts, new OnWorkoutHistoryClickListener());
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
				
				//TODO: Show a workout fragment here.
			}
			//If the view that called this is our options button...
			else if(v.getId() == R.id.list_item_workout_options_button) {

				//TODO: show the options for this list item.
			}
		}
	}
}
