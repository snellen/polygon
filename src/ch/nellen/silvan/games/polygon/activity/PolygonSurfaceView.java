package ch.nellen.silvan.games.polygon.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import ch.nellen.silvan.games.polygon.game.impl.PolygonGame;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonRenderer;

public class PolygonSurfaceView extends GLSurfaceView {

	private PolygonRenderer mRenderer;
	private PolygonGame mGame;

	public PolygonSurfaceView(Context context) {
		super(context);

		mRenderer = new PolygonRenderer();
		mGame = new PolygonGame();
		mRenderer.setEventHandler(mGame);
		mRenderer.init(context);

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(mRenderer);

	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// Forward to rendering thread
		queueEvent(new Runnable() {
			public void run() {
				mGame.handleTouchEvent(getWidth(), getHeight(), event);
			}
		});
		return true;

	}
}
