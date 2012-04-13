package ch.nellen.silvan.games.polygon.graphics.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageSprite extends Sprite {

	private int mHeight = 0;
	private int mWidth = 0;
	private Bitmap mBitmap = null;
	private int mResourceId = -1;

	public ImageSprite(int id) {
		super();
		mResourceId = id;
		PolygonRenderer.instance().registerRenderable2D(this);
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
