package com.example.comunidadconectada;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlertasAdapter extends RecyclerView.Adapter<AlertasAdapter.AlertaViewHolder> {

    private final List<Alerta> listaAlertas;

    public AlertasAdapter(List<Alerta> listaAlertas) {
        this.listaAlertas = listaAlertas;
    }

    @NonNull
    @Override
    public AlertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alerta, parent, false);
        return new AlertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertaViewHolder holder, int position) {
        Alerta alerta = listaAlertas.get(position);
        if (alerta == null) return;
        Context context = holder.itemView.getContext();

        holder.tvTitulo.setText(alerta.getTitulo() != null ? alerta.getTitulo() : "");
        holder.tvDescripcion.setText(alerta.getDescripcion() != null ? alerta.getDescripcion() : "");
        String nombreResidente = alerta.getResidente() != null ? alerta.getResidente() : "Vecino";
        String residenteText = context.getString(R.string.label_alerta_reportado_por) + " " + nombreResidente;
        holder.tvResidente.setText(residenteText);
        holder.tvFecha.setText(alerta.getFechaHora() != null ? alerta.getFechaHora() : "");

        int estadoTextRes;
        int bgColorRes;
        int textColorRes;

        EstadoAlerta estado = alerta.getEstado() != null ? alerta.getEstado() : EstadoAlerta.ACTIVA;
        switch (estado) {
            case ACTIVA:
                estadoTextRes = R.string.label_estado_activa;
                bgColorRes = R.color.estado_activa_bg;
                textColorRes = R.color.estado_activa_text;
                break;
            case ATENDIDA:
                estadoTextRes = R.string.label_estado_atendida;
                bgColorRes = R.color.estado_atendida_bg;
                textColorRes = R.color.estado_atendida_text;
                break;
            case CERRADA:
            default:
                estadoTextRes = R.string.label_estado_cerrada;
                bgColorRes = R.color.estado_cerrada_bg;
                textColorRes = R.color.estado_cerrada_text;
                break;
        }

        holder.tvEstado.setText(context.getString(estadoTextRes));
        int bgColor = ContextCompat.getColor(context, bgColorRes);
        int textColor = ContextCompat.getColor(context, textColorRes);
        ViewCompat.setBackgroundTintList(holder.tvEstado, ColorStateList.valueOf(bgColor));
        holder.tvEstado.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return listaAlertas.size();
    }

    public static class AlertaViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitulo;
        final TextView tvDescripcion;
        final TextView tvResidente;
        final TextView tvFecha;
        final TextView tvEstado;

        public AlertaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_alerta_titulo);
            tvDescripcion = itemView.findViewById(R.id.tv_alerta_descripcion);
            tvResidente = itemView.findViewById(R.id.tv_alerta_residente);
            tvFecha = itemView.findViewById(R.id.tv_alerta_fecha);
            tvEstado = itemView.findViewById(R.id.tv_alerta_estado);
        }
    }
}
