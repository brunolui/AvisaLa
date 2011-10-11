package android.avisala.banco;

import java.util.LinkedList;

import android.avisala.objetos.Destino;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DataHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "android.db";
	private static final String TABLE_NAME = "destino";

	private SQLiteDatabase database;
	private SQLiteStatement insertStmt;

	private static final String INSERT = "insert into " + TABLE_NAME + "(descricao, latitude, longitude) values (?, ?, ?)";

	public DataHelper(Context context) {
      	SQLiteHelper helper = new SQLiteHelper(context, DATABASE_NAME, DATABASE_VERSION);
      	this.database = helper.getWritableDatabase();
      	this.insertStmt = this.database.compileStatement(INSERT);
	}

	public long insert(final String nome, final double latitude, final double longitude) {
		this.insertStmt.bindString(1, nome);
		this.insertStmt.bindDouble(2, latitude);
		this.insertStmt.bindDouble(3, longitude);
		return this.insertStmt.executeInsert();
	}

	public void delete(int id) {
		this.database.delete(TABLE_NAME, "_id = " + id, null);
	}
	
	public void deleteAll() {
		this.database.delete(TABLE_NAME, null, null);
	}

	public LinkedList<Destino> selectAll() {
		LinkedList<Destino> destinos = new LinkedList<Destino>();
		
		Cursor cursor = this.database.query(TABLE_NAME, new String[] { "_id", "descricao", "latitude", "longitude" },null, null, null, null, "descricao asc");
		if (cursor.moveToFirst()) {
    	  do {
    		  Destino destino = new Destino(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
			  destinos.add(destino);
        	 
     		} while (cursor.moveToNext());
      	}
      	if (cursor != null && !cursor.isClosed()) {
    	  cursor.close();
      	}
      	return destinos;
   }
}
