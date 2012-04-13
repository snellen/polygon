package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;

import ch.nellen.silvan.games.polygon.game.IGameState;

/*
 * Holds the important information about the game,
 * notifies listeners when the game phase changes or a new highscore is reached
 * */
public class GameState extends Observable implements IGameState {

	private int mAngularDir = 0;
	private long mTimeElapsed;
	private long mHighscore = 0;
	private float mCameraZ;
	private Phase mCurrentPhase = Phase.START;
	private static Scene cScene;

	private static GameState instance = null;
	public static GameState instance() {
		if(instance == null)
			instance = new GameState();
		return instance;
	}
	
	private GameState() {
		super();
	}
	
	public static Scene getScene() {
		if(cScene == null)
			cScene = new Scene();
		return cScene;
	}
	
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
	public void setTotalTime(long time) {
		mTimeElapsed = time;
	}

	@Override
	public long getTotalTime() {
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

	public long getCurrentHighscore() {
		return mHighscore;
	}

	public void updateHighscore(long highscore) {
		if (mHighscore < highscore) {
			mHighscore = highscore;
			setChanged();
			notifyObservers(null);
		}
	}

}
