package com.example.cavalaro.aula3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {
    ArrayList<Item> data = new ArrayList<Item>();
    ListView lv;

    public class Item{
        public String TvId, TvNome, TvDia, TvProfessor;
        Item(String tv_id, String tv_nome, String tv_dia,
             String tv_professor){
            this.TvId=tv_id;
            this.TvNome=tv_nome;
            this.TvDia=tv_dia;
            this.TvProfessor=tv_professor;
        }
    }
    public class MinhaViewHolder{
        TextView TvId, TvNome, TvDia, TvProfessor;
        ImageButton btEditar;
        MinhaViewHolder(View v){
            TvId =(TextView)v.findViewById(R.id.tvId);
            TvNome =(TextView)v.findViewById(R.id.tvNome);
            TvDia =(TextView)v.findViewById(R.id.tvDia);
            TvProfessor =(TextView)v.findViewById(R.id.tvProfessor);
            btEditar = (ImageButton)v.findViewById(R.id.btEditar);
        }
    }
    public class MeuAdapter extends BaseAdapter {
        Context c;
        MeuAdapter(Context context){
            this.c=context;
        }
        public int getCount(){
            return data.size();
        }
        public Object getItem(int position){
            return data.get(position);
        }
        public long getItemId(int position){
            return position;
        }
        public View getView(final int position,View convertView,
                            ViewGroup parent){
            View row = convertView;
            MinhaViewHolder holder =null;
            if(row==null){
                LayoutInflater inflater = (LayoutInflater)
                        c.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.lista_item,
                        parent,false);
                holder = new MinhaViewHolder(row);
                row.setTag(holder);
            }else{
                holder = (MinhaViewHolder)row.getTag();
            }
            holder.TvId.setText(data.get(position).TvId);
            holder.TvNome.setText(data.get(position).TvNome);
            holder.TvDia.setText(data.get(position).TvDia);
            holder.TvProfessor.setText(data.get(position).TvProfessor);
            holder.btEditar.setOnClickListener(
                    new View.OnClickListener(){
                        public void onClick(View v){
                            Intent int2= new Intent(
                                    getApplicationContext(),Editar.class);
                            int2.putExtra("ID",data.get(position).TvId);
                            int2.putExtra("NOME",data.get(position).TvNome);
                            int2.putExtra("DIA",data.get(position).TvDia);
                            int2.putExtra("PROFESSOR",data.get(position).TvProfessor);
                            int2.putExtra("EDITAR","1");
                            startActivity(int2);
                            finish();
                        }
                    }
            );
            return row;
        }
    }

    protected void onStart(){
        super.onStart();
        String sql ="select * from materia";
        sql = sql.replace(" ","%20");
        new JsonTask().execute(
                "http://dan.pe.hu/cavalaro/servidorJSON.php?sql="+
                        sql+"&usuario=felipe&senha=felipe");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        lv=(ListView)findViewById(R.id.listview);
//        executar();
    }
    public class JsonTask extends AsyncTask<String,String,String> {
        HttpURLConnection c;
        BufferedReader reader;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListaActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Carregando...");
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
            progressDialog.dismiss();
            try{
                JSONObject jsonObject= new JSONObject(result);
                JSONArray jsonArray=new JSONArray(
                        jsonObject.getString("dados"));
                if(data.size()>0) data.clear();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject json_data =
                            jsonArray.getJSONObject(i);
                    Item temp = new Item(
                            Integer.toString(json_data.getInt("id")),
                            json_data.getString("nome"),
                            json_data.getString("dia"),
                            json_data.getString("professor")
                    );
                    data.add(temp);
                }
                MeuAdapter adapter=new MeuAdapter(ListaActivity.this);
                lv.setAdapter(adapter);
            }catch(Exception e){
                Log.i("JSON",e.getLocalizedMessage());
            }
        }
    }
    public void adicionar(View v){
        Intent int1 = new Intent(getApplicationContext(),
                Editar.class);
        int1.putExtra("EDITAR","0");
        startActivity(int1);
        finish();
    }
}
