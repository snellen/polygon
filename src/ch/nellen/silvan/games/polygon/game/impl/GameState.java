package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.game.ICollisionDetection;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
import ch.nellen.silvan.games.polygon.graphics.IScene;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;
import ch.nellen.silvan.games.polygon.graphics.impl.RGBAColor;

public class GameState implements IGameState {

	private float mAngle = 0f;
	private float rotationSpeed = 0.04f;
	private float playerSpeed = 0.15f;
	private float shrinkSpeed = 0.8f / 1000;
	private long totalTime = 0;

	private ICollisionDetection collDec = null;
	private IScene mScene = null;
	private IInputHandler mInputHandler = null;

	public GameState(IScene scene, IInputHandler inputHandler) {
		super();

		collDec = new CollisionDetection();
		mInputHandler = inputHandler;
		mScene = scene;
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

	private boolean[] edgeEnabled = new boolean[IPolygonModel.NUMBER_OF_VERTICES];

	private void updateAngle(long timeElapsed) {
		mAngle += rotationSpeed * timeElapsed;
		if (mAngle > 360)
			mAngle -= 360;
	}

	public void update(long timeElapsed) {

		if (!this.mInputHandler.pauseState()) {
			totalTime += timeElapsed;

			updateAngle(timeElapsed);

			PolygonUnfilled[] mPolygonModels = mScene.getPolygonModels();
			// Update polygons
			for (int i = 0; i < mPolygonModels.length; ++i) {
				float r = mPolygonModels[i].getRadius() - shrinkSpeed
						* timeElapsed;
				if (r < 0.1) {
					r = 6f;
					for (int j = 0; j < edgeEnabled.length; ++j)
						edgeEnabled[j] = (Math.random() < 0.3);
					mPolygonModels[i].setEdgesActive(edgeEnabled);
				}
				mPolygonModels[i].setRadius(r);
				mPolygonModels[i].setAngle(mAngle);
			}

			// Update center
			mScene.getCenterPolygonBorder().setAngle(mAngle);
			mScene.getCenterPolygon().setAngle(mAngle);

			// Update player
			PlayerModel playerModel = mScene.getPlayerModel();
			float playerAngle = playerModel.getAngle();
			playerAngle += mInputHandler.currentPlayerAngluarDir()
					* playerSpeed * timeElapsed;
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
}
