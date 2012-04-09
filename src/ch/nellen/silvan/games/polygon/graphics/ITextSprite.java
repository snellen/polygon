package ch.nellen.silvan.games.polygon.graphics;

import android.graphics.Typeface;

public interface ITextSprite extends IRenderable, ISprite {
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
	
	Typeface getTypeface();
	
	void setTypeface(Typeface tf);
}
