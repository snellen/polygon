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
	private ImageSprite leftKey;
	private ImageSprite rightKey;

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
		pauseButton.setTextColor(background | 0xff000000);
		pauseButton.setX(20);
		pauseButton.setY(10);
		pauseButton.setTextSize(24);
		pauseButton.setText("PAUSE");
		pauseButton.setPaddingHorizontal(10);
		pauseButton.setPaddingVertical(20);
		rc.getRenderer().registerRenderable2D(pauseButton);

		totalTime = new TextSprite();
		totalTime.setBackgroundColor(background);
		totalTime.setTextColor(background | 0xff000000);
		totalTime.setTextSize(24);
		totalTime.setText("TIME: 00:0");
		totalTime.setPaddingHorizontal(5);
		rc.getRenderer().registerRenderable2D(totalTime);

		pausedText = new TextSprite();
		pausedText.setBackgroundColor(Color.TRANSPARENT);
		pausedText.setTextColor(background | 0xff000000);
		pausedText.setTextSize(40);
		pausedText.setText("PAUSED");
		rc.getRenderer().registerRenderable2D(pausedText);

		logo = new ImageSprite(resources, R.drawable.logo);
		logo.scale(1.3f);
		logo.setX(0);
		logo.setY(0);
		rc.getRenderer().registerRenderable2D(logo);
		
		leftKey = new ImageSprite(resources, R.drawable.key_left);
		leftKey.setX(0);
		leftKey.setY(0);
		rc.getRenderer().registerRenderable2D(leftKey);
		
		rightKey = new ImageSprite(resources, R.drawable.key_right);
		rightKey.setX(0);
		rightKey.setY(0);
		rc.getRenderer().registerRenderable2D(rightKey);
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
		timeString = "TIME " + String.format("%02d", (time / 1000)) + ":"
				+ String.format("%02d", (time / 10) % 100 );
		totalTime.setText(timeString);
		totalTime.setX(mScreenWidth - totalTime.getWidth());

		logo.isVisible(!mGameState.getStarted() || mGameState.getPaused());
		
		pausedText.setText(!mGameState.getStarted()?"TAP TO START":"PAUSED");
		pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
		
		pausedText.isVisible(!mGameState.getStarted() || mGameState.getPaused());
		
		pauseButton
				.isVisible(mGameState.getStarted()
						&& (mGameState.getPausedChangeable() && !mGameState
								.getPaused()));
		
		rightKey.isVisible(mGameState.getStarted()
						&& (mGameState.getPausedChangeable() && !mGameState
								.getPaused()));
		leftKey.isVisible(mGameState.getStarted()
				&& (mGameState.getPausedChangeable() && !mGameState
						.getPaused()));
	}

	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;

		totalTime.setX(mScreenWidth - totalTime.getWidth());
		totalTime.setY(10);

		float scale = (float) (mScreenWidth*0.8/logo.getWidth());
		logo.scale(scale);
		logo.setX((mScreenWidth - logo.getWidth()) / 2);
		logo.setY((mScreenHeight - logo.getHeight()) / 2);
		
		pausedText.setX((mScreenWidth - pausedText.getWidth()) / 2);
		pausedText.setY(logo.getY()+logo.getHeight()+5);
		
		int distTop = 90, distBorder = 5;
		
		leftKey.setX(distBorder);
		leftKey.setY(distTop);
		leftKey.setHeight(screenHeight-2*distTop);
		leftKey.setWidth(screenWidth/6);
		
		rightKey.setX(screenWidth-distBorder-screenWidth/6);
		rightKey.setY(distTop);
		rightKey.setWidth(screenWidth/6);
		rightKey.setHeight(screenHeight-2*distTop);
	}

}
