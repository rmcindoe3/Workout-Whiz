package com.mcindoe.workoutwhiz.models;

import java.util.ArrayList;

public class Exercise {

	private String name;
	private int weight;
	private ArrayList<Integer> reps;
	private ArrayList<Integer> lastReps;
	
	public Exercise(String name) {
		this.setName(name);
		this.setWeight(0);
		reps = new ArrayList<Integer>();
		lastReps = new ArrayList<Integer>();
	}

	public Exercise(String name, int weight) {
		this.setName(name);
		this.setWeight(weight);
		reps = new ArrayList<Integer>();
		lastReps = new ArrayList<Integer>();
	}
	
	/**
	 * Adds a set to our current exercise.
	 * @param num - the number of reps performed for this exercise.
	 */
	public void addRep(int num) {
		reps.add(num);
	}
	
	/**
	 * Grabs information about the last time the user has performed this 
	 * exercise and stores it in our lastReps ArrayList.
	 */
	public void updateLastReps() {
		//TODO: Query database once it's finished and fill out the amount of
		//	reps they have completed in this exercise in the past.
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
