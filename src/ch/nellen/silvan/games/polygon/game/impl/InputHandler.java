package ch.nellen.silvan.games.polygon.game.impl;

import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IInputHandler;

public class InputHandler implements IInputHandler {

	private int mAngleDir = 0;

	@Override
	public void handleMotionEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {
			mAngleDir = 0;
		} else {
			float glX = event.getX() - screenWidth / 2;
			// float glY = -(event.getY()-screenHeight/2);
			mAngleDir = glX < 0 ? 1 : -1;
		}
	}

	@Override
	public int currentPlayerAngluarDir() {
		return mAngleDir;
	}

}
