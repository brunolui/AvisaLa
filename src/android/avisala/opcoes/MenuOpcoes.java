package android.avisala.opcoes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuOpcoes extends ListActivity{
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		String[] mStrings = new String[] {"Perimetro", "Voltar"};

		this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mStrings));
	}
	
    protected void onListItemClick(ListView l, View v, int position, long id) {
		
		switch (position) {
			case 0:
				startActivity(new Intent(this, Perimetro.class));
				break;
			default:
				finish();
		}
	}

}
