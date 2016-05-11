package com.joseblandon.practicasql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jose on 04/05/2016.
 */
public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(Context context,String name,SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table peluchitos(nombre text primary key , id integer, cantidad integer, valor integer)");
        db.execSQL("create table ventas(id integer primary key autoincrement , nombre text , cantidad integer , total integer )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists peluchitos");
        db.execSQL("create table peluchitos(nombre text primary key , id integer, cantidad integer, valor integer)");
        db.execSQL("create table ventas(id integer primary key autoincrement , nombre text , cantidad integer , total integer )");
    }
}
