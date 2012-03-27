package ch.nellen.silvan.games.polygon.game.impl;

import android.graphics.Color;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.impl.TextSprite;

public class HeadsUpDisplay implements IInputHandler, IUpdatable {

	private TextSprite pauseButton;
	private TextSprite pausedText;
	private TextSprite totalTime;

	IGameState mGameState = null;

	public HeadsUpDisplay(IRenderContext rc, IGameState gameState) {
		super();

		mGameState = gameState;

		int background = Color.argb(128, (int) (0.93671875f * 255),
				(int) (0.76953125f * 255), (int) (0.22265625f * 255));
		pauseButton = new TextSprite();
		pauseButton.setBackgroundColor(background);
		pauseButton.setX(20);
		pauseButton.setY(20);
		pauseButton.setTextSize(32);
		pauseButton.setText("PAUSE");
		rc.getRenderer().registerRenderable2D(pauseButton);

		totalTime = new TextSprite();
		totalTime.setBackgroundColor(background);
		totalTime.setTextColor(background | 0xff000000);
		totalTime.setTextSize(32);
		totalTime.setText("00:00");
		rc.getRenderer().registerRenderable2D(totalTime);

		pausedText = new TextSprite();
		pausedText.setBackgroundColor(Color.TRANSPARENT);
		pausedText.setTextColor(background | 0xff000000);
		pausedText.setTextSize(64);
		pausedText.setText("PAUSED");
		rc.getRenderer().registerRenderable2D(pausedText);
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
				mGameState.setPaused(true);
			}
			return true; // Event handled
		}

		if (mGameState.getPaused()) {
			mGameState.setPaused(false);
			return true;
		}

		return false;
	}

	@Override
	public void update(long timeElapsed) {

		String timeString;
		long time = mGameState.getTimeElapsed();
		timeString = Long.toString((time / 1000)) + ":"
				+ Long.toString((time / 100) % 10);
		totalTime.setText(timeString);

		pausedText.isVisible(mGameState.getPaused());
		pauseButton.isVisible(!mGameState.getPaused());

	}

	public void onScreenChanged(int screenWidth, int screenHeight) {
		totalTime.setX(screenWidth - 100);
		totalTime.setY(20);

		pausedText.setX((screenWidth - pausedText.getWidth()) / 2);
		pausedText.setY((screenHeight - pausedText.getHeight()) / 2);
	}

}
