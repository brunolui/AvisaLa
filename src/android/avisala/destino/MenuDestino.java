package android.avisala.destino;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.avisala.banco.DataHelper;
import android.avisala.location.AlarmLocation;
import android.avisala.objetos.Destino;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuDestino extends ListActivity {

	DataHelper dataHelper = null;
	LinkedList<Destino> destinos = null;
	Destino destino = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		dataHelper = new DataHelper(this);
		destinos = dataHelper.selectAll();

		List<String> menuItens = new ArrayList<String>();
		menuItens.add("Incluir destino");

		for (Destino destino : destinos) {
			menuItens.add(destino.getEndereco());
		}
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItens));
	}

	protected void onListItemClick(ListView l, View v, final int posicaoItem, long id) {

		if (posicaoItem == 0) {
			incluirDestino();
		} else {
			int posicaoDoDestino = posicaoItem - 1;
			destino = destinos.get(posicaoDoDestino);
			gerarMenuModal();
		}
	}
	
	private void incluirDestino() {
		Intent intent = new Intent(this, android.avisala.destino.BuscaDestino.class);
		startActivity(intent);
	}
	
	private void gerarMenuModal() {
		CharSequence[] opcoesDestino = new CharSequence[] {"Ir até este destino", "Calcular rota até este destino", "Ativar alarme", "Remover este destino" };

		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Destino")
		.setItems(opcoesDestino, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				switch (id) {
				case 0:
					irAteDestino();
					break;
				case 1:
					criarRotaAteDestino();
					break;
				case 2:
					ativarAlarme();
					break;
				case 3:
					removerDestino();
					break;
				}
			}
		}).create();

		dialog.show();
	}

	protected void ativarAlarme() {
		Intent intent = new Intent(this, AlarmLocation.class);
		intent.putExtra("latitudeDestino", destino.getLatitude());
		intent.putExtra("longitudeDestino", destino.getLongitude());
		startActivity(intent);
	}

	protected void criarRotaAteDestino() {
		Intent intent = new Intent(this, android.avisala.destino.RotaDestino.class);
		intent.putExtra("latitudeDestino", destino.getLatitude());
		intent.putExtra("longitudeDestino", destino.getLongitude());
		startActivity(intent);		
	}

	protected void irAteDestino() {
		Intent intent = new Intent(this, android.avisala.destino.BuscaDestino.class);
		intent.putExtra("endereco", destino.getEndereco());
		intent.putExtra("latitude", destino.getLatitude());
		intent.putExtra("longitude", destino.getLongitude());
		startActivity(intent);						
	}

	protected void removerDestino() {
		dataHelper.delete(destino.getId());
		reloadDestinos();
	}
	
	protected void reloadDestinos() {
		startActivity(new Intent(this,android.avisala.destino.MenuDestino.class));		
	}

}