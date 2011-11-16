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
	private RadioButton radioBt1;
	private RadioButton radioBt2;
	private RadioButton radioBt5;
	private RadioButton radioBt10;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perimetro);
		final Button btSalvar = (Button) findViewById(R.id.btPerimetro);
		radioBt1 = (RadioButton) findViewById(R.id.perimetro1);
		radioBt2 = (RadioButton) findViewById(R.id.perimetro2);
		radioBt5 = (RadioButton) findViewById(R.id.perimetro5);
		radioBt10 = (RadioButton) findViewById(R.id.perimetro10);
		
		selecionarAtual();

		radioBt1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 1000;
			}
		});

		radioBt2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 2000;
			}
		});

		radioBt5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 5000;
			}
		});

		radioBt10.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				distancia = 10000;
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

	private void selecionarAtual() {
		
		SharedPreferences sharedPreferences = getSharedPreferences("perimetro", MODE_PRIVATE);
		int distanciaAtual = sharedPreferences.getInt("distancia", 1000);
		
		switch (distanciaAtual) {
			case 1000:
				radioBt1.setChecked(true);
				break;
			case 2000:
				radioBt2.setChecked(true);
				break;
			case 5000:
				radioBt5.setChecked(true);
				break;
			case 10000:
				radioBt10.setChecked(true);
				break;
			default:
				break;
		}
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
