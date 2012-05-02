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
package ch.nellen.silvan.games.polygon.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.impl.PolygonGame;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonRenderer;

public class PolygonView extends GLSurfaceView {

	private PolygonRenderer mRenderer;
	private PolygonGame mGame;

	public PolygonView(Context context) {
		super(context);

		mRenderer = new PolygonRenderer();
		mGame = new PolygonGame();
		mRenderer.setEventHandler(mGame);
		mRenderer.init(context);

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(mRenderer);

	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// Forward to rendering thread
		queueEvent(new Runnable() {
			public void run() {
				mGame.handleTouchEvent(getWidth(), getHeight(), event);
			}
		});
		return true;

	}

	@Override
	public void onPause() {
		super.onPause();
		queueEvent(new Runnable() {
			public void run() {
				mGame.onPause();
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		queueEvent(new Runnable() {
			public void run() {
				mGame.onResume();
			}
		});
	}
	
	public void onStop() {
		queueEvent(new Runnable() {
			public void run() {
				mGame.onStop();
			}
		});
	}
}
