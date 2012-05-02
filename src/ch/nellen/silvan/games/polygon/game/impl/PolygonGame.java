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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameModel;
import ch.nellen.silvan.games.polygon.graphics.IRenderEventHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import ch.nellen.silvan.games.polygon.sound.SoundManager;

public class PolygonGame implements Observer, IRenderEventHandler {
	private Handler mHandler = null;

	private GameModel mGameModel = null;
	private GameController mGameController = null;
	private PlayerInputHandler mPlayerInputHandler = null;
	private HeadsUpDisplay mHud = null;

	private SoundManager mSoundManager = null;

	private Context mContext;

	private static final String PREFERENCES = PolygonGame.class.getName();
	private static final String PREFERENCES_BEST = "POLYGONBESTTIME";

	public PolygonGame() {
		super();
	}

	// Called when renderer is initialized
	// Load and initialize Models, Textures, Game Model...
	@Override
	public void init(IRenderer renderer, Context context) {

		mContext = context;
		SharedPreferences prefs = context
				.getSharedPreferences(PREFERENCES, 0/* MODE_PRIVATE */);

		mGameModel = new GameModel(new Scene(renderer));
		mGameModel.updateHighscore(prefs.getLong(PREFERENCES_BEST, 0));
		mGameModel.addObserver(this);
		mHud = new HeadsUpDisplay(renderer, context, mGameModel);
		mPlayerInputHandler = new PlayerInputHandler(renderer, mGameModel);
		mGameController = new GameController(mGameModel);

		mHandler = new Handler();

		mSoundManager = new SoundManager(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.nellen.silvan.games.polygon.graphics.impl.IRenderEventHandler#update
	 * (long)
	 */
	@Override
	public void onRender(IRenderer r, long timeElapsed) {
		mGameController.update(timeElapsed);
		mHud.update(timeElapsed);
		r.setCameraZ(mGameModel.getCameraZ());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.nellen.silvan.games.polygon.graphics.impl.IRenderEventHandler#
	 * onSurfaceChanged()
	 */
	@Override
	public void onSurfaceChanged(IRenderer r, int width, int height) {
		mHud.onSurfaceChanged(width, height);
		mPlayerInputHandler.onSurfaceChanged(width, height);
		mGameController.onSurfaceChanged(r, width, height);
	}

	public void onPause() {
		if (mGameModel.getCurrentPhase() == GameModel.Phase.RUNNING)
			mGameModel.setCurrentPhase(IGameModel.Phase.PAUSED);
		mSoundManager.pauseBackgroundMusic();
	}

	public void onResume() {
		if (mSoundManager.isBackgroundMusicPaused())
			mSoundManager.resumeBackgroundMusic();
	}

	public void onStop() {
		mSoundManager.stop();
	}

	public void handleTouchEvent(float screenWidth, float screenHeight,
			MotionEvent event) {
		if (!mHud.handleMotionEvent(screenWidth, screenHeight, event))
			mPlayerInputHandler.handleMotionEvent(screenWidth, screenHeight,
					event);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 == null) {
			GameModel gs = (GameModel) arg0;
			final long highscore = gs.getCurrentHighscore();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					SharedPreferences prefs = mContext.getSharedPreferences(
							PREFERENCES, 0/* MODE_PRIVATE */);
					Editor editor = prefs.edit();
					editor.putLong(PREFERENCES_BEST, highscore);
					editor.commit();
				}
			});
		} else {
			GameModel.PhaseChange phaseUpdate = (GameModel.PhaseChange) arg1;
			if (phaseUpdate.newPhase == GameModel.Phase.RUNNING
					&& phaseUpdate.oldPhase == GameModel.Phase.START) {
				mSoundManager.resumeBackgroundMusic();
			}
			if (phaseUpdate.newPhase == GameModel.Phase.GAMEOVER) {
				mSoundManager.playExplosionSound();
				mSoundManager.pauseBackgroundMusic();
			}
		}

	}
}