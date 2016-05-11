package com.joseblandon.practicasql;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText eNombre, eId,eCantidad,eValor;
    TextView tvInventario;
    private Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eNombre=(EditText) findViewById(R.id.eNombre);
        eCantidad= (EditText) findViewById(R.id.eCantidad);
        eId= (EditText) findViewById(R.id.eId);
        eValor = (EditText) findViewById(R.id.eValor);
        tvInventario = (TextView) findViewById(R.id.tvInventario);
    }

    public void agregar (View view){
        BaseDeDatos peluchitos = new BaseDeDatos(this, "Basededatos1", null, 1);
        SQLiteDatabase bd = peluchitos.getWritableDatabase();

        String nombre = eNombre.getText().toString();
        String id = eId.getText().toString();
        String cantidad = eCantidad.getText().toString();
        String valor = eValor.getText().toString();

        if((nombre.equals(""))||(id.equals(""))||(cantidad.equals(""))||(valor.equals(""))){
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        else {
                    ContentValues registro = new ContentValues();
                    registro.put("id", id);
                    registro.put("nombre", nombre);
                    registro.put("cantidad", cantidad);
                    registro.put("valor", valor);

                    bd.insert("peluchitos", null, registro);
                    bd.close();
                    eNombre.setText("");
                    eId.setText("");
                    eCantidad.setText("");
                    eValor.setText("");
                    Toast.makeText(this, "Se agrego un peluchito", Toast.LENGTH_SHORT).show();
                }
        }

    public void buscar (View view){
        BaseDeDatos peluchitos = new BaseDeDatos(this,"Basededatos1",null,1);
        SQLiteDatabase bd = peluchitos.getWritableDatabase();
        String nombre = eNombre.getText().toString();

        if(nombre.equals("")){
            Toast.makeText(this, "Ingrese el nombre del peluchito que desea buscar", Toast.LENGTH_SHORT).show();
        }
        else {
            c = bd.rawQuery("select id, cantidad, valor from peluchitos where nombre='" + nombre + "'", null);
            if (c.moveToFirst() == true) {
                eId.setText(c.getString(0));
                eCantidad.setText(c.getString(1));
                eValor.setText(c.getString(2));
            } else {
                Toast.makeText(this, "No existe el peluchito", Toast.LENGTH_SHORT).show();
            }
            bd.close();
        }
    }

    public void eliminar (View view){
        BaseDeDatos admin = new BaseDeDatos(this,"Basededatos1",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nombre = eNombre.getText().toString();
        if(nombre.equals("")){
            Toast.makeText(this, "Ingrese el nombre del peluchito que desea eliminar", Toast.LENGTH_SHORT).show();
        }
        else {
            int cant = bd.delete("peluchitos", "nombre='" + nombre + "'", null);
            bd.close();
            eId.setText("");
            eNombre.setText("");
            eCantidad.setText("");
            eValor.setText("");

            if (cant == 1) {
                Toast.makeText(this, "se elimino el peluchito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se elimino el peluchito", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void actualizar (View view){
        BaseDeDatos estudiante = new BaseDeDatos(this,"Basededatos1",null,1);
        SQLiteDatabase bd = estudiante.getWritableDatabase();

        String Nombre = eNombre.getText().toString();
        String Cantidad = eCantidad.getText().toString();
        int resultado=0;

        if((Nombre.equals(""))||(Cantidad.equals(""))){
            Toast.makeText(this, "Ingrese el nombre del peluchito y la cantidad de peluches a ingresar", Toast.LENGTH_SHORT).show();
        }
        else {
            int cantN=Integer.parseInt(eCantidad.getText().toString());
            c = bd.rawQuery("select cantidad from peluchitos where nombre='" + Nombre + "'", null);
            if(c.moveToFirst()==true){
                int cantBD= Integer.parseInt(c.getString(0));
                resultado=cantBD+cantN;
            }

            ContentValues registro = new ContentValues();
            registro.put("nombre", Nombre);
            registro.put("cantidad", resultado);

            int cant = bd.update("peluchitos", registro, "nombre='" + Nombre + "'", null);
            bd.close();
            if (cant == 1) {
                Toast.makeText(this, "se modifico la cantidad de peluches", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "no existe el peluche", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void inventario (View view){
        tvInventario.setText("");
        BaseDeDatos peluchitos = new BaseDeDatos(this, "Basededatos1", null, 1);
        SQLiteDatabase bd = peluchitos.getWritableDatabase();

        c = bd.rawQuery("select nombre, id, cantidad, valor from peluchitos",null);
        if(c.moveToFirst()){
            do{
                tvInventario.append(" "+c.getString(0)+" -- "+c.getString(1)+" -- "+c.getString(2)+" -- "+c.getString(3)+"\n");
            }while(c.moveToNext());
        }
        bd.close();
    }

    public void venta(View view) {
        Intent i = new Intent(this, Venta.class );
        startActivity(i);
    }

    public void ganancias (View view){
        tvInventario.setText("");
        int acumulador=0,totales=0;
        BaseDeDatos venta = new BaseDeDatos(this, "Basededatos1", null, 1);
        SQLiteDatabase bd = venta.getWritableDatabase();

        c = bd.rawQuery("select total from ventas",null);
        while(c.moveToNext()){
                totales= Integer.parseInt(c.getString(0));
                acumulador=acumulador+totales;
            }
        tvInventario.setText("Ganancias Totales: \n" +acumulador );
        bd.close();
    }

    public void inicializar(View view){
        BaseDeDatos peluchitos = new BaseDeDatos(this, "Basededatos1", null, 1);
        SQLiteDatabase bd = peluchitos.getWritableDatabase();
        ContentValues registro = new ContentValues();

        String[] peluches={"Iron_Man","Viuda_Negra","Capitan_America","Hulk","Bruja_Escarlta","Spider_Man"};
        String[] valor={"15000","12000","15000","12000","15000","10000"};

        for (int i=0;i<6;i++){
            registro.put("id", String.valueOf(i+1));
            registro.put("nombre", peluches[i]);
            registro.put("cantidad", "10");
            registro.put("valor", valor[i]);
            bd.insert("peluchitos",null,registro);
        }
        bd.close();
        Toast.makeText(this, "La base de datos se ha inicializado",Toast.LENGTH_SHORT).show();
    }

}