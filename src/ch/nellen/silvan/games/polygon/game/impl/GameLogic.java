package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import ch.nellen.silvan.games.polygon.game.ICollisionDetection;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.graphics.IScene;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;
import ch.nellen.silvan.games.polygon.graphics.impl.RGBAColor;

public class GameLogic implements IUpdatable, Observer {

	private static final long CHANGEDIR_INTERVAL = 10000; // ms
	private static final RGBAColor[] COLORS = {
			new RGBAColor(((float) 238) / 255, ((float) 24) / 255, 0, 1),
			new RGBAColor(((float) 238) / 255, ((float) 244) / 255, 0, 1) };

	private static final float PAUSE_CAM_POSITION = 2.1f;
	private static final float CAM_POSITION = 5f;
	private static final float CAM_SPEED = 4f / 1000;

	private static final float PLAYERSPEED = 0.35f;

	private float mMaxVisibleRadius;
	private float mMaxRadius = 0;
	private float mAngle = 0f;
	private RGBAColor mColor = new RGBAColor(((float) 238) / 255,
			((float) 244) / 255, 0, 1);
	private float rotationSpeed = 0.08f;
	private float shrinkSpeed = 1.5f / 1000;

	private ICollisionDetection collDec = null;
	private IScene mScene = null;
	private GameState mGameState = null;
	private PolygonAdversary polyAdv = new PolygonAdversary();

	public GameLogic(IScene scene, GameState gameState) {
		super();

		collDec = new CollisionDetection();
		mGameState = gameState;
		mScene = scene;
		mScene.getPlayerModel().isVisible(false);

		mGameState.setCurrentPhase(IGameState.Phase.START);
		mGameState.setCameraZ(PAUSE_CAM_POSITION);
		mGameState.setAcceptInput(true);
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
		float targetPos = mGameState.getCurrentPhase() != IGameState.Phase.RUNNING ? PAUSE_CAM_POSITION
				: CAM_POSITION;
		if (Math.abs(camPosition - targetPos) > 0.0001) {
			// Move camera towards target position
			mGameState.setAcceptInput(false);
			float dPosition = Math.signum(targetPos - camPosition) * CAM_SPEED
					* timeElapsed;
			if (Math.signum(targetPos - camPosition) != Math.signum(targetPos
					- camPosition - dPosition) // Overshoot
					|| Math.abs(targetPos - camPosition - dPosition) < 0.001) { // Close
																				// enough
				camPosition = targetPos;
				mGameState.setAcceptInput(true);
			} else {
				camPosition += dPosition;
			}
			mGameState.setCameraZ(camPosition);
			return true;
		}
		return false;
	}

	private int targetIndex = 0;
	private RGBAColor colorV = new RGBAColor(COLORS[targetIndex].r - mColor.r,
			COLORS[targetIndex].g - mColor.g, COLORS[targetIndex].b - mColor.b,
			COLORS[targetIndex].alpha - mColor.alpha);

	private void updateColor(long timeElapsed) {
		float dT = ((float) timeElapsed) / 1000;
		float dR = colorV.r * dT;
		float dG = colorV.g * dT;
		float dB = colorV.b * dT;

		if (Math.signum(COLORS[targetIndex].r - mColor.r - dR) != Math
				.signum(COLORS[targetIndex].r - mColor.r)
				|| Math.signum(COLORS[targetIndex].g - mColor.g - dG) != Math
						.signum(COLORS[targetIndex].g - mColor.g)
				|| Math.signum(COLORS[targetIndex].b - mColor.b - dB) != Math
						.signum(COLORS[targetIndex].b - mColor.b)) {
			mColor.r = COLORS[targetIndex].r;
			mColor.g = COLORS[targetIndex].g;
			mColor.b = COLORS[targetIndex].b;

			targetIndex = (targetIndex + 1) % COLORS.length;
			colorV.r = COLORS[targetIndex].r - mColor.r;
			colorV.g = COLORS[targetIndex].g - mColor.g;
			colorV.b = COLORS[targetIndex].b - mColor.b;
		} else {
			mColor.r += colorV.r * dT;
			mColor.g += colorV.g * dT;
			mColor.b += colorV.b * dT;
		}
	}

	public void update(long timeElapsed) {

		boolean cameraMoving = moveCamera(timeElapsed);
		long totalTime = mGameState.getTimeElapsed();
		long interval = totalTime / CHANGEDIR_INTERVAL;

		if (!cameraMoving
				&& mGameState.getCurrentPhase() == IGameState.Phase.RUNNING) {
			totalTime += timeElapsed;
			mGameState.setTimeElapsed(totalTime);
			updateColor(timeElapsed);
			mScene.getCenterPolygonBorder().setColor(mColor);
		}

		PlayerModel playerModel = mScene.getPlayerModel();
		float playerAngle = playerModel.getAngle();
		if (mGameState.getCurrentPhase() == IGameState.Phase.RUNNING
				|| mGameState.getCurrentPhase() == IGameState.Phase.START) {
			updateAngle(timeElapsed);
			// Rotate center
			mScene.getCenterPolygonBorder().setAngle(mAngle);
			mScene.getCenterPolygon().setAngle(mAngle);
			// Rotate player
			playerAngle += rotationSpeed * timeElapsed;
			playerModel.setAngle(playerAngle);
		}

		if (mGameState.getCurrentPhase() == IGameState.Phase.RUNNING) {

			if (interval != totalTime / CHANGEDIR_INTERVAL) {
				rotationSpeed = (float) ((Math.random() * 0.08 + 0.04) * Math
						.signum(Math.random() - 0.5));
			}

			updateMaxRadius(timeElapsed);

			PolygonUnfilled[] mPolygonModels = mScene.getPolygonModels();
			for (int i = 0; i < mPolygonModels.length; ++i) {
				PolygonUnfilled p = mPolygonModels[i];
				if (!cameraMoving) {
					float r = p.getRadius() - shrinkSpeed * timeElapsed;
					if (r + p.getWidth() < mScene.getCenterRadius()) {
						polyAdv.configureNextPolygon(p, mMaxVisibleRadius,
								mMaxRadius);
						mMaxRadius = p.getWidth() + p.getRadius();
					} else {
						p.setRadius(r);
					}
					p.setColor(mColor);
				}
				p.setAngle(mAngle);
			}
			// Update player
			playerAngle += mGameState.getPlayerAngluarDir() * PLAYERSPEED
					* timeElapsed;
			playerModel.setAngle(playerAngle);

			// Collision
			if (collDec.isPlayerCollided(mScene)) {
				mGameState.setHighscore(totalTime);
				mGameState.setCurrentPhase(IGameState.Phase.GAMEOVER);
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

		if (arg1 == null)
			return;

		GameState.PhaseChange phaseUpdate = (GameState.PhaseChange) arg1;
		if (phaseUpdate.newPhase == GameState.Phase.RUNNING) {
			mScene.getPlayerModel().isVisible(true);
			if (phaseUpdate.oldPhase == GameState.Phase.START) {
				// New game started, init polygons
				mMaxRadius = mMaxVisibleRadius;
				PolygonUnfilled[] mPolygonModels = mScene.getPolygonModels();
				for (int i = 0; i < mPolygonModels.length; ++i) {
					PolygonUnfilled p = mPolygonModels[i];
					p.isVisible(true);
					polyAdv.configureNextPolygon(p, mMaxVisibleRadius,
							mMaxRadius);
					mMaxRadius = p.getWidth() + p.getRadius();
				}
			}
		} else if (phaseUpdate.newPhase == GameState.Phase.START) {
			mScene.getPlayerModel().isVisible(false);
			PolygonUnfilled[] mPolygonModels = mScene.getPolygonModels();
			for (int i = 0; i < mPolygonModels.length; ++i) {
				PolygonUnfilled p = mPolygonModels[i];
				p.isVisible(false);
			}
			if (phaseUpdate.oldPhase == GameState.Phase.GAMEOVER) {
				mGameState.setTimeElapsed(0);
			}
		}
	}

}
