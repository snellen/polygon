package ch.nellen.silvan.games.polygon.game;

public interface IGameState {

	public abstract void setAngularDir(int mAngularDir);

	public abstract void setPauseState(boolean mPauseState);

	public abstract int currentPlayerAngluarDir();

	public abstract boolean pauseState();

}