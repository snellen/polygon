package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;

import ch.nellen.silvan.games.polygon.game.IGameState;

public class GameState extends Observable implements IGameState {
	
	private int mAngularDir = 0;
	private long mTimeElapsed;
	private float mCameraZ;
	private boolean mAcceptInput;
	private Phase mCurrentPhase = Phase.START;
	
	@Override
	public Phase getCurrentPhase() {
		return mCurrentPhase;
	}

	@Override
	public void setCurrentPhase(Phase newPhase) {
		if (mCurrentPhase != newPhase) {
			PhaseChange stateChange = new PhaseChange();
			stateChange.oldPhase = mCurrentPhase;
			stateChange.newPhase = newPhase;
			
			mCurrentPhase = newPhase;
			
			setChanged();
			notifyObservers(stateChange);
		}
	}

	public void setPlayerAngularDir(int mAngularDir) {
		this.mAngularDir = mAngularDir;
	}

	@Override
	public int getPlayerAngluarDir() {
		return mAngularDir;
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
	public void setAcceptInput(boolean flag) {
		mAcceptInput = flag;
	}

	@Override
	public boolean getAcceptInput() {
		return mAcceptInput;
	}

}
