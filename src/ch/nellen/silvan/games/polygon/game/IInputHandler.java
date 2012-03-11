package ch.nellen.silvan.games.polygon.game;

import android.view.MotionEvent;

public interface IInputHandler {
	public abstract int currentPlayerAngluarDir();
	public abstract void handleMotionEvent(float screenWidth,
			float screenHeight, final MotionEvent event);
}
