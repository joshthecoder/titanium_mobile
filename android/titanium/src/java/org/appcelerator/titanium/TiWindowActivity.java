/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package org.appcelerator.titanium;

import java.util.Stack;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.proxy.TiBaseWindowProxy;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


/**
 * An activity for hosting a stack of Windows.
 */
public class TiWindowActivity extends TiActivity {
	private static final String TAG = "TiWindowActivity";

	private Stack<TiBaseWindowProxy> windowStack = new Stack<TiBaseWindowProxy>();

	public void addWindow(TiBaseWindowProxy proxy) {
		if (windowStack.contains(proxy)) {
			Log.e(TAG, "Error 37! Window already exists in stack");
			return;
		}
		boolean isEmpty = windowStack.empty();
		if (!isEmpty) {
			windowStack.peek().fireEvent(TiC.EVENT_BLUR, null);
		}
		windowStack.add(proxy);
		if (!isEmpty) { 
			proxy.fireEvent(TiC.EVENT_FOCUS, null, false);
		}
	}

	public void removeWindow(TiBaseWindowProxy proxy) {
		proxy.fireEvent(TiC.EVENT_BLUR, null);
		windowStack.remove(proxy);
		if (!windowStack.empty()) {
			TiBaseWindowProxy nextWindow = windowStack.peek();
			nextWindow.fireEvent(TiC.EVENT_FOCUS, null, false);
		}
	}

	public TiBaseWindowProxy getTopWindow() {
		return windowStack.empty() ? null : windowStack.peek();
	}

	// TODO(josh): re-enable this logic in some way.
	private void windowCreated()
	{
		boolean fullscreen = getIntentBoolean(TiC.PROPERTY_FULLSCREEN, false);
		boolean navBarHidden = getIntentBoolean(TiC.PROPERTY_NAV_BAR_HIDDEN, false);
		boolean modal = getIntentBoolean(TiC.PROPERTY_MODAL, false);
		int softInputMode = getIntentInt(TiC.PROPERTY_WINDOW_SOFT_INPUT_MODE, -1);
		boolean hasSoftInputMode = softInputMode != -1;

		if (fullscreen) {
			getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		setNavBarHidden(navBarHidden);

		if (modal) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		}

		if (hasSoftInputMode) {
			if (TiConfig.DEBUG) {
				Log.d(TAG, "windowSoftInputMode: " + softInputMode);
			}

			getWindow().setSoftInputMode(softInputMode);
		}

		boolean useActivityWindow = getIntentBoolean(TiC.INTENT_PROPERTY_USE_ACTIVITY_WINDOW, false);
		if (useActivityWindow) {
			int windowId = getIntentInt(TiC.INTENT_PROPERTY_WINDOW_ID, -1);
			TiActivityWindows.windowCreated(this, windowId);
		}
	}

	private void setNavBarHidden(boolean hidden)
	{
		if (!hidden) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				// Do not enable these features on Honeycomb or later since it will break the action bar.
				this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
				this.requestWindowFeature(Window.FEATURE_RIGHT_ICON);
			}

			this.requestWindowFeature(Window.FEATURE_PROGRESS);
			this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		} else {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!windowStack.empty()) {
			windowStack.peek().fireEvent(TiC.EVENT_FOCUS, null, false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (!windowStack.empty()) {
			windowStack.peek().fireEvent(TiC.EVENT_BLUR, null);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
		boolean handled = false;
		
		TiBaseWindowProxy window = getTopWindow();
		if (window == null) {
			return super.dispatchKeyEvent(event);
		}

		switch(event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK : {
				if (window.hasListeners(TiC.EVENT_ANDROID_BACK)) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						window.fireEvent(TiC.EVENT_ANDROID_BACK, null);
					}
					handled = true;
				}

				break;
			}
			case KeyEvent.KEYCODE_CAMERA : {
				if (window.hasListeners(TiC.EVENT_ANDROID_CAMERA)) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						window.fireEvent(TiC.EVENT_ANDROID_CAMERA, null);
					}
					handled = true;
				}

				break;
			}
			case KeyEvent.KEYCODE_FOCUS : {
				if (window.hasListeners(TiC.EVENT_ANDROID_FOCUS)) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						window.fireEvent(TiC.EVENT_ANDROID_FOCUS, null);
					}
					handled = true;
				}

				break;
			}
			case KeyEvent.KEYCODE_SEARCH : {
				if (window.hasListeners(TiC.EVENT_ANDROID_SEARCH)) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						window.fireEvent(TiC.EVENT_ANDROID_SEARCH, null);
					}
					handled = true;
				}

				break;
			}
			case KeyEvent.KEYCODE_VOLUME_UP : {
				if (window.hasListeners(TiC.EVENT_ANDROID_VOLUP)) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						window.fireEvent(TiC.EVENT_ANDROID_VOLUP, null);
					}
					handled = true;
				}

				break;
			}
			case KeyEvent.KEYCODE_VOLUME_DOWN : {
				if (window.hasListeners(TiC.EVENT_ANDROID_VOLDOWN)) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						window.fireEvent(TiC.EVENT_ANDROID_VOLDOWN, null);
					}
					handled = true;
				}

				break;
			}
		}
			
		if (!handled) {
			handled = super.dispatchKeyEvent(event);
		}

		return handled;
	}
}
