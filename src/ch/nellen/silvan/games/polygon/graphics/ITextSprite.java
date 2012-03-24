package ch.nellen.silvan.games.polygon.graphics;

public interface ITextSprite extends IRenderable {
	void setText(String text);

	String getText();

	void setTextSize(float textSize);

	float getTextSize();

	void setTextColor(int color);

	int getTextColor();

	// Pixels
	void setPaddingHorizontal(int padding);

	// Pixels
	int getPaddingHorizontal();

	// Pixels
	void setPaddingVertical(int padding);

	// Pixels
	int getPaddingVertical();

	void setBackgroundColor(int color);

	int getBackgroundColor();

	// Pixels
	void setX(int x);

	// Pixels
	int getX();

	// Pixels
	void setY(int x);

	// Pixels
	int getY();

}
