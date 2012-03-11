package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
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

	// Static Scene Parameters
	private int MAXMODELS = 8;
	private float CENTER_RADIUS = 0.3f;
	private float CENTER_WIDTH = 0.05f;

	public Scene(IRenderContext rc) {
		super();

		mCenterPolygon = new PolygonFilled(rc);
		mCenterPolygon.setRadius(CENTER_RADIUS);
		mCenterPolygon.setZCoord(0.1f);
		mCenterPolygonBorder = new PolygonFilled(rc);
		mCenterPolygonBorder.setRadius(CENTER_RADIUS + CENTER_WIDTH);
		mCenterPolygonBorder.setZCoord(0.05f);
		mCenterPolygonBorder.setColor(new RGBAColor(0.93671875f, 0.76953125f,
				0.22265625f, 1.0f));

		mPlayerModel = new PlayerModel(rc);
		mPlayerModel.setRadius(CENTER_RADIUS + CENTER_WIDTH + 0.1f);
		mPlayerModel.setAngle(90);
		mPlayerModel.setZCoord(0.1f);

		mPolygonModels = new PolygonUnfilled[MAXMODELS];
		boolean[] edgeEnabled = new boolean[IPolygonModel.NUMBER_OF_VERTICES];
		for (int i = 0; i < mPolygonModels.length; ++i) {
			mPolygonModels[i] = new PolygonUnfilled(rc);
			mPolygonModels[i].setRadius(i * 4 / 8f);
			for (int j = 0; j < edgeEnabled.length; ++j)
				edgeEnabled[j] = (Math.random() < 0.5);
			mPolygonModels[i].setEdgesActive(edgeEnabled);
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
}
