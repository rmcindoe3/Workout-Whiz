package com.mcindoe.workoutwhiz.models;

import java.util.ArrayList;

public class Workout {
	
	private String name;
	private ArrayList<Exercise> exercises;

	public Workout(String name) {
		this.setName(name);
		exercises = new ArrayList<Exercise>();
	}
	
	/**
	 * Adds an exercise to our workout.
	 * @param exer - the exercise you want to add.
	 */
	public void addExercise(Exercise exer) {
		exercises.add(exer);
	}
	
	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
