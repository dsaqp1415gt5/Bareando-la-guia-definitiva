package edu.upc.eetac.dsa.acouceiro.bareando;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.upc.eetac.dsa.acouceiro.bareando.api.Bar;
import edu.upc.eetac.dsa.acouceiro.bareando.api.Comentario;

public class ComentarioAdapter extends BaseAdapter {

    private ArrayList<Comentario> data;
    private LayoutInflater inflater;
    private Context mContext;

    public ComentarioAdapter(Context context, ArrayList<Comentario> data) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
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
        return ((Comentario) getItem(position)).getComentarioID();
    }

    private static class ViewHolder {
        TextView tvComentarioNick;
        TextView tvComentarioMensaje;
        ImageView ivImagenUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comentario_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tvComentarioMensaje = (TextView) convertView
                    .findViewById(R.id.tvComentarioMensaje);
            viewHolder.tvComentarioNick = (TextView) convertView
                    .findViewById(R.id.tvComentarioNick);
            viewHolder.ivImagenUser = (ImageView) convertView
                    .findViewById(R.id.ivImagenUser);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String nick = data.get(position).getNick();
        String mensaje = data.get(position).getMensaje();
        viewHolder.tvComentarioNick.setText(nick);
        viewHolder.tvComentarioMensaje.setText(mensaje);

        Picasso.with(mContext).load("http://147.83.7.200:8080/bareando-api/bares/usr-" + nick).into(viewHolder.ivImagenUser);
        return convertView;
    }
}
