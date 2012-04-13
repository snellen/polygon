package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;

public class PolygonUnfilled extends PolygonModel {
	// Properties
	private boolean[] mEdgeEnabled = new boolean[NUMBER_OF_VERTICES];
	private int numEdgesEnabled = NUMBER_OF_VERTICES;
	private float mWidth = 0.5f;

	public PolygonUnfilled() {
		super();
		PolygonRenderer.instance().registerRenderable3D(this);
	}

	public void setWidth(float w) {
		mWidth = w;
	}

	public float getWidth() {
		return mWidth;
	}

	public final boolean[] getEdgesEnabled() {
		return mEdgeEnabled;
	}

	public void render(IRenderContext rc) {
		FloatBuffer vBuffer = rc.getGlVertexBuffer();
		ShortBuffer iBuffer = rc.getGlIndicesBuffer();

		recalculateGeometry(vBuffer, iBuffer);

		GL10 gl = rc.getGl();

		// Draw the polygon
		gl.glColor4f(mColor.r, mColor.g, mColor.b, mColor.alpha);

		gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, numEdgesEnabled * 6, // 2 triangles
																	// per edge
				GL10.GL_UNSIGNED_SHORT, iBuffer);
	}

	private void recalculateGeometry(FloatBuffer vertexBuffer,
			ShortBuffer indicesBuffer) {
		float outerRadius = mRadius + mWidth;
		int j = 0;
		int iBase = 0;
		numEdgesEnabled = 0;
		for (int i = 0; i < NUMBER_OF_VERTICES; ++i) {
			iBase = i * 6;
			vertexBuffer.put(iBase, polygonPrototype[i][0] * mRadius); // Xi.1
			vertexBuffer.put(iBase + 1, polygonPrototype[i][1] * mRadius); // Yi.1
			vertexBuffer.put(iBase + 2, mZCoord); // Zi.1
			vertexBuffer.put(iBase + 3, polygonPrototype[i][0] * outerRadius); // Xi.2
			vertexBuffer.put(iBase + 4, polygonPrototype[i][1] * outerRadius); // Yi.2
			vertexBuffer.put(iBase + 5, mZCoord); // Zi.2

			if (mEdgeEnabled[i]) {
				final int jBase = j * 6;
				final int indBase = i * 2;
				final int MAXCORNERSIND = NUMBER_OF_VERTICES * 2;
				// Triangle 1
				indicesBuffer.put(jBase,
						(short) ((short) (indBase) % (MAXCORNERSIND)));
				indicesBuffer.put(jBase + 1,
						(short) ((short) (indBase + 1) % (MAXCORNERSIND)));
				indicesBuffer.put(jBase + 2,
						(short) ((short) (indBase + 2) % (MAXCORNERSIND)));
				// Triangle 2
				indicesBuffer.put(jBase + 3,
						(short) ((short) (indBase + 1) % (MAXCORNERSIND)));
				indicesBuffer.put(jBase + 4,
						(short) ((short) (indBase + 2) % (MAXCORNERSIND)));
				indicesBuffer.put(jBase + 5,
						(short) ((short) (indBase + 3) % (MAXCORNERSIND)));
				j++;
				numEdgesEnabled++;
			}
		}
	}

	@Override
	public int vertexBufferRequirement() {
		return NUMBER_OF_VERTICES * 6;
	}

	@Override
	public int indicesBufferRequirement() {
		return NUMBER_OF_VERTICES * 6;
	}

}
