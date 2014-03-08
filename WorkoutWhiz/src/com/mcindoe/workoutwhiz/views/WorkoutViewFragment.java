package com.mcindoe.workoutwhiz.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
	private ImageView favoriteStarView;

	private Workout mWorkout;

	public interface WorkoutViewButtonListener {
		public void eraseWorkout(Workout workout);
		public void favoriteWorkout(Workout workout);
		public void performWorkout(Workout workout);
	}
	
	private WorkoutViewButtonListener srcActivity;
	
	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            srcActivity = (WorkoutViewButtonListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement WorkoutViewButtonListener");
        }	
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_workout_view, container, false);
        
        //Grab the image view for the favorite star.
        favoriteStarView = (ImageView)fragmentView.findViewById(R.id.favorite_star_image_view);

        //Grab our buttons
        eraseWorkoutButton = (Button)fragmentView.findViewById(R.id.erase_workout_button);
        favoriteWorkoutButton = (Button)fragmentView.findViewById(R.id.favorite_workout_button);
        performWorkoutButton = (Button)fragmentView.findViewById(R.id.perform_workout_button);

        //If our workout is a favorite, change the name of the favorite button
        // and draw the favorite star in our image view.
        if(mWorkout.getFavorite() != 0) {
        	favoriteWorkoutButton.setText("Un-favorite");
        	favoriteStarView.setImageResource(R.drawable.favorite_star);
        }

        //Setup our erase button listener
        eraseWorkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				srcActivity.eraseWorkout(mWorkout);
			}
        });

        //Setup our favorite button listener
        favoriteWorkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				srcActivity.favoriteWorkout(mWorkout);
			}
        });

        //Setup our perform button listener
        performWorkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				srcActivity.performWorkout(mWorkout);
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
