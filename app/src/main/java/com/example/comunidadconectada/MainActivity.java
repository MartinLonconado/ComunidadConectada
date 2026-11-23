package com.example.comunidadconectada;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        MaterialButton btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        MaterialButton btnRegistrarse = findViewById(R.id.btn_registrarse);
        MaterialButton btnRecuperarPassword = findViewById(R.id.btn_recuperar_password);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());
        btnRegistrarse.setOnClickListener(v -> abrirRegistro());
        btnRecuperarPassword.setOnClickListener(v -> recuperarPassword());
    }

    private void iniciarSesion() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        boolean esValido = true;

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_campo_requerido));
            esValido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalido));
            esValido = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_campo_requerido));
            esValido = false;
        }

        if (esValido) {
            Toast.makeText(this, getString(R.string.msg_login_exitoso), Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirRegistro() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void recuperarPassword() {
        tilEmail.setError(null);
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_campo_requerido));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalido));
        } else {
            Toast.makeText(this, getString(R.string.msg_recuperar_password), Toast.LENGTH_LONG).show();
        }
    }
}
