package ch.nellen.silvan.games.polygon.graphics;

public interface IRenderer {
	public void registerRenderable(IRenderable r);

	public void unregisterRenderable(IRenderable r);
}
