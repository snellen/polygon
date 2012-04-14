package ch.nellen.silvan.games.polygon.graphics.impl;

import android.content.Context;
import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
import ch.nellen.silvan.games.polygon.graphics.IRenderable;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;

public abstract class PolygonModel extends Renderable implements IRenderable,
		IPolygonModel {

	// Cache for precalculated corner coordinates
	// Dimension 1: Vertices
	// Dimension 2: 3 dimensional coordinates
	protected static float[][] polygonPrototype = null;

	// Radius of this polygon
	protected float mRadius = 1.0f;

	// z.coord
	protected float mZCoord = 0.0f;

	protected float mAngle;

	protected RGBAColor mColor = new RGBAColor(0, 0, 0, 1);

	public PolygonModel(IRenderer r) {
		super();

		mAngle = 0.0f;
		/* Init PolygonPrototype */
		if (polygonPrototype == null) {
			polygonPrototype = new float[NUMBER_OF_VERTICES][3];
			float radius = 1.0f;
			final float dAngle = (float) (2 * Math.PI / NUMBER_OF_VERTICES);
			float angle = 0;
			for (int i = 0; i < NUMBER_OF_VERTICES; ++i) {
				polygonPrototype[i][0] = (float) (radius * Math.cos(angle)); // X
				polygonPrototype[i][1] = (float) (radius * Math.sin(angle)); // Y
				polygonPrototype[i][2] = 0.0f; // Z
				angle += dAngle;
			}
		}
		setColor(((float) 238) / 255, ((float) 244) / 255, 0, 1);
		r.registerRenderable3D(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.nellen.silvan.games.polygon.graphics.IPolygonModel#setRadius(float)
	 */
	@Override
	public void setRadius(float r) {
		mRadius = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.nellen.silvan.games.polygon.graphics.IPolygonModel#getRadius()
	 */
	@Override
	public float getRadius() {
		return mRadius;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.nellen.silvan.games.polygon.graphics.IPolygonModel#getZCoord()
	 */
	@Override
	public float getZCoord() {
		return mZCoord;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.nellen.silvan.games.polygon.graphics.IPolygonModel#setZCoord(float)
	 */
	@Override
	public void setZCoord(float zCoord) {
		this.mZCoord = zCoord;
	}

	public void setAngle(float angle) {
		mAngle = angle;
		while (mAngle < 0f)
			mAngle += 360f;
		while (mAngle > 360f)
			mAngle -= 360f;
	}

	public float getAngle() {
		return mAngle;
	}

	public void setColor(float r, float g, float b, float a) {
		mColor.r = r;
		mColor.g = g;
		mColor.b = b;
		mColor.alpha = a;
	}

	public void setColor(RGBAColor c) {
		setColor(c.r, c.g, c.b, c.alpha);
	}

	public RGBAColor getColor() {
		return mColor;
	}
	
	@Override
	public void init(Context context) {
		// nothing to do
	}

	@Override
	public void onTextureCleared() {
		// nothing to do
	}
	

}