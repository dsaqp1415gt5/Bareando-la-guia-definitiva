package edu.upc.eetac.dsa.acouceiro.bareando;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.Header;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import edu.upc.eetac.dsa.acouceiro.bareando.api.AppException;
import edu.upc.eetac.dsa.acouceiro.bareando.api.BareandoAPI;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Usuario;

public class CapturarImagen extends Activity {
    private static final int PICK_FROM_CAMERA = 1;
    ImageView picture;
    private String rol;
    private String nick;
    private String ExternalURL;
    private String confirmacion;
    private String latitud;
    private String longitud;
    private Bitmap photo;
    Context context;
    URL url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capturar_imagen_layout);
        context = this;
        rol = getIntent().getStringExtra("rol");
        nick = getIntent().getStringExtra("nick");
        latitud = getIntent().getStringExtra("latitud");
        longitud = getIntent().getStringExtra("longitud");
        confirmacion = "no";
        ExternalURL = "http://147.83.7.200:8080/bareando-api/foto/upload-" + nick;


        picture = (ImageView) findViewById(R.id.picture);
        Button buttonCamera = (Button) findViewById(R.id.capturarfoto);
        buttonCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {

                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);

                } catch (ActivityNotFoundException e) {
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_CAMERA) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                photo = extras.getParcelable("data");
                picture.setImageBitmap(photo);

                confirmacion = "ok";

            }
        }
    }

    public void enviarfoto(View v){
        if(confirmacion.equals("ok")) {
             try {
                url = new URL(ExternalURL);
            } catch (MalformedURLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }

            (new ImageUploader()).execute();
        }
        else if (confirmacion.equals("no")){
            Toast.makeText(CapturarImagen.this, "Debes hacerte una foto!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startBareandoActivity() {
        Intent intent = new Intent(this, BareandoActivity.class);
        intent.putExtra("rol", rol);
        intent.putExtra("nick", nick);
        intent.putExtra("latitud", latitud);
        intent.putExtra("longitud", longitud);
        startActivity(intent);
        finish();
    }

    public void mastarde(View v){
        Toast.makeText(CapturarImagen.this, "Recuerda subir tu foto des de la web!",
                Toast.LENGTH_SHORT).show();
        startBareandoActivity();

    }

    private class ImageUploader extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            String filename = nick + ".png";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 60, bos);
            Log.d("File:  ", bos.toString());
            byte[] imageBytes = bos.toByteArray();

            File outputDir = context.getCacheDir(); // context being the Activity pointer
            File outputFile = new File(filename);
            try {
                outputFile = File.createTempFile("prefix", "extension", outputDir);
            }catch (IOException e){

            }

            try {

                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.write(imageBytes);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.d("Fallo:   ", "Relleno fichero");
            }
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPostRequest = new HttpPost(ExternalURL);
            try {
                FileBody bin = new FileBody(outputFile);

                MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
                multiPartEntityBuilder.addPart("file", bin);
                httpPostRequest.setEntity(multiPartEntityBuilder.build());
                HttpResponse httpResponse = null;
                httpResponse = httpClient.execute(httpPostRequest);
                InputStream inputStream = null;
                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
                return result;
            } catch (Exception e) {

                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CapturarImagen.this, "Subiendo...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(CapturarImagen.this, "Foto subida con éxito!",
                    Toast.LENGTH_SHORT).show();
            startBareandoActivity();
        }

    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}