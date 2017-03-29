package com.example.cavalaro.aula3;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class media extends AppCompatActivity {

    EditText edN1,edN2,edN3,edPFG;
    Button btCalcular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
    }

    public void calcular(View v){

        edN1=(EditText)findViewById(R.id.edN1);
        edN2=(EditText)findViewById(R.id.edN2);
        edN3=(EditText)findViewById(R.id.edN3);
        edPFG=(EditText)findViewById(R.id.edPFG);

        btCalcular=(Button)findViewById(R.id.btCalcular);

        double n1 = Double.parseDouble(edN1.getText().toString());
        double n2 = Double.parseDouble(edN2.getText().toString());
        double n3 = Double.parseDouble(edN3.getText().toString());
        double pfg = Double.parseDouble(edPFG.getText().toString());
        
        double nota1 = 0;
        double nota2 = 0;
        double nota3 = 0;
        double notanova1, notanova2, pfgnovo;

        if (n3>n2){
            n2=n3;
        }if (n3>n1){
            n1=n3;
        }

        notanova1 = n1*0.45;
        notanova2 = n2*0.45;
        pfgnovo = pfg*0.10;

        double resultado = notanova1+notanova2+pfgnovo;


        AlertDialog.Builder mensagem = new AlertDialog.Builder(media.this);
        mensagem.setTitle("Calculo de sua média");
        mensagem.setMessage("Sua média é="+ resultado);
        mensagem.setNeutralButton("OK", null);
        mensagem.show();


    }
}
