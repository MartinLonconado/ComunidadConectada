package com.example.comunidadconectada;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {

    private final List<ChatMessage> messageList;

    public ChatMessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.tvNombre.setText(message.getNombre());
        holder.tvVivienda.setText(holder.itemView.getContext().getString(R.string.fmt_vivienda, message.getVivienda()));
        holder.tvTexto.setText(message.getMensaje());
        holder.tvFechaHora.setText(message.getFechaHora());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre;
        final TextView tvVivienda;
        final TextView tvTexto;
        final TextView tvFechaHora;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_mensaje_nombre);
            tvVivienda = itemView.findViewById(R.id.tv_mensaje_vivienda);
            tvTexto = itemView.findViewById(R.id.tv_mensaje_texto);
            tvFechaHora = itemView.findViewById(R.id.tv_mensaje_fecha_hora);
        }
    }
}
