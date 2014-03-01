package com.mcindoe.workoutwhiz.models;

import java.util.ArrayList;

public class Workout {
	
	private String name;
	private String date;
	private ArrayList<Exercise> incompleteExercises;
	private ArrayList<Exercise> completeExercises;

	public Workout(String name) {
		this.setName(name);
		setCompleteExercises(new ArrayList<Exercise>());
		setIncompleteExercises(new ArrayList<Exercise>());
	}
	
	/**
	 * Moves an exercise from the incomplete list to the complete list
	 * @param exer - the exercise that has been completed.
	 */
	public void completeExercise(Exercise exer) {
		//If our incomplete exercises list contains the exercise, remove it
		if(incompleteExercises.contains(exer)) {
			incompleteExercises.remove(exer);
		}
		//Now add it to our complete exercises list.
		completeExercises.add(exer);
	}

	public ArrayList<Exercise> getIncompleteExercises() {
		return incompleteExercises;
	}

	public void setIncompleteExercises(ArrayList<Exercise> incompleteExercise) {
		this.incompleteExercises = incompleteExercise;
	}

	public ArrayList<Exercise> getCompleteExercises() {
		return completeExercises;
	}

	public void setCompleteExercises(ArrayList<Exercise> completeExercises) {
		this.completeExercises = completeExercises;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
