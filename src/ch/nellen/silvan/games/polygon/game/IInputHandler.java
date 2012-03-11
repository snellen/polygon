package ch.nellen.silvan.games.polygon.game;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface IInputHandler {
	public abstract int currentPlayerAngluarDir();
	public abstract boolean pauseState();
	public abstract void handleMotionEvent(float screenWidth,
			float screenHeight, final MotionEvent event);

	public abstract void handleKeyUpEvent(int keyCode, KeyEvent event);
}
