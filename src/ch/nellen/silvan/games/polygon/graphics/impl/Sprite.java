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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import ch.nellen.silvan.games.polygon.graphics.ISprite;

public abstract class Sprite extends Renderable implements ISprite {

	protected int mX = 0;
	protected int mY = 0;
	
	// Subclasses may set this flag to indicate that the texture has changed and needs to be reloaded on next render pass
	protected boolean mRefresh = true; // Force refresh on first render pass

	static FloatBuffer cTextureCoordsBuffer = null;
	static ShortBuffer cIndiceBuffer = null;

	private FloatBuffer mVertexBuffer = null;
	protected int[] mTextureId = new int[1];

	public Sprite(IRenderer r) {
		super();
		if (cIndiceBuffer == null) {
			short[] indices = new short[] { 0, 1, 2, 0, 2, 3 };
			ByteBuffer idb = ByteBuffer.allocateDirect(indices.length * 2);
			idb.order(ByteOrder.nativeOrder());
			cIndiceBuffer = idb.asShortBuffer();
			cIndiceBuffer.put(indices);
			cIndiceBuffer.position(0);
		}

		if (cTextureCoordsBuffer == null) {
			float texCoords[] = {
					// X, Y
					0f, 0, 0, 1f, 1f, 1f, 1f, 0 };
			ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			cTextureCoordsBuffer = tbb.asFloatBuffer();
			cTextureCoordsBuffer.put(texCoords);
			cTextureCoordsBuffer.position(0);
		}
		
		if (mVertexBuffer == null) {
			float quadCoords[] = { 0, 0, 0, 0, 0, 0, 0, 0 };
			ByteBuffer vbb = ByteBuffer.allocateDirect(quadCoords.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();
			mVertexBuffer.put(quadCoords);
			mVertexBuffer.position(0);
		}

		mTextureId[0] = -1;
		r.registerRenderable2D(this);
	}

	@Override
	public void setX(int x) {
		mX = x;
	}

	@Override
	public int getX() {
		return mX;
	}

	@Override
	public void setY(int y) {
		mY = y;
	}

	@Override
	public int getY() {
		return mY;
	}

	@Override
	public int vertexBufferRequirement() {
		return 0;
	}

	@Override
	public int indicesBufferRequirement() {
		return 0;
	}

	
	@Override
	public void onTextureCleared() {
		mRefresh = true;
		mTextureId[0] = -1;
	}

	@Override
	public void render(IRenderContext rc) {
		GL10 gl = rc.getGl();

		if (mRefresh) {
			Bitmap bitmap = getTexture();
			mRefresh = false;
			
			int width = getWidthOnScreen();
			int height = getHeightOnScreen();

			/* Quad */
			mVertexBuffer.put(3, height);
			mVertexBuffer.put(4, width);
			mVertexBuffer.put(5, height);
			mVertexBuffer.put(6, width);

			if (mTextureId[0] < 0) {
				gl.glGenTextures(1, mTextureId, 0);
			}
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId[0]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		} else {
			// Load texture
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId[0]);			
		}
		
		gl.glTranslatef(mX, mY, 0);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cTextureCoordsBuffer);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
				cIndiceBuffer);
	}

	abstract Bitmap getTexture();
	/* Returns the width of this sprite on the screen */
	protected abstract int getWidthOnScreen();
	/* Returns the height of this sprite on the screen */
	protected abstract int getHeightOnScreen();
}
