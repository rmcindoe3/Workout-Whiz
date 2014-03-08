package com.mcindoe.workoutwhiz.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

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
	
	private WorkoutHistoryListener mWorkoutHistoryListener;
	private WorkoutViewFragment.WorkoutViewButtonListener mWorkoutViewButtonListener;

	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mWorkoutHistoryListener = (WorkoutHistoryListener) activity;
            mWorkoutViewButtonListener = (WorkoutViewFragment.WorkoutViewButtonListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement WorkoutHistoryListener and WorkoutViewButtonListener");
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
				
				mWorkoutHistoryListener.showWorkout(layout.getWorkout());
			}
			//If the view that called this is our options button...
			else if(v.getId() == R.id.list_item_workout_options_button) {

				showWorkoutOptions(v);
			}
		}
	}
	
	/**
	 * Shows the workout options popup menu.
	 * @param anchorView - the view that called this popup
	 */
	public void showWorkoutOptions(View anchorView) {

		//Grabs the parent layout of the settings button - the exercise list item linear layout
		final WorkoutLinearLayout srcLayout = ((WorkoutLinearLayout)anchorView.getParent());

		//Inflate our layout
		View workoutOptions = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_workout_options, null);
		
		//Create our popup
		final PopupWindow workoutPopup = new PopupWindow(workoutOptions, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//We want the popup window to be focusable
		workoutPopup.setFocusable(true);
		
		//We also want the popup to be dismissed when a touch is made outside of it.
		workoutPopup.setBackgroundDrawable(new ColorDrawable());
		
		Button viewButton = (Button)workoutOptions.findViewById(R.id.workout_options_view_button);
		Button performButton = (Button)workoutOptions.findViewById(R.id.workout_options_perform_button);
		Button favoriteButton = (Button)workoutOptions.findViewById(R.id.workout_options_favorite_button);
		Button eraseButton = (Button)workoutOptions.findViewById(R.id.workout_options_erase_button);
		
		viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWorkoutHistoryListener.showWorkout(srcLayout.getWorkout());
				workoutPopup.dismiss();
			}
		});

		performButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWorkoutViewButtonListener.performWorkout(srcLayout.getWorkout());
				workoutPopup.dismiss();
			}
		});

		//If the workout for this drop down is already a favorite, change the favorite button to
		// become an 'Un-favorite' button.
		if(srcLayout.getWorkout().getFavorite() != 0) {
			favoriteButton.setText("Un-favorite");
		}
		favoriteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWorkoutViewButtonListener.favoriteWorkout(srcLayout.getWorkout());
				workoutPopup.dismiss();
			}
		});

		eraseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWorkoutViewButtonListener.eraseWorkout(srcLayout.getWorkout());
				workoutPopup.dismiss();
			}
		});

		//Show the popup as a drop down from our anchor view
		workoutPopup.showAsDropDown(anchorView);
	}
}
