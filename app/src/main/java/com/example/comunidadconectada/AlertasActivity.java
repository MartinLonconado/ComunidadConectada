package com.example.comunidadconectada;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertasActivity extends AppCompatActivity {

    private final List<Alerta> listaAlertas = new ArrayList<>();
    private AlertasAdapter adapter;
    private DatabaseReference mAlertasRef;
    private ValueEventListener alertasListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alertas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_alertas), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvAlertas = findViewById(R.id.rv_alertas);
        adapter = new AlertasAdapter(listaAlertas);
        rvAlertas.setAdapter(adapter);

        mAlertasRef = FirebaseDatabase.getInstance().getReference("alertas");
        cargarAlertasDesdeFirebase();
    }

    private void cargarAlertasDesdeFirebase() {
        alertasListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAlertas.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Alerta alerta = item.getValue(Alerta.class);
                    if (alerta != null) {
                        listaAlertas.add(alerta);
                    }
                }
                // Si la base de datos está vacía, mostrar alertas de demostración
                if (listaAlertas.isEmpty()) {
                    cargarAlertasDemo();
                } else {
                    Collections.reverse(listaAlertas); // Las más recientes arriba
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listaAlertas.isEmpty()) {
                    cargarAlertasDemo();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        mAlertasRef.addValueEventListener(alertasListener);
    }

    private void cargarAlertasDemo() {
        listaAlertas.add(new Alerta(
                "Alarma de Emergencia",
                "Activación del botón de pánico desde el departamento. Solicitud de asistencia inmediata.",
                "Juan Pérez (Depto. 302)",
                "Hoy, 14:30 hrs",
                EstadoAlerta.ACTIVA
        ));
        listaAlertas.add(new Alerta(
                "Ruidos Molestos",
                "Música a elevado volumen en área de quinchos fuera del horario permitido.",
                "María González (Depto. 501)",
                "Hoy, 11:15 hrs",
                EstadoAlerta.ATENDIDA
        ));
        listaAlertas.add(new Alerta(
                "Fuga de Agua",
                "Filtración de agua detectada en el nivel de estacionamientos subterráneos.",
                "Carlos Rojas (Depto. 104)",
                "Ayer, 18:45 hrs",
                EstadoAlerta.CERRADA
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertasRef != null && alertasListener != null) {
            mAlertasRef.removeEventListener(alertasListener);
        }
    }
}
