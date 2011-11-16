package android.avisala.destino;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.avisala.R;
import android.avisala.gps.RoadProvider;
import android.avisala.objetos.Rota;
import android.avisala.overlay.MapOverlay;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RotaDestino extends MapActivity {
	
	public static final int ACTUAL_LOCATION_REQUEST = 0;
	private MapView mapa;
	private Rota rota;
	private String urlToMapWebservice;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.rota_destino);
	    mapa = (MapView) findViewById(R.id.mapa);
	    mapa.setBuiltInZoomControls(true);
	       
	    getActualPosition();
	}

	private void getActualPosition() {
		startActivityForResult(new Intent(this,android.avisala.gps.AndroidLocation.class), ACTUAL_LOCATION_REQUEST);
	}
	  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTUAL_LOCATION_REQUEST && resultCode == RESULT_OK) {
			double myLatitude = data.getExtras().getDouble("myLatitude");
	        double myLongitude = data.getExtras().getDouble("myLongitude");
	            
	        buildUrlToMapWebservice(myLatitude, myLongitude);
	        startThread();
	    }
	}
	
	private void buildUrlToMapWebservice(double myLatitude, double myLongitude) {
	    Rota rota = getRota();
	    
	    StringBuffer url = new StringBuffer();
	    url.append("http://maps.google.com/maps?f=d&hl=en");
	    url.append("&saddr=");// from
	    url.append(Double.toString(myLatitude));
	    url.append(",");
	    url.append(Double.toString(myLongitude));
	    url.append("&daddr=");// to
	    url.append(Double.toString(rota.getLatitudeDestino()));
	    url.append(",");
	    url.append(Double.toString(rota.getLongitudeDestino()));
	    url.append("&ie=UTF8&0&om=0&output=kml");
	    urlToMapWebservice = url.toString();
	}

  
	private Rota getRota() {
		Bundle extras = getIntent().getExtras(); 
		if(extras != null) {
			Rota rota = new Rota();
			rota.setLatitudeDestino(extras.getDouble("latitudeDestino"));
			rota.setLongitudeDestino(extras.getDouble("longitudeDestino"));
			return rota;
		}
		return null;
	}
  
  
  
	private void startThread() {
		new Thread() {
			@Override
            public void run() {
                InputStream inputStream = getConnection(urlToMapWebservice);
                rota = RoadProvider.getRoute(inputStream);
                mHandler.sendEmptyMessage(0);
            }
        }.start();    
	}
  
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			TextView textView = (TextView) findViewById(R.id.descricao);
            textView.setText(rota.getNomeDaRota() + "\n" + rota.getDescricaoFormatada());
            
            MapOverlay mapOverlay = new MapOverlay(rota, mapa);
            List<Overlay> listOfOverlays = mapa.getOverlays();
            listOfOverlays.clear();
            listOfOverlays.add(mapOverlay);
            mapa.invalidate();
		};
	};
  
	private InputStream getConnection(String url) {
		InputStream inputStream = null;
        try {
        	URLConnection connection = new URL(url).openConnection();
            inputStream = connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}