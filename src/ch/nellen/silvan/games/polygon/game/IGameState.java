package ch.nellen.silvan.games.polygon.game;

public interface IGameState {

	enum Phase {
		START, RUNNING, PAUSED, GAMEOVER
	}

	public class PhaseChange {
		public Phase oldPhase;
		public Phase newPhase;
	}

	public abstract void setPlayerAngularDir(int mAngularDir);

	public abstract int getPlayerAngluarDir();

	public abstract void setTimeElapsed(long time);

	public abstract long getTimeElapsed();

	public abstract void setCameraZ(float time);

	public abstract float getCameraZ();

	public abstract Phase getCurrentPhase();

	public abstract void setCurrentPhase(Phase newPhase);

}