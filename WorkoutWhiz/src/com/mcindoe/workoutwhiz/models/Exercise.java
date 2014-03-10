package com.mcindoe.workoutwhiz.models;

import java.util.ArrayList;
import java.util.Locale;

public class Exercise {

    private String name;
    private int lastWeight;
    private int newWeight;
    private ArrayList<Integer> reps;
    private ArrayList<Integer> lastReps;

    public static final String NO_EXERCISES = "20jf328ndskl21,sn0svnd83n";

    public Exercise(String name) {
        this.setName(name);
        this.setLastWeight(0);
        setReps(new ArrayList<Integer>());
        setLastReps(new ArrayList<Integer>());
    }

    public Exercise(String name, int weight) {
        this.setName(name);
        this.setLastWeight(weight);
        setReps(new ArrayList<Integer>());
        setLastReps(new ArrayList<Integer>());
    }

    /**
     * Returns whether or not this activity has been completed
     * @return - whether or not the activity has been completed
     */
    public boolean isCompleted() {
        return (getReps().size() > 0);
    }

    /**
     * Performs a more thorough equals comparison for when you want to
     * see if the current reps and weight are the same as well
     * @param other - the other exercise
     * @return - if the two exercises are the same
     */
    public boolean completeEquals(Exercise other) {

        //Do a standard equals check.
        if(!equals(other)) {
            return false;
        }

        //If the new weights aren't the same, return false.
        if(this.getNewWeight() != other.getNewWeight()) {
            return false;
        }

        //If the new reps sizes aren't the same return false.
        if(this.getReps().size() != other.getReps().size()) {
            return false;
        }

        //If there are any differences between the two rep sets, return false.
        for(int i = 0; i < this.getReps().size(); i++) {
            if(this.getReps().get(i) != other.getReps().get(i)) {
                return false;
            }
        }

        //Otherwise return true;
        return true;
    }

    /**
     * Overridden equals method to compare exercises.  This equals only checks
     * the "last" variables (lastWeight and lastReps).  For a full check, perform
     * a completeEquals call.
     */
    @Override
    public boolean equals(Object other) {

        //Basic checks to make sure we're comparing a real exercise.
        if(other == null) {
            return false;
        }
        else if(!(other instanceof Exercise)) {
            return false;
        }

        //Make our other exercise cast.
        Exercise exer = (Exercise)other;

        //If the name or last weights aren't the same, return false.
        if(!this.getName().equals(exer.getName()) || this.getLastWeight() != exer.getLastWeight()) {
            return false;
        }

        //If the last reps size aren't the same, return false.
        if(this.getLastReps().size() != exer.getLastReps().size()) {
            return false;
        }

        //If any of the last rep sets aren't the same, return false.
        for(int i = 0; i < this.getLastReps().size(); i++) {
            if(this.getLastReps().get(i) != exer.getLastReps().get(i)) {
                return false;
            }
        }

        //Ohterwise return true.
        return true;
    }

    /**
     * Parses out a readable string that describes this exercise object.
     */
    @Override
    public String toString() {
        String ret = getName();
        ret += ": Last Weight (" + getLastWeight() + "), Weight (" + getNewWeight() + "), Last Reps (";
        for(int i = 0; i < getLastReps().size(); i++) {
            ret += "" + getLastReps().get(i);
            if(i != getLastReps().size()-1) {
                ret += ", ";
            }
        }
        ret += "), Curr Reps (";
        for(int i = 0; i < getReps().size(); i++) {
            ret += "" + getReps().get(i);
            if(i != getReps().size()-1) {
                ret += ", ";
            }
        }
        ret += ")";
        return ret;
    }

    /**
     * Adds a set to our current exercise.
     * @param num - the number of reps performed for this exercise.
     */
    public void addRep(int num) {
        reps.add(num);
    }

    /**
     * Capitalizes the first letter of a word and every first letter after spaces
     * @param inStr - the string you want to fix the capitalization of.
     * @return - properly capitalized string.
     */
    public static String capitalizeLetters(String inStr) {
        inStr = inStr.toLowerCase(Locale.US);
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

    public int getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(int lastWeight) {
        this.lastWeight = lastWeight;
    }

    public int getNewWeight() {
        return newWeight;
    }

    public void setNewWeight(int newWeight) {
        this.newWeight = newWeight;
    }
}
