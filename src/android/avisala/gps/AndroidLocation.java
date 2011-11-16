package android.avisala.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class AndroidLocation extends Activity {

	private LocationManager locationManager;
	private LocationListener locationListener;
	private String bestProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		locationListener = new LocationListenerImpl(getApplicationContext());

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(locationListener);

		bestProvider = locationManager.getBestProvider(createCriteria(), true);
		locationManager.requestLocationUpdates(bestProvider, 0, 50,	locationListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
		if (lastKnownLocation != null) {
			getIntent().putExtra("myLatitude", lastKnownLocation.getLatitude());
			getIntent().putExtra("myLongitude", lastKnownLocation.getLongitude());
			setResult(RESULT_OK, getIntent());
		}
		finish();
	}

	/** Criteria to get the best location provider */
	public static Criteria createCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		return criteria;
	}

	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(bestProvider, 20000, 1, locationListener);
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}
}