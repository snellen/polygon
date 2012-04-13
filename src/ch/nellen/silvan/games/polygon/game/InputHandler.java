package ch.nellen.silvan.games.polygon.game;

import android.view.MotionEvent;

public abstract class InputHandler {
	private static boolean cAcceptInput;

	public abstract boolean handleMotionEvent(float screenWidth,
			float screenHeight, final MotionEvent event);
	
	public static boolean acceptInput() {
		return cAcceptInput;
	}
	
	public static void acceptInput(boolean flag) {
		cAcceptInput = flag;
	}
	
}
