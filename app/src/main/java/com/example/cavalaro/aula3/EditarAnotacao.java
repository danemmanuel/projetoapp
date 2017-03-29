package com.example.cavalaro.aula3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditarAnotacao extends AppCompatActivity {

    EditText edId,edTitulo,edMateria,edConteudo;
    Button btAlterar,btExcluir,btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_anotacao);

        edId=(EditText)findViewById(R.id.tvId);
        edTitulo=(EditText)findViewById(R.id.edTitulo);
        edMateria=(EditText)findViewById(R.id.edMateria);
        edConteudo=(EditText)findViewById(R.id.edConteudo);
        btAlterar=(Button)findViewById(R.id.btAlterar);
        btExcluir=(Button)findViewById(R.id.btExcluir);
        btAdd=(Button)findViewById(R.id.btAdd);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(Integer.valueOf(bundle.getString("EDITAR"))==1){
                edId.setText(bundle.getString("ID"));
                edTitulo.setText(bundle.getString("TITULO"));
                edMateria.setText(bundle.getString("MATERIA"));
                edConteudo.setText(bundle.getString("CONTEUDO"));
                btAdd.setVisibility(View.INVISIBLE);
                btAlterar.setVisibility(View.VISIBLE);
                btExcluir.setVisibility(View.VISIBLE);
            }else{
                edId.setText("");
                edTitulo.setText("");
                edMateria.setText("");
                edConteudo.setText("");
                btAdd.setVisibility(View.VISIBLE);
                btAlterar.setVisibility(View.INVISIBLE);
                btExcluir.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class JsonTask extends AsyncTask<String,String,String> {
        HttpURLConnection c;
        BufferedReader reader;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditarAnotacao.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Enviando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            try{
                URL u = new URL(strings[0]);
                c=(HttpURLConnection) u.openConnection();
                c.connect();
                InputStream stream = c.getInputStream();
                reader = new BufferedReader(
                        new InputStreamReader(stream));
                StringBuffer sb= new StringBuffer();
                String line="";
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }catch (Exception e){
                Log.i("JSON",e.getLocalizedMessage());
            }finally {
                if(c !=null) c.disconnect();
                try{
                    if (reader !=null) reader.close();
                }catch (IOException e){
                    Log.i("JSON",e.getLocalizedMessage());
                }
            }
            return null;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try{
                progressDialog.dismiss();
                JSONObject jsonObject= new JSONObject(result);
                if(jsonObject.getString("codigo")=="1"){
                    Toast.makeText(getApplicationContext(),
                            "Dados enviados com sucesso!",
                            Toast.LENGTH_SHORT).show();
                    Intent int2 = new Intent(getApplicationContext(),
                            anotacao.class);
                    startActivity(int2);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Erro ao enviar os dados!",
                            Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Log.i("JSON",e.getLocalizedMessage());
            }
        }
    }

    public void add(View v){
        String sql="insert into anotacao(titulo,materia,conteudo)"+
                " values('"+edTitulo.getText().toString()+"',"+
                "'"+edMateria.getText().toString()+"',"+
                "'"+edConteudo.getText().toString()+"')";
        sql=sql.replace(" ","%20");
        new EditarAnotacao.JsonTask().execute(
                "http://dan.pe.hu/cavalaro/servidorJSON.php?sql="+
                        sql+"&usuario=felipe&senha=felipe");

    }

    public void excluir(View v){
        String sql="delete from anotacao where id="+
                "'"+edId.getText().toString()+"'";
        sql=sql.replace(" ","%20");
        new EditarAnotacao.JsonTask().execute(
                "http://dan.pe.hu/cavalaro/servidorJSON.php?sql="+
                        sql+"&usuario=felipe&senha=felipe");

    }
    public void alterar(View v){
        String sql="update anotacao set titulo="+
                "'"+edTitulo.getText().toString()+"'"+
                ",materia='"+edMateria.getText().toString()+"'"+
                ",conteudo='"+edConteudo.getText().toString()+"'"+
                " where id='"+edId.getText().toString()+"'";
        sql=sql.replace(" ","%20");
        new EditarAnotacao.JsonTask().execute(
                "http://dan.pe.hu/cavalaro/servidorJSON.php?sql="+
                        sql+"&usuario=felipe&senha=felipe");

    }

    public void cancelar(View v){
        Intent int3 = new Intent(getApplicationContext(),
                anotacao.class);
        startActivity(int3);
        finish();
    }




}
