package android.avisala.location;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import com.commonsware.cwac.locpoll.LocationPoller;

public class AlarmLocation extends Activity {

	private static final int INTERVALO_10_MIN = 5000;//600000;
	private PendingIntent pendingIntent = null;
	private AlarmManager alarmManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent receiverIntent = setExtrasParaOReceiver();
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		String bestProvider = locationManager.getBestProvider(createCriteria(), true);
		
		Intent locationPollerIntent = new Intent(this, LocationPoller.class);
		locationPollerIntent.putExtra(LocationPoller.EXTRA_INTENT, receiverIntent);
		locationPollerIntent.putExtra(LocationPoller.EXTRA_PROVIDER, bestProvider);

		pendingIntent = PendingIntent.getBroadcast(this, 222, locationPollerIntent, 0);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INTERVALO_10_MIN, pendingIntent);
		
		Toast.makeText(this, "Alarme ativado!! Sua localização será atualizada a cada 10 minutos", Toast.LENGTH_LONG).show();
	}

	private Intent setExtrasParaOReceiver() {
		Bundle extras = getIntent().getExtras(); 
		
		if(extras != null) {
			Intent receiverIntent = new Intent(this, LocationReceiver.class);
			receiverIntent.putExtra("latitudeDestino", extras.getDouble("latitudeDestino"));
			receiverIntent.putExtra("longitudeDestino", extras.getDouble("longitudeDestino"));
			receiverIntent.putExtra("distanciaMinima", getDistanciaMinimaParaDispararAlarme());
			return receiverIntent;
		}		
		return null;
	}
	
	public void omgPleaseStop(View v) {
		alarmManager.cancel(pendingIntent);
		finish();
	}
	
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
	
	public int getDistanciaMinimaParaDispararAlarme() {
		SharedPreferences sharedPreferences = getSharedPreferences("perimetro", MODE_PRIVATE);
		return sharedPreferences.getInt("distancia", 1000);
	}
}
