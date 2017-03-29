package com.example.cavalaro.aula3;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void materia(View v){
        Intent int1= new Intent(this,ListaActivity.class);
        startActivity(int1);
    }

    public void media(View v){
        Intent int2= new Intent(this,media.class);
        startActivity(int2);
    }
    public void anotacoes(View v){
        Intent int3= new Intent(this,anotacao.class);
        startActivity(int3);
    }
}
