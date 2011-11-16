package android.avisala.alarm;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.avisala.MenuAvisaLa;
import android.avisala.R;
import android.avisala.location.LocationReceiver;
import android.avisala.objetos.Endereco;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

public class AlarmNotifier extends Activity {

	private static final long UM_MINUTO = 60000;
	private MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_notifier);

		findViewById(R.id.btDesligarAlarme).setOnClickListener(
		new View.OnClickListener() {
			public void onClick(View v) {
				terminar();
			}
		});

		mostrarEnderecoFinal();

		try {
			tocar();
			vibrar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarEnderecoFinal() {
		Endereco endereco = getEnderecoFinal();
		TextView textView = (TextView) findViewById(R.id.endereco);
		textView.setText(endereco.getEnderecoCompleto());
	}

	private void terminar() {
		this.finish();
	}
	
	private void tocar() throws IllegalArgumentException, IllegalStateException, IOException {
		mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
		mediaPlayer.start();
	}

	private void vibrar() {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(UM_MINUTO);
	}

	private Endereco getEnderecoFinal() {
		Bundle extras = getIntent().getExtras();
		double latitude = extras.getDouble("latitude");
		double longitude = extras.getDouble("longitude");

		Address address = null;
		try {
			Geocoder geocoder = new Geocoder(this, new Locale("pt", "BR"));
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) { 
				address = addresses.get(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Endereco(address);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.stopAll();

		startActivity(new Intent(getApplicationContext(), MenuAvisaLa.class));
	}

	private void stopAll() {
		stopVibrating();
		stopRinging();
		cancelAlarm();
	}
	
	private void stopRinging() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	private void stopVibrating() {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.cancel();
	}

	private void cancelAlarm() {
		Intent intentToStop = new Intent(this, LocationReceiver.class);
		PendingIntent pendingIntentToStop = PendingIntent.getBroadcast(this, 222, intentToStop, 0);

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(pendingIntentToStop);
		
		this.stopService(intentToStop);
	}
}
