package ch.nellen.silvan.games.polygon.game.impl;

import java.util.Observable;
import java.util.Observer;

import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.game.InputHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
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

	private static final float PLAYERSPEED = 0.55f;
	private static float MAX_ROTATION_SPEED = 0.20f;
	private static float MIN_ROTATION_SPEED = 0.10f;
	
	private float shrinkSpeed = 2.2f / 1000;
	private float rotationSpeed = MIN_ROTATION_SPEED;

	private float mMaxVisibleRadius;
	private float mMaxRadius = 0;
	private float mAngle = 0f;
	private RGBAColor mColor = new RGBAColor(((float) 238) / 255,
			((float) 244) / 255, 0, 1);

	private CollisionDetection collDec = new CollisionDetection();
	private PolygonAdversary polyAdv = new PolygonAdversary();
	private GameState mGameState;

	public GameLogic(GameState gameState) {
		super();
		
		mGameState = gameState;
		gameState.getScene().getPlayerModel().isVisible(false);

		gameState.setCurrentPhase(IGameState.Phase.START);
		gameState.setCameraZ(PAUSE_CAM_POSITION);
		InputHandler.acceptInput(true);
		gameState.addObserver((Observer) this);
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
			InputHandler.acceptInput(false);
			float dPosition = Math.signum(targetPos - camPosition) * CAM_SPEED
					* timeElapsed;
			if (Math.signum(targetPos - camPosition) != Math.signum(targetPos
					- camPosition - dPosition) // Overshoot
					|| Math.abs(targetPos - camPosition - dPosition) < 0.001) { // Close
																				// enough
				camPosition = targetPos;
				InputHandler.acceptInput(true);
			} else {
				camPosition += dPosition;
			}
			mGameState.setCameraZ(camPosition);
			return true;
		}
		return false;
	}

	private int targetColorIndex = 0;
	private RGBAColor colorV = new RGBAColor(COLORS[targetColorIndex].r - mColor.r,
			COLORS[targetColorIndex].g - mColor.g, COLORS[targetColorIndex].b - mColor.b,
			COLORS[targetColorIndex].alpha - mColor.alpha);

	private void updateColor(long timeElapsed) {
		float dT = ((float) timeElapsed) / 1000;
		float dR = colorV.r * dT;
		float dG = colorV.g * dT;
		float dB = colorV.b * dT;

		if (Math.signum(COLORS[targetColorIndex].r - mColor.r - dR) != Math
				.signum(COLORS[targetColorIndex].r - mColor.r)
				|| Math.signum(COLORS[targetColorIndex].g - mColor.g - dG) != Math
						.signum(COLORS[targetColorIndex].g - mColor.g)
				|| Math.signum(COLORS[targetColorIndex].b - mColor.b - dB) != Math
						.signum(COLORS[targetColorIndex].b - mColor.b)) {
			mColor.r = COLORS[targetColorIndex].r;
			mColor.g = COLORS[targetColorIndex].g;
			mColor.b = COLORS[targetColorIndex].b;

			targetColorIndex = (targetColorIndex + 1) % COLORS.length;
			colorV.r = COLORS[targetColorIndex].r - mColor.r;
			colorV.g = COLORS[targetColorIndex].g - mColor.g;
			colorV.b = COLORS[targetColorIndex].b - mColor.b;
		} else {
			mColor.r += colorV.r * dT;
			mColor.g += colorV.g * dT;
			mColor.b += colorV.b * dT;
		}
	}

	public void update(long timeElapsed) {

		Scene theScene = mGameState.getScene();
		
		boolean cameraMoving = moveCamera(timeElapsed);
		long totalTime = mGameState.getTotalTime();
		long changeDirInterval = totalTime / CHANGEDIR_INTERVAL;

		if (!cameraMoving
				&& mGameState.getCurrentPhase() == IGameState.Phase.RUNNING) {
			totalTime += timeElapsed;
			mGameState.setTotalTime(totalTime);
			updateColor(timeElapsed);
			theScene.getCenterPolygonBorder().setColor(mColor);
		}

		PlayerModel playerModel = theScene.getPlayerModel();
		float playerAngle = playerModel.getAngle();
		if (mGameState.getCurrentPhase() == IGameState.Phase.RUNNING
				|| mGameState.getCurrentPhase() == IGameState.Phase.START) {
			updateAngle(timeElapsed);
			// Rotate center
			theScene.getCenterPolygonBorder().setAngle(mAngle);
			theScene.getCenterPolygon().setAngle(mAngle);
			// Rotate player
			playerAngle += rotationSpeed * timeElapsed;
			playerModel.setAngle(playerAngle);
		}

		if (mGameState.getCurrentPhase() == IGameState.Phase.RUNNING) {

			if (changeDirInterval != totalTime / CHANGEDIR_INTERVAL) {
				rotationSpeed = (float) ((Math.random() * (MAX_ROTATION_SPEED-MIN_ROTATION_SPEED) + MIN_ROTATION_SPEED) * Math
						.signum(Math.random() - 0.5));
			}

			if (!cameraMoving)
				updateMaxRadius(timeElapsed);

			PolygonUnfilled[] mPolygonModels = theScene.getPolygonModels();
			for (PolygonUnfilled p : mPolygonModels) {
				if (!cameraMoving) {
					float r = p.getRadius() - shrinkSpeed * timeElapsed;
					if (r + p.getWidth() < theScene.getCenterRadius()) {
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
			if (collDec.isPlayerCollided(theScene)) {
				mGameState.updateHighscore(totalTime);
				mGameState.setCurrentPhase(IGameState.Phase.GAMEOVER);
			}
		}
	}

	private void updateMaxRadius(long timeElapsed) {
		mMaxRadius = Math.max(mMaxVisibleRadius, mMaxRadius - shrinkSpeed
				* timeElapsed);
	}

	public void onSurfaceChanged(IRenderer r, int mScreenWidth, int mScreenHeight) {

		mMaxVisibleRadius = (float) (r.getScreenRadiusZNearRatio() * CAM_POSITION / Math
				.cos(Math.PI / PolygonModel.NUMBER_OF_VERTICES));

		mGameState.getScene().onMaxVisibleRadiusChanged(mMaxVisibleRadius);
	}

	// Handle changes in GameState
	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg1 == null)
			return;

		Scene theScene = mGameState.getScene();
		
		GameState.PhaseChange phaseUpdate = (GameState.PhaseChange) arg1;
		if (phaseUpdate.newPhase == GameState.Phase.RUNNING) {
			theScene.getPlayerModel().isVisible(true);
			if (phaseUpdate.oldPhase == GameState.Phase.START) {
				// New game started, init polygons
				polyAdv.reset();
				mMaxRadius = mMaxVisibleRadius;
				PolygonUnfilled[] mPolygonModels = theScene.getPolygonModels();
				for (PolygonUnfilled p : mPolygonModels) {
					p.isVisible(true);
					polyAdv.configureNextPolygon(p, mMaxVisibleRadius,
							mMaxRadius);
					mMaxRadius = p.getWidth() + p.getRadius();
				}
			}
		} else if (phaseUpdate.newPhase == GameState.Phase.START) {
			theScene.getPlayerModel().isVisible(false);
			PolygonUnfilled[] mPolygonModels = theScene.getPolygonModels();
			for (PolygonUnfilled p : mPolygonModels) {
				p.isVisible(false);
			}
			if (phaseUpdate.oldPhase == GameState.Phase.GAMEOVER) {
				mGameState.setTotalTime(0);
			}
		}
	}

}
