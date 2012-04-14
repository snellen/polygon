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

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;

public class RenderContext implements IRenderContext {

	// Preallocated rendering buffer
	// At any time only one instance of IRenderable is rendering,
	// the buffers can be shared for performance and memory consumption reasons.
	private GL10 mGl = null;
	private IRenderer mRenderer;
	
	private FloatBuffer vBuffer = null;
	private ShortBuffer iBuffer = null;

	public RenderContext(IRenderer renderer) {
		super();
		this.mRenderer = renderer;
	}

	@Override
	public void registerVertexBuffer(int size) {
		/* Initialize vertex buffer */
		if (vBuffer == null || vBuffer.limit() < size) {
			float[] mVertexBuffer = new float[size];
			// (# of coordinate values * 4 bytes per float)
			ByteBuffer vbb = ByteBuffer.allocateDirect(mVertexBuffer.length * 4);

			vbb.order(ByteOrder.nativeOrder());// use the device hardware's native
												// byte order
			vBuffer = vbb.asFloatBuffer(); // create a floating point buffer from
											// the ByteBuffer
			
		}
	}

	@Override
	public void registerIndicesBuffer(int size) {
		/* Initialize indices buffer */
		if (iBuffer == null || iBuffer.limit() < size) {
			short[] mIndicesBuffer = new short[size];
			ByteBuffer idb = ByteBuffer.allocateDirect(mIndicesBuffer.length * 2);
			idb.order(ByteOrder.nativeOrder());
			iBuffer = idb.asShortBuffer();
		}
	}

	public FloatBuffer getGlVertexBuffer() {
		return vBuffer;
	}

	public ShortBuffer getGlIndicesBuffer() {
		return iBuffer;
	}

	@Override
	public GL10 getGl() {
		return mGl ;
	}

	@Override
	public void setGl(GL10 gl) {
		mGl = gl;
	}

	@Override
	public IRenderer getRenderer() {
		return mRenderer;
	}

}
