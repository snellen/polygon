package ch.nellen.silvan.games.polygon.graphics;

public interface ISprite extends IRenderable {

	public abstract void setX(int x);

	public abstract int getX();

	public abstract void setY(int y);

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

}