package com.hw3.actionmanager;

public class Clock {

	static final long START_TIME = System.currentTimeMillis();
	static boolean paused;
	static long currentTime;
	static long replayStartTime;
	static long lastTime;
	static long timeElapsed;
	static long pausedElapsed;
	static double delta = 0;
	public static final int DEFAULT_TIC_SIZE = 40;
	static int ticSize = DEFAULT_TIC_SIZE;
	static double timeStep = 1000/ ticSize;
	
	private Clock() {
		// TODO Auto-generated constructor stub
	}

	public static boolean isPaused() {
		return paused;
	}
	
	public static void setReplayStartTime() {
		replayStartTime = getSystemTime();
	}

	public static long getReplayTime() {
		return replayStartTime;
	}
	
	public static long getSystemTime() {
		return System.currentTimeMillis() - START_TIME;
	}

	public static void setCurrentTime() {
		currentTime = getSystemTime();
	}

	public static void setLastToCurrent() {
		lastTime = currentTime;
	}

	public static long lastUpdatedTime() {
		return lastTime;
	}

	public static void updateDelta() {
		// diff computes time in the game time frame
		// by dividing by the tic size :) :)
		double diff = currentTime - lastUpdatedTime();
		delta += diff;
		timeElapsed += diff;
		if (paused)
			pausedElapsed += diff;
	}

	public static double getdeltaTime() {
		return delta;
	}

	public static void decrementDelta() {
		delta -= timeStep;
	}

	public static void pause() {
		paused = true;
	}

	public static void unPause() {
		paused = false;
	}

	public static void setTic(int tics) {
		// If the tic size increases the nsPerTic decrease
		// thereby making the frame move faster
		ticSize = tics;
		timeStep = 1000 / ticSize;

	}

	public static double getTimeStep() {
		return timeStep; // 1000 is number of ms in a sec
	}

	public static double getTics(long startTime, long endTime) {
		return (endTime - startTime) / timeStep;
	}

}
