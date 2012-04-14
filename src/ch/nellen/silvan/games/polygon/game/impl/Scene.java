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

import ch.nellen.silvan.games.polygon.graphics.IRenderer;
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

	public Scene(IRenderer r) {
		super();

		mCenterPolygon = new PolygonFilled(r);
		mCenterPolygon.setZCoord(0.011f);
		mCenterPolygon.setColor(0, 0, 0, 1);
		mCenterPolygonBorder = new PolygonFilled(r);
		mCenterPolygonBorder.setZCoord(0.011f);

		mPlayerModel = new PlayerModel(r);
		mPlayerModel.setAngle(90);
		mPlayerModel.setZCoord(0.011f);

		mPolygonModels = new PolygonUnfilled[MAXMODELS];
		for (int i = 0; i < mPolygonModels.length; ++i) {
			mPolygonModels[i] = new PolygonUnfilled(r);
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
		mPlayerModel.setSize((float) (0.015*mMaxVisibleRadius));
		mCenterPolygonBorder.setRadius(CENTER_RADIUS + CENTER_WIDTH);
		mCenterPolygon.setRadius(CENTER_RADIUS);
	}
}
