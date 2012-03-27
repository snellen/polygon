package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.game.IGameState;

public class GameState implements IGameState {

	private int mAngularDir = 0;
	private boolean mPauseState = false;
	private long mTimeElapsed;
	private float mTime;

	public void setPlayerAngularDir(int mAngularDir) {
		this.mAngularDir = mAngularDir;
	}

	public void setPaused(boolean mPauseState) {
		this.mPauseState = mPauseState;
	}

	@Override
	public int getPlayerAngluarDir() {
		return mAngularDir;
	}

	@Override
	public boolean getPaused() {
		return mPauseState;
	}

	@Override
	public void setTimeElapsed(long time) {
		mTimeElapsed = time;
	}

	@Override
	public long getTimeElapsed() {
		return mTimeElapsed;
	}

	@Override
	public void setCameraZ(float time) {
		mTime = time;
	}

	@Override
	public float getCameraZ() {
		return mTime;
	}

}
