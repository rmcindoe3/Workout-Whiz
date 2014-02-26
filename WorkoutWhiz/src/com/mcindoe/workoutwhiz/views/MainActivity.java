package com.mcindoe.workoutwhiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.mcindoe.workoutwhiz.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Called when the Workout! button is clicked in the main activity.
	 */
	public void onWorkoutButtonClicked(View view) {
		//TODO: Open select exercise activity.
	}

	/**
	 * Called when the History button is clicked in the main activity.
	 */
	public void onHistoryButtonClicked(View view) {
		//TODO: Open history activity.
	}

	/**
	 * Called when the Settings button is clicked in the main activity.
	 */
	public void onSettingsButtonClicked(View view) {
		//TODO: Open settings activity.
	}
}
