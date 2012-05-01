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

import ch.nellen.silvan.games.polygon.game.IGameModel;

/*
 * Holds the important information about the game,
 * notifies listeners when the game phase changes or a new highscore is reached
 * */
public class GameModel extends Observable implements IGameModel {

	private float mPlayerSpeed = 0;
	private long mTimeElapsed;
	private long mHighscore = 0;
	private float mCameraZ;
	private Phase mCurrentPhase = Phase.START;
	private Scene mScene;

	public GameModel(Scene s) {
		super();
		mScene = s;
	}

	public Scene getScene() {
		return mScene;
	}

	@Override
	public Phase getCurrentPhase() {
		return mCurrentPhase;
	}

	@Override
	public void setCurrentPhase(Phase newPhase) {
		if (mCurrentPhase != newPhase) {
			PhaseChange stateChange = new PhaseChange();
			stateChange.oldPhase = mCurrentPhase;
			stateChange.newPhase = newPhase;

			mCurrentPhase = newPhase;

			setChanged();
			notifyObservers(stateChange);
		}
	}

	public void setPlayerSpeed(float speed) {
		this.mPlayerSpeed = speed;
	}

	@Override
	public float getPlayerSpeed() {
		return mPlayerSpeed;
	}

	@Override
	public void setTotalTime(long time) {
		mTimeElapsed = time;
	}

	@Override
	public long getTotalTime() {
		return mTimeElapsed;
	}

	@Override
	public void setCameraZ(float z) {
		mCameraZ = z;
	}

	@Override
	public float getCameraZ() {
		return mCameraZ;
	}

	public long getCurrentHighscore() {
		return mHighscore;
	}

	public void updateHighscore(long highscore) {
		if (mHighscore < highscore) {
			mHighscore = highscore;
			setChanged();
			notifyObservers(null);
		}
	}

}
