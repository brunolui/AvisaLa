package android.avisala.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Implementacao de SQLiteOpenHelper
 * Classe utilitária para abrir, criar, e atualizar o banco de dados
 */
class SQLiteHelper extends SQLiteOpenHelper {

	private static final String SCRIPT_DELETE = "DROP TABLE IF EXISTS destino";
	private static final String SCRIPT_CREATE = "create table destino ( _id integer primary key autoincrement, descricao text not null, latitude double not null, longitude double not null)";

	SQLiteHelper(Context context, String nomeBanco, int versaoBanco) {
		super(context, nomeBanco, null, versaoBanco);
	}
	
	@Override
	public void onCreate(SQLiteDatabase datebase) {
		datebase.execSQL(SCRIPT_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL(SCRIPT_DELETE);
		onCreate(database);
	}
	
}