package com.hw3.actionmanager;

public class Clock {

	public int ticSize;
	boolean paused;
	long currentTime;
	long timeElapsed;
	long pausedElapsed;
	long nsInSec = 1000000000;
	double nsPerTic;
	double delta = 1;
	public static final int DEFAULT_TIC_SIZE = 60;
	public Clock() {
		// TODO Auto-generated constructor stub
		ticSize = DEFAULT_TIC_SIZE; // meaning 60 tics in a second // default
		nsPerTic = nsInSec / ticSize; // 16666666.6667 is the default tic size
	}

	public boolean isPaused() {
		return paused;
	}

	public long getSystemTime() {
		return System.nanoTime();
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
		double diff = (getSystemTime() - lastUpdatedTime()) / nsPerTic;
		delta += diff;
		System.out.println(delta);
		timeElapsed += diff;
		if (paused)
			pausedElapsed += diff;
	}

	public double getdeltaTime() {
		return delta;
	}

	public void decrementDelta() {
		delta--;
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
		nsPerTic = nsInSec / ticSize;
	}
	
	public static double getTics(long startTime, long endTime, double nsPerTic) {
		return (endTime - startTime) / nsPerTic;
	}
	
	public double getNsPerTic(int ticSize) {
		return nsInSec/ticSize;
	}

}
