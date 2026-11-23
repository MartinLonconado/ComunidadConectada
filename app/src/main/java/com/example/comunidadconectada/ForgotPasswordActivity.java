package com.example.comunidadconectada;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_forgot_password), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);

        btnEnviar = findViewById(R.id.btn_enviar_recuperacion);
        MaterialButton btnVolver = findViewById(R.id.btn_volver_login);

        btnEnviar.setOnClickListener(v -> enviarInstrucciones());
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }

    private void enviarInstrucciones() {
        tilEmail.setError(null);
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_campo_requerido));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalido));
        } else {
            btnEnviar.setEnabled(false);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        btnEnviar.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.msg_recuperar_password), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Error al enviar correo";
                            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
