package com.mcindoe.workoutwhiz.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	
	public ArrayList<Workout> getFavoriteWorkouts() {
		
		//Initialize our return array.
		ArrayList<Workout> ret = new ArrayList<Workout>();
		
		//Initialize our workout to null, will return null if there is no workout in the database already
		Workout workout = null;
		
		Cursor favoriteCursor = database.rawQuery("SELECT DISTINCT favorite FROM workout ORDER BY favorite DESC", new String[] {});
		
		while(favoriteCursor.moveToNext()) {
			
			int favoriteNum = favoriteCursor.getInt(favoriteCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_FAVORITE));
			
			//If the next object in this cursor result is also a favorite...
			if(favoriteNum != 0) {
				ret.add(getMostRecentFavoriteWorkout(favoriteNum));
			}
		}
		
		return ret;
	}
	
	/**
	 * Gets the most recent workout that has the given favorite number
	 * @param favoriteNum - the favorite number for which you want to find the most recent workout of
	 * @return - the most recent workout with the given favorite number
	 */
	public Workout getMostRecentFavoriteWorkout(int favoriteNum) {
		
		//Initialize our workout object to null
		Workout workout = null;
		
		//Perform the appropriate query
		Cursor workoutCursor = database.rawQuery("SELECT * FROM workout WHERE favorite=? ORDER BY _id DESC", new String[] {String.valueOf(favoriteNum)});
		
		if(workoutCursor.moveToNext()) {
			
			//Initialize our workout variable with the name and date
			workout = new Workout(workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_NAME)));
			workout.setDate(workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_DATE)));
			workout.setFavorite(workoutCursor.getInt(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_FAVORITE)));
			workout.setId(workoutCursor.getLong(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_ID)));
			
			//Grab the workout id for this workout.
			long workoutId = workoutCursor.getLong(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_ID));
			
			workout.setIncompleteExercises(getExercises(workoutId));
		}
		
		return workout;
	}
	
	/**
	 * Returns the number of entries in this database that have the given
	 * favorite number.
	 * @param favoriteNum - the favorite number you want the count of
	 * @return - the number of times this favorite is in the database.
	 */
	public int getFavoriteWorkoutCount(int favoriteNum) {
		
		int ret = 0;
		
		Cursor cursor = database.rawQuery("SELECT * FROM workout WHERE favorite=?", new String[] {String.valueOf(favoriteNum)});
		
		while(cursor.moveToNext()) {
			ret++;
		}
		
		return ret;
	}
	
	/**
	 * Updates the given workout in the database to be a favorite.
	 * @param workout - the workout to be turned into a favorite
	 * @return - the database update command result
	 */
	public int updateWorkoutAsFavorite(Workout workout) {
		
		//Pack our values for the workout table
		ContentValues values = new ContentValues();
		values.put(WorkoutDBSQLiteHelper.WORKOUT_FAVORITE, workout.getFavorite());

		return database.update(WorkoutDBSQLiteHelper.TABLE_WORKOUT, values, "_id=?", new String[] {String.valueOf(workout.getId())});
	}
	
	/**
	 * Updates the given workout in the database as not a favorite
	 * @param workout - the workout to be unfavorited.
	 * @return - the number of workouts unfavorited..
	 */
	public int updateWorkoutAsNotFavorite(Workout workout) {
		
		//Pack our values for the workout table
		ContentValues values = new ContentValues();
		values.put(WorkoutDBSQLiteHelper.WORKOUT_FAVORITE, 0);

		return database.update(WorkoutDBSQLiteHelper.TABLE_WORKOUT, values, "favorite=?", new String[] {String.valueOf(workout.getFavorite())});
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
			
			//Grab the workout id for this workout.
			long workoutId = workoutCursor.getLong(workoutCursor.getColumnIndex(WorkoutDBSQLiteHelper.WORKOUT_ID));
			
			workout.setIncompleteExercises(getExercises(workoutId));
			ret.add(workout);
		}
		
		return ret;
	}
	
	/**
	 * Gets the exercises from our database that are associated with the given workout id
	 * @param workoutId - the id of the workout we want the exercises for
	 * @return - List of exercises associated with given workout id.
	 */
	public ArrayList<Exercise> getExercises(long workoutId) {

		//Create a list of exercises that we'll be adding to
		ArrayList<Exercise> prevExercises = new ArrayList<Exercise>();

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

			//Set the last reps for our exercise and add it to the exercise list.
			exer.setLastReps(getReps(exerciseId));
			prevExercises.add(exer);
		}
		
		return prevExercises;
	}
	
	/**
	 * Gets the rep count information for the given exercise id
	 * @param exerciseId - the id for the exercise that we want rep information for
	 * @return - List of reps associated with the given exercise id
	 */
	public ArrayList<Integer> getReps(long exerciseId) {

		ArrayList<Integer> lastReps = new ArrayList<Integer>();

		//Query for reps associated with that exercise id.
		Cursor repCursor = database.rawQuery("SELECT * FROM rep WHERE exercise_id = ? ORDER BY _id ASC", new String[] {""+exerciseId});

		//For every rep associated with this exercise
		while(repCursor.moveToNext()) {
			lastReps.add(repCursor.getInt(repCursor.getColumnIndex(WorkoutDBSQLiteHelper.REP_COUNT)));
		}
		
		return lastReps;
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
