package ch.nellen.silvan.games.polygon.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonRenderer;

public class PolygonSurfaceView extends GLSurfaceView {

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				PolygonRenderer.instance().mGame.handleTouchEvent(getWidth(), getHeight(), event);
			}
		});
		return true;

	}

	public PolygonSurfaceView(Context context) {
		super(context);

		PolygonRenderer.instance().init(context);
		
		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(PolygonRenderer.instance());

	}

	@Override
	public void onPause() {
		super.onPause();
		
		PolygonRenderer.instance().onPause();
	}
	
	

}
