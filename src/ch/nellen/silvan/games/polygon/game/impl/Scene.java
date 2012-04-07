package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IScene;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonFilled;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;
import ch.nellen.silvan.games.polygon.graphics.impl.RGBAColor;

public class Scene implements IScene {

	private PolygonUnfilled[] mPolygonModels = null;
	private PolygonFilled mCenterPolygon = null;
	private PolygonFilled mCenterPolygonBorder = null;
	private PlayerModel mPlayerModel = null;

	private int MAXMODELS = 8;
	private float CENTER_RADIUS;
	private float CENTER_WIDTH;

	public Scene(IRenderContext rc) {
		super();

		mCenterPolygon = new PolygonFilled(rc);
		mCenterPolygon.setZCoord(0.1f);
		mCenterPolygonBorder = new PolygonFilled(rc);
		mCenterPolygonBorder.setZCoord(0.05f);
		mCenterPolygonBorder.setColor(new RGBAColor(0.93671875f, 0.76953125f,
				0.22265625f, 1.0f));

		mPlayerModel = new PlayerModel(rc);
		mPlayerModel.setAngle(90);
		mPlayerModel.setZCoord(0.1f);

		mPolygonModels = new PolygonUnfilled[MAXMODELS];
		for (int i = 0; i < mPolygonModels.length; ++i) {
			mPolygonModels[i] = new PolygonUnfilled(rc);
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

	@Override
	public void onMaxVisibleRadiusChanged(float mMaxVisibleRadius) {
		CENTER_RADIUS = mMaxVisibleRadius * 0.09f;
		CENTER_WIDTH = mMaxVisibleRadius * 0.01f;

		mPlayerModel
				.setRadius((float) (CENTER_RADIUS + CENTER_WIDTH + mMaxVisibleRadius * 0.01));
		mCenterPolygonBorder.setRadius(CENTER_RADIUS + CENTER_WIDTH);
		mCenterPolygon.setRadius(CENTER_RADIUS);
	}
}
