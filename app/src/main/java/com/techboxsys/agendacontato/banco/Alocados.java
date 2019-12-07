package com.techboxsys.agendacontato.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import  com.techboxsys.agendacontato.modelo.Contatos;

import java.util.ArrayList;
import java.util.List;

public class Alocados extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "contatos.db";

    private static final String TABLE_CONTATO = "contato";

    private static final String COLUMN_TC_ID = "id";
    private static final String COLUMN_TC_NOME = "str_nome";
    private static final String COLUMN_TC_NUMERO = "str_numero";
    private static final String COLUMN_TC_APELIDO = "str_apelido";
    private static final String COLUMN_TC_ALT = "str_alt";

    private final String CREATE_COMPUTER_TB =
        "CREATE TABLE " +
            TABLE_CONTATO + "(" +
            COLUMN_TC_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TC_NOME + " TEXT, " +
            COLUMN_TC_APELIDO + " TEXT, " +
            COLUMN_TC_NUMERO + " TEXT, " +
            COLUMN_TC_ALT + " TEXT " + ")";

    public Alocados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COMPUTER_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTATO);
        onCreate(db);
    }

    public void adiconar(Contatos contato) {
        String status = "N";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TC_NOME, contato.getNome());
        values.put(COLUMN_TC_NUMERO, contato.getNumero());
        values.put(COLUMN_TC_APELIDO, contato.getApelido());
        values.put(COLUMN_TC_ALT, status);

        database.insert(TABLE_CONTATO, null, values);

    }

    public Contatos obterContato(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.query(TABLE_CONTATO,
            new String[]{COLUMN_TC_ID, COLUMN_TC_NOME, COLUMN_TC_APELIDO, COLUMN_TC_NUMERO},
            COLUMN_TC_ID + " = ?",
            new String[] {String.valueOf(id)}, // "" + id
            null, null, null);

        Contatos contato = null;

        if (c != null) {
            c.moveToFirst();

            contato = new Contatos(c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getString(3)
            );
        }

        c.close();

        return contato;
    }

    public int removerContato(Contatos contato){
        String status = "D";
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TC_ALT, status);

        int res = database.update(TABLE_CONTATO,
                values,
                COLUMN_TC_ID + " = ?",
                new String[] {"" + contato.getId()});

        Log.i("res", "removerContato: " + res);

        return res;
    }

    public void deleteTodos(){
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        database.delete(TABLE_CONTATO, null, null);
    }

    public void restore(){
        String status = "D";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        database.delete(TABLE_CONTATO, COLUMN_TC_ALT + " =? ", new String[] {status});
        status = "";
        values.put(COLUMN_TC_ALT, status);

        database.update(TABLE_CONTATO,
                values,
                null,
                null);
    }

    public List<Contatos> obterTodosContatos() {
        String status = "D";
        SQLiteDatabase database = this.getReadableDatabase();
        String queryAll = "SELECT * FROM " + TABLE_CONTATO + " WHERE " + COLUMN_TC_ALT + " != '" + status + "'";
        Cursor c = database.rawQuery(queryAll, null);

        List<Contatos> listaContato = new ArrayList<>();

        if(c != null && c.moveToFirst()) {
            do {
                Contatos contato = new Contatos(c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
                );

                listaContato.add(contato);
            } while(c.moveToNext());
        }

        c.close();
        Log.i("Lista", "obterTodosContatos: " + listaContato);

        return listaContato;
    }

    public List<Contatos> obterContatosTipo(String status) {
        SQLiteDatabase database = this.getReadableDatabase();
        String queryAll = "SELECT * FROM " + TABLE_CONTATO + " WHERE " + COLUMN_TC_ALT + " = '" + status + "'";
        Cursor c = database.rawQuery(queryAll, null);

        List<Contatos> listaContato = new ArrayList<>();

        if(c != null && c.moveToFirst()) {
            do {
                Contatos contato = new Contatos(c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)
                );

                listaContato.add(contato);
            } while(c.moveToNext());
        }

        c.close();

        return listaContato;
    }

    public int atualizarContato(Contatos contato) {
        String status = "U";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TC_NOME, contato.getNome());
        values.put(COLUMN_TC_NUMERO, contato.getNumero());
        values.put(COLUMN_TC_APELIDO, contato.getApelido());
        values.put(COLUMN_TC_ALT, status);

        return database.update(TABLE_CONTATO,
            values,
            COLUMN_TC_ID + " = ?",
            new String[] {"" + contato.getId()});
    }

    public int obterQuantidadeContatos() {
        SQLiteDatabase database = this.getReadableDatabase();
        String queryAll = "SELECT * FROM " + TABLE_CONTATO;
        Cursor c = database.rawQuery(queryAll, null);
        c.close();

        return c.getCount();
    }
}
