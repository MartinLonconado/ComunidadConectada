package com.example.comunidadconectada;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlertasActivity extends AppCompatActivity {

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

        List<Alerta> listaAlertas = new ArrayList<>();
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
        listaAlertas.add(new Alerta(
                "Portón Principal Trabado",
                "Falla en el sensor del portón vehicular de acceso principal.",
                "Conserjería",
                "21/05/2024 09:10 hrs",
                EstadoAlerta.CERRADA
        ));

        AlertasAdapter adapter = new AlertasAdapter(listaAlertas);
        rvAlertas.setAdapter(adapter);
    }
}
