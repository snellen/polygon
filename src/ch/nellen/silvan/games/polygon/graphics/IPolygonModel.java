package ch.nellen.silvan.games.polygon.graphics;

public interface IPolygonModel {

	// The number of corners of all instances of AbstractPolygonModel
	public static final int NUMBER_OF_VERTICES = 6;

	public abstract void setRadius(float r);

	public abstract float getRadius();

	public abstract float getZCoord();

	public abstract void setZCoord(float zCoord);
	
	public abstract void setAngle(float angle);

	public abstract float getAngle();

}