package ch.nellen.silvan.games.polygon.game;

import android.view.MotionEvent;



public interface IGameState {
	// Update game state based on the time elapsed since the last update
	public void update(long timeElapsed);

	public void onTouchEvent(float screenWidth, float screenHeight, final MotionEvent event);
}
