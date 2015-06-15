package edu.upc.eetac.dsa.acouceiro.bareando;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import edu.upc.eetac.dsa.acouceiro.bareando.api.Bar;

public class BarAdapter extends BaseAdapter {
    private ArrayList<Bar> data;
    private LayoutInflater inflater;
    private Context mContext;
    private Double miLatitud;
    private Double miLongitud;

    public BarAdapter(Context context, ArrayList<Bar> data, String latitud, String longitud) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        miLatitud = ((Double.parseDouble(latitud))*1000000)/1000000;

        miLongitud = ((Double.parseDouble(longitud))*1000000)/1000000;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Bar) getItem(position)).getBarID();
    }

    private static class ViewHolder {
        TextView tvNombre;
        TextView tvNota;
        ImageView ivImagen;
        TextView tvGenero;
        TextView tvDistancia;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_bares, null);
            viewHolder = new ViewHolder();
            viewHolder.tvNombre = (TextView) convertView
                    .findViewById(R.id.tvNombre);
            viewHolder.tvNota = (TextView) convertView
                    .findViewById(R.id.tvNota);
            viewHolder.tvDistancia = (TextView) convertView
                    .findViewById(R.id.tvDistancia);
            viewHolder.ivImagen = (ImageView) convertView
                    .findViewById(R.id.ivImagen);
            viewHolder.tvGenero = (TextView) convertView
                    .findViewById(R.id.tvGenero);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int BarID = data.get(position).getBarID();
        Picasso.with(mContext).load("http://147.83.7.200:8080/bareando-api/bares/img-" + Integer.toString(BarID)).into(viewHolder.ivImagen);
        String nombre = data.get(position).getNombre();
        double nota = data.get(position).getNota();
        viewHolder.tvNombre.setText(nombre);
        String nota2 = String.valueOf(nota);
        viewHolder.tvNota.setText(nota2);
        String genero = data.get(position).getGenero();
        viewHolder.tvGenero.setText(genero);

        Double latitudBar = data.get(position).getLat();
        Double longitudBar = data.get(position).getLon();

        double miLatitudFinal = (miLatitud*10000/90);
        double miLongitudFinal = (miLongitud*40000/360);
        double latitudBarFinal = (latitudBar*10000/90);
        double longitudBarFinal = (longitudBar*40000/360);

        double diferenciaLatitud = miLatitudFinal - latitudBarFinal;
        double diferenciaLongitud = miLongitudFinal - longitudBarFinal;

        double diferenciaLatitud2 = diferenciaLatitud*diferenciaLatitud;
        double diferenciaLongitud2 = diferenciaLongitud*diferenciaLongitud;

        double suma = diferenciaLatitud2+diferenciaLongitud2;
        double raiz = (Math.sqrt(suma));

        double resultado = Math.rint(raiz*10)/10;

        String distanciaFinal = String.valueOf(resultado) + " km";

        viewHolder.tvDistancia.setText(distanciaFinal);

        return convertView;
    }

}
