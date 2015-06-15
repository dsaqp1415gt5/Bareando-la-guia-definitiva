package edu.upc.eetac.dsa.acouceiro.bareando;


import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;

public class UbicacionBarActivity extends Activity implements OnMapReadyCallback {

    private String genero;
    private String nombre;
    private Double BarLatitud;
    private Double BarLongitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubicacion_layout);

        genero = getIntent().getStringExtra("genero");
        nombre = getIntent().getStringExtra("nombre");
        String BarLatitudString = getIntent().getStringExtra("barlat");
        String BarLongitudString = getIntent().getStringExtra("barlon");
        BarLatitud = Double.parseDouble(BarLatitudString);
        BarLongitud = Double.parseDouble(BarLongitudString);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng Barcelona = new LatLng(BarLatitud, BarLongitud);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Barcelona, 16));

        map.addMarker(new MarkerOptions()
                .title(nombre)
                .snippet(genero)
                .position(Barcelona));
    }
}