package com.example.comunidadconectada;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvNombreResidente = findViewById(R.id.tv_nombre_residente);
        TextView tvNumeroVivienda = findViewById(R.id.tv_numero_vivienda);

        if (getIntent().hasExtra("NOMBRE_RESIDENTE")) {
            String nombre = getIntent().getStringExtra("NOMBRE_RESIDENTE");
            if (nombre != null && !nombre.isEmpty()) {
                tvNombreResidente.setText(nombre);
            }
        }
        if (getIntent().hasExtra("NUMERO_VIVIENDA")) {
            String vivienda = getIntent().getStringExtra("NUMERO_VIVIENDA");
            if (vivienda != null && !vivienda.isEmpty()) {
                tvNumeroVivienda.setText(vivienda);
            }
        }

        MaterialButton btnChatGeneral = findViewById(R.id.btn_chat_general);
        MaterialButton btnAnuncios = findViewById(R.id.btn_anuncios);
        MaterialButton btnProductos = findViewById(R.id.btn_productos);
        MaterialButton btnAlertas = findViewById(R.id.btn_alertas);
        MaterialButton btnActivarAlarma = findViewById(R.id.btn_activar_alarma);
        MaterialButton btnMiPerfil = findViewById(R.id.btn_mi_perfil);

        btnChatGeneral.setOnClickListener(v -> mostrarSeccion(getString(R.string.btn_chat_general)));
        btnAnuncios.setOnClickListener(v -> mostrarSeccion(getString(R.string.btn_anuncios)));
        btnProductos.setOnClickListener(v -> mostrarSeccion(getString(R.string.btn_productos)));
        btnAlertas.setOnClickListener(v -> mostrarSeccion(getString(R.string.btn_alertas)));
        btnMiPerfil.setOnClickListener(v -> mostrarSeccion(getString(R.string.btn_mi_perfil)));

        btnActivarAlarma.setOnClickListener(v -> activarAlarma());
    }

    private void mostrarSeccion(String seccion) {
        Toast.makeText(this, getString(R.string.msg_abriendo_seccion, seccion), Toast.LENGTH_SHORT).show();
    }

    private void activarAlarma() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_activar_alarma)
                .setMessage(R.string.msg_alarma_activada)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
