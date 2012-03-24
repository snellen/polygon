package ch.nellen.silvan.games.polygon.game;

import android.view.MotionEvent;

public interface IInputHandler {
	public abstract boolean handleMotionEvent(float screenWidth,
			float screenHeight, final MotionEvent event);
}
