package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import android.view.MotionEvent;
import ch.nellen.silvan.games.R;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.InputHandler;
import ch.nellen.silvan.games.polygon.graphics.impl.ImageSprite;

public class PlayerController extends InputHandler implements Observer {

	private ImageSprite leftKey;
	private ImageSprite rightKey;

	public PlayerController() {
		super();
		
		GameState.instance().addObserver(this);

		leftKey = new ImageSprite(R.drawable.key_left);
		leftKey.setX(0);
		leftKey.setY(0);
		leftKey.isVisible(false);

		rightKey = new ImageSprite(R.drawable.key_right);
		rightKey.setX(0);
		rightKey.setY(0);
		rightKey.isVisible(false);
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {

		if (acceptInput()
				&& GameState.instance().getCurrentPhase() == IGameState.Phase.RUNNING) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				GameState.instance().setPlayerAngularDir(0);
			} else {
				float glX = event.getX() - screenWidth / 2;
				// float glY = -(event.getY()-screenHeight/2);
				GameState.instance().setPlayerAngularDir(glX < 0 ? 1 : -1);
			}
		} else {
			GameState.instance().setPlayerAngularDir(0);
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
		GameState.instance().setPlayerAngularDir(0);
	}

	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		int distTop = HeadsUpDisplay.MAX_WIDTH_FROM_TOP, distBorder = (int) (screenWidth * 0.02);
		int keyWidth = screenWidth / 6;
		int keyHeight = screenHeight - distTop;

		leftKey.setX(distBorder);
		leftKey.setY(distTop);
		leftKey.setHeight(keyHeight);
		leftKey.setWidth(keyWidth);

		rightKey.setX(screenWidth - distBorder - keyWidth);
		rightKey.setY(distTop);
		rightKey.setWidth(keyWidth);
		rightKey.setHeight(keyHeight);
	}
}
