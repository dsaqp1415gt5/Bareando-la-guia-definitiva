package edu.upc.eetac.dsa.acouceiro.bareando;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Usuario;

public class RegisterActivity extends Activity {
    private final static String TAG = RegisterActivity.class.getName();

    private EditText inputFullName;
    private EditText inputNickName;
    private EditText inputEmail;
    private EditText inputPassword;
    private String rol;

    private String name;
    private String nick;
    private String email;
    private String password;
    private String latitud;
    private String longitud;

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "registro()");
        setContentView(R.layout.register_layout);
    }

    public void registro(View view) {
        inputFullName = (EditText) findViewById(R.id.tvUsername);
        inputNickName = (EditText) findViewById(R.id.tvNickName);
        inputEmail = (EditText) findViewById(R.id.tvEmail);
        inputPassword = (EditText) findViewById(R.id.tvPass);

        name = inputFullName.getText().toString();
        nick = inputNickName.getText().toString();
        email = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        latitud = getIntent().getStringExtra("latitud");
        longitud = getIntent().getStringExtra("longitud");

        if(name.equals("") || nick.equals("") || email.equals("") || password.equals("") ){
            Toast.makeText(RegisterActivity.this, "Debes darnos todos los datos",
                    Toast.LENGTH_SHORT).show();
        } else {
            (new PostExisteTask()).execute(nick);
        }

    }
    private class PostExisteTask extends AsyncTask<String, Void, Usuario> {
        private ProgressDialog pd;

        @Override
        protected Usuario doInBackground(String... params) {
            Usuario user = null;
            try {
                user = BareandoAPI.getInstance(RegisterActivity.this)
                        .existeUser(params[0]);
            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return user;
        }

        @Override
        protected void onPostExecute(Usuario result) {
            if(result.getExiste().equals("false"))
            {
                pd.dismiss();
                if (validateEmail(email)){
                    (new PostRegisterTask()).execute(name, nick, password, email);
                } else {
                    Toast.makeText(RegisterActivity.this, "La dirección de correo no es valida",
                            Toast.LENGTH_SHORT).show();
                }

            }
            else if(result.getExiste().equals("true"))
            {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Prueba otro nick, este ya existe",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(RegisterActivity.this);
            pd.setTitle("Wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }
    private void startCapturarActivity() {
        Intent intent = new Intent(this, CapturarImagen.class);
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nick);
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        startActivity(intent);
        finish();
    }

    private class PostRegisterTask extends AsyncTask<String, Void, Usuario> {
        private ProgressDialog pd;

        @Override
        protected Usuario doInBackground(String... params) {
            Usuario user = null;
            try {
                user = BareandoAPI.getInstance(RegisterActivity.this)
                        .registerUser(params[0], params[1], params[2], params[3]);
            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return user;
        }

        @Override
        protected void onPostExecute(Usuario result) {
            if(result.getNombre().equals("null") || result.getNick().equals("null") || result.getMail().equals("null"))
            {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Error!",
                        Toast.LENGTH_SHORT).show();
            }
            else if(result.getSuccessful())
            {
                pd.dismiss();
                rol = "registrado";
                startCapturarActivity();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(RegisterActivity.this);
            pd.setTitle("Wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    public void cancel(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public static boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

}