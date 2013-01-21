package org.appcelerator.titanium.view;

import org.appcelerator.titanium.proxy.TiViewProxy;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class TiUIFragment extends TiUIView {
	private static int viewId = 1000;

	private Fragment fragment;

	public TiUIFragment(TiViewProxy proxy, Activity activity) {
		super(proxy);

		TiCompositeLayout container = new TiCompositeLayout(activity, proxy);
		container.setId(viewId++);
		setNativeView(container);

		FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		fragment = createFragment();
		transaction.add(container.getId(), fragment);
		transaction.commit();
	}

	public Fragment getFragment() {
		return fragment;
	}

	protected abstract Fragment createFragment();
}
