package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;

public class PolygonUnfilled extends AbstractPolygonModel {
	// Properties
	private boolean[] mEdgeEnabled;
	private int numEdgesEnabled = NUMBER_OF_VERTICES;
	private float mWidth = 0.25f;

	public PolygonUnfilled(IRenderContext rc) {
		super();
		rc.getRenderer().registerRenderable(this);
		/* Init number of edges enabled */
		boolean[] edgeEnabled = new boolean[NUMBER_OF_VERTICES];
		for (int i = 0; i < NUMBER_OF_VERTICES; ++i)
			edgeEnabled[i] = true;
		this.setEdgesActive(edgeEnabled);
	}

	public void setWidth(float w) {
		mWidth = w;
	}
	
	public float getWidth() {
		return mWidth;
	}

	public void setEdgesActive(boolean[] flags) {
		assert (flags.length == NUMBER_OF_VERTICES);
		mEdgeEnabled = flags.clone();
		updateNumberOfEdgesEnabled();
	}
	
	public final boolean[] getEdgesEnabled() {
		return mEdgeEnabled;
	}

	public void setColors(RGBAColor[] colors) {
		assert (colors.length == NUMBER_OF_VERTICES);
		// TODO
	}

	public void render(IRenderContext rc) {
		recalculateGeometry(rc.getVertexBuffer(), rc.getIndicesBuffer());

		GL10 gl = rc.getGl();
		FloatBuffer vBuffer = rc.getGlVertexBuffer();
		ShortBuffer iBuffer = rc.getGlIndicesBuffer();

		// Draw the polygon
		gl.glColor4f(0.93671875f, 0.76953125f, 0.22265625f, 0.0f); // TODO:
																	// multicolor
																	// edges
		gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, numEdgesEnabled * 6, // 2 triangles
																	// per edge
				GL10.GL_UNSIGNED_SHORT, iBuffer);
	}

	private void recalculateGeometry(float[] vertexBuffer, short[] indicesBuffer) {
		float outerRadius = mRadius + mWidth;
		int j = 0;
		int iBase = 0;
		for (int i = 0; i < NUMBER_OF_VERTICES; ++i) {
			iBase = i * 6;
			vertexBuffer[iBase] = polygonPrototype[i][0] * mRadius; // Xi.1
			vertexBuffer[iBase + 1] = polygonPrototype[i][1] * mRadius; // Yi.1
			vertexBuffer[iBase + 2] = mZCoord; // Zi.1
			vertexBuffer[iBase + 3] = polygonPrototype[i][0] * outerRadius; // Xi.2
			vertexBuffer[iBase + 4] = polygonPrototype[i][1] * outerRadius; // Yi.2
			vertexBuffer[iBase + 5] = mZCoord; // Zi.2

			if (mEdgeEnabled[i]) {
				final int jBase = j * 6;
				final int indBase = i * 2;
				final int MAXCORNERSIND = NUMBER_OF_VERTICES * 2;
				// Triangle 1
				indicesBuffer[jBase] = (short) ((short) (indBase) % (MAXCORNERSIND));
				indicesBuffer[jBase + 1] = (short) ((short) (indBase + 1) % (MAXCORNERSIND));
				indicesBuffer[jBase + 2] = (short) ((short) (indBase + 2) % (MAXCORNERSIND));
				// Triangle 2
				indicesBuffer[jBase + 3] = (short) ((short) (indBase + 1) % (MAXCORNERSIND));
				indicesBuffer[jBase + 4] = (short) ((short) (indBase + 2) % (MAXCORNERSIND));
				indicesBuffer[jBase + 5] = (short) ((short) (indBase + 3) % (MAXCORNERSIND));
				j++;
			}
		}
	}

	private void updateNumberOfEdgesEnabled() {
		numEdgesEnabled = 0;
		for (int i = 0; i < mEdgeEnabled.length; ++i) {
			if (mEdgeEnabled[i])
				numEdgesEnabled++;
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
