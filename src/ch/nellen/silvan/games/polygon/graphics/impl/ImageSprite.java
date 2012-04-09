package ch.nellen.silvan.games.polygon.graphics.impl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageSprite extends Sprite {

	private int mHeight = 0;
	private int mWidth = 0;
	private Bitmap mBitmap = null;

	public ImageSprite(Resources resource, int id) {
		//Get the texture from the Android resource directory
    	mBitmap = BitmapFactory.decodeResource(resource, id);
		mHeight = mBitmap.getHeight();
		mWidth = mBitmap.getWidth();
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

}
