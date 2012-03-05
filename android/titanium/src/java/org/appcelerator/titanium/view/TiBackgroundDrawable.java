/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package org.appcelerator.titanium.view;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

public class TiBackgroundDrawable extends StateListDrawable {

	//private int backgroundColor;
	//private Bitmap backgroundImage;
	private static final String TAG = "TiBackgroundDrawable";

	private Drawable background;
	private static final int NOT_SET = -1;

	public TiBackgroundDrawable()
	{
		background = new ColorDrawable(Color.TRANSPARENT);
	}

	@Override
	protected boolean onStateChange(int[] stateSet) {
		boolean changed = super.onStateChange(stateSet);
		changed = setState(stateSet);
		boolean drawableChanged = false;
		if (background != null) {
//			Log.e("TiBackground", "background="+background.getClass().getSimpleName()+",state.len="+stateSet.length);
//			for (int i = 0; i < stateSet.length; i++) {
//				Log.e("TiBackground", "    state[" + i + "]=" + stateSet[i]);
//			}
			drawableChanged = background.setState(stateSet);
			if (drawableChanged) {
				invalidateSelf();
			}
		}

		return changed || drawableChanged;
	}

	@Override
	public void addState(int[] stateSet, Drawable drawable) {
		if (background instanceof StateListDrawable) {
			((StateListDrawable)background).addState(stateSet, drawable);
		}
	}

	@Override
	protected boolean onLevelChange(int level) {
		boolean changed = super.onLevelChange(level);
		boolean backgroundChanged = false;
		if (background instanceof StateListDrawable) {
			backgroundChanged = ((StateListDrawable)background).setLevel(level);
		}
		return changed || backgroundChanged;
	}

	
	@Override
	public void invalidateSelf() {
		super.invalidateSelf();
		if (background instanceof StateListDrawable) {
			((StateListDrawable)background).invalidateSelf();			
		}
	}

	@Override
	public void invalidateDrawable(Drawable who) {
		super.invalidateDrawable(who);
		if (background instanceof StateListDrawable) {
			((StateListDrawable)background).invalidateDrawable(who);
		}
	}

	@Override
	public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
			throws XmlPullParserException, IOException {
		super.inflate(r, parser, attrs);
		if (background != null) {
			background.inflate(r, parser, attrs);
		}
	}

	public void releaseDelegate() {
		if (background != null) {
			if (background instanceof BitmapDrawable) {
				((BitmapDrawable)background).getBitmap().recycle();
			}
			background.setCallback(null);
			background = null;
		}
	}

	public void setBackgroundColor(int backgroundColor) {
		releaseDelegate();
		this.background = new PaintDrawable(backgroundColor);
	}

	public void setBackgroundImage(Bitmap backgroundImage) {
		releaseDelegate();
		this.background = new BitmapDrawable(backgroundImage);
	}

	public void setBackgroundDrawable(Drawable drawable) {
		releaseDelegate();
		this.background = drawable;
		onStateChange(getState());
	}
}
