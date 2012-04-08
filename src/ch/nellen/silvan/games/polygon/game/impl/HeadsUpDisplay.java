package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

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

public class HeadsUpDisplay implements IInputHandler, IUpdatable, Observer {

	private TextSprite pauseButton;
	private TextSprite pausedText;
	private TextSprite totalTime;
	private ImageSprite logo;

	GameState mGameState = null;
	private int mScreenWidth;
	private int mScreenHeight;

	public HeadsUpDisplay(Resources resources, IRenderContext rc,
			GameState gameState) {
		super();

		mGameState = gameState;
		mGameState.addObserver(this);

		int background = Color.argb(128, (int) (0.93671875f * 255),
				(int) (0.76953125f * 255), (int) (0.22265625f * 255));
		pauseButton = new TextSprite();
		pauseButton.setBackgroundColor(background);
		pauseButton.setTextColor(background | 0xff000000);
		pauseButton.setX(20);
		pauseButton.setY(10);
		pauseButton.setTextSize(24);
		pauseButton.setText("PAUSE");
		pauseButton.setPaddingHorizontal(10);
		pauseButton.setPaddingVertical(20);
		pauseButton.isVisible(false);
		rc.getRenderer().registerRenderable2D(pauseButton);

		totalTime = new TextSprite();
		totalTime.setBackgroundColor(background);
		totalTime.setTextColor(background | 0xff000000);
		totalTime.setTextSize(24);
		totalTime.setText("HIGHSCORE " + formatTime(mGameState.getHighscore()));
		totalTime.setX(mScreenWidth - totalTime.getWidth());
		totalTime.setPaddingHorizontal(5);
		rc.getRenderer().registerRenderable2D(totalTime);

		pausedText = new TextSprite();
		pausedText.setBackgroundColor(Color.TRANSPARENT);
		pausedText.setTextColor(background | 0xff000000);
		pausedText.setTextSize(40);
		pausedText.setText("TAP TO START");
		rc.getRenderer().registerRenderable2D(pausedText);

		logo = new ImageSprite(resources, R.drawable.logo);
		logo.scale(1.3f);
		logo.setX(0);
		logo.setY(0);
		rc.getRenderer().registerRenderable2D(logo);
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			MotionEvent event) {

		float evX = event.getX();
		float evY = event.getY();

		boolean inputValid = (event.getAction() == MotionEvent.ACTION_UP && mGameState
				.getAcceptInput());

		if (pauseButton.isVisible() && evX > pauseButton.getX()
				&& evX < pauseButton.getX() + pauseButton.getWidth()
				&& evY > pauseButton.getY()
				&& evY < pauseButton.getY() + pauseButton.getHeight()) {
			if (inputValid)
				mGameState.setCurrentPhase(IGameState.Phase.PAUSED);
			return true; // Event handled
		}

		if (mGameState.getCurrentPhase() == IGameState.Phase.PAUSED
				|| mGameState.getCurrentPhase() == IGameState.Phase.START) {
			if (inputValid)
				mGameState.setCurrentPhase(IGameState.Phase.RUNNING);
			return true;
		}

		if (mGameState.getCurrentPhase() == IGameState.Phase.GAMEOVER) {
			if (inputValid)
				mGameState.setCurrentPhase(IGameState.Phase.START);
			return true;
		}

		return false;
	}

	@Override
	public void update(long timeElapsed) {
		if (mGameState.getCurrentPhase() == GameState.Phase.RUNNING) {
			updateTimeDisplay();
		}
	}

	private void updateTimeDisplay() {
		String timeString;
		long time = mGameState.getTimeElapsed();
		timeString = "TIME " + formatTime(time);
		totalTime.setText(timeString);
		totalTime.setX(mScreenWidth - totalTime.getWidth());
	}

	String formatTime(long time) {
		return String.format("%02d", (time / 1000)) + ":"
				+ String.format("%02d", (time / 10) % 100);
	}

	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;

		totalTime.setX(mScreenWidth - totalTime.getWidth());
		totalTime.setY(10);

		float scale = (float) (mScreenWidth * 0.8 / logo.getWidth());
		logo.scale(scale);
		logo.setX((mScreenWidth - logo.getWidth()) / 2);
		logo.setY((mScreenHeight - logo.getHeight()) / 2);

		pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
		pausedText.setY(logo.getY() + logo.getHeight() + 5);
	}

	@Override
	public void update(Observable observable, Object data) {

		if (data == null)
			return;

		GameState.PhaseChange phaseUpdate = (GameState.PhaseChange) data;
		switch (phaseUpdate.newPhase) {
		case START:
			logo.isVisible(true);
			pausedText.isVisible(true);
			pausedText.setText("TAP TO START");
			pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
			pauseButton.isVisible(false);
			totalTime.setText("HIGHSCORE "
					+ formatTime(mGameState.getHighscore()));
			totalTime.setX(mScreenWidth - totalTime.getWidth());
			break;
		case RUNNING:
			logo.isVisible(false);
			pausedText.isVisible(false);
			pauseButton.isVisible(true);
			break;
		case PAUSED:
			logo.isVisible(true);
			pausedText.isVisible(true);
			pausedText.setText("PAUSED, TAP TO CONTINUE");
			pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
			pauseButton.isVisible(false);
			break;
		case GAMEOVER:
			logo.isVisible(true);
			pausedText.isVisible(true);
			pausedText.setText("GAME OVER, TAP TO RETRY");
			pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
			pauseButton.isVisible(false);
			updateTimeDisplay();
			break;
		}
	}

}
