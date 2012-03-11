package ch.nellen.silvan.games.polygon.game;




public interface IGameState {
	// Update game state based on the time elapsed since the last update
	public void update(long timeElapsed);
}
