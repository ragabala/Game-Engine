package com.hw3.actionmanager;

public class Clock {

	
	public static final long START_TIME = System.currentTimeMillis(); 
	public int ticSize;
	boolean paused;
	long currentTime;
	long lastTime;
	double timeStep;
	long timeElapsed;
	long pausedElapsed;
	double delta = 0;
	public static final int DEFAULT_TIC_SIZE = 40;
	public Clock() {
		// TODO Auto-generated constructor stub
		setTic(DEFAULT_TIC_SIZE);// meaning 60 tics in a second // default
	}

	public boolean isPaused() {
		return paused;
	}

	public long getSystemTime() {
		return System.currentTimeMillis() - START_TIME;
	}

	public void setCurrentTime() {
		currentTime = getSystemTime();
	}
	
	public void setLastToCurrent() {
		lastTime = currentTime;
	}

	public long lastUpdatedTime() {
		return lastTime;
	}

	public void updateDelta() {
		// diff computes time in the game time frame 
		// by dividing by the tic size :) :)
		double diff = currentTime - lastUpdatedTime();
		delta += diff;
		timeElapsed += diff;
		if (paused)
			pausedElapsed += diff;
	}

	public double getdeltaTime() {
		return delta;
	}

	public void decrementDelta() {
		delta-=timeStep;
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
		timeStep = 1000/ticSize;

	}
	
	public double getTimeStep() {
		return timeStep; // 1000 is number of ms in a sec
	}
	
	public static double getTics(long startTime, long endTime, double ticSize) {
		return (endTime - startTime) / ticSize;
	}


}
