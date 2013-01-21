package ti.modules.titanium.map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIFragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class TiUIMapView extends TiUIFragment {
	public TiUIMapView(TiViewProxy proxy, Activity activity) {
		super(proxy, activity);
	}

	@Override
	protected Fragment createFragment() {
		return SupportMapFragment.newInstance();
	}

	@Override
	public void processProperties(KrollDict d) {
		super.processProperties(d);
		if (d.optBoolean(TiC.PROPERTY_USER_LOCATION, false)) {
			setUserLocation(true);
		}
	}

	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy) {
		super.propertyChanged(key, oldValue, newValue, proxy);
		if (key == TiC.PROPERTY_USER_LOCATION) {
			setUserLocation(TiConvert.toBoolean(newValue));
		}
	}

	private GoogleMap getMap() {
		return ((SupportMapFragment) getFragment()).getMap();
	}

	public void setUserLocation(boolean enabled) {
		getMap().setMyLocationEnabled(enabled);
	}
}
