package com.example.comunidadconectada;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private String currentUid = "";

    private DatabaseReference mChatRef;
    private ChildEventListener chatEventListener;

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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUid = currentUser.getUid();
        }

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

        mChatRef = FirebaseDatabase.getInstance().getReference("chat_general");
        escucharMensajesFirebase();

        btnEnviar.setOnClickListener(v -> enviarMensaje());
    }

    private void escucharMensajesFirebase() {
        chatEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage message = snapshot.getValue(ChatMessage.class);
                if (message != null) {
                    if (message.getSenderUid() != null && !currentUid.isEmpty()) {
                        message.setEsMio(message.getSenderUid().equals(currentUid));
                    }
                    messageList.add(message);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    rvMessages.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        mChatRef.addChildEventListener(chatEventListener);
    }

    private void enviarMensaje() {
        String texto = etInputMensaje.getText() != null ? etInputMensaje.getText().toString().trim() : "";
        if (!TextUtils.isEmpty(texto)) {
            String fechaHora = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(new Date());
            ChatMessage nuevoMensaje = new ChatMessage(currentUid, nombreUsuario, viviendaUsuario, texto, fechaHora);
            mChatRef.push().setValue(nuevoMensaje);
            etInputMensaje.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatRef != null && chatEventListener != null) {
            mChatRef.removeEventListener(chatEventListener);
        }
    }
}
