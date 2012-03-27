package ch.nellen.silvan.games.polygon.graphics.impl;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.game.impl.GameLogic;
import ch.nellen.silvan.games.polygon.game.impl.GameState;
import ch.nellen.silvan.games.polygon.game.impl.HeadsUpDisplay;
import ch.nellen.silvan.games.polygon.game.impl.GameController;
import ch.nellen.silvan.games.polygon.game.impl.Scene;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderable;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;
import ch.nellen.silvan.games.polygon.graphics.IScene;

public class PolygonRenderer implements GLSurfaceView.Renderer, IRenderer {

	private IRenderContext mRenderContext = null;
	private Vector<IRenderable> mRenderables3D = new Vector<IRenderable>(16);
	private Vector<IRenderable> mRenderables2D = new Vector<IRenderable>(16);
	private IUpdatable mGameLogic = null;
	long lastUpdate = 0;
	private IScene mScene = null;
	private IInputHandler mGameController = null;
	private HeadsUpDisplay mHud = null;
	private IGameState mGameState = null;
	private int mScreenWidth;
	private int mScreenHeight;

	public PolygonRenderer() {
		super();
		mRenderContext = new RenderContext(this);
		mScene = new Scene(mRenderContext);
		
		mGameState = new GameState();
		mHud = new HeadsUpDisplay(mRenderContext, mGameState);
		mGameController = new GameController(mGameState);
		mGameLogic = new GameLogic(mScene, mGameState);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		// Enable use of vertex arrays
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	public void onDrawFrame(GL10 gl) {
		// Update game state
		long currTime = SystemClock.uptimeMillis();
		long timeElapsed = 0;
		if (lastUpdate != 0)
			timeElapsed = currTime - lastUpdate;

		mGameLogic.update(timeElapsed);
		mHud.update(timeElapsed);

		lastUpdate = currTime;

		/* Render 3D */

		// Redraw background color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, mGameState.getCameraZ(), 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		mRenderContext.setGl(gl);
		for (int i = 0; i < mRenderables3D.size(); ++i) {
			IRenderable r = mRenderables3D.get(i);
			if (r.isVisible()) {
				gl.glPushMatrix();
				r.render(mRenderContext);
				gl.glPopMatrix();
			}
		}

		/* Render 2D */
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

		for (int i = 0; i < mRenderables2D.size(); ++i) {
			IRenderable r = mRenderables2D.get(i);
			if (r.isVisible()) {
				gl.glPushMatrix();
				r.render(mRenderContext);
				gl.glPopMatrix();
			}
		}

		/* End Render 2D */
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		
		mHud.onScreenChanged(mScreenWidth, mScreenHeight);
		
		gl.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION); // set matrix to projection mode
		gl.glLoadIdentity(); // reset the matrix to its default state
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7); // apply the projection
													// matrix
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

	public void handleTouchEvent(float screenWidth, float screenHeight,
			final MotionEvent event) {
		if (!mHud.handleMotionEvent(screenWidth, screenHeight, event))
			mGameController.handleMotionEvent(screenWidth, screenHeight, event);
	}
}
