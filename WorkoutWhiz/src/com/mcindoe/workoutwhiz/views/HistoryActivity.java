package com.mcindoe.workoutwhiz.views;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.models.Workout;

public class HistoryActivity extends Activity implements WorkoutHistoryFragment.WorkoutHistoryListener {
	
	private WorkoutHistoryFragment mWorkoutHistoryFragment;
	private boolean workoutViewExtended;
	private int screenWidth;
	
	private FractionTranslateLinearLayout historyLayout;
	private FractionTranslateLinearLayout workoutLayout;
	private LinearLayout historyActivityLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_history);

		//Grabs our GUI elements.
	    historyLayout = (FractionTranslateLinearLayout)findViewById(R.id.history_fragment_container);
	    workoutLayout = (FractionTranslateLinearLayout)findViewById(R.id.workout_fragment_container);
	    historyActivityLayout = (LinearLayout)findViewById(R.id.history_activity_layout);
		
	    //We're going to start without the workout view extended across the screen
		workoutViewExtended = false;

		//Lets create our workout history fragment
		mWorkoutHistoryFragment = new WorkoutHistoryFragment();
		
		//And then add it to our history fragment container.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.history_fragment_container, mWorkoutHistoryFragment, "workout_history_fragment");
		ft.commit();
	}
	
	/**
	 * Called when our workout history fragment has a workout selected.
	 * @param workout
	 */
	@Override
	public void showWorkout(Workout workout) {

		//Create a new workout view fragment
		WorkoutFragment newFrag = new WorkoutFragment();
		newFrag.setWorkout(workout);

		//Replace the current workout fragment with the new one.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.workout_fragment_container, newFrag, "workout_fragment");
		ft.commit();
		
		//If the workout view isn't already extended, extend it.
		if(!workoutViewExtended) {
			
			//Grabs the width of the screen in pixels from our layout.
			screenWidth = historyLayout.getScreenWidth();

			//Extends the workout view fragment.
			extendWorkoutFragment();
		}
	}
	
	/**
	 * Extends the workout view fragment.
	 */
	public void extendWorkoutFragment() {
		
		//Set our state boolean to true
		workoutViewExtended = true;

		//Make sure our two layouts are set to static widths according to the width of the screen.
	    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)workoutLayout.getLayoutParams();
	    params.width = (int) (screenWidth*0.8f);
	    workoutLayout.setLayoutParams(params);

	    params = (LinearLayout.LayoutParams)historyLayout.getLayoutParams();
	    params.width = screenWidth;
	    historyLayout.setLayoutParams(params);

	    //Animate the transition.
		ObjectAnimator.ofFloat(this, "workoutHistoryLeftMargin", 0, 0.8f).setDuration(200).start();
	}
	
	/**
	 * Hides the workout view fragment from the screen.
	 */
	public void hideWorkoutFragment() {
		
		//sets our state properly.
		workoutViewExtended = false;

		//Animate the transition between states.
		ObjectAnimator.ofFloat(this, "workoutHistoryLeftMargin", 0.8f, 0).setDuration(200).start();
	}

	/**
	 * Called by an object animator to animate the movement of our workout 
	 * history fragment. 
	 * @param value - percentage of the screen to move the left margin of the layout.
	 */
	@SuppressWarnings("unused")
	private void setWorkoutHistoryLeftMargin(float value) {
		
		//Grab our layouts params and then move them to the left according to the given value.
	    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)historyLayout.getLayoutParams();
	    params.leftMargin = (int) (0 - value*screenWidth);
	    historyLayout.setLayoutParams(params);
	    
	    historyActivityLayout.requestLayout();
	}
	
	/**
	 * Overrides the default back pressed function to implement
	 * hiding of the workout view if it's extended.
	 */
	@Override
	public void onBackPressed() {

		//If the workout view is extended, then hide it
		if(workoutViewExtended) {
			hideWorkoutFragment();
		}
		//If the workout view is already hidden, just do a normal backPressed
		else {
			super.onBackPressed();
		}
	}
	
	/**
	 * Called when the user pressed the clear workouts button in the interface.
	 * @param view - the view that called this method
	 */
	public void onClearWorkoutsButtonClicked(View view) {
		
		final HistoryActivity srcActivity = (HistoryActivity)this;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete workout history?");
		builder.setMessage("You'll lose all workout data!");
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//Clear the database if they click yes
				WorkoutDataSource wds = new WorkoutDataSource(srcActivity);
				wds.open();
				wds.clearDatabase();
				wds.close();
				mWorkoutHistoryFragment.updateWorkoutListView();
			}
		});
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//The dialog will be dismissed and nothing happens.
			}
		});
		AlertDialog dialog = builder.create();
		
		dialog.show();
	}
	
	/**
	 * Called when the user pressed the export workouts button in the interface.
	 * @param view - the view that called this method
	 */
	public void onExportWorkoutsButtonClicked(View view) {
		
		//TODO: implement exporting of workouts here.
	}
}
