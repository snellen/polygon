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

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderEventHandler;
import ch.nellen.silvan.games.polygon.graphics.IRenderable;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;

public class PolygonRenderer implements GLSurfaceView.Renderer, IRenderer {
	private IRenderContext mRenderContext = null;
	private Vector<IRenderable> mRenderables3D = new Vector<IRenderable>(16);
	private Vector<IRenderable> mRenderables2D = new Vector<IRenderable>(16);
	private long lastUpdate = 0;

	private int mScreenWidth;
	private int mScreenHeight;
	private static final float Z_NEAR = 2;
	private float mScreenRadiusZNearRatio;

	private boolean mIsInitialized = false;

	private IRenderEventHandler mEventHandler = null;

	private float mCameraZ = 0f;

	public PolygonRenderer() {
		super();
	}

	public void setEventHandler(IRenderEventHandler eventHandler) {
		mEventHandler = eventHandler;
	}

	public void init(Context context) {
		mRenderContext = new RenderContext(this);
		mEventHandler.init(this, context);
		for (IRenderable r : mRenderables3D) {
			r.init(context);
		}
		for (IRenderable r : mRenderables2D) {
			r.init(context);
		}
		mIsInitialized = true;
	}

	@Override
	public float getCameraZ() {
		return mCameraZ;
	}

	@Override
	public void setCameraZ(float cameraZ) {
		this.mCameraZ = cameraZ;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		// Enable use of vertex arrays
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		for (IRenderable r : mRenderables3D) {
			r.onTextureCleared();
		}
		for (IRenderable r : mRenderables2D) {
			r.onTextureCleared();
		}

	}

	public void onDrawFrame(GL10 gl) {

		if(!mIsInitialized){
			String msg = "Attempt to render before init";
			throw new RuntimeException(msg);
		}
		
		// Update game
		long currTime = SystemClock.uptimeMillis();
		long timeElapsed = 0;
		if (lastUpdate != 0)
			timeElapsed = currTime - lastUpdate;

		mEventHandler.onRender(this, timeElapsed);
		lastUpdate = currTime;

		/* Begin render 3D */
		// Redraw background color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, mCameraZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		mRenderContext.setGl(gl);
		for (IRenderable r : mRenderables3D) {
			if (r.isVisible()) {
				gl.glPushMatrix();
				r.render(mRenderContext);
				gl.glPopMatrix();
			}
		}
		/* End render 3D */

		/* Begin render 2D */
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0, mScreenWidth, mScreenHeight, 0, -1, 1);
		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glColor4f(1f, 1f, 1f, 1f);

		for (IRenderable r : mRenderables2D) {
			if (r.isVisible()) {
				gl.glPushMatrix();
				r.render(mRenderContext);
				gl.glPopMatrix();
			}
		}

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		/* End Render 2D */
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;

		gl.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		gl.glMatrixMode(GL10.GL_PROJECTION); // set matrix to projection mode
		gl.glLoadIdentity(); // reset the matrix to its default state
		float ratio = (float) width / height;
		gl.glFrustumf(-ratio, ratio, -1, 1, Z_NEAR, 7); // apply the projection
		// matrix

		mScreenRadiusZNearRatio = (float) (Math.sqrt(ratio * ratio + 1) / Z_NEAR);

		// Update Game
		mEventHandler.onSurfaceChanged(this, width, height);
	}

	@Override
	public void registerRenderable3D(IRenderable r) {
		if (!mRenderables3D.contains(r)) {
			mRenderContext.registerIndicesBuffer(r.indicesBufferRequirement());
			mRenderContext.registerVertexBuffer(r.vertexBufferRequirement());
			mRenderables3D.add(r);
		}
	}

	@Override
	public void registerRenderable2D(IRenderable r) {
		if (!mRenderables2D.contains(r)) {
			mRenderContext.registerIndicesBuffer(r.indicesBufferRequirement());
			mRenderContext.registerVertexBuffer(r.vertexBufferRequirement());
			mRenderables2D.add(r);
		}
	}

	@Override
	public void unregisterRenderable(IRenderable r) {
		if (mRenderables3D.contains(r))
			mRenderables3D.remove(r);
		if (mRenderables2D.contains(r))
			mRenderables2D.remove(r);
	}

	@Override
	public float getScreenRadiusZNearRatio() {
		return mScreenRadiusZNearRatio;
	}
}
