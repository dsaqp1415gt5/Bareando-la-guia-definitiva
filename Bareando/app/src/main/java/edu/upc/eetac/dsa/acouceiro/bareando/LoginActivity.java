package edu.upc.eetac.dsa.acouceiro.bareando;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BarCollection;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Usuario;

public class LoginActivity extends Activity {
    private final static String TAG = LoginActivity.class.getName();

    private EditText loginUsername;
    private EditText loginPassword;
    private String rol;
    private String nick;
    private String nombre;
    private String password;
    private String longitud;
    private String latitud;
    private CheckBox saveLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        comenzarLocalizacion();

        SharedPreferences prefs = getSharedPreferences("bareando-profile",
                Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        rol = prefs.getString("rol", rol);
        if ((username != null) && (password != null)) {
            this.nombre = username;
            this.password = password;
            (new PostExisteTask()).execute(nombre);
        }
        setContentView(R.layout.login_layout);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLogin);
    }

    private void comenzarLocalizacion()
    {
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Log.d("Ultima localización" , String.valueOf(loc));

        latitud = String.valueOf(loc.getLatitude());
        longitud = String.valueOf(loc.getLongitude());
        Log.d("latitud", String.valueOf(loc.getLatitude()));
        Log.d("longitud", String.valueOf(loc.getLongitude()));

        LocationListener locListener = new MiLocationListener() {
            public void onLocationChanged(Location location) {
                Log.d("Nueva localización", String.valueOf(location));
            }

            public void onProviderDisabled(String provider)
            {
                Toast.makeText(LoginActivity.this, "GPS Desactivado",
                        Toast.LENGTH_SHORT).show();
            }
            public void onProviderEnabled(String provider)
            {

            }
            public void onStatusChanged(String provider, int status, Bundle extras){}
        };

        locManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 15000, 0, locListener);
    }

    public class MiLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc)
        {
            loc.getLatitude();
            loc.getLongitude();

            latitud = String.valueOf(loc.getLatitude());
            longitud = String.valueOf(loc.getLongitude());

            Log.d(TAG, String.valueOf(latitud) + " " + String.valueOf(longitud));
        }
        public void onProviderDisabled(String provider)
        {
            Toast.makeText(LoginActivity.this, "GPS Desactivado",
                    Toast.LENGTH_SHORT).show();
        }
        public void onProviderEnabled(String provider)
        {

        }
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    public void signIn(View v) {
        loginUsername = (EditText) findViewById(R.id.editUsername);
        loginPassword = (EditText) findViewById(R.id.editPassword);

        nombre = loginUsername.getText().toString();
        password = loginPassword.getText().toString();

        if (nombre.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "Debes poner tus datos",
                    Toast.LENGTH_SHORT).show();
        } else {
            (new PostExisteTask()).execute(nombre);
        }
    }

    public void invitado(View v){
        rol = "invitado";
        startBareandoActivity();
    }

    public void registrarse(View v) {
        startRegisterActivity();

    }

    private void startBareandoActivity() {
        Intent intent = new Intent(this, BareandoActivity.class);
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nombre);
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        startActivity(intent);
        finish();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        startActivity(intent);
        finish();
    }

    private class PostLoginTask extends AsyncTask<String, Void, Usuario> {
        private ProgressDialog pd;

        @Override
        protected Usuario doInBackground(String... params) {
            Usuario user = null;
            try {
                user = BareandoAPI.getInstance(LoginActivity.this)
                        .login(params[0], params[1]);
            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return user;
        }

        @Override
        protected void onPostExecute(Usuario result) {
            Log.d(TAG, result.getLoginsuccesful().toString());
            if(result.getLoginsuccesful())
            {
                pd.dismiss();
                if(nombre.equals("makitos666") || nombre.equals("adricouci")){
                    rol = "admin";
                } else {
                    rol = "registrado";
                }
                if (saveLoginCheckBox.isChecked() == true){
                    SharedPreferences prefs = getSharedPreferences("bareando-profile",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.putString("username", nombre);
                    editor.putString("password", password);
                    editor.putString("rol", rol);
                    editor.commit();
                }

                startBareandoActivity();
            }
            else if(!result.getLoginsuccesful())
            {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Contraseña erronea",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "El usuario no existe",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setTitle("Wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    private class PostExisteTask extends AsyncTask<String, Void, Usuario> {
        private ProgressDialog pd;

        @Override
        protected Usuario doInBackground(String... params) {
            Usuario user = null;
            try {
                user = BareandoAPI.getInstance(LoginActivity.this)
                        .existeUser(params[0]);
            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return user;
        }

        @Override
        protected void onPostExecute(Usuario result) {
            if(result.getExiste().equals("true"))
            {
                pd.dismiss();
                (new PostLoginTask()).execute(nombre, password);
            }
            else if(result.getExiste().equals("false"))
            {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "El usuario no existe",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setTitle("Wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }
}


