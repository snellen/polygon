package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;

public class PlayerModel extends Renderable {

	private float mRadius;
	private float mAngle;
	private RGBAColor mColor = null;

	private static float SIZE = 0.06f;
	private static float triangleCoords[] = {
			// X, Y, Z
			SIZE, 0, 0, -SIZE / 2, SIZE * 0.866025404f, 0, -SIZE / 2,
			-SIZE * 0.866025404f, 0 };
	private float mZCoord;

	public PlayerModel() {
		PolygonRenderer.instance().registerRenderable3D(this);
		mColor = new RGBAColor(1f, 0f, 0f, 1f);
		mZCoord = 0.0f;
		mAngle = 0f;
	}

	@Override
	public void render(IRenderContext rc) {
		FloatBuffer vBuffer = rc.getGlVertexBuffer();
		ShortBuffer iBuffer = rc.getGlIndicesBuffer();

		recalculateGeometry(vBuffer, iBuffer);

		GL10 gl = rc.getGl();

		// Draw the polygon
		gl.glColor4f(mColor.r, mColor.g, mColor.b, mColor.alpha);
		float[] coord = getCoords();
		gl.glTranslatef(coord[0], coord[1], 0f);
		gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, iBuffer);
	}

	private void recalculateGeometry(FloatBuffer vertexBuffer,
			ShortBuffer indicesBuffer) {
		int iBase = 0;
		for (int i = 0; i < 3; ++i) {
			iBase = i * 3;
			vertexBuffer.put(iBase, triangleCoords[iBase]); // Xi.1
			vertexBuffer.put(iBase + 1, triangleCoords[iBase + 1]); // Yi.1
			vertexBuffer.put(iBase + 2, mZCoord); // Zi.1
		}
		indicesBuffer.put(0, (short) 0);
		indicesBuffer.put(1, (short) 1);
		indicesBuffer.put(2, (short) 2);
	}

	public void setRadius(float r) {
		mRadius = r;
	}

	public float getRadius() {
		return mRadius;
	}

	public float getSize() {
		return SIZE;
	}

	public void setSize(float size) {
		SIZE = size;
		triangleCoords[0] = SIZE;
		triangleCoords[3] = -SIZE / 2;
		triangleCoords[4] = SIZE * 0.866025404f;
		triangleCoords[6] = -SIZE / 2;
		triangleCoords[7] = -SIZE * 0.866025404f;
	}

	public float getTangentialSize() {
		return triangleCoords[4];
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

	public void setColor(RGBAColor color) {
		mColor = color;
	}

	public float getZCoord() {
		return mZCoord;
	}

	public void setZCoord(float zCoord) {
		this.mZCoord = zCoord;
	}

	@Override
	public int vertexBufferRequirement() {
		return 9;
	}

	@Override
	public int indicesBufferRequirement() {
		return 3;
	}

	public float[] getCoords() {
		float[] res = new float[2];
		float angleRad = (float) (mAngle * Math.PI / 180f);
		res[0] = (float) (mRadius * Math.cos(angleRad));
		res[1] = (float) (mRadius * Math.sin(angleRad));
		return res;
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
