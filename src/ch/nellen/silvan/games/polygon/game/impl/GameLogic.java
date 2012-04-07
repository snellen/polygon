package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import ch.nellen.silvan.games.polygon.game.ICollisionDetection;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.game.impl.GameState.ATTRIBUTE;
import ch.nellen.silvan.games.polygon.graphics.IScene;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;
import ch.nellen.silvan.games.polygon.graphics.impl.RGBAColor;

public class GameLogic implements IUpdatable, Observer {

	private class PolygonConfigurator {
		// enum State {
		//
		// }

		void configureNextPolygon(PolygonUnfilled polygon) {
			boolean[] edgeEnabled = polygon.getEdgesEnabled();
			for (int j = 0; j < edgeEnabled.length; ++j){
				edgeEnabled[j] = (Math.random() < 0.3);
			}
			polygon.setRadius((float) (mMaxRadius+(Math.random() * 0.05+0.03)*mMaxVisibleRadius));
			polygon.setWidth((float) (Math.random() * 0.005 + 0.09)
					* mMaxVisibleRadius);
			mMaxRadius = polygon.getWidth() + polygon.getRadius();
		}
	}

	private static final float PAUSE_CAM_POSITION = 3.1f;
	private static final float CAM_POSITION = 5f;
	private static final float CAM_SPEED = 4f / 1000;
	private float mMaxVisibleRadius;
	private float mMaxRadius = 0;

	private float mAngle = 0f;
	private float rotationSpeed = 0.04f;
	private float playerSpeed = 0.15f;
	private float shrinkSpeed = 0.8f / 1000;
	private long totalTime = 0;

	private ICollisionDetection collDec = null;
	private IScene mScene = null;
	private GameState mGameState = null;
	private PolygonConfigurator polyConfig = new PolygonConfigurator();

	public GameLogic(IScene scene, GameState gameState) {
		super();

		collDec = new CollisionDetection();
		mGameState = gameState;
		mScene = scene;

		mGameState.setStarted(false);
		mGameState.setPaused(true);
		mGameState.setCameraZ(PAUSE_CAM_POSITION);
		mGameState.setPausedChangeable(true);
		mGameState.addObserver((Observer) this);

	}

	public float getShrinkSpeed() {
		return shrinkSpeed;
	}

	public void setShrinkSpeed(float shrinkSpeed) {
		this.shrinkSpeed = shrinkSpeed;
	}

	// Rotation speed degrees/millisecond
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	// Rotation speed degrees/millisecond
	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	private void updateAngle(long timeElapsed) {
		mAngle += rotationSpeed * timeElapsed;
		if (mAngle > 360)
			mAngle -= 360;
	}

	private boolean moveCamera(long timeElapsed) {
		float camPosition = mGameState.getCameraZ();
		float targetPos = mGameState.getPaused() ? PAUSE_CAM_POSITION
				: CAM_POSITION;
		if (Math.abs(camPosition - targetPos) > 0.0001) {
			// Move camera towards target position
			mGameState.setPausedChangeable(false);
			float dPosition = Math.signum(targetPos - camPosition) * CAM_SPEED
					* timeElapsed;
			if (Math.signum(targetPos - camPosition) != Math.signum(targetPos
					- camPosition - dPosition) // Overshoot
					|| Math.abs(targetPos - camPosition - dPosition) < 0.001) { // Close
																				// enough
				camPosition = targetPos;
				mGameState.setPausedChangeable(true);
			} else {
				camPosition += dPosition;
			}
			mGameState.setCameraZ(camPosition);
			return true;
		}
		return false;
	}

	public void update(long timeElapsed) {

		if (!moveCamera(timeElapsed) && !mGameState.getPaused()) {
			totalTime += timeElapsed;
			mGameState.setTimeElapsed(totalTime);
			if (!mGameState.getStarted())
				mGameState.setStarted(true);
		}

		updateAngle(timeElapsed);

		// Update center
		mScene.getCenterPolygonBorder().setAngle(mAngle);
		mScene.getCenterPolygon().setAngle(mAngle);

		if (mGameState.getStarted() && !mGameState.getPaused()) {
			updateMaxRadius(timeElapsed);

			// Update polygons
			PolygonUnfilled[] mPolygonModels = mScene.getPolygonModels();
			for (int i = 0; i < mPolygonModels.length; ++i) {
				PolygonUnfilled p = mPolygonModels[i];
				float r = p.getRadius() - shrinkSpeed * timeElapsed;
				if (r + p.getWidth() < mScene.getCenterRadius()) {
					polyConfig.configureNextPolygon(p);
				} else {
					p.setRadius(r);
				}
				p.setAngle(mAngle);
			}
			// Update player
			PlayerModel playerModel = mScene.getPlayerModel();
			playerModel.isVisible(true);
			float playerAngle = playerModel.getAngle();
			playerAngle += mGameState.getPlayerAngluarDir() * playerSpeed
					* timeElapsed;
			playerAngle += rotationSpeed * timeElapsed;
			playerModel.setAngle(playerAngle);

			// Collision
			if (collDec.isPlayerCollided(mScene)) {
				playerModel.setColor(new RGBAColor(0f, 0f, 1f, 1.0f));
			} else {
				playerModel.setColor(new RGBAColor(1f, 0f, 0f, 1.0f));
			}
		}
	}

	private void updateMaxRadius(long timeElapsed) {
		mMaxRadius = Math.max(mMaxVisibleRadius, mMaxRadius - shrinkSpeed
				* timeElapsed);
	}

	public void onSurfaceChanged(int mScreenWidth, int mScreenHeight,
			float screenRadiusZNearRatio) {

		mMaxVisibleRadius = (float) (screenRadiusZNearRatio * CAM_POSITION / Math
				.cos(Math.PI / PolygonModel.NUMBER_OF_VERTICES));

		mScene.onMaxVisibleRadiusChanged(mMaxVisibleRadius);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		GameState gs = (GameState) arg0;
		GameState.ATTRIBUTE attr = (ATTRIBUTE) arg1;
		if (attr == GameState.ATTRIBUTE.STARTED && gs.getStarted()) {
			mMaxRadius = mMaxVisibleRadius;
			PolygonUnfilled[] mPolygonModels = mScene.getPolygonModels();
			for (int i = 0; i < mPolygonModels.length; ++i) {
				PolygonUnfilled p = mPolygonModels[i];
				p.isVisible(true);
				polyConfig.configureNextPolygon(p);
			}
		}

	}

}
