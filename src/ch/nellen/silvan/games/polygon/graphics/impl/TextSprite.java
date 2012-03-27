package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.opengl.GLUtils;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.ITextSprite;

public class TextSprite implements ITextSprite {

	static FloatBuffer cTextureCoordsBuffer = null;
	static ShortBuffer cIndiceBuffer = null;

	FloatBuffer mVertexBuffer = null;
	Paint mTextPaint = null;
	private int mX = 0;
	private int mY = 0;
	private int mBackgroundColor = Color.WHITE;
	private int mPaddingV = 0;
	private int mPaddingH = 0;
	private String mText = "";
	private boolean mVisible = true;
	private boolean mRefresh = true; // Force refresh on first render pass
	private int[] mTextureId = new int[1];

	public TextSprite() {
		super();
		if (cIndiceBuffer == null) {
			short[] indices = new short[] { 0, 1, 2, 0, 2, 3 };
			ByteBuffer idb = ByteBuffer.allocateDirect(indices.length * 2);
			idb.order(ByteOrder.nativeOrder());
			cIndiceBuffer = idb.asShortBuffer();
			cIndiceBuffer.put(indices);
			cIndiceBuffer.position(0);
		}

		if (cTextureCoordsBuffer == null) {
			float texCoords[] = {
					// X, Y
					0f, 0, 0, 1f, 1f, 1f, 1f, 0 };
			ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			cTextureCoordsBuffer = tbb.asFloatBuffer();
			cTextureCoordsBuffer.put(texCoords);
			cTextureCoordsBuffer.position(0);
		}

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextAlign(Paint.Align.LEFT);
		mTextPaint.setTypeface(Typeface.DEFAULT);

		mTextureId[0] = -1;
	}

	@Override
	public void render(IRenderContext rc) {
		GL10 gl = rc.getGl();
		refreshIfNecessary(gl);
		gl.glTranslatef(mX, mY, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId[0]);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cTextureCoordsBuffer);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
				cIndiceBuffer);
	}

	@Override
	public int vertexBufferRequirement() {
		return 0;
	}

	@Override
	public int indicesBufferRequirement() {
		return 0;
	}

	@Override
	public boolean isVisible() {
		return mVisible;
	}

	@Override
	public void isVisible(boolean flag) {
		mVisible = flag;
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
		if (mBackgroundColor != color) {
			mBackgroundColor = color;
			onTextChanged();
		}
	}

	@Override
	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	@Override
	public void setX(int x) {
		mX = x;

	}

	@Override
	public int getX() {
		return mX;
	}

	@Override
	public void setY(int y) {
		mY = y;
	}

	@Override
	public int getY() {
		return mY;
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

	private float squareCoords[] = { 0, 0, 0, 0, 0, 0, 0, 0 };
	private Canvas canvas = null;
	private Bitmap bitmap = null;

	private void refreshIfNecessary(GL10 gl) {
		if (mRefresh) {
			mRefresh = false;
			int width = getWidth();
			int height = getHeight();

			if (canvas == null || bitmap == null
					|| bitmap.getHeight() != height
					|| bitmap.getWidth() != width) {

				bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
				canvas = new Canvas(bitmap);
				/* Quad */
				squareCoords[3] = height;
				squareCoords[4] = width;
				squareCoords[5] = height;
				squareCoords[6] = width;
				ByteBuffer vbb = ByteBuffer
						.allocateDirect(squareCoords.length * 4);
				vbb.order(ByteOrder.nativeOrder());
				mVertexBuffer = vbb.asFloatBuffer();
				mVertexBuffer.put(squareCoords);
				mVertexBuffer.position(0);
			}

			bitmap.eraseColor(mBackgroundColor);
			
			FontMetrics fontMetrics = mTextPaint.getFontMetrics();
			canvas.drawText(mText, mPaddingH, Math.abs(fontMetrics.ascent)
					+ mPaddingV, mTextPaint);

			if (mTextureId[0] < 0) {
				gl.glGenTextures(1, mTextureId, 0);
			}

			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId[0]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		}
	}

	public int getWidth() {
		return (int) Math.ceil(mTextPaint.measureText(mText)) + 2 * mPaddingH;
	}

	public int getHeight() {
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		return (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent) + 2
				* mPaddingV;
	}
}
