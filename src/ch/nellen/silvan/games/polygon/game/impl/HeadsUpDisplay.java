package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.R;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.impl.ImageSprite;
import ch.nellen.silvan.games.polygon.graphics.impl.TextSprite;

public class HeadsUpDisplay implements IInputHandler, IUpdatable {

	private TextSprite pauseButton;
	private TextSprite pausedText;
	private TextSprite totalTime;
	private ImageSprite logo;

	IGameState mGameState = null;
	private int mScreenWidth;
	private int mScreenHeight;

	public HeadsUpDisplay(Resources resources, IRenderContext rc,
			IGameState gameState) {
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
		pauseButton.setPaddingHorizontal(10);
		rc.getRenderer().registerRenderable2D(pauseButton);

		totalTime = new TextSprite();
		totalTime.setBackgroundColor(background);
		totalTime.setTextColor(background | 0xff000000);
		totalTime.setTextSize(32);
		totalTime.setText("TIME: 00:0");
		totalTime.setPaddingHorizontal(5);
		rc.getRenderer().registerRenderable2D(totalTime);

		pausedText = new TextSprite();
		pausedText.setBackgroundColor(Color.TRANSPARENT);
		pausedText.setTextColor(background | 0xff000000);
		pausedText.setTextSize(64);
		pausedText.setText("PAUSED");
		rc.getRenderer().registerRenderable2D(pausedText);

		logo = new ImageSprite(resources, R.drawable.logo);
		logo.scale(0.75f);
		logo.setX(0);
		logo.setY(0);
		rc.getRenderer().registerRenderable2D(logo);
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			MotionEvent event) {

		float evX = event.getX();
		float evY = event.getY();

		if (mGameState.getPausedChangeable()) {
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
		}

		return false;
	}

	@Override
	public void update(long timeElapsed) {

		String timeString;
		long time = mGameState.getTimeElapsed();
		timeString = "TIME: " + Long.toString((time / 1000)) + ":"
				+ Long.toString((time / 100) % 10);
		totalTime.setText(timeString);
		totalTime.setX(mScreenWidth - totalTime.getWidth());

		pausedText.isVisible(mGameState.getStarted() && mGameState.getPaused());
		pauseButton
				.isVisible(mGameState.getStarted()
						&& (mGameState.getPausedChangeable() && !mGameState
								.getPaused()));
		logo.isVisible(!mGameState.getStarted());

	}

	public void onScreenChanged(int screenWidth, int screenHeight) {
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;

		totalTime.setX(mScreenWidth - totalTime.getWidth());
		totalTime.setY(20);

		pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
		pausedText.setY((mScreenHeight - pausedText.getHeight()) / 2);

		logo.setX((mScreenWidth - logo.getWidth()) / 2);
		logo.setY((mScreenHeight - logo.getHeight()) / 2);
	}

}
