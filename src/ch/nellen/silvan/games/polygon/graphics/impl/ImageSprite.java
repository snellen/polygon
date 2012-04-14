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
package ch.nellen.silvan.games.polygon.graphics.impl;

import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageSprite extends Sprite {

	private int mHeight = 0;
	private int mWidth = 0;
	private Bitmap mBitmap = null;
	private int mResourceId = -1;

	public ImageSprite(IRenderer r, int id) {
		super(r);
		mResourceId = id;
	}

	// Overrides height and width
	public void scale(float factor) {
		setHeight((int) (mBitmap.getHeight() * factor));
		setWidth((int) (mBitmap.getWidth() * factor));
	}

	public void setHeight(int mHeight) {
		if (this.mHeight != mHeight) {
			this.mHeight = mHeight;
			mRefresh = true;
		}
	}

	public void setWidth(int mWidth) {
		if (this.mWidth != mWidth) {
			this.mWidth = mWidth;
			mRefresh = true;
		}
	}

	@Override
	public int getWidth() {
		return mWidth;
	}

	@Override
	public int getHeight() {
		return mHeight;
	}

	@Override
	Bitmap getTexture() {
		return mBitmap;
	}

	@Override
	protected int getWidthOnScreen() {
		return mWidth;
	}

	@Override
	protected int getHeightOnScreen() {
		return mHeight;
	}

	@Override
	public void init(Context context) {
		// Get the texture from the Android resource directory
    	mBitmap = BitmapFactory.decodeResource(context.getResources(), mResourceId);
	}

}
