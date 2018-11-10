package com.hw3.timemanager;

public class Clock {

	int ticSize;
	boolean paused;
	long currentTime;
	long timeElapsed;
	long pausedElapsed;
	long nsPerTic;
	long nsInSec = 1000000000;
	long delta;

	public Clock() {
		// TODO Auto-generated constructor stub
		ticSize = 60; // meaning 60 tics in a second // default
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

	public void deltaTime() {
		delta += (System.nanoTime() - lastUpdatedTime()) / nsPerTic;
	}

	public long getDelta() {
		return delta;
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

}
