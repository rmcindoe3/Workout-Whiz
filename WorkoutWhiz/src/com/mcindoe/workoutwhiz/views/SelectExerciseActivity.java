package com.mcindoe.workoutwhiz.views;

import com.mcindoe.workoutwhiz.R;
import com.mcindoe.workoutwhiz.R.layout;
import com.mcindoe.workoutwhiz.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SelectExerciseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_exercise);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_exercise, menu);
		return true;
	}

}
