package edu.upc.eetac.dsa.acouceiro.bareando;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Bar;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Comentario;
import edu.upc.eetac.dsa.acouceiro.bareando.api.ComentariosCollection;


public class ComentariosActivity extends ListActivity {

    private int BarID;
    private String barid;
    private ArrayList<Comentario> comentariosList;
    private ComentarioAdapter adapter;
    private final static String TAG = BareandoActivity.class.toString();
    private String rol;
    private String nick;
    private String url;
    HttpDelete delete;
    private String comentario;
    private int idcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bareando);
        barid = getIntent().getStringExtra("BarID");
        BarID = Integer.parseInt(barid);
        rol = getIntent().getStringExtra("rol");
        nick = getIntent().getStringExtra("nick");
        url =  getIntent().getStringExtra("url");

        comentariosList = new ArrayList<Comentario>();
        adapter = new ComentarioAdapter(this, comentariosList);
        setListAdapter(adapter);


        (new FetchComentariosTask()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comentario_admin, menu);
        if(rol.equals("invitado")){
            MenuItem item = menu.findItem(R.id.cerrarsesion);
            item.setTitle("Iniciar sesión");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.PostComent) {
            if(rol.equals("admin") || rol.equals("registrado"))
            {
                startEscribircomentario();
            }
            else if(rol.equals("invitado"))
            {
                Toast.makeText(ComentariosActivity.this, "Tienes que estar registrado",
                        Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.Volver) {
            Intent intent = new Intent(this, BareandoDetailActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("rol", rol);
            intent.putExtra("nick", nick);
            startActivity(intent);
            finish();
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

    private void startEscribircomentario() {
        Intent intent = new Intent(this, EscribirComentario.class);
        intent.putExtra("BarID", barid);
        intent.putExtra("nick", nick);
        intent.putExtra("rol", rol);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    private class FetchComentariosTask extends AsyncTask<Void, Void, ComentariosCollection> {
        private ProgressDialog pd;

        @Override
        protected ComentariosCollection doInBackground(Void... params) {
            ComentariosCollection comentarios = null;
            try {
                comentarios = BareandoAPI.getInstance(ComentariosActivity.this)
                        .getComentarios(BarID);
            } catch (AppException e) {
                e.printStackTrace();
            }
            return comentarios;
        }

        @Override
        protected void onPostExecute(ComentariosCollection result) {
            addComentarios(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ComentariosActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    private void addComentarios(ComentariosCollection comentarios){
        comentariosList.addAll(comentarios.getComentarios());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Comentario comment = comentariosList.get(position);
        idcomment = comment.getComentarioID();
        String nombre = comment.getNick();

        if (rol.equals("admin") || nombre.equals(nick)) {
            ConfirmarBorrar();
        }
        else {
            if(rol.equals("invitado")){

            }
            else{
                Toast.makeText(ComentariosActivity.this, "No tienes permitido borrar este comentario",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class DeleteComent extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                delete = new HttpDelete("http://147.83.7.200:8080/bareando-api/comentarios/" + idcomment);
                HttpClient httpClient = new DefaultHttpClient();
                comentario = "";
                HttpResponse response = null;
                Log.d("Fallo:  ", httpClient.toString());
                Log.d("fallo:  ", delete.toString());
                response = httpClient.execute(delete);

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }

            return comentario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(ComentariosActivity.this, "Comentario borrado!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ComentariosActivity.this, ComentariosActivity.class);
            intent.putExtra("BarID", barid);
            intent.putExtra("rol", rol);
            intent.putExtra("nick", nick);
            intent.putExtra("url", url);
            startActivity(intent);
            finish();
        }
    }

    public void ConfirmarBorrar () {
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("Quiere borrar este comentario?")
                .setCancelable(false)
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                (new DeleteComent()).execute();
                            }
                        })

                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Borrar comentario");
        alert.setIcon(R.drawable.basura);
        alert.show();
    }
}
