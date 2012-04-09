package ch.nellen.silvan.games.polygon.graphics;

import android.content.Context;

public interface IRenderable {

	// Initialize, load resources etc.
	// Called right before the first render pass
	public abstract void init(Context context);
	
	// Called when Open 
	public abstract void onTextureCleared();

	public abstract void render(IRenderContext gl);

	public abstract int vertexBufferRequirement();

	public abstract int indicesBufferRequirement();

	public abstract boolean isVisible();

	public abstract void isVisible(boolean flag);

}