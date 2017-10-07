package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SQLitehelper extends SQLiteOpenHelper {
 
	 private static final String DATABASE_NAME = "DBList";
	private static final int DATABASE_VERSION = 1;
 
    public SQLitehelper(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL("CREATE TABLE maindb (id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, checked INTEGER, priority INTEGER, task VARCHAR, position INTEGER, child INTEGER)");
    	db.execSQL("CREATE TABLE subdb (id INTEGER PRIMARY KEY AUTOINCREMENT, idref INTEGER, type INTEGER, checked INTEGER, priority INTEGER, task VARCHAR, position INTEGER, child INTEGER)");
    	
    /*	db.execSQL("CREATE TABLE pfolio (name VARCHAR(20), currency VARCHAR(20))");
    	db.execSQL("CREATE TABLE connect (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(20), idshare INTEGER)");
    	db.execSQL("CREATE TABLE share (id INTEGER PRIMARY KEY AUTOINCREMENT, tick VARCHAR(5), fullname TEXT, shares INT, value REAL)");*/
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
    	
    }
}