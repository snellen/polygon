package ch.nellen.silvan.games.polygon.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonRenderer;

public class PolygonSurfaceView extends GLSurfaceView {

	private PolygonRenderer mRenderer = null;

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				mRenderer.mGame.handleTouchEvent(getWidth(), getHeight(), event);
			}
		});
		return true;

	}

	public PolygonSurfaceView(Context context) {
		super(context);

		mRenderer = new PolygonRenderer();
		mRenderer.init(context);
		
		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(mRenderer);

	}

	@Override
	public void onPause() {
		super.onPause();
		
		mRenderer.onPause();
	}
	
	

}
