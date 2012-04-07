package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import android.content.res.Resources;
import android.view.MotionEvent;
import ch.nellen.silvan.games.R;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.impl.ImageSprite;

public class GameController implements IInputHandler, Observer {
	private GameState mGameState;

	private ImageSprite leftKey;
	private ImageSprite rightKey;

	public GameController(Resources resources, IRenderContext rc, GameState gs) {
		super();
		mGameState = gs;
		mGameState.addObserver(this);

		leftKey = new ImageSprite(resources, R.drawable.key_left);
		leftKey.setX(0);
		leftKey.setY(0);
		leftKey.isVisible(false);
		rc.getRenderer().registerRenderable2D(leftKey);

		rightKey = new ImageSprite(resources, R.drawable.key_right);
		rightKey.setX(0);
		rightKey.setY(0);
		rightKey.isVisible(false);
		rc.getRenderer().registerRenderable2D(rightKey);
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {

		if (mGameState.getAcceptInput()) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				mGameState.setPlayerAngularDir(0);
			} else {
				float glX = event.getX() - screenWidth / 2;
				// float glY = -(event.getY()-screenHeight/2);
				mGameState.setPlayerAngularDir(glX < 0 ? 1 : -1);
			}
		}

		return true;
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data == null)
			return;

		GameState.PhaseChange phaseUpdate = (GameState.PhaseChange) data;
		switch (phaseUpdate.newPhase) {
		case RUNNING:
			rightKey.isVisible(true);
			leftKey.isVisible(true);
			break;
		case START:
		case PAUSED:
		case GAMEOVER:
			rightKey.isVisible(false);
			leftKey.isVisible(false);
		}
	}
	
	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		int distTop = 90, distBorder = 5;

		leftKey.setX(distBorder);
		leftKey.setY(distTop);
		leftKey.setHeight(screenHeight - 2 * distTop);
		leftKey.setWidth(screenWidth / 6);

		rightKey.setX(screenWidth - distBorder - screenWidth / 6);
		rightKey.setY(distTop);
		rightKey.setWidth(screenWidth / 6);
		rightKey.setHeight(screenHeight - 2 * distTop);
	}
}
