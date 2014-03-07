package com.mcindoe.workoutwhiz.views;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.controllers.WorkoutDataSource;
import com.mcindoe.workoutwhiz.models.Workout;

public class HistoryActivity extends Activity implements WorkoutHistoryFragment.WorkoutHistoryListener {
	
	private WorkoutHistoryFragment mWorkoutHistoryFragment;
	private boolean workoutViewExtended;
	private int screenWidth;
	
	private float percentOfScreen;
	
	private FractionTranslateLinearLayout historyLayout;
	private FractionTranslateLinearLayout workoutLayout;
	private LinearLayout historyActivityLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_history);
		
		//Set initial screen width to -1 so we know to change it later.
		screenWidth = -1;

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
	 * Updates the screen width to the correct value and then appropriately sizes
	 * the two fragment containers on our screen.
	 */
	private void updateScreenWidth() {
		
		percentOfScreen = 0.9f;

		//Grabs the width of the screen in pixels from our layout.
		screenWidth = historyLayout.getScreenWidth();

		//Make sure our two layouts are set to static widths according to the width of the screen.
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)workoutLayout.getLayoutParams();
		params.width = (int) (screenWidth*percentOfScreen);
		workoutLayout.setLayoutParams(params);

		params = (LinearLayout.LayoutParams)historyLayout.getLayoutParams();
		params.width = screenWidth;
		historyLayout.setLayoutParams(params);
	}

	/**
	 * Called when our workout history fragment has a workout selected.
	 * @param workout
	 */
	@Override
	public void showWorkout(Workout workout) {
		
		if(screenWidth == -1) {
			updateScreenWidth();
		}

		//Create a new workout view fragment
		WorkoutViewFragment newFrag = new WorkoutViewFragment();
		newFrag.setWorkout(workout);

		//Replace the current workout fragment with the new one.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.workout_fragment_container, newFrag, "workout_fragment");
		ft.commit();
		
		//Extend the workout fragment if necessary.
		extendWorkoutFragment();
	}
	
	/**
	 * Extends the workout view fragment.
	 */
	public void extendWorkoutFragment() {
		
		if(!workoutViewExtended) {
			
			//The duration we want this animation to last.
			int animDuration = 300;

			//Set our state boolean to true
			workoutViewExtended = true;

			//Animate the transition.
			ObjectAnimator.ofFloat(this, "workoutHistoryLeftMargin", 0, percentOfScreen).setDuration(animDuration).start();
			ObjectAnimator.ofFloat(historyLayout, "alpha", 1, 0.2f).setDuration(animDuration).start();
		}
	}
	
	/**
	 * Hides the workout view fragment from the screen.
	 */
	public void hideWorkoutFragment() {
		
		//If the workout view is extended
		if(workoutViewExtended) {

			//The duration we want this animation to last.
			int animDuration = 300;

			//sets our state properly.
			workoutViewExtended = false;

			//Animate the transition between states.
			ObjectAnimator.ofFloat(this, "workoutHistoryLeftMargin", percentOfScreen, 0).setDuration(animDuration).start();
			ObjectAnimator.ofFloat(historyLayout, "alpha", 0.2f, 1).setDuration(animDuration).start();
		}
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
