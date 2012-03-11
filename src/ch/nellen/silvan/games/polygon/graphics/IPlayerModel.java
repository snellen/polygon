package ch.nellen.silvan.games.polygon.graphics;

public interface IPlayerModel extends IRenderable {
	// Inner Radius
	public void setRadius(float r);

	public float getRadius();

	public void setAngle(float angle);

	public float getAngle();
	
	public float getSize();
	
	public float[] getCoords();
}
