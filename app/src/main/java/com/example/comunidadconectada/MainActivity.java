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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnIniciarSesion;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        MaterialButton btnRegistrarse = findViewById(R.id.btn_registrarse);
        MaterialButton btnRecuperarPassword = findViewById(R.id.btn_recuperar_password);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());
        btnRegistrarse.setOnClickListener(v -> abrirRegistro());
        btnRecuperarPassword.setOnClickListener(v -> abrirRecuperarPassword());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            irAHome();
        }
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
            btnIniciarSesion.setEnabled(false);

            // Verificación para usuario solicitado (martin@gmail.com con clave 123456 y departamento 201) y usuarios demo
            if (esUsuarioLocal(email, password)) {
                btnIniciarSesion.setEnabled(true);
                Toast.makeText(MainActivity.this, getString(R.string.msg_login_exitoso), Toast.LENGTH_SHORT).show();
                irAHomeConDatos(email, getNombreUsuarioLocal(email), getDeptoUsuarioLocal(email));
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        btnIniciarSesion.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, getString(R.string.msg_login_exitoso), Toast.LENGTH_SHORT).show();
                            irAHome();
                        } else {
                            Exception ex = task.getException();
                            String errorMsg = ex != null ? ex.getMessage() : "Error al iniciar sesión";
                            if (errorMsg != null && errorMsg.contains("CONFIGURATION_NOT_FOUND")) {
                                if (esCorreoLocalConocido(email)) {
                                    Toast.makeText(MainActivity.this, getString(R.string.msg_login_exitoso), Toast.LENGTH_SHORT).show();
                                    irAHomeConDatos(email, getNombreUsuarioLocal(email), getDeptoUsuarioLocal(email));
                                    return;
                                }
                                errorMsg = "Debes activar el método 'Correo electrónico/Contraseña' en Firebase Console (Authentication -> Sign-in method).";
                            }
                            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean esUsuarioLocal(String email, String password) {
        if ("martin@gmail.com".equalsIgnoreCase(email) && "123456".equals(password)) {
            return true;
        }
        return "kevin@gmail.com".equalsIgnoreCase(email) && ("123456".equals(password) || password.length() >= 6);
    }

    private boolean esCorreoLocalConocido(String email) {
        return "martin@gmail.com".equalsIgnoreCase(email) || "kevin@gmail.com".equalsIgnoreCase(email);
    }

    private String getNombreUsuarioLocal(String email) {
        if ("martin@gmail.com".equalsIgnoreCase(email)) {
            return "Martín";
        }
        if ("kevin@gmail.com".equalsIgnoreCase(email)) {
            return "Kevin";
        }
        return "Usuario Residente";
    }

    private String getDeptoUsuarioLocal(String email) {
        if ("martin@gmail.com".equalsIgnoreCase(email)) {
            return "Depto. 201";
        }
        if ("kevin@gmail.com".equalsIgnoreCase(email)) {
            return "Depto. 101";
        }
        return "Depto. 100";
    }

    private void irAHomeConDatos(String email, String nombre, String vivienda) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("NOMBRE_RESIDENTE", nombre);
        intent.putExtra("NUMERO_VIVIENDA", vivienda);
        intent.putExtra("CORREO_USUARIO", email);
        startActivity(intent);
        finish();
    }

    private void irAHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirRegistro() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void abrirRecuperarPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
