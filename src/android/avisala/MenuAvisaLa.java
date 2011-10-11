package android.avisala;

import android.app.ListActivity;
import android.avisala.destino.MenuDestino;
import android.avisala.opcoes.MenuOpcoes;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/*
 * Menu Principal AvisaLá
 */
public class MenuAvisaLa extends ListActivity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		String[] mStrings = new String[] {"Destinos", "Opções", "Sair"};

		this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mStrings));
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		switch (position) {
			case 0:
				startActivity(new Intent(this,MenuDestino.class));
				break;
			case 1:
				startActivity(new Intent(this,MenuOpcoes.class));
				break;
			default:
				finish();
		}
	}

}


