package edu.upc.eetac.dsa.acouceiro.bareando;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Comentario;

public class EscribirComentario extends Activity {
    private final static String TAG = EscribirComentario.class.getName();
    private String nick;
    private String BarID;
    private String rol;
    private String url;

    private class PostComentarioTask extends AsyncTask<String, Void, Comentario> {
        private ProgressDialog pd;

        @Override
        protected Comentario doInBackground(String... params) {
            Comentario comentario = null;
            try {
                comentario = BareandoAPI.getInstance(EscribirComentario.this)
                        .postComentario(params[0], params[1], params[2]);

            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return comentario;
        }

        @Override
        protected void onPostExecute(Comentario result) {
            if(result == null){
                Toast.makeText(EscribirComentario.this, "Ui... Parece que ha habido un error",
                        Toast.LENGTH_SHORT).show();
            } else {
                startComentariosActivity();
            }
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(EscribirComentario.this);
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comentario_layout);

        nick = getIntent().getStringExtra("nick");
        BarID = getIntent().getStringExtra("BarID");
        rol = getIntent().getStringExtra("rol");
        url =  getIntent().getStringExtra("url");

    }

    public void cancel(View v) {
        Intent intent = new Intent(this, ComentariosActivity.class);
        intent.putExtra("BarID", BarID);
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nick);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    public void postSting(View v) {
        EditText etComentario = (EditText) findViewById(R.id.etComentario);

        String comentario = etComentario.getText().toString();
        if (comentario.equals("")){
            Toast.makeText(EscribirComentario.this, "Debes escribir un comentario",
                    Toast.LENGTH_SHORT).show();
        }else{
            (new PostComentarioTask()).execute(nick, BarID, comentario);
        }

    }

    private void startComentariosActivity() {
        Intent intent = new Intent(this, ComentariosActivity.class);
        intent.putExtra("BarID", BarID);
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nick);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }
}
