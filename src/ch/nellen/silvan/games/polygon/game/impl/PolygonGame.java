package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.graphics.IRenderEventHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;

// To keep all game specific code in one place...
public class PolygonGame implements Observer, IRenderEventHandler {
	private Handler mHandler = null;

	private GameState mGameState = null;
	private GameLogic mGameLogic = null;
	private PlayerController mGameController = null;
	private HeadsUpDisplay mHud = null;

	private Context mContext;

	private static final String PREFERENCES = PolygonGame.class.getName();
	private static final String PREFERENCES_BEST = "POLYGONBESTTIME";

	public PolygonGame() {
		super();
	}

	// Called when renderer is initialized
	// Initialize load and init Models, Textures, Gamestate...
	@Override
	public void init(IRenderer renderer, Context context) {

		mContext = context;
		SharedPreferences prefs = context
				.getSharedPreferences(PREFERENCES, 0/* MODE_PRIVATE */);

		mGameState = new GameState(new Scene(renderer));
		mGameState.updateHighscore(prefs.getLong(PREFERENCES_BEST, 0));
		mGameState.addObserver(this);
		mHud = new HeadsUpDisplay(renderer, context, mGameState);
		mGameController = new PlayerController(renderer, mGameState);
		mGameLogic = new GameLogic(mGameState);

		mHandler = new Handler();
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
		mGameLogic.update(timeElapsed);
		mHud.update(timeElapsed);
		r.setCameraZ(mGameState.getCameraZ());
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
		mGameController.onSurfaceChanged(width, height);
		mGameLogic.onSurfaceChanged(r, width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.nellen.silvan.games.polygon.graphics.impl.IRenderEventHandler#onPause
	 * ()
	 */
	@Override
	public void onPause() {
		if (mGameState.getCurrentPhase() == GameState.Phase.RUNNING)
			mGameState.setCurrentPhase(IGameState.Phase.PAUSED);
	}
	
	
	public void handleTouchEvent(float screenWidth, float screenHeight,
			MotionEvent event) {
		if (!mHud.handleMotionEvent(screenWidth, screenHeight, event))
			mGameController.handleMotionEvent(screenWidth, screenHeight, event);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 == null) {
			GameState gs = (GameState) arg0;
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
		}
	}

}