package ch.nellen.silvan.games.polygon.graphics.impl;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.impl.GameState;
import ch.nellen.silvan.games.polygon.game.impl.Scene;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderable;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import ch.nellen.silvan.games.polygon.graphics.IScene;

public class HexagonRenderer implements GLSurfaceView.Renderer, IRenderer {

	private IRenderContext mRenderContext = null;
	private Vector<IRenderable> mRenderables = new Vector<IRenderable>(16);
	private IGameState mGameState = null;
	long lastUpdate = 0;
	private IScene mScene;

	public HexagonRenderer() {
		super();
		mRenderContext = new RenderContext(this);
		mScene = new Scene(mRenderContext);
		mGameState = new GameState(mScene);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		// Enable use of vertex arrays
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	public void onDrawFrame(GL10 gl) {
		// Redraw background color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Set GL_MODELVIEW transformation mode
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity(); // reset the matrix to its default state

		// When using GL_MODELVIEW, you must set the view point
		GLU.gluLookAt(gl, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Update game state
		long currTime = SystemClock.uptimeMillis();
		long timeElapsed = 0;
		if (lastUpdate != 0)
			timeElapsed = currTime - lastUpdate;

		mGameState.update(timeElapsed);

		lastUpdate = currTime;

		// Render
		mRenderContext.setGl(gl);
		for (int i = 0; i < mRenderables.size(); ++i) {
			IRenderable r = mRenderables.get(i);
			if (r.isVisible()) {
				gl.glPushMatrix();
				r.render(mRenderContext);
				gl.glPopMatrix();
			}
		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION); // set matrix to projection mode
		gl.glLoadIdentity(); // reset the matrix to its default state
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7); // apply the projection
													// matrix
	}

	@Override
	public void registerRenderable(IRenderable r) {
		if (!mRenderables.contains(r)) {
			mRenderContext.registerIndicesBuffer(r.indicesBufferRequirement());
			mRenderContext.registerVertexBuffer(r.vertexBufferRequirement());
			mRenderables.add(r);
		}
	}

	@Override
	public void unregisterRenderable(IRenderable r) {
		mRenderables.remove(r);
	}

	public void handleTouchEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {
		mGameState.onTouchEvent(screenWidth, screenHeight, event);
	}

}
