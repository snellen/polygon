package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonFilled;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;

public class Scene {

	private PolygonUnfilled[] mPolygonModels = null;
	private PolygonFilled mCenterPolygon = null;
	private PolygonFilled mCenterPolygonBorder = null;
	private PlayerModel mPlayerModel = null;

	private int MAXMODELS = 8;
	private float CENTER_RADIUS;
	private float CENTER_WIDTH;

	public Scene() {
		super();

		mCenterPolygon = new PolygonFilled();
		mCenterPolygon.setZCoord(0.011f);
		mCenterPolygon.setColor(0, 0, 0, 1);
		mCenterPolygonBorder = new PolygonFilled();
		mCenterPolygonBorder.setZCoord(0.011f);
		;

		mPlayerModel = new PlayerModel();
		mPlayerModel.setAngle(90);
		mPlayerModel.setZCoord(0.011f);

		mPolygonModels = new PolygonUnfilled[MAXMODELS];
		for (int i = 0; i < mPolygonModels.length; ++i) {
			mPolygonModels[i] = new PolygonUnfilled();
			mPolygonModels[i].setZCoord(0.0f);
			mPolygonModels[i].isVisible(false);
		}
	}

	public PolygonFilled getCenterPolygon() {
		return mCenterPolygon;
	}

	public PolygonFilled getCenterPolygonBorder() {
		return mCenterPolygonBorder;
	}

	public float getCenterRadius() {
		return CENTER_RADIUS + CENTER_WIDTH;
	}

	public PolygonUnfilled[] getPolygonModels() {
		return mPolygonModels;
	}

	public PlayerModel getPlayerModel() {
		return mPlayerModel;
	}

	public void onMaxVisibleRadiusChanged(float mMaxVisibleRadius) {
		CENTER_RADIUS = mMaxVisibleRadius * 0.09f;
		CENTER_WIDTH = mMaxVisibleRadius * 0.01f;

		mPlayerModel
				.setRadius((float) (CENTER_RADIUS + CENTER_WIDTH + mMaxVisibleRadius * 0.01));
		mPlayerModel.setSize((float) (0.01*mMaxVisibleRadius));
		mCenterPolygonBorder.setRadius(CENTER_RADIUS + CENTER_WIDTH);
		mCenterPolygon.setRadius(CENTER_RADIUS);
	}
}
