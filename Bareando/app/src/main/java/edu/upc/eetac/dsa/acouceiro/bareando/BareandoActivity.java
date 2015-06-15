package edu.upc.eetac.dsa.acouceiro.bareando;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Bar;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BarCollection;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;


public class BareandoActivity extends ListActivity {
    private ArrayList<Bar> baresList;
    private BarAdapter adapter;
    private String nota;
    private String genero;
    private String nick;
    private String rol;
    private String latitud;
    private String longitud;
    private int id;


    private final static String TAG = BareandoActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bareando);

        rol = getIntent().getStringExtra("rol");
        nick = getIntent().getStringExtra("nick");
        latitud = getIntent().getStringExtra("latitud");
        longitud = getIntent().getStringExtra("longitud");

        baresList = new ArrayList<Bar>();
        adapter = new BarAdapter(this, baresList, latitud, longitud);
        setListAdapter(adapter);


        (new FetchBaresTask()).execute();
    }

    private void addBares(BarCollection bares){
        baresList.addAll(bares.getBares());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bareando, menu);
        if(rol.equals("invitado")){
            MenuItem item = menu.findItem(R.id.cerrarsesion);
            item.setTitle("Iniciar sesión");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_Cervezas) {
            genero = null;
            genero = "Cervezas";
            baresList = new ArrayList<Bar>();
            adapter = new BarAdapter(this, baresList, latitud, longitud);
            setListAdapter(adapter);

            (new FetchBaresGeneroTask()).execute();
        }

        if (id == R.id.filter_Copas) {
            genero = null;
            genero = "Cocktails";
            baresList = new ArrayList<Bar>();
            adapter = new BarAdapter(this, baresList, latitud, longitud);
            setListAdapter(adapter);

            (new FetchBaresGeneroTask()).execute();
        }

        if (id == R.id.filter_Vinos) {
            genero = null;
            genero = "Vinos";
            baresList = new ArrayList<Bar>();
            adapter = new BarAdapter(this, baresList, latitud, longitud);
            setListAdapter(adapter);

            (new FetchBaresGeneroTask()).execute();
        }

        if (id == R.id.filter_Tapas) {
            genero = null;
            genero = "tapas";
            baresList = new ArrayList<Bar>();
            adapter = new BarAdapter(this, baresList, latitud, longitud);
            setListAdapter(adapter);

            (new FetchBaresGeneroTask()).execute();
        }

        else if (id == R.id.filter_nota_asc) {
            nota = null;
            nota = "asc";
            baresList = new ArrayList<Bar>();
            adapter = new BarAdapter(this, baresList, latitud, longitud);
            setListAdapter(adapter);

            (new FetchBaresNotaTask()).execute();
        }

        else if (id == R.id.filter_nota_des) {
            nota = null;
            nota = "desc";
            baresList = new ArrayList<Bar>();
            adapter = new BarAdapter(this, baresList, latitud, longitud);
            setListAdapter(adapter);

            (new FetchBaresNotaTask()).execute();
        }

        else if (id == R.id.cerrarsesion) {
            SharedPreferences prefs = getSharedPreferences("bareando-profile",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Bar bar = baresList.get(position);
        Log.d(TAG, rol);
        Log.d(TAG, bar.getLinks().get("get").getTarget());
        id = bar.getBarID();

        Intent intent = new Intent(this, BareandoDetailActivity.class);
        intent.putExtra("url", bar.getLinks().get("get").getTarget());
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nick);

        startActivity(intent);
    }

    private class FetchBaresTask extends AsyncTask<Void, Void, BarCollection> {
        private ProgressDialog pd;

        @Override
        protected BarCollection doInBackground(Void... params) {
            BarCollection bares = null;
            try {
                bares = BareandoAPI.getInstance(BareandoActivity.this)
                        .getBares();
            } catch (AppException e) {
                e.printStackTrace();
            }
            return bares;
        }

        @Override
        protected void onPostExecute(BarCollection result) {
            addBares(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BareandoActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }

    private class FetchBaresNotaTask extends AsyncTask<Void, Void, BarCollection> {
        private ProgressDialog pd;

        @Override
        protected BarCollection doInBackground(Void... params) {
            BarCollection bares = null;
            try {
                bares = BareandoAPI.getInstance(BareandoActivity.this)
                        .getBaresNota(nota);
            } catch (AppException e) {
                e.printStackTrace();
            }
            return bares;
        }

        @Override
        protected void onPostExecute(BarCollection result) {
            addBares(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BareandoActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }

    private class FetchBaresGeneroTask extends AsyncTask<Void, Void, BarCollection> {
        private ProgressDialog pd;

        @Override
        protected BarCollection doInBackground(Void... params) {
            BarCollection bares = null;
            try {
                bares = BareandoAPI.getInstance(BareandoActivity.this)
                        .getBaresGenero(genero);
            } catch (AppException e) {
                e.printStackTrace();
            }
            return bares;
        }

        @Override
        protected void onPostExecute(BarCollection result) {
            addBares(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BareandoActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }
}
