package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import ch.nellen.silvan.games.R;
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

	private static final int DIST_FROM_BORDER = 20;
	private static final int DIST_FROM_TOP = 10;

	public HeadsUpDisplay(Context context, IRenderContext rc,
			GameState gameState) {
		super();

		mGameState = gameState;
		mGameState.addObserver(this);

		int background = Color.argb(128, (int) (0.3f * 255),
				(int) (0.3f * 255), (int) (0.3f * 255));
		int textColor = Color.argb(255, (int) (0.93671875f * 255),
				(int) (0.16953125f * 255), (int) (0.02265625f * 255));
		pauseButton = new TextSprite();
		pauseButton.setBackgroundColor(background);
		pauseButton.setTextColor(textColor);
		pauseButton.setX(DIST_FROM_BORDER);
		pauseButton.setY(DIST_FROM_TOP);
		pauseButton.setText("PAUSE");
		pauseButton.setPaddingHorizontal(15);
		pauseButton.setPaddingVertical(10);
		pauseButton.isVisible(false);
		rc.getRenderer().registerRenderable2D(pauseButton);

		totalTime = new TextSprite();
		totalTime.setBackgroundColor(background);
		totalTime.setTextColor(textColor);
		totalTime.setText("HIGHSCORE " + formatTime(mGameState.getHighscore()));
		totalTime.setX(mScreenWidth - totalTime.getWidth());
		totalTime.setPaddingHorizontal(5);
		rc.getRenderer().registerRenderable2D(totalTime);

		pausedText = new TextSprite();
		pausedText.setBackgroundColor(Color.TRANSPARENT);
		pausedText.setTextColor(textColor);
		pausedText.setText("TAP TO START");
		rc.getRenderer().registerRenderable2D(pausedText);

		logo = new ImageSprite(context.getResources(), R.drawable.logo);
		logo.setX(0);
		logo.setY(0);
		rc.getRenderer().registerRenderable2D(logo);

		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"It_wasn_t_me.ttf");
		if (tf != null) {
			pauseButton.setTypeface(tf);
			pausedText.setTypeface(tf);
			totalTime.setTypeface(tf);
		}

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
		setTimeText(timeString);
	}

	private void setTimeText(String txt) {
		totalTime.setText(txt);
		totalTime.setX(mScreenWidth - totalTime.getWidth() - DIST_FROM_BORDER);
	}

	String formatTime(long time) {
		return String.format("%02d", (time / 1000)) + ":"
				+ String.format("%02d", (time / 10) % 100);
	}

	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;

		logo.setWidth((int) (mScreenWidth));
		logo.setHeight((int) (mScreenHeight * 0.3));
		logo.setX((mScreenWidth - logo.getWidth()) / 2);
		logo.setY((mScreenHeight - logo.getHeight()) / 2);
		
		totalTime.setTextSize(52 * screenHeight / 600);
		totalTime.setX(mScreenWidth - totalTime.getWidth() - DIST_FROM_BORDER);
		totalTime.setY(10);

		pausedText.setTextSize(78 * screenHeight / 600);
		pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
		pausedText.setY(logo.getY() + logo.getHeight() + 5);

		pauseButton.setTextSize(52 * screenHeight / 600);
		
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
			setTimeText("HIGHSCORE " + formatTime(mGameState.getHighscore()));
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
