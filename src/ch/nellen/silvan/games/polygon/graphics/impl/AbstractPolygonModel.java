package ch.nellen.silvan.games.polygon.graphics.impl;

import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
import ch.nellen.silvan.games.polygon.graphics.IRenderable;


public abstract class AbstractPolygonModel extends Renderable implements IRenderable, IPolygonModel {

	// Cache for precalculated corner coordinates
	// Dimension 1: Vertices
	// Dimension 2: 3 dimensional coordinates
	protected static float[][] polygonPrototype = null;

	// Radius of this polygon
	protected float mRadius = 1.0f;

	// z.coord
	protected float mZCoord = 0.0f;

	protected float mAngle;

	public AbstractPolygonModel() {
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
	}

	/* (non-Javadoc)
	 * @see ch.nellen.silvan.games.polygon.graphics.IPolygonModel#setRadius(float)
	 */
	@Override
	public void setRadius(float r) {
		mRadius = r;
	}

	/* (non-Javadoc)
	 * @see ch.nellen.silvan.games.polygon.graphics.IPolygonModel#getRadius()
	 */
	@Override
	public float getRadius() {
		return mRadius;
	}

	/* (non-Javadoc)
	 * @see ch.nellen.silvan.games.polygon.graphics.IPolygonModel#getZCoord()
	 */
	@Override
	public float getZCoord() {
		return mZCoord;
	}

	/* (non-Javadoc)
	 * @see ch.nellen.silvan.games.polygon.graphics.IPolygonModel#setZCoord(float)
	 */
	@Override
	public void setZCoord(float zCoord) {
		this.mZCoord = zCoord;
	}
	
	public void setAngle(float angle) {
		mAngle = angle;
		while(mAngle < 0f)
			mAngle += 360f;
		while(mAngle > 360f)
			mAngle -= 360f;
	}

	public float getAngle() {
		return mAngle;
	}

}