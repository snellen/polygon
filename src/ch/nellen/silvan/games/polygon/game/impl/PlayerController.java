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

import android.view.MotionEvent;
import ch.nellen.silvan.games.R;
import ch.nellen.silvan.games.polygon.game.IGameModel;
import ch.nellen.silvan.games.polygon.game.InputHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import ch.nellen.silvan.games.polygon.graphics.impl.ImageSprite;

public class PlayerController extends InputHandler implements Observer {

	private ImageSprite leftKey;
	private ImageSprite rightKey;
	private GameModel mGameState;

	public PlayerController(IRenderer r, GameModel gameState) {
		super();
		mGameState = gameState;
		mGameState.addObserver(this);

		leftKey = new ImageSprite(r,R.drawable.key_left);
		leftKey.setX(0);
		leftKey.setY(0);
		leftKey.isVisible(false);

		rightKey = new ImageSprite(r,R.drawable.key_right);
		rightKey.setX(0);
		rightKey.setY(0);
		rightKey.isVisible(false);
	}

	@Override
	public boolean handleMotionEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {

		if (acceptInput()
				&& mGameState.getCurrentPhase() == IGameModel.Phase.RUNNING) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				mGameState.setPlayerAngularDir(0);
			} else {
				float glX = event.getX() - screenWidth / 2;
				// float glY = -(event.getY()-screenHeight/2);
				mGameState.setPlayerAngularDir(glX < 0 ? 1 : -1);
			}
		} else {
			mGameState.setPlayerAngularDir(0);
		}

		return true;
	}

	// Handle changes in GameState
	@Override
	public void update(Observable observable, Object data) {
		if (data == null)
			return;

		GameModel.PhaseChange phaseUpdate = (GameModel.PhaseChange) data;
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
		mGameState.setPlayerAngularDir(0);
	}

	public void onSurfaceChanged(int screenWidth, int screenHeight) {
		int distTop = HeadsUpDisplay.MAX_WIDTH_FROM_TOP, distBorder = (int) (screenWidth * 0.02);
		int keyWidth = screenWidth / 6;
		int keyHeight = screenHeight - distTop- distBorder;

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
