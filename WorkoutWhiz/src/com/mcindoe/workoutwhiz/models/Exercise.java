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
		setReps(new ArrayList<Integer>());
		setLastReps(new ArrayList<Integer>());
	}

	public Exercise(String name, int weight) {
		this.setName(name);
		this.setWeight(weight);
		setReps(new ArrayList<Integer>());
		setLastReps(new ArrayList<Integer>());
	}
	
	/**
	 * Overridden equals method to compare the names of exercises.
	 */
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		else if(!(other instanceof Exercise)) {
			return false;
		}
		else {
			if(((Exercise)other).getName().equals(this.getName())) {
				return true;
			}
		}
		return false;
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
	
	/**
	 * Capitalizes the first letter of a word and every first letter after spaces
	 * @param inStr - the string you want to fix the capitalization of.
	 * @return - properly capitalized string.
	 */
	public static String capitalizeLetters(String inStr) {
		inStr = inStr.toLowerCase();
		boolean capitalizeNext = true;
		for(int i = 0; i < inStr.length(); i++) {
			if(capitalizeNext && inStr.charAt(i) >= 'a' && inStr.charAt(i) <= 'z') {
				inStr = inStr.substring(0, i) + (char)(inStr.charAt(i) - 'a' + 'A') + inStr.substring(i+1);
				capitalizeNext = false;
			}
			if(inStr.charAt(i) == ' ') {
				capitalizeNext = true;
			}
		}
		return inStr;
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
	
	public ArrayList<Integer> getReps() {
		return reps;
	}

	public void setReps(ArrayList<Integer> reps) {
		this.reps = reps;
	}

	public ArrayList<Integer> getLastReps() {
		return lastReps;
	}

	public void setLastReps(ArrayList<Integer> lastReps) {
		this.lastReps = lastReps;
	}
}
