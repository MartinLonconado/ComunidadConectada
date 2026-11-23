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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilNombre, tilDepartamento, tilEmail, tilPassword;
    private TextInputEditText etNombre, etDepartamento, etEmail, etPassword;
    private MaterialButton btnRegistro;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilNombre = findViewById(R.id.til_nombre);
        tilDepartamento = findViewById(R.id.til_departamento);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);

        etNombre = findViewById(R.id.et_nombre);
        etDepartamento = findViewById(R.id.et_departamento);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        btnRegistro = findViewById(R.id.btn_registro);
        btnRegistro.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        tilNombre.setError(null);
        tilDepartamento.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);

        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String departamento = etDepartamento.getText() != null ? etDepartamento.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        boolean esValido = true;

        if (TextUtils.isEmpty(nombre)) {
            tilNombre.setError(getString(R.string.error_campo_requerido));
            esValido = false;
        }

        if (TextUtils.isEmpty(departamento)) {
            tilDepartamento.setError(getString(R.string.error_campo_requerido));
            esValido = false;
        }

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
        } else if (password.length() < 6) {
            tilPassword.setError("La contraseña debe tener al menos 6 caracteres");
            esValido = false;
        }

        if (esValido) {
            btnRegistro.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        btnRegistro.setEnabled(true);
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                String deptoFmt = departamento.startsWith("Depto") ? departamento : "Depto. " + departamento;
                                Usuario nuevoUsuario = new Usuario(uid, nombre, deptoFmt, email, "Residente");

                                mDatabase.child("usuarios").child(uid).setValue(nuevoUsuario)
                                        .addOnCompleteListener(dbTask -> {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.msg_registro_exitoso), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                            intent.putExtra("NOMBRE_RESIDENTE", nombre);
                                            intent.putExtra("NUMERO_VIVIENDA", deptoFmt);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        });
                            }
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Error en el registro";
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
