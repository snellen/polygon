package ch.nellen.silvan.games.polygon.game.impl;

import android.graphics.Color;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.impl.TextSprite;

public class HeadsUpDisplay implements IInputHandler {

	private TextSprite pauseButton;
	IGameState mGameState = null;

	public HeadsUpDisplay(IRenderContext rc, IGameState gameState) {
		super();

		mGameState = gameState;

		pauseButton = new TextSprite();
		pauseButton.setBackgroundColor(Color.argb(128,
				(int) (0.93671875f * 255), (int) (0.76953125f * 255),
				(int) (0.22265625f * 255)));
		pauseButton.setX(20);
		pauseButton.setY(20);
		pauseButton.setTextSize(32);
		pauseButton.setText("Pause");
		rc.getRenderer().registerRenderable2D(pauseButton);
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			MotionEvent event) {

		float evX = event.getX();
		float evY = event.getY();

		if (pauseButton.isVisible() && evX > pauseButton.getX()
				&& evX < pauseButton.getX() + pauseButton.getWidth()
				&& evY > pauseButton.getY()
				&& evY < pauseButton.getY() + pauseButton.getHeight()) {
			// Touch on pause button
			if (event.getAction() == MotionEvent.ACTION_UP) {
				// Pause button released
				mGameState.setPauseState(true);
				pauseButton.isVisible(false);
			}
			return true; // Event handled
		}

		if (mGameState.pauseState()) {
			pauseButton.isVisible(true);
			mGameState.setPauseState(false);
			return true;
		}

		return false;
	}

}
