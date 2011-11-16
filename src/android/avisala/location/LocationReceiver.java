package android.avisala.location;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.avisala.R;
import android.avisala.alarm.AlarmNotifier;
import android.avisala.destino.RotaDestino;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.commonsware.cwac.locpoll.LocationPoller;

public class LocationReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1;
	private double latitudeDestino;
	private double longitudeDestino;
	private int distanciaMinimaParaAlertar;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		latitudeDestino = bundle.getDouble("latitudeDestino");
		longitudeDestino = bundle.getDouble("longitudeDestino");
		distanciaMinimaParaAlertar = bundle.getInt("distanciaMinima");
		
		Location localizacaoAtual = (Location) bundle.get(LocationManager.KEY_LOCATION_CHANGED);
		
		String menssage;
		if (localizacaoAtual == null) {
			localizacaoAtual = (Location) bundle.get(LocationPoller.EXTRA_LASTKNOWN);

			if (localizacaoAtual == null) {
				menssage = intent.getStringExtra(LocationPoller.EXTRA_ERROR);
			} else {
				menssage = "Ocorreu um erro.";
				cancelarAlarme(context);
			}
			Toast.makeText(context, menssage , Toast.LENGTH_LONG).show();
			
		} else {
			
			double distancia = calcularDistanciaAteDestino(context, localizacaoAtual);
			
			if (distancia <= distanciaMinimaParaAlertar) {
				this.dispararAlarme(context);			
			} else {
				this.notificar(context, distancia);
			}
		}
	}

	private double calcularDistanciaAteDestino(Context context, Location localizacaoAtual) {
		float[] resultado = new float[1];
		Location.distanceBetween(localizacaoAtual.getLatitude(), localizacaoAtual.getLongitude(), latitudeDestino, longitudeDestino, resultado);
		double distanciaEmMetros = resultado[0];
		return distanciaEmMetros;
	}

	private void notificar(Context context, double distancia) {
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
		decimalFormat.setMinimumFractionDigits(3);
		decimalFormat.setMaximumFractionDigits(3);
		
		String notificacao = "Distância até o destino: " + decimalFormat.format((distancia/1000)) + " Km";
		PendingIntent contentIntent = prepararIntentDaNotificacao(context);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.avisala, notificacao, System.currentTimeMillis());
		notification.setLatestEventInfo(context, "AvisaLá", notificacao, contentIntent);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	private PendingIntent prepararIntentDaNotificacao(Context context) {
		Intent intent = new Intent(context, RotaDestino.class);
		intent.putExtra("latitudeDestino", latitudeDestino);
		intent.putExtra("longitudeDestino", longitudeDestino);
		return PendingIntent.getActivity(context, 0, intent, 0);
	}
	
	private void mostrarRota(Context context, Location localizacaoAtual) {
		Intent intent = new Intent(context, RotaDestino.class);
		intent.putExtra("latitudeOrigem", localizacaoAtual.getLatitude());
		intent.putExtra("longitudeOrigem", localizacaoAtual.getLongitude());
		intent.putExtra("latitudeDestino", latitudeDestino);
		intent.putExtra("longitudeDestino", longitudeDestino);
		
		intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);		
	}

	private void dispararAlarme(Context context) {
		Intent notifierIntent = new Intent(context, AlarmNotifier.class);
		notifierIntent.putExtra("latitude", latitudeDestino);
		notifierIntent.putExtra("longitude", longitudeDestino);

		notifierIntent.addFlags(Intent.FLAG_FROM_BACKGROUND); 
		notifierIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
		notifierIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(notifierIntent);
	}
	
	private void cancelarAlarme(Context context) {
		Intent intentToStop = new Intent(context, LocationPoller.class);
		PendingIntent pendingIntentToStop = PendingIntent.getBroadcast(context, 222, intentToStop, 0);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntentToStop);
	}
	
}