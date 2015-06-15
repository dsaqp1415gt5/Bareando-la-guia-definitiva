package edu.upc.eetac.dsa.acouceiro.bareando;


import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Bar;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BarCollection;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Comentario;
import edu.upc.eetac.dsa.acouceiro.bareando.api.ComentariosCollection;


public class BareandoDetailActivity extends Activity {
    private final static String TAG = BareandoDetailActivity.class.getName();
    private String url;
    private String rol;
    private int BarID;
    private String nick;
    private String BarIDstring;

    private String genero;
    private String nombre;
    private String BarLatitudString;
    private String BarLongitudString;

    ImageView ivImagenDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_detail_layout);

        url = (String) getIntent().getExtras().get("url");
        (new FetchBarTask()).execute();
        rol = getIntent().getStringExtra("rol");
        nick = getIntent().getStringExtra("nick");
    }

    private void loadBar(Bar bar) {
        BarID = bar.getBarID();
        BarIDstring = Integer.toString(BarID);

        TextView tvNombreDetail = (TextView) findViewById(R.id.tvNombreDetail);
        TextView tvDescripcionDetail = (TextView) findViewById(R.id.tvDescripcionDetail);
        TextView tvNotaDetail = (TextView) findViewById(R.id.tvNotaDetail);
        TextView tvGeneroDetail = (TextView) findViewById(R.id.tvGeneroDetail);
        ivImagenDetail = (ImageView) findViewById(R.id.ivImagenDetail);

        tvNombreDetail.setText(bar.getNombre());
        tvDescripcionDetail.setText(bar.getDescripcion());
        tvNotaDetail.setText(String.valueOf(bar.getNota()));
        tvGeneroDetail.setText(bar.getGenero());

        genero = bar.getGenero();
        nombre = bar.getNombre();
        BarLatitudString = String.valueOf(bar.getLat());
        BarLongitudString = String.valueOf(bar.getLon());

        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute("http://147.83.7.200:8080/bareando-api/bares/img-"+ Integer.toString(BarID));
    }

    private class FetchBarTask extends AsyncTask<Void, Void, Bar> {
        private ProgressDialog pd;

        @Override
        protected Bar doInBackground(Void... params) {
            Bar bar = null;
            try {
                bar = BareandoAPI.getInstance(BareandoDetailActivity.this)
                        .getBar(url);
            } catch (AppException e) {
                Log.d(TAG, e.getMessage(), e);
            }
            return bar;
        }

        @Override
        protected void onPostExecute(Bar result) {
            loadBar(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BareandoDetailActivity.this);
            pd.setTitle("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    private class CargaImagenes extends AsyncTask<String, Void, Bitmap>{

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(BareandoDetailActivity.this);
            pDialog.setMessage("Cargando Imagen");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground" , "Entra en doInBackground");
            String url = params[0];
            Bitmap imagen = descargarImagen(url);
            Log.i("doInBackground" , url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            ivImagenDetail.setImageBitmap(result);
            pDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ubicacion, menu);
        if(rol.equals("invitado")){
            MenuItem item = menu.findItem(R.id.cerrarsesion);
            item.setTitle("Iniciar sesión");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.MostrarUbicacion) {
            startMostrarUbicacionActivity();
        }

        else if (id == R.id.cerrarsesion) {
            SharedPreferences prefs = getSharedPreferences("bareando-profile",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }

    public void cargarComentarios(View v){
        startComentariosActivity();
    }

    private void startComentariosActivity() {
        Intent intent = new Intent(this, ComentariosActivity.class);
        intent.putExtra("BarID", BarIDstring);
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nick);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    private void startMostrarUbicacionActivity() {
        Intent intent = new Intent(this, UbicacionBarActivity.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("genero", genero);
        intent.putExtra("barlat", BarLatitudString);
        intent.putExtra("barlon", BarLongitudString);
        startActivity(intent);
        finish();
    }
}
