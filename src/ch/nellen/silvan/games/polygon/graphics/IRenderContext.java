package ch.nellen.silvan.games.polygon.graphics;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public interface IRenderContext {
	// Renderer that this IRenderContext belongs to.
	public IRenderer getRenderer();

	public GL10 getGl();

	public void setGl(GL10 gl);

	public void registerVertexBuffer(int size);

	public void registerIndicesBuffer(int size);

	public FloatBuffer getGlVertexBuffer();

	public ShortBuffer getGlIndicesBuffer();
}
