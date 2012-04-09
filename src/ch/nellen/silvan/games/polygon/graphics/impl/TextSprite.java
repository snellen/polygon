package ch.nellen.silvan.games.polygon.graphics.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import ch.nellen.silvan.games.polygon.graphics.ITextSprite;

public class TextSprite extends Sprite implements ITextSprite {

	Paint mTextPaint = null;
	Paint mBackgroundPaint = null;
	private int mPaddingV = 0;
	private int mPaddingH = 0;
	private String mText = "";

	public TextSprite() {
		super();

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextAlign(Paint.Align.LEFT);
		mTextPaint.setTypeface(Typeface.DEFAULT);

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(Color.WHITE);
	}

	@Override
	public void setText(String text) {
		if (!text.equals(mText)) {
			mText = text;
			onTextChanged();
		}
	}

	@Override
	public String getText() {
		return mText;
	}

	@Override
	public void setTextSize(float textSize) {
		if (textSize != mTextPaint.getTextSize()) {
			mTextPaint.setTextSize(textSize);
			onTextChanged();
		}
	}

	@Override
	public float getTextSize() {
		return mTextPaint.getTextSize();
	}

	@Override
	public void setPaddingHorizontal(int padding) {
		if (mPaddingH != padding) {
			mPaddingH = padding;
			onTextChanged();
		}
	}

	@Override
	public int getPaddingHorizontal() {
		return mPaddingH;
	}

	@Override
	public void setPaddingVertical(int padding) {
		if (mPaddingV != padding) {
			mPaddingV = padding;
			onTextChanged();
		}
	}

	@Override
	public int getPaddingVertical() {
		return mPaddingV;
	}

	@Override
	public void setBackgroundColor(int color) {
		if (mBackgroundPaint.getColor() != color) {
			mBackgroundPaint.setColor(color);
			onTextChanged();
		}
	}

	@Override
	public int getBackgroundColor() {
		return mBackgroundPaint.getColor();
	}

	@Override
	public void setTextColor(int color) {
		if (mTextPaint.getColor() != color) {
			mTextPaint.setColor(color);
			onTextChanged();
		}

	}

	@Override
	public int getTextColor() {
		return mTextPaint.getColor();
	}

	private void onTextChanged() {
		mRefresh = true;
	}

	private Canvas canvas = null;
	private Bitmap mBitmap = null;

	Bitmap getTexture() {
		if (mRefresh) {
			int width = getWidth();
			int height = getHeight();

			if (canvas == null || mBitmap == null
					|| mBitmap.getHeight() < height
					|| mBitmap.getWidth() < width) {
				// Allocate a power-of-two texture for best compatibility
				int dim = (int) Math.pow(
						2,
						Math.ceil(Math.log(Math.max(width, height))
								/ Math.log(2)));
				mBitmap = Bitmap
						.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
				canvas = new Canvas(mBitmap);
			}

			mBitmap.eraseColor(Color.TRANSPARENT);
			canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
			canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
			FontMetrics fontMetrics = mTextPaint.getFontMetrics();
			canvas.drawText(mText, mPaddingH, Math.abs(fontMetrics.ascent)
					+ mPaddingV, mTextPaint);

		}
		return mBitmap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.nellen.silvan.games.polygon.graphics.impl.ISprite#getWidth()
	 */
	@Override
	public int getWidth() {
		return (int) Math.ceil(mTextPaint.measureText(mText)) + 2 * mPaddingH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.nellen.silvan.games.polygon.graphics.impl.ISprite#getHeight()
	 */
	@Override
	public int getHeight() {
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		return (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent) + 2
				* mPaddingV;
	}

	@Override
	protected int getWidthOnScreen() {
		return mBitmap != null ? mBitmap.getWidth() : 0;
	}

	@Override
	protected int getHeightOnScreen() {
		return mBitmap != null ? mBitmap.getHeight() : 0;
	}

	@Override
	public Typeface getTypeface() {
		return mTextPaint.getTypeface();
	}

	@Override
	public void setTypeface(Typeface tf) {
		if (!mTextPaint.getTypeface().equals(tf) && tf != null) {
			mTextPaint.setTypeface(tf);
			onTextChanged();
		}
	}
}
