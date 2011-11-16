package android.avisala.location;

import android.app.Activity;
import android.app.PendingIntent;
import android.avisala.MenuAvisaLa;
import android.avisala.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AlarmLocation extends Activity {

	private static final int INTERVALO_10_MIN = 5000;//600000;
	private PendingIntent pendingIntent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarme);

		findViewById(R.id.btDesligarAlarme).setOnClickListener(
		new View.OnClickListener() {
			public void onClick(View v) {
				terminar();
			}
		});
		
		Intent receiverIntent = setExtrasParaOReceiver();
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		String bestProvider = locationManager.getBestProvider(createCriteria(), true);
		
		pendingIntent = PendingIntent.getBroadcast(this, 222, receiverIntent, 0);
		locationManager.requestLocationUpdates(bestProvider, INTERVALO_10_MIN, 0, pendingIntent);
		
		Toast.makeText(this, "Alarme ativado!! Sua localização será atualizada a cada 5 minutos", Toast.LENGTH_LONG).show();
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
	
	private void terminar() {
		this.finish();
		
		Intent intentToStop = new Intent(this, LocationReceiver.class);
		
		PendingIntent pendingIntentToStop = PendingIntent.getBroadcast(this, 222, intentToStop, 0);

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(pendingIntentToStop);
		
		this.stopService(intentToStop);
		this.startActivity(new Intent(getApplicationContext(), MenuAvisaLa.class));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}