package com.mcindoe.workoutwhiz.controllers;

public class NumberPadController {

	private int number;
	private final int MAX_NUMBER = 400;
	
	public NumberPadController() {
		setNumber(0);
	}
	
	public int clearNumber() {
		setNumber(0);
		return 0;
	}
	
	public void capNumber() {
		if(number > MAX_NUMBER) {
			number = MAX_NUMBER;
		}
	}
	
	public int oneButtonClicked() {
		number *= 10;
		number += 1;
		capNumber();
		return number;		
	}
	
	public int twoButtonClicked() {
		number *= 10;
		number += 2;
		capNumber();
		return number;		
	}
	
	public int threeButtonClicked() {
		number *= 10;
		number += 3;
		capNumber();
		return number;		
	}
	
	public int fourButtonClicked() {
		number *= 10;
		number += 4;
		capNumber();
		return number;		
	}
	
	public int fiveButtonClicked() {
		number *= 10;
		number += 5;
		capNumber();
		return number;		
	}
	
	public int sixButtonClicked() {
		number *= 10;
		number += 6;
		capNumber();
		return number;		
	}
	
	public int sevenButtonClicked() {
		number *= 10;
		number += 7;
		capNumber();
		return number;		
	}
	
	public int eightButtonClicked() {
		number *= 10;
		number += 8;
		capNumber();
		return number;		
	}
	
	public int nineButtonClicked() {
		number *= 10;
		number += 9;
		capNumber();
		return number;		
	}
	
	public int zeroButtonClicked() {
		number *= 10;
		capNumber();
		return number;		
	}
	
	public int backspaceButtonClicked() {
		number /= 10;
		return number;		
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
