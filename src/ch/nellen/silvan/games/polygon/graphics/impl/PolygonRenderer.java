package ch.nellen.silvan.games.polygon.graphics.impl;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IUpdatable;
import ch.nellen.silvan.games.polygon.game.impl.PlayerController;
import ch.nellen.silvan.games.polygon.game.impl.GameLogic;
import ch.nellen.silvan.games.polygon.game.impl.GameState;
import ch.nellen.silvan.games.polygon.game.impl.HeadsUpDisplay;
import ch.nellen.silvan.games.polygon.game.impl.Scene;
import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderable;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;

public class PolygonRenderer implements GLSurfaceView.Renderer, IRenderer {

	private IRenderContext mRenderContext = null;
	private Vector<IRenderable> mRenderables3D = new Vector<IRenderable>(16);
	private Vector<IRenderable> mRenderables2D = new Vector<IRenderable>(16);
	long lastUpdate = 0;
	private boolean mInitialized = false;
	private int mScreenWidth;
	private int mScreenHeight;
	private static final float Z_NEAR = 2;

	public static final String PREFERENCES = PolygonGame.class.getName();
	public static final String PREFERENCES_BEST = "POLYGONBESTTIME";
	private Context mContext;

	// To keep all game specific code in one place...
	public class PolygonGame implements IUpdatable, Observer {
		private Handler mHandler = null;

		private GameLogic mGameLogic = null;
		private Scene mScene = null;
		private PlayerController mGameController = null;
		private HeadsUpDisplay mHud = null;
		private GameState mGameState = null;

		public PolygonGame(Context context) {
			mGameState = new GameState();
			SharedPreferences prefs = mContext.getSharedPreferences(
					PolygonRenderer.PREFERENCES, 0/* MODE_PRIVATE */);
			mGameState.setHighscore(prefs.getLong(PREFERENCES_BEST, 0));
			mGameState.addObserver(this);

			mScene = new Scene(mRenderContext);
			mHud = new HeadsUpDisplay(context, mRenderContext, mGameState);
			mGameController = new PlayerController(mRenderContext, mGameState);
			mGameLogic = new GameLogic(mScene, mGameState);
			mHandler = new Handler();
		}

		public float getCameraZ() {
			return mGameState.getCameraZ();
		}

		@Override
		public void update(long timeElapsed) {
			mGameLogic.update(timeElapsed);
			mHud.update(timeElapsed);
		}

		public void onSurfaceChanged() {
			mHud.onSurfaceChanged(mScreenWidth, mScreenHeight);
			mGameController.onSurfaceChanged(mScreenWidth, mScreenHeight);
			float ratio = (float) mScreenWidth / mScreenHeight;
			mGameLogic.onSurfaceChanged(mScreenWidth, mScreenHeight,
					(float) (Math.sqrt(ratio * ratio + 1) / Z_NEAR));
		}

		public void handleTouchEvent(float screenWidth, float screenHeight,
				MotionEvent event) {
			if (!mHud.handleMotionEvent(screenWidth, screenHeight, event))
				mGameController.handleMotionEvent(screenWidth, screenHeight,
						event);
		}

		@Override
		public void update(Observable arg0, Object arg1) {
			if (arg1 == null) {
				GameState gs = (GameState) arg0;
				final long highscore = gs.getHighscore();
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						SharedPreferences prefs = mContext
								.getSharedPreferences(
										PolygonRenderer.PREFERENCES, 0/* MODE_PRIVATE */);
						Editor editor = prefs.edit();
						editor.putLong(PREFERENCES_BEST, highscore);
						editor.commit();
					}
				});
			}
		}
	}

	public PolygonGame mGame = null;

	public PolygonRenderer() {
		super();
	}

	public void init(Context context) {
		mContext = context;
		mRenderContext = new RenderContext(this);
		mGame = new PolygonGame(context);
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

		if (!mInitialized) {
			for (IRenderable r : mRenderables3D) {
				r.init(mContext);
			}
			for (IRenderable r : mRenderables2D) {
				r.init(mContext);
			}
			mInitialized = true;
		}

		// Update game
		long currTime = SystemClock.uptimeMillis();
		long timeElapsed = 0;
		if (lastUpdate != 0)
			timeElapsed = currTime - lastUpdate;

		mGame.update(timeElapsed);
		lastUpdate = currTime;

		/* Begin render 3D */
		// Redraw background color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, mGame.getCameraZ(), 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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

		// Update Game
		mGame.onSurfaceChanged();
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
}
