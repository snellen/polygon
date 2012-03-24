package ch.nellen.silvan.games.polygon.graphics;

public interface IRenderer {
	public void registerRenderable3D(IRenderable r);
	
	public void registerRenderable2D(IRenderable r);

	public void unregisterRenderable(IRenderable r);
}
