package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.IGameState;
import ch.nellen.silvan.games.polygon.game.IInputHandler;
import ch.nellen.silvan.games.polygon.game.impl.GameState;
import ch.nellen.silvan.games.polygon.game.impl.InputHandler;
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
	private IScene mScene = null;
	private IInputHandler mInputHandler = null;

	public HexagonRenderer() {
		super();
		mRenderContext = new RenderContext(this);
		mScene = new Scene(mRenderContext);
		mInputHandler = new InputHandler();
		mGameState = new GameState(mScene, mInputHandler);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		// Enable use of vertex arrays
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	int[] textures = new int[4];
	private int mScreenWidth;
	private int mScreenHeight;

	public void onDrawFrame(GL10 gl) {
		// Update game state
		long currTime = SystemClock.uptimeMillis();
		long timeElapsed = 0;
		if (lastUpdate != 0)
			timeElapsed = currTime - lastUpdate;

		mGameState.update(timeElapsed);

		lastUpdate = currTime;

		/* Render 3D */
		// Redraw background color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Set GL_MODELVIEW transformation mode
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity(); // reset the matrix to its default state
		// When using GL_MODELVIEW, you must set the view point
		GLU.gluLookAt(gl, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		mRenderContext.setGl(gl);
		for (int i = 0; i < mRenderables.size(); ++i) {
			IRenderable r = mRenderables.get(i);
			if (r.isVisible()) {
				gl.glPushMatrix();
				r.render(mRenderContext);
				gl.glPopMatrix();
			}
		}
		/*
		/* Render 2D 
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof (0, mScreenWidth, mScreenHeight, 0, 0, 1);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glMatrixMode (GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		FloatBuffer triangleVB;
        float triangleCoords[] = {
                // X, Y, Z
                50f, 250f, 0,
                150f, 250f, 0,
                100f,  355.9016994f, 0
            }; 
            
            // initialize vertex Buffer for triangle  
            ByteBuffer vbb = ByteBuffer.allocateDirect(
                    // (# of coordinate values * 4 bytes per float)
                    triangleCoords.length * 4); 
            vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
            triangleVB = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
            triangleVB.put(triangleCoords);    // add the coordinates to the FloatBuffer
            triangleVB.position(0);  
            // Draw the triangle
            gl.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 0.0f);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleVB);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
		
	
		// Create an empty, mutable bitmap
		Bitmap bitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_4444);
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);

		// Draw the text
		Paint textPaint = new Paint();
		textPaint.setTextSize(32);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
		// draw the text centered
		canvas.drawText("Hello World", 160,312, textPaint);
 
		//Generate one texture pointer...
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Clean up
		bitmap.recycle();
		
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		*/

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
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
		mInputHandler.handleMotionEvent(screenWidth, screenHeight, event);
	}

	public void handleKeyUpEvent(int keyCode, KeyEvent event) {
		mInputHandler.handleKeyUpEvent(keyCode, event);
	}

}
