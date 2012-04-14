/*  This file is part of Polygon, an action game for Android phones. 
 
    Copyright (C) 2012  Silvan Nellen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import ch.nellen.silvan.games.R;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.game.InputHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import ch.nellen.silvan.games.polygon.graphics.ISprite;
import ch.nellen.silvan.games.polygon.graphics.impl.ImageSprite;
import ch.nellen.silvan.games.polygon.graphics.impl.TextSprite;

public class HeadsUpDisplay extends InputHandler implements IUpdatable, Observer {

	private TextSprite pauseButton;
	private TextSprite pausedText;
	private TextSprite totalTime;
	private TextSprite fpsDisplay;
	private ImageSprite mCredits;
	private ImageSprite logo;

	private int mScreenWidth;
	private int mScreenHeight;

	private GameState mGameState;

	private static int DIST_FROM_SIDE = 20;
	private static int DIST_FROM_TOP = 10;
	public static int MAX_WIDTH_FROM_TOP = 90;

	public HeadsUpDisplay(IRenderer r, Context context, GameState gameState) {
		super();

		mGameState = gameState;
		mGameState.addObserver(this);

		int background = Color.argb(128, (int) (0.3f * 255),
				(int) (0.3f * 255), (int) (0.3f * 255));
		int textColor = Color.argb(255, (int) (0.93671875f * 255),
				(int) (0.16953125f * 255), (int) (0.02265625f * 255));
		pauseButton = new TextSprite(r);
		pauseButton.setBackgroundColor(background);
		pauseButton.setTextColor(textColor);
		pauseButton.setText("PAUSE");
		pauseButton.setPaddingHorizontal(15);
		pauseButton.setPaddingVertical(10);
		pauseButton.isVisible(false);

		totalTime = new TextSprite(r);
		totalTime.setBackgroundColor(background);
		totalTime.setTextColor(textColor);
		totalTime.setText("HIGHSCORE " + formatTime(mGameState.getCurrentHighscore()));
		totalTime.setPaddingHorizontal(5);
		totalTime.setPaddingVertical(5);

		fpsDisplay = new TextSprite(r);
		fpsDisplay.setBackgroundColor(Color.TRANSPARENT);
		fpsDisplay.setTextColor(textColor);
		fpsDisplay.setPaddingHorizontal(5);
		fpsDisplay.setPaddingVertical(10);
		fpsDisplay.setText("FPS  0");
		fpsDisplay.setTextSize(14);
		fpsDisplay.isVisible(false);

		pausedText = new TextSprite(r);
		pausedText.setBackgroundColor(Color.TRANSPARENT);
		pausedText.setTextColor(textColor);
		pausedText.setText("TAP TO START");

		logo = new ImageSprite(r, R.drawable.logo);
		
		mCredits = new ImageSprite(r, R.drawable.credits);

		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"It_wasn_t_me.ttf");
		if (tf != null) {
			pauseButton.setTypeface(tf);
			pausedText.setTypeface(tf);
			totalTime.setTypeface(tf);
		}

	}

	private boolean touchOn(float touchX, float touchY, ISprite s) {
		return (touchX > s.getX() && touchX < s.getX() + s.getWidth()
				&& touchY > s.getY() && touchY < s.getY() + s.getHeight());
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			MotionEvent event) {

		float evX = event.getX();
		float evY = event.getY();

		boolean inputValid = (event.getAction() == MotionEvent.ACTION_UP && acceptInput());

		if (pauseButton.isVisible() && touchOn(evX, evY, pauseButton)) {
			if (inputValid)
				mGameState.setCurrentPhase(IGameState.Phase.PAUSED);
			return true; // Event handled
		}

		if (touchOn(evX, evY, fpsDisplay)) {
			if (inputValid) {
				lastFpsMeasure = 0;
				frames = 0;
				fpsDisplay.isVisible(!fpsDisplay.isVisible());
			}
		}

		if (mGameState.getCurrentPhase() == IGameState.Phase.PAUSED
				|| mGameState.getCurrentPhase() == IGameState.Phase.START) {
			if (inputValid
					&& (mGameState.getCurrentPhase() == IGameState.Phase.PAUSED)
					|| startTimer <= 0)
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

	static long startTimer = 0;
	static long lastFpsMeasure = 0;// ms
	static int frames = 0;

	@Override
	public void update(long timeElapsed) {

		if (startTimer > 0)
			startTimer -= timeElapsed;

		if (mGameState.getCurrentPhase() == GameState.Phase.RUNNING) {
			updateTimeDisplay();
		}

		if (fpsDisplay.isVisible()) {
			lastFpsMeasure += timeElapsed;
			frames++;
			if (frames >= 25) {
				float fps = ((float) frames * 1000) / lastFpsMeasure;
				fpsDisplay.setText("FPS " + String.format("%02.2f", fps));
				fpsDisplay.setX(mScreenWidth - fpsDisplay.getWidth());
				lastFpsMeasure = 0;
				frames = 0;
			}
		}
	}

	private void updateTimeDisplay() {
		String timeString;
		long time = mGameState.getTotalTime();
		timeString = "TIME " + formatTime(time);
		setTimeText(timeString);
	}

	private void setTimeText(String txt) {
		totalTime.setText(txt);
		totalTime.setX(mScreenWidth - totalTime.getWidth() - DIST_FROM_SIDE);
	}

	String formatTime(long time) {
		return String.format("%02d", (time / 1000)) + ":"
				+ String.format("%02d", (time / 10) % 100);
	}

	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;

		MAX_WIDTH_FROM_TOP = mScreenHeight / 8;
		DIST_FROM_SIDE = mScreenWidth / 50;
		DIST_FROM_TOP = mScreenHeight / 100;

		logo.setWidth((int) (mScreenWidth));
		logo.setHeight((int) (mScreenHeight * 0.3));
		logo.setX((mScreenWidth - logo.getWidth()) / 2);
		logo.setY((mScreenHeight - logo.getHeight()) / 2);

		totalTime.setTextSize(52 * mScreenHeight / 600);
		totalTime.setX(mScreenWidth - totalTime.getWidth() - DIST_FROM_SIDE);
		totalTime.setY(Math.min(MAX_WIDTH_FROM_TOP - totalTime.getHeight(),
				DIST_FROM_TOP));

		pausedText.setTextSize(78 * mScreenHeight / 600);
		pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
		pausedText.setY(logo.getY() + logo.getHeight() + 5);

		int dim = (int) (mScreenWidth*((float)9)/10);
		mCredits.setWidth(dim);
		mCredits.setHeight(dim);
		mCredits.setX((mScreenWidth - mCredits.getWidth()) / 2);
		mCredits.setY((int) (mScreenHeight-((float)21)/256*dim));
		
		pauseButton.setTextSize(52 * mScreenHeight / 600);
		pauseButton.setX(DIST_FROM_SIDE);
		pauseButton.setY(Math.min(MAX_WIDTH_FROM_TOP - pauseButton.getHeight(),
				DIST_FROM_TOP));

		fpsDisplay.setX(mScreenWidth - fpsDisplay.getWidth());
		fpsDisplay.setY(mScreenHeight - fpsDisplay.getHeight());

	}

	// Handle changes in GameState
	@Override
	public void update(Observable observable, Object data) {

		if (data == null)
			return;

		GameState.PhaseChange phaseUpdate = (GameState.PhaseChange) data;
		switch (phaseUpdate.newPhase) {
		case START:
			startTimer = 250;
			logo.isVisible(true);
			pausedText.isVisible(true);
			mCredits.isVisible(true);
			pausedText.setText("TAP TO START");
			pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
			pauseButton.isVisible(false);
			setTimeText("HIGHSCORE " + formatTime(mGameState.getCurrentHighscore()));
			break;
		case RUNNING:
			logo.isVisible(false);
			mCredits.isVisible(false);
			pausedText.isVisible(false);
			pauseButton.isVisible(true);
			break;
		case PAUSED:
			logo.isVisible(true);
			mCredits.isVisible(false);
			pausedText.isVisible(true);
			pausedText.setText("PAUSED, TAP TO CONTINUE");
			pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
			pauseButton.isVisible(false);
			break;
		case GAMEOVER:
			logo.isVisible(true);
			mCredits.isVisible(false);
			pausedText.isVisible(true);
			pausedText.setText("GAME OVER, TAP TO RETRY");
			pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
			pauseButton.isVisible(false);
			updateTimeDisplay();
			break;
		}
	}

}
