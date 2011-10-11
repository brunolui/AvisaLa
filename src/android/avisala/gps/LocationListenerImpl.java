package android.avisala.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class LocationListenerImpl implements LocationListener {
	
	private Context context;
	
	public LocationListenerImpl(Context context) {
		this.context = context;
	}
	
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(context,"GPS Disabled", Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(context,"GPS Enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}