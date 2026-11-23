package com.example.comunidadconectada;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.AnuncioViewHolder> {

    private final List<Anuncio> listaAnuncios;

    public AnunciosAdapter(List<Anuncio> listaAnuncios) {
        this.listaAnuncios = listaAnuncios;
    }

    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new AnuncioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder holder, int position) {
        Anuncio anuncio = listaAnuncios.get(position);
        if (anuncio == null) return;
        Context context = holder.itemView.getContext();

        holder.tvTitulo.setText(anuncio.getTitulo() != null ? anuncio.getTitulo() : "");
        holder.tvContenido.setText(anuncio.getContenido() != null ? anuncio.getContenido() : "");
        String publicadoPor = anuncio.getPublicadoPor() != null ? anuncio.getPublicadoPor() : "Administración";
        String pubText = context.getString(R.string.label_publicado_por) + " " + publicadoPor;
        holder.tvPublicadoPor.setText(pubText);
        holder.tvFecha.setText(anuncio.getFecha() != null ? anuncio.getFecha() : "");
    }

    @Override
    public int getItemCount() {
        return listaAnuncios.size();
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitulo;
        final TextView tvContenido;
        final TextView tvPublicadoPor;
        final TextView tvFecha;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_anuncio_titulo);
            tvContenido = itemView.findViewById(R.id.tv_anuncio_contenido);
            tvPublicadoPor = itemView.findViewById(R.id.tv_anuncio_publicado_por);
            tvFecha = itemView.findViewById(R.id.tv_anuncio_fecha);
        }
    }
}
