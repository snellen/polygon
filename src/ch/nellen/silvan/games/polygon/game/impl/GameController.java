package ch.nellen.silvan.games.polygon.game.impl;

import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;

public class GameController implements IInputHandler {
	private IGameState mGameState;
	
	public GameController(IGameState gs) {
		super();
		mGameState = gs;
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {
			mGameState.setPlayerAngularDir(0);
		} else {
			float glX = event.getX() - screenWidth / 2;
			// float glY = -(event.getY()-screenHeight/2);
			mGameState.setPlayerAngularDir( glX < 0 ? 1 : -1);
		}
		return true;
	}
}
