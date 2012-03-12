package ch.nellen.silvan.games.polygon.activity;

import ch.nellen.silvan.games.polygon.graphics.impl.PolygonRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PolygonSurfaceView extends GLSurfaceView {

	private PolygonRenderer mRenderer = null;

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				mRenderer.handleTouchEvent(getWidth(), getHeight(), event);
			}
		});
		return true;

	}

	@Override
	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				mRenderer.handleKeyUpEvent(keyCode, event);
			}
		});
		return true;
	}

	public PolygonSurfaceView(Context context) {
		super(context);

		mRenderer = new PolygonRenderer();

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(mRenderer);

	}

}
