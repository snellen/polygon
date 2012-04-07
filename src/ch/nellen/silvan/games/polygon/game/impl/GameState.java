package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;

import ch.nellen.silvan.games.polygon.game.IGameState;

public class GameState extends Observable implements IGameState {

	enum ATTRIBUTE {
		STARTED, PAUSED
	}

	private int mAngularDir = 0;
	private boolean mPauseState = false;
	private long mTimeElapsed;
	private float mCameraZ;
	private boolean mPausedChangeable;
	private boolean mStarted;

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
	public void setCameraZ(float z) {
		mCameraZ = z;
	}

	@Override
	public float getCameraZ() {
		return mCameraZ;
	}

	@Override
	public void setPausedChangeable(boolean mPauseState) {
		mPausedChangeable = mPauseState;
	}

	@Override
	public boolean getPausedChangeable() {
		return mPausedChangeable;
	}

	@Override
	public void setStarted(boolean flag) {
		if (mStarted != flag) {
			mStarted = flag;
			setChanged();
			notifyObservers(ATTRIBUTE.STARTED);
		}
	}

	@Override
	public boolean getStarted() {
		return mStarted;
	}

}
