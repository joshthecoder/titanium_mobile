package org.appcelerator.titanium.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.widget.FrameLayout;

/*
 * A layout for a single view that is framed by a border.
 */
public class BorderLayout extends FrameLayout
{
	private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int borderWidth = 1;
	private float borderRadius = 0.0f;

	public BorderLayout(Context context)
	{
		super(context);
	}

	// Set the width of the border in pixel units.
	public void setBorderWidth(int width)
	{
		this.borderWidth = width;
		setPadding(width, width, width, width);
	}

	public void setBorderColor(int borderColor)
	{
		borderPaint.setColor(borderColor);
	}

	public void setBorderRadius(float radius)
	{
		borderRadius = radius;
	}

	// Draws the border bitmap to fit the given dimensions.
	private void drawBorder(int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		if (borderRadius > 0.0f) {
			RectF outerRect = new RectF(0.0f, 0.0f, width, height);
			canvas.drawRoundRect(outerRect, borderRadius, borderRadius, borderPaint);
		} else {
			canvas.drawPaint(borderPaint);
		}

		// To prevent the border from blending with the child
		// view being framed, we need to create a transparent region.
		// This region will fill the inner rectangle with the child
		// view being drawn on top.
		borderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		Rect innerRect = new Rect(borderWidth, borderWidth, width - borderWidth, height - borderWidth);
		canvas.drawRect(innerRect, borderPaint);
		borderPaint.setXfermode(null);

		setBackgroundDrawable(new BitmapDrawable(bitmap));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		drawBorder(w, h);
	}

}
