package ch.nellen.silvan.games.polygon.graphics;

public interface IRenderable {

	public abstract void render(IRenderContext gl);

	public abstract int vertexBufferRequirement();

	public abstract int indicesBufferRequirement();

}