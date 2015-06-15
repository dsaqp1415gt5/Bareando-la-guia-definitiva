package edu.upc.eetac.dsa.acouceiro.bareando.api;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BareandoAPI {
    private final static String TAG = BareandoAPI.class.getName();
    private static BareandoAPI instance = null;
    private URL url;
    private Map<String, Bar> baresCache = new HashMap<String, Bar>();

    private BareandoRootAPI rootAPI = null;

    private BareandoAPI(Context context) throws IOException, AppException {
        super();

        AssetManager assetManager = context.getAssets();
        Properties config = new Properties();
        config.load(assetManager.open("config.properties"));
        String urlHome = config.getProperty("bareando.home");
        url = new URL(urlHome);

        Log.d("LINKS", url.toString());
        getRootAPI();
    }

    public final static BareandoAPI getInstance(Context context) throws AppException {
        if (instance == null)
            try {
                instance = new BareandoAPI(context);
            } catch (IOException e) {
                throw new AppException(
                        "Can't load configuration file");
            }
        return instance;
    }

    private void getRootAPI() throws AppException {
        Log.d(TAG, "getRootAPI()");
        rootAPI = new BareandoRootAPI();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, rootAPI.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Bareando API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Bareando Root API");
        }
    }

    public BarCollection getBares() throws AppException {
        Log.d(TAG, "getBares()");
        BarCollection bares = new BarCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("collection").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, bares.getLinks());


            JSONArray jsonBares = jsonObject.getJSONArray("bares");
            for (int i = 0; i < jsonBares.length(); i++) {
                Bar bar = new Bar();
                JSONObject jsonBar = jsonBares.getJSONObject(i);
                bar.setBarID(jsonBar.getInt("ID"));
                bar.setDescripcion(jsonBar.getString("descripcion"));
                bar.setGenero(jsonBar.getString("genero"));
                bar.setNombre(jsonBar.getString("nombre"));
                bar.setLat(Double.parseDouble(jsonBar.getString("lat")));
                bar.setLon(Double.parseDouble(jsonBar.getString("lon")));
                bar.setNota(jsonBar.getDouble("nota"));
                jsonLinks = jsonBar.getJSONArray("links");
                parseLinks(jsonLinks, bar.getLinks());
                bares.getBares().add(bar);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Bareando API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Bareando Root API");
        }

        return bares;
    }

    public BarCollection getBaresGenero(String genero) throws AppException {
        Log.d(TAG, "getBaresGenero()");
        BarCollection bares = new BarCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("genero").getTarget() + genero).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, bares.getLinks());


            JSONArray jsonBares = jsonObject.getJSONArray("bares");
            for (int i = 0; i < jsonBares.length(); i++) {
                Bar bar = new Bar();
                JSONObject jsonBar = jsonBares.getJSONObject(i);
                bar.setBarID(jsonBar.getInt("ID"));
                bar.setDescripcion(jsonBar.getString("descripcion"));
                bar.setGenero(jsonBar.getString("genero"));
                bar.setNombre(jsonBar.getString("nombre"));
                bar.setNota(jsonBar.getDouble("nota"));
                bar.setLat(Double.parseDouble(jsonBar.getString("lat")));
                bar.setLon(Double.parseDouble(jsonBar.getString("lon")));
                jsonLinks = jsonBar.getJSONArray("links");
                parseLinks(jsonLinks, bar.getLinks());
                bares.getBares().add(bar);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Bareando API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Bareando Root API");
        }

        return bares;
    }

    public BarCollection getBaresNota(String nota) throws AppException {
        Log.d(TAG, "getBaresNota()");
        BarCollection bares = new BarCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get(nota).getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, bares.getLinks());


            JSONArray jsonBares = jsonObject.getJSONArray("bares");
            for (int i = 0; i < jsonBares.length(); i++) {
                Bar bar = new Bar();
                JSONObject jsonBar = jsonBares.getJSONObject(i);
                bar.setBarID(jsonBar.getInt("ID"));
                bar.setDescripcion(jsonBar.getString("descripcion"));
                bar.setGenero(jsonBar.getString("genero"));
                bar.setNombre(jsonBar.getString("nombre"));
                bar.setLat(Double.parseDouble(jsonBar.getString("lat")));
                bar.setLon(Double.parseDouble(jsonBar.getString("lon")));
                bar.setNota(jsonBar.getDouble("nota"));
                jsonLinks = jsonBar.getJSONArray("links");
                parseLinks(jsonLinks, bar.getLinks());
                bares.getBares().add(bar);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Bareando API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Bareando Root API");
        }

        return bares;
    }

    public Bar getBar(String urlBar) throws AppException {
        Log.d(TAG, "getBar()");
        Bar bar = new Bar();

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlBar);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(TAG, urlConnection.toString());
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, bar.getLinks());

            Log.d(TAG, "Lo conseguí");

            JSONArray jsonBares = jsonObject.getJSONArray("bares");
            JSONObject jsonBar = jsonBares.getJSONObject(0);
            bar.setBarID(jsonBar.getInt("ID"));
            Log.d(TAG, String.valueOf(bar.getBarID()));
            bar.setDescripcion(jsonBar.getString("descripcion"));
            Log.d(TAG, bar.getDescripcion());
            bar.setGenero(jsonBar.getString("genero"));
            bar.setLat(Double.parseDouble(jsonBar.getString("lat")));
            Log.d(TAG, String.valueOf(bar.getLat()));
            bar.setLon(Double.parseDouble(jsonBar.getString("lon")));
            Log.d(TAG, String.valueOf(bar.getLon()));
            Log.d(TAG, bar.getGenero());
            bar.setNombre(jsonBar.getString("nombre"));
            Log.d(TAG, bar.getNombre());
            bar.setNota(jsonBar.getDouble("nota"));
            Log.d(TAG, String.valueOf(bar.getNota()));
            jsonLinks = jsonBar.getJSONArray("links");
            parseLinks(jsonLinks, bar.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Bareando API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Bareando Root API");
        }

        return bar;
    }

    private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
            throws AppException, JSONException {
        for (int i = 0; i < jsonLinks.length(); i++) {
            Link link = null;
            try {
                link = SimpleLinkHeaderParser
                        .parseLink(jsonLinks.getString(i));
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
            String rel = link.getParameters().get("rel");
            String rels[] = rel.split("\\s");
            for (String s : rels)
                map.put(s, link);
        }
    }

    public Usuario login(String nick, String pass) throws AppException {
        Usuario user = new Usuario();
        user.setNick(nick);
        user.setPass(pass);
        HttpURLConnection urlConnection = null;
        try {
            JSONObject jsonUser = createJsonUser(user);
            URL urlRegisterUser = new URL(rootAPI.getLinks().get("login")
                    .getTarget());
            Log.d(TAG, urlRegisterUser.toString());
            urlConnection = (HttpURLConnection) urlRegisterUser.openConnection();
            urlConnection.setRequestProperty("Accept",
                    MediaType.BAREANDO_API_USUARIO);
            urlConnection.setRequestProperty("Content-Type",
                    MediaType.BAREANDO_API_USUARIO);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            PrintWriter writer = new PrintWriter(
                urlConnection.getOutputStream());
            writer.println(jsonUser.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            jsonUser = new JSONObject(sb.toString());
            user.setLoginsuccesful(jsonUser.getBoolean("loginSuccessful"));
            user.setNick(jsonUser.getString("nick"));

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error parsing response");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error getting response");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return user;
    }

    public Usuario registerUser(String nombre, String nick, String pass, String mail) throws AppException {
        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setNick(nick);
        user.setPass(pass);
        user.setMail(mail);
        HttpURLConnection urlConnection = null;
        try {
            JSONObject jsonUser = createJsonUser(user);
            URL urlRegisterUser = new URL(rootAPI.getLinks().get("registrar")
                    .getTarget());
            Log.d(TAG, urlRegisterUser.toString());
            urlConnection = (HttpURLConnection) urlRegisterUser.openConnection();
            urlConnection.setRequestProperty("Accept",
                    MediaType.BAREANDO_API_USUARIO);
            urlConnection.setRequestProperty("Content-Type",
                    MediaType.BAREANDO_API_USUARIO);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            PrintWriter writer = new PrintWriter(
                    urlConnection.getOutputStream());
            writer.println(jsonUser.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            jsonUser = new JSONObject(sb.toString());

            user.setSuccessful(jsonUser.getBoolean("loginSuccessful"));
            user.setMail(jsonUser.getString("mail"));
            user.setNick(jsonUser.getString("nick"));
            user.setNombre(jsonUser.getString("nombre"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error parsing response");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error getting response");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return user;
    }

    public Usuario existeUser(String nick) throws AppException {
        Usuario user = new Usuario();
        user.setNick(nick);
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("registrar").getTarget() + "/" + nick).openConnection();
            Log.d(TAG, urlConnection.toString());
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            user.setExiste(sb.toString());
            Log.d(TAG, sb.toString());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error getting response");
        }

        return user;
    }

    public Comentario postComentario(String nick, String BarID, String mensaje) throws AppException {
        Comentario comentario = new Comentario();
        comentario.setNick(nick);
        comentario.setBarID(Integer.valueOf(BarID));
        comentario.setMensaje(mensaje);
        HttpURLConnection urlConnection = null;
        try {
            JSONObject jsonComentario = createJsonComentario(comentario);
            URL urlRegisterUser = new URL(rootAPI.getLinks().get("comentarios")
                    .getTarget());
            Log.d(TAG, urlRegisterUser.toString());
            urlConnection = (HttpURLConnection) urlRegisterUser.openConnection();
            urlConnection.setRequestProperty("Accept",
                    MediaType.BAREANDO_API_COMENTARIO);
            urlConnection.setRequestProperty("Content-Type",
                    MediaType.BAREANDO_API_COMENTARIO);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            PrintWriter writer = new PrintWriter(
                    urlConnection.getOutputStream());
            writer.println(jsonComentario.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            jsonComentario = new JSONObject(sb.toString());
            comentario.setMensaje(jsonComentario.getString("mensaje"));
            Log.d(TAG, jsonComentario.getString("mensaje"));
            comentario.setNick(jsonComentario.getString("nick"));
            Log.d(TAG, jsonComentario.getString("nick"));
            comentario.setBarID(jsonComentario.getInt("id_bar"));
            Log.d(TAG, Integer.toString(jsonComentario.getInt("id_bar")));

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error parsing response");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error getting response");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return comentario;
    }


    public ComentariosCollection getComentarios(int id) throws AppException {
        Log.d(TAG, "getComentarios()");
        ComentariosCollection comentarios = new ComentariosCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("comentarios").getTarget() + "/" + Integer.toString(id)).openConnection();
            Log.d(TAG, urlConnection.toString());
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            throw new AppException(
                    "Can't connect to Bareando API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());


            JSONArray jsonComentarios = jsonObject.getJSONArray("comentarios");
            for (int i = 0; i < jsonComentarios.length(); i++) {
                Comentario comentario = new Comentario();
                JSONObject jsonComentario = jsonComentarios.getJSONObject(i);
                comentario.setComentarioID(jsonComentario.getInt("id"));
                comentario.setNick(jsonComentario.getString("nick"));
                comentario.setMensaje(jsonComentario.getString("mensaje"));
                comentario.setBarID(jsonComentario.getInt("id_bar"));
                comentarios.getComentarios().add(comentario);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Bareando API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Bareando Root API");
        }

        return comentarios;
    }

    private JSONObject createJsonUser(Usuario user) throws JSONException {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("nombre", user.getNombre());
        jsonUser.put("nick", user.getNick());
        jsonUser.put("pass", user.getPass());
        jsonUser.put("mail", user.getMail());

        return jsonUser;
    }

    private JSONObject createJsonComentario(Comentario comentario) throws JSONException {
        JSONObject jsonComentario = new JSONObject();
        jsonComentario.put("nick", comentario.getNick());
        jsonComentario.put("id_bar", Integer.toString(comentario.getBarID()));
        jsonComentario.put("mensaje", comentario.getMensaje());

        return jsonComentario;
    }



    public String deleteComentario(String id) throws AppException {
        HttpClient httpClient = new DefaultHttpClient();
        String comentario = "";

        HttpDelete del =
                new HttpDelete("http://147:83:7:200:8080/bareando-api/comentarios/" + id);

        del.setHeader("content-type", "application/json");

        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());

            if(respStr.equals("true"))
                comentario = "ok";
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
        }

        return comentario;
    }
}
