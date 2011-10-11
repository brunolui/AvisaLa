package android.avisala.opcoes;

import android.app.Activity;
import android.app.AlertDialog;
import android.avisala.MenuAvisaLa;
import android.avisala.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Perimetro extends Activity {

	public static final String PERIMETRO = "perimetro";
	private int distancia;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perimetro);
		final Button btSalvar = (Button) findViewById(R.id.btPerimetro);
		final RadioButton radioBt1 = (RadioButton) findViewById(R.id.perimetro1);
		final RadioButton radioBt2 = (RadioButton) findViewById(R.id.perimetro2);
		final RadioButton radioBt5 = (RadioButton) findViewById(R.id.perimetro5);
		final RadioButton radioBt10 = (RadioButton) findViewById(R.id.perimetro10);

		radioBt1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 1;
			}
		});

		radioBt2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 2;
			}
		});

		radioBt5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 5;
			}
		});

		radioBt10.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 10;
			}
		});

		btSalvar.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getSharedPreferences(PERIMETRO, MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("distancia", distancia);
				editor.commit();

				mostrarMensagemDeSucesso();
			}
		});
	}

	protected void mostrarMensagemDeSucesso() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Salvar");
		builder.setMessage("Perímetro salvo com sucesso");
		builder.setPositiveButton("OK", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				voltarParaMenu();
			}
		});
		
		builder.show();
	}

	protected void voltarParaMenu() {
		startActivity(new Intent(this,MenuAvisaLa.class));		
	}

}
