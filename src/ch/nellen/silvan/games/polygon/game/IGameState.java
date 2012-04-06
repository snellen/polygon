package ch.nellen.silvan.games.polygon.game;

public interface IGameState {

	public abstract void setPlayerAngularDir(int mAngularDir);
	
	public abstract int getPlayerAngluarDir();

	public abstract void setPaused(boolean mPauseState);

	public abstract boolean getPaused();
	
	public abstract void setStarted(boolean mPauseState);

	public abstract boolean getStarted();
	
	public abstract void setPausedChangeable(boolean mPauseState);

	public abstract boolean getPausedChangeable();
	
	public abstract void setTimeElapsed(long time);
	
	public abstract long getTimeElapsed();
	
	public abstract void setCameraZ(float time);
	
	public abstract float getCameraZ();
	
}