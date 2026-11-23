package com.example.comunidadconectada;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private final List<ChatMessage> messageList = new ArrayList<>();
    private ChatMessageAdapter adapter;
    private RecyclerView rvMessages;
    private TextInputEditText etInputMensaje;

    private String nombreUsuario = "Juan Pérez";
    private String viviendaUsuario = "Depto. 302";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_chat), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().hasExtra("NOMBRE_RESIDENTE")) {
            String nombre = getIntent().getStringExtra("NOMBRE_RESIDENTE");
            if (nombre != null && !nombre.isEmpty()) {
                nombreUsuario = nombre;
            }
        }
        if (getIntent().hasExtra("NUMERO_VIVIENDA")) {
            String vivienda = getIntent().getStringExtra("NUMERO_VIVIENDA");
            if (vivienda != null && !vivienda.isEmpty()) {
                viviendaUsuario = vivienda;
            }
        }

        rvMessages = findViewById(R.id.rv_chat_messages);
        etInputMensaje = findViewById(R.id.et_input_mensaje);
        MaterialButton btnEnviar = findViewById(R.id.btn_enviar_mensaje);

        adapter = new ChatMessageAdapter(messageList);
        rvMessages.setAdapter(adapter);

        cargarMensajesIniciales();

        btnEnviar.setOnClickListener(v -> enviarMensaje());
    }

    private void cargarMensajesIniciales() {
        messageList.add(new ChatMessage("Carlos Mendoza", "Depto. 101", "¡Buenos días vecinos! ¿Alguien sabe la clave del Wi-Fi de la sala común?", "09:15 - 24/05/2024", false));
        messageList.add(new ChatMessage("Ana Rivas", "Depto. 205", "Hola Carlos, la clave está pegada al lado del router en el primer piso.", "09:18 - 24/05/2024", false));
        messageList.add(new ChatMessage("Roberto Gómez", "Depto. 402", "Recuerden que hoy a las 18:00 hrs es la mantención del ascensor.", "10:02 - 24/05/2024", false));
        adapter.notifyDataSetChanged();
        if (!messageList.isEmpty()) {
            rvMessages.scrollToPosition(messageList.size() - 1);
        }
    }

    private void enviarMensaje() {
        String texto = etInputMensaje.getText() != null ? etInputMensaje.getText().toString().trim() : "";
        if (!TextUtils.isEmpty(texto)) {
            String fechaHora = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(new Date());
            ChatMessage nuevoMensaje = new ChatMessage(nombreUsuario, viviendaUsuario, texto, fechaHora, true);
            messageList.add(nuevoMensaje);
            adapter.notifyItemInserted(messageList.size() - 1);
            rvMessages.smoothScrollToPosition(messageList.size() - 1);
            etInputMensaje.setText("");
        }
    }
}
