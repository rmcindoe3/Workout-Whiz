package com.mcindoe.workoutwhiz.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Workout {

	private String name;
	private String date;
	private long id;
	private ArrayList<Exercise> incompleteExercises;
	private ArrayList<Exercise> completeExercises;

	public Workout(String name) {
		this.setName(name);
		setCompleteExercises(new ArrayList<Exercise>());
		setIncompleteExercises(new ArrayList<Exercise>());
	}

	/**
	 * Changes the given date into the format we want for our date text view
	 * @param date - the original formatting of the date
	 * @return - the corrected format of the date
	 */
	public static String formatDate(String date) {
		
		//Grab a date object out of the given string.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateObj = new Date();
		try {
			dateObj = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "Date Format Error";
		}
		
		//Change the pattern to what we want and then return the new foramt.
		sdf.applyPattern("yyyy-MM-dd");
		return sdf.format(dateObj);
	}
	
	/**
	 * Removes the given exercise from the incomplete exercises list.
	 * @param exer - the exercise to remove.
	 */
	public void removeIncompleteExercise(Exercise exer) {
		for(int i = 0; i < incompleteExercises.size(); i++) {
			if(exer.completeEquals(incompleteExercises.get(i))) {
				incompleteExercises.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Removes the given exercise from the complete exercises list.
	 * @param exer - the exercise to remove.
	 */
	public void removeCompleteExercise(Exercise exer) {
		for(int i = 0; i < completeExercises.size(); i++) {
			if(exer.completeEquals(completeExercises.get(i))) {
				completeExercises.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Moves the given exercise from the complete exercise list to
	 * the incomplete exercise list
	 * @param exer - the exercise to move.
	 */
	public void markAsIncomplete(Exercise exer) {
		for(int i = 0; i < completeExercises.size(); i++) {
			if(exer.completeEquals(completeExercises.get(i))) {
				completeExercises.get(i).setReps(new ArrayList<Integer>());
				incompleteExercises.add(completeExercises.get(i));
				completeExercises.remove(i);
				break;
			}
		}
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
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		else if(!(other instanceof Workout)) {
			return false;
		}
		Workout wo = (Workout)other;
		if(!wo.getName().equals(this.getName())) {
			return false;
		}
		if(!wo.getDate().equals(this.getDate())) {
			return false;
		}
		if(this.completeExercises.size() == wo.completeExercises.size()) {
			for(int i = 0; i < this.completeExercises.size(); i++) {
				if(!this.completeExercises.get(i).equals(wo.completeExercises.get(i))) {
					return false;
				}
			}
		}
		else {
			return false;
		}
		if(this.incompleteExercises.size() == wo.incompleteExercises.size()) {
			for(int i = 0; i < this.incompleteExercises.size(); i++) {
				if(!this.incompleteExercises.get(i).equals(wo.incompleteExercises.get(i))) {
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		return true;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
