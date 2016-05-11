package com.joseblandon.practicasql;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Venta extends AppCompatActivity {
    EditText nombre, cantidad;
    private Cursor cursorVen,cursorPel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        nombre=(EditText) findViewById(R.id.eNVenta);
        cantidad = (EditText) findViewById(R.id.eNCantidad);

    }

    public void vender(View view){
        int totalCantidad=0,totalVenta=0;
        BaseDeDatos venta = new BaseDeDatos(this, "Basededatos1", null, 1);
        SQLiteDatabase bd = venta.getWritableDatabase();

        String nombrePel = nombre.getText().toString();
        String cantidadPel = cantidad.getText().toString();

        if((nombrePel.equals(""))||(cantidadPel.equals(""))){
            Toast.makeText(this, "Ingrese el nombre del peluchito y la cantidad de peluches a ingresar", Toast.LENGTH_SHORT).show();
        }
        else {

            int cantV=Integer.parseInt(cantidad.getText().toString());
            cursorPel = bd.rawQuery("select cantidad, valor from peluchitos where nombre='" + nombrePel + "'", null);
            if(cursorPel.moveToFirst()==true){
                int cantBD= Integer.parseInt(cursorPel.getString(0));
                int valorPel= Integer.parseInt(cursorPel.getString(1));
                if(cantV<cantBD) {
                    totalCantidad = cantBD - cantV;
                    totalVenta = cantV * valorPel;
                    ContentValues registroVentas = new ContentValues();
                    ContentValues registro = new ContentValues();
                    registro.put("cantidad", totalCantidad);
                    registroVentas.put("nombre", nombrePel);
                    registroVentas.put("cantidad", cantidadPel);
                    registroVentas.put("total", totalVenta);
                    bd.update("peluchitos", registro, "nombre='" + nombrePel + "'", null);
                    bd.insert("ventas", null, registroVentas);
                    Toast.makeText(this, "Gracias por la compra", Toast.LENGTH_SHORT).show();
                    bd.close();
                }
                else{
                    Toast.makeText(this, "No hay la cantidad de peluches para satisfacer la compra", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "No existe el peluchito", Toast.LENGTH_SHORT).show();
            }
            if(totalCantidad <= 5){
                String title,content,ticker;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                title= "Peluchito Agotado";
                content="Se agoto:"+nombrePel;
                ticker="Se agoto un peluchito";

                builder.setContentTitle(title)
                        .setContentText(content)
                        .setTicker(ticker)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentInfo("Dato");

                Intent notIntent = new Intent(Venta.this, MainActivity.class);

                PendingIntent contIntent = PendingIntent.getActivity(Venta.this,0,notIntent,0);
                builder.setContentIntent(contIntent);
                NotificationManager nm = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);

                nm.notify(1,builder.build());
            }
        }
    }
}
