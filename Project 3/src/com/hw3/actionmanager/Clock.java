package com.hw3.actionmanager;

public class Clock {

	public int ticSize;
	boolean paused;
	long currentTime;
	long timeElapsed;
	long pausedElapsed;
	double delta = 0;
	public static final int DEFAULT_TIC_SIZE = 2;
	public Clock() {
		// TODO Auto-generated constructor stub
		ticSize = DEFAULT_TIC_SIZE; // meaning 60 tics in a second // default
	}

	public boolean isPaused() {
		return paused;
	}

	public long getSystemTime() {
		return System.currentTimeMillis();
	}

	public void setCurrentTime() {
		currentTime = getSystemTime();
	}

	public long lastUpdatedTime() {
		return currentTime;
	}

	public void updateTime() {
		// diff computes time in the game time frame 
		// by dividing by the tic size :) :)
		double diff = getSystemTime() - lastUpdatedTime();
		delta += diff;
		timeElapsed += diff;
		if (paused)
			pausedElapsed += diff;
	}

	public double getdeltaTime() {
		return delta;
	}

	public void decrementDelta() {
		delta-=ticSize;
	}

	public void pause() {
		paused = true;
	}

	public void unPause() {
		paused = false;
	}

	public void setTic(int ticSize) {
		// If the tic size increases the nsPerTic decrease
		// thereby making the frame move faster
		this.ticSize = ticSize;

	}
	
	public static double getTics(long startTime, long endTime, double ticSize) {
		return (endTime - startTime) / ticSize;
	}


}
