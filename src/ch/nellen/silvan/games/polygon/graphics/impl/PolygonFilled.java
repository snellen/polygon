package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;

public class PolygonFilled extends PolygonModel {

	RGBAColor mColor;

	public PolygonFilled(IRenderContext rc) {
		super();
		rc.getRenderer().registerRenderable3D(this);
		mColor = new RGBAColor(0f, 0f, 0f, 1f);
	}

	public void setColor(RGBAColor color) {
		mColor = color;
	}

	public void render(IRenderContext rc) {
		recalculateGeometry(rc.getVertexBuffer(), rc.getIndicesBuffer());
		
		GL10 gl = rc.getGl();
		FloatBuffer vBuffer = rc.getGlVertexBuffer();
		ShortBuffer iBuffer = rc.getGlIndicesBuffer();

		// Draw the polygon
		gl.glColor4f(mColor.r, mColor.g, mColor.b, mColor.alpha);
		gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, NUMBER_OF_VERTICES*3, GL10.GL_UNSIGNED_SHORT, iBuffer);
	}

	private void recalculateGeometry(float[] vertexBuffer, short[] indicesBuffer) {
		int iBase = 0;
		for (int i = 0; i < NUMBER_OF_VERTICES; ++i) {
			iBase = i * 3;
			vertexBuffer[iBase] = polygonPrototype[i][0] * mRadius; // Xi.1
			vertexBuffer[iBase + 1] = polygonPrototype[i][1] * mRadius; // Yi.1
			vertexBuffer[iBase + 2] = mZCoord; // Zi.1
			
			indicesBuffer[iBase] = NUMBER_OF_VERTICES; // center point at last position
			indicesBuffer[iBase + 1] = (short) i;
			indicesBuffer[iBase + 2] = (short) (((short)(i+1))%NUMBER_OF_VERTICES);
		}
		// Center point
		iBase = NUMBER_OF_VERTICES*3;
		vertexBuffer[iBase] = 0.0f; // Xi.1
		vertexBuffer[iBase + 1] = 0.0f; // Yi.1
		vertexBuffer[iBase + 2] = 0.0f; // Zi.1
		
	}

	@Override
	public int vertexBufferRequirement() {
		return (NUMBER_OF_VERTICES+1)*3;/* one vertex per corner, plus central vertex, 3 coordinates each */
	}

	@Override
	public int indicesBufferRequirement() {
		return NUMBER_OF_VERTICES*3;/* 3 vertices per triangle */
	}

}
