package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.game.IGameState;

public class GameState implements IGameState {

	private int mAngularDir = 0;
	private boolean mPauseState = false;

	public void setAngularDir(int mAngularDir) {
		this.mAngularDir = mAngularDir;
	}

	public void setPauseState(boolean mPauseState) {
		this.mPauseState = mPauseState;
	}

	@Override
	public int currentPlayerAngluarDir() {
		return mAngularDir;
	}

	@Override
	public boolean pauseState() {
		return mPauseState;
	}

}
