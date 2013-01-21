package ti.modules.titanium.map;

import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;

@Kroll.proxy(creatableInModule=MapModule.class, name="View", propertyAccessors={
	TiC.PROPERTY_USER_LOCATION
})
public class MapViewProxy extends TiViewProxy {

	@Override
	public TiUIView createView(Activity activity) {
		return new TiUIMapView(this, activity);
	}
}
