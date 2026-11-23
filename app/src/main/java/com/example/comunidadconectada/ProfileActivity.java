package com.example.comunidadconectada;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvNombreUsuario;
    private TextView tvNumeroVivienda;
    private TextView tvCorreoUsuario;
    private TextView tvRolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNombreUsuario = findViewById(R.id.tv_nombre_usuario);
        tvNumeroVivienda = findViewById(R.id.tv_numero_vivienda);
        tvCorreoUsuario = findViewById(R.id.tv_correo_usuario);
        tvRolUsuario = findViewById(R.id.tv_rol_usuario);

        if (getIntent().hasExtra("NOMBRE_RESIDENTE")) {
            String nombre = getIntent().getStringExtra("NOMBRE_RESIDENTE");
            if (nombre != null && !nombre.isEmpty()) {
                tvNombreUsuario.setText(nombre);
            }
        }
        if (getIntent().hasExtra("NUMERO_VIVIENDA")) {
            String vivienda = getIntent().getStringExtra("NUMERO_VIVIENDA");
            if (vivienda != null && !vivienda.isEmpty()) {
                tvNumeroVivienda.setText(vivienda);
            }
        }
        if (getIntent().hasExtra("CORREO_USUARIO")) {
            String correo = getIntent().getStringExtra("CORREO_USUARIO");
            if (correo != null && !correo.isEmpty()) {
                tvCorreoUsuario.setText(correo);
            }
        }
        if (getIntent().hasExtra("ROL_USUARIO")) {
            String rol = getIntent().getStringExtra("ROL_USUARIO");
            if (rol != null && !rol.isEmpty()) {
                tvRolUsuario.setText(rol);
            }
        }

        MaterialButton btnEditarNombre = findViewById(R.id.btn_editar_nombre);
        btnEditarNombre.setOnClickListener(v -> mostrarDialogoEditarNombre());
    }

    private void mostrarDialogoEditarNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_editar_nombre_titulo);

        final EditText input = new EditText(this);
        input.setHint(R.string.dialog_editar_nombre_hint);
        input.setText(tvNombreUsuario.getText().toString());
        input.setSelection(input.getText().length());

        FrameLayout container = new FrameLayout(this);
        int paddingPx = (int) (20 * getResources().getDisplayMetrics().density);
        container.setPadding(paddingPx, 10, paddingPx, 0);
        container.addView(input);
        builder.setView(container);

        builder.setPositiveButton(R.string.btn_guardar, (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();
            if (!TextUtils.isEmpty(nuevoNombre)) {
                tvNombreUsuario.setText(nuevoNombre);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseDatabase.getInstance().getReference("usuarios")
                            .child(user.getUid())
                            .child("nombre")
                            .setValue(nuevoNombre);
                }
                Toast.makeText(ProfileActivity.this, R.string.msg_nombre_actualizado, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.btn_cancelar, (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
