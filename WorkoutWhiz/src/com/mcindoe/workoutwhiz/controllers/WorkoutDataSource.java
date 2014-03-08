package com.mcindoe.workoutwhiz.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mcindoe.workoutwhiz.models.Exercise;
import com.mcindoe.workoutwhiz.models.Workout;

public class WorkoutDataSource {
	
	private SQLiteDatabase database;
	private WorkoutDBSQLiteHelper dbHelper;
	
	public WorkoutDataSource(Context context) {
		dbHelper = new WorkoutDBSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Workout insertWorkout(Workout workout) {
		long workoutId, exerciseId, repId;
		
		//Pack our values for the workout table
		ContentValues values = new ContentValues();
		values.put(WorkoutDBSQLiteHelper.WORKOUT_NAME, workout.getName());
		values.put(WorkoutDBSQLiteHelper.WORKOUT_FAVORITE, workout.getFavorite());
		values.put(WorkoutDBSQLiteHelper.WORKOUT_DATE, getCurrentDate());
		
		//Attempt to insert the values into the database
		workoutId = database.insert(WorkoutDBSQLiteHelper.TABLE_WORKOUT, null, values);
		
		//If the insert failed, return null
		if(workoutId == -1) {
			return null;
		}
		
		//For each of the completed exercises...
		for(Exercise exer : workout.getCompleteExercises()) {
			
			//Pack our values for the exercise table.
			values = new ContentValues();
			values.put(WorkoutDBSQLiteHelper.EXERCISE_NAME, exer.getName());
			values.put(WorkoutDBSQLiteHelper.EXERCISE_WORKOUT_ID, workoutId);
			values.put(WorkoutDBSQLiteHelper.EXERCISE_INTENSITY, exer.getWeight());
			
			//Attempt to insert the values into the database
			exerciseId = database.insert(WorkoutDBSQLiteHelper.TABLE_EXERCISE, null, values);
			
			//If the insert failed, return null
			if(exerciseId == -1) {
				return null;
			}
			
			for(Integer integer : exer.getReps()) {
				
				//Pack our values for the reps table
				values = new ContentValues();
				values.put(WorkoutDBSQLiteHelper.REP_EXERCISE_ID, exerciseId);
				values.put(WorkoutDBSQLiteHelper.REP_COUNT, integer.intValue());
				
				//Attempt to insert the values into the database.
				repId = database.insert(WorkoutDBSQLiteHelper.TABLE_REP, null, values);
				
				//If the insert failed, return null
				if(repId == -1) {
					return null;
				}
			}
		}
		
		return workout;
	}
	
	/**
	 * Gets the most recent workouts, up to the given amount.
	 * @param numWorkouts - the number of workouts you want to grab
	 * @return - a list containing the "numWorkouts"th most recent workouts stored in the db.
	 */
	public ArrayList<Workout> getMostRecentWorkouts(int numWorkouts) {
		
		//Initialize our return array.
		ArrayList<Workout> ret = new ArrayList<Workout>();

		//Initialize our workout to null, will return null if there is no workout in the database already
		Workout workout = null;
		
		//Make initial query to get the most recent workout.
		Cursor workoutCursor = database.rawQuery("SELECT * FROM workout ORDER BY _id DESC", new String[] {});
		
		//If there is a workout stored, grab the most recent one
		while(workoutCursor.moveToNext() && ret.size() < numWorkouts) {
			
			//Initialize our workout variable with the name and date
			workout = new Workout(workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_NAME)));
			workout.setDate(workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_DATE)));
			workout.setFavorite(workoutCursor.getInt(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_FAVORITE)));
			workout.setId(workoutCursor.getLong(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_ID)));
			
			//Create a list of exercises that we'll be adding to
			ArrayList<Exercise> prevExercises = new ArrayList<Exercise>();
			
			//Grab the workout id for this workout.
			long workoutId = workoutCursor.getLong(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_ID));
			
			//Query for exercises associated with that workout id.
			Cursor exerciseCursor = database.rawQuery("SELECT * FROM exercise WHERE workout_id = ? ORDER BY _id ASC", new String[] {""+workoutId});
			
			//For every exercise associated with this workout...
			while(exerciseCursor.moveToNext()) {
				
				//Grab the exercise id
				long exerciseId = exerciseCursor.getLong(exerciseCursor.getColumnIndex(WorkoutDBSQLiteHelper.EXERCISE_ID));
				
				//Grab the exercise name and intensity and create our exercise object as well as the previous reps list
				String exerciseName = exerciseCursor.getString(exerciseCursor.getColumnIndex(WorkoutDBSQLiteHelper.EXERCISE_NAME));
				int exerciseWeight = exerciseCursor.getInt(exerciseCursor.getColumnIndex(WorkoutDBSQLiteHelper.EXERCISE_INTENSITY));
				Exercise exer = new Exercise(exerciseName, exerciseWeight);
				ArrayList<Integer> lastReps = new ArrayList<Integer>();
				
				//Query for reps associated with that exercise id.
				Cursor repCursor = database.rawQuery("SELECT * FROM rep WHERE exercise_id = ? ORDER BY _id ASC", new String[] {""+exerciseId});
				
				//For every rep associated with this exercise
				while(repCursor.moveToNext()) {
					lastReps.add(repCursor.getInt(repCursor.getColumnIndex(WorkoutDBSQLiteHelper.REP_COUNT)));
				}
				
				//Set the last reps for our exercise and add it to the exercise list.
				exer.setLastReps(lastReps);
				prevExercises.add(exer);
			}
			
			workout.setIncompleteExercises(prevExercises);
			ret.add(workout);
		}
		
		return ret;
	}
	
	/**
	 * Deletes the Workout from the database that matches the given ID
	 * @param id - the ID of the workout we want to delete.
	 * @return - how many rows were deleted from the database.
	 */
	public int deleteWorkout(long id) {
		return database.delete(WorkoutDBSQLiteHelper.TABLE_WORKOUT, "_id=?", new String[] {String.valueOf(id)});
	}
	
	/**
	 * Clears the database of all tuples.
	 */
	public void clearDatabase() {
		database.execSQL("DROP TABLE IF EXISTS " + WorkoutDBSQLiteHelper.TABLE_WORKOUT);
		database.execSQL("DROP TABLE IF EXISTS " + WorkoutDBSQLiteHelper.TABLE_EXERCISE);
		database.execSQL("DROP TABLE IF EXISTS " + WorkoutDBSQLiteHelper.TABLE_REP);
		database.execSQL(WorkoutDBSQLiteHelper.WORKOUT_TABLE_CREATE);
		database.execSQL(WorkoutDBSQLiteHelper.EXERCISE_TABLE_CREATE);
		database.execSQL(WorkoutDBSQLiteHelper.REP_TABLE_CREATE);
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
}
