package android.avisala.destino;

import java.util.List;

import android.app.AlertDialog;
import android.avisala.R;
import android.avisala.banco.DataHelper;
import android.avisala.objetos.Coordenada;
import android.avisala.objetos.CoordenadaInicial;
import android.avisala.objetos.Endereco;
import android.avisala.overlay.PointOverlay;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class BuscaDestino extends MapActivity implements OnClickListener {

	private MapController controlador;
	private MapView mapa;
	private Coordenada coordenadaAtual; 

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.busca_destino);

		final Button botaoBuscarEndereco = (Button) findViewById(R.id.btBuscarEndereco);
		botaoBuscarEndereco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	buscarEndereco();
            }
        });
		
		final Button botaoSalvarDestino = (Button) findViewById(R.id.btSalvarDestino);
		botaoSalvarDestino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	salvarEndereco();
            }
        });
		
		configurarMapa();
	}

	private void configurarMapa() {
		mapa = (MapView) findViewById(R.id.mapa);

		Coordenada coordenada = getCoordenada();
		PointOverlay pointOverlay = new PointOverlay(coordenada, R.drawable.ponto);

		EditText editText =(EditText) findViewById(R.id.endereco);
		editText.setText(coordenada.getEndereco());
		
		controlador = mapa.getController();
		controlador.setZoom(15);
		controlador.animateTo(coordenada);
		
		mapa.getOverlays().add(pointOverlay);
		mapa.setStreetView(true);
		mapa.setClickable(true);
		mapa.setBuiltInZoomControls(true);
		mapa.invalidate();
	}

	private Coordenada getCoordenada() {
		Bundle extras = getIntent().getExtras(); 
		if(extras != null) {
			double latitude = extras.getDouble("latitude");
			double longitude = extras.getDouble("longitude");
			String endereco = extras.getString("endereco");
			
			return new Coordenada(latitude, longitude, endereco);
		}
		return new CoordenadaInicial();
	}

	protected void salvarEndereco() {
		
		DataHelper dataHelper = new DataHelper(this);
		EditText textEndereco = (EditText) findViewById(R.id.endereco);
		String endereco = textEndereco.getText().toString();
		dataHelper.insert(endereco, coordenadaAtual.getLatitude(), coordenadaAtual.getLongitude());
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Salvar destino");
		builder.setMessage("Destino salvo com sucesso");
		builder.setPositiveButton("OK", null);
		builder.show();
	}

	protected void buscarEndereco() {
		EditText textEndereco = (EditText) findViewById(R.id.endereco);
		String endereco = textEndereco.getText().toString();
		final List<Endereco> enderecosEncontrados = Endereco.buscar(this, endereco);
		
		if (enderecosEncontrados != null && enderecosEncontrados.size() > 0) {
			final String[] enderecos = new String[enderecosEncontrados.size()];
			int i = 0;
			for (Endereco e : enderecosEncontrados) {
				enderecos[i++] = e.getEnderecoCompleto();
			}
	
			final Context contexto = this;
			AlertDialog create = new AlertDialog.Builder(contexto)
	        .setTitle("Endreço")
	        .setItems(enderecos, new DialogInterface.OnClickListener() {
	        	
	            public void onClick(DialogInterface dialog, int id) {
					Endereco endereco = enderecosEncontrados.get(id);
					coordenadaAtual = new Coordenada(endereco);
					
					PointOverlay pointOverlay = new PointOverlay(coordenadaAtual, R.drawable.ponto);
					controlador.animateTo(coordenadaAtual);
					
					EditText editText =(EditText) findViewById(R.id.endereco);
					editText.setText(endereco.getEnderecoCompleto());
					
					mapa.getOverlays().add(pointOverlay);
					mapa.invalidate();
	            }
	        })
	        .create();
			create.show();
		}
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			// Satelite
			mapa.setSatellite(true);
			mapa.setStreetView(false);
			mapa.setTraffic(false);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_R) {
			// Rua
			mapa.setStreetView(true);
			mapa.setSatellite(false);
			mapa.setTraffic(false);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			// Traffic
			mapa.setTraffic(true);
			mapa.setStreetView(false);
			mapa.setSatellite(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Satelite");
		menu.add(0, 1, 1, "Rua");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// Clicou no menu
		switch (item.getItemId()) {
		case 0:
			// Satelite
			mapa.setSatellite(true);
			mapa.setStreetView(false);
			mapa.setTraffic(false);
			break;
		case 1:
			// Rua
			mapa.setStreetView(true);
			mapa.setSatellite(false);
			mapa.setTraffic(false);
			break;
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
	}

}