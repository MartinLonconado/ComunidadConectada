package com.example.comunidadconectada;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView tvNombreResidente;
    private TextView tvNumeroVivienda;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Usuario usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Iniciar conexión MQTT de fondo
        MqttManager.getInstance().connect(this, null, null, null, null);

        // Suscribirse a notificaciones push FCM de anuncios y alertas
        FirebaseMessaging.getInstance().subscribeToTopic("anuncios");
        FirebaseMessaging.getInstance().subscribeToTopic("alertas");

        // Solicitar permiso de notificaciones en Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNombreResidente = findViewById(R.id.tv_nombre_residente);
        tvNumeroVivienda = findViewById(R.id.tv_numero_vivienda);

        cargarDatosUsuario();

        MaterialButton btnChatGeneral = findViewById(R.id.btn_chat_general);
        MaterialButton btnAnuncios = findViewById(R.id.btn_anuncios);
        MaterialButton btnAlertas = findViewById(R.id.btn_alertas);
        MaterialButton btnActivarAlarma = findViewById(R.id.btn_activar_alarma);
        MaterialButton btnMiPerfil = findViewById(R.id.btn_mi_perfil);
        MaterialButton btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);

        btnChatGeneral.setOnClickListener(v -> abrirChat());
        btnAnuncios.setOnClickListener(v -> abrirAnuncios());
        btnAlertas.setOnClickListener(v -> abrirAlertas());
        btnActivarAlarma.setOnClickListener(v -> activarAlarma());

        btnMiPerfil.setOnClickListener(v -> abrirPerfil());
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarDatosUsuario() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usuarioActual = snapshot.getValue(Usuario.class);
                    if (usuarioActual != null) {
                        if (usuarioActual.getNombre() != null && !usuarioActual.getNombre().isEmpty()) {
                            tvNombreResidente.setText(usuarioActual.getNombre());
                        }
                        if (usuarioActual.getDepartamento() != null && !usuarioActual.getDepartamento().isEmpty()) {
                            tvNumeroVivienda.setText(usuarioActual.getDepartamento());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void abrirChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("NOMBRE_RESIDENTE", tvNombreResidente.getText().toString());
        intent.putExtra("NUMERO_VIVIENDA", tvNumeroVivienda.getText().toString());
        startActivity(intent);
    }

    private void abrirAnuncios() {
        Intent intent = new Intent(this, AnunciosActivity.class);
        startActivity(intent);
    }

    private void abrirAlertas() {
        Intent intent = new Intent(this, AlertasActivity.class);
        startActivity(intent);
    }

    private void activarAlarma() {
        String residente = tvNombreResidente.getText().toString();
        String vivienda = tvNumeroVivienda.getText().toString();
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // 1. Enviar evento a broker MQTT para sirenas/hardware IoT
        MqttManager.getInstance().publishAlarma(residente, vivienda, new MqttManager.OnPublishCompleteListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Alarma transmitida por MQTT", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(Exception e) {
            }
        });

        // 2. Registrar la alerta en Firebase Realtime Database
        String idAlerta = mDatabase.child("alertas").push().getKey();
        Alerta alerta = new Alerta(
                idAlerta,
                "Alarma de Emergencia (Pánico)",
                "Activación de botón de pánico por residente.",
                residente + " (" + vivienda + ")",
                fechaHora,
                EstadoAlerta.ACTIVA
        );
        if (idAlerta != null) {
            mDatabase.child("alertas").child(idAlerta).setValue(alerta);
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_activar_alarma)
                .setMessage(R.string.msg_alarma_activada)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void abrirPerfil() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("NOMBRE_RESIDENTE", tvNombreResidente.getText().toString());
        intent.putExtra("NUMERO_VIVIENDA", tvNumeroVivienda.getText().toString());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            intent.putExtra("CORREO_USUARIO", currentUser.getEmail());
        }
        if (usuarioActual != null && usuarioActual.getRol() != null) {
            intent.putExtra("ROL_USUARIO", usuarioActual.getRol());
        }
        startActivity(intent);
    }

    private void cerrarSesion() {
        mAuth.signOut();
        MqttManager.getInstance().disconnect();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
