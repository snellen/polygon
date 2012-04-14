package ch.nellen.silvan.games.polygon.graphics;

import android.content.Context;

public interface IRenderEventHandler {

	public abstract void init(IRenderer renderer, Context context);
	
	public abstract void onRender(IRenderer r, long timeElapsed);

	public abstract void onPause();

	public abstract void onSurfaceChanged(IRenderer r, int width, int height);

}