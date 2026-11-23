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

public class AnunciosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anuncios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_anuncios), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvAnuncios = findViewById(R.id.rv_anuncios);

        List<Anuncio> listaAnuncios = new ArrayList<>();
        listaAnuncios.add(new Anuncio(
                "Corte Programado de Agua",
                "Se realizará un corte del suministro de agua potable por trabajos de reparación en la bomba principal mañana entre las 09:00 y 13:00 hrs.",
                "Administración",
                "24/05/2024"
        ));
        listaAnuncios.add(new Anuncio(
                "Asamblea General Ordinaria",
                "Invitamos a todos los copropietarios a la asamblea del mes este viernes a las 19:30 hrs en el salón de eventos.",
                "Comité de Administración",
                "22/05/2024"
        ));
        listaAnuncios.add(new Anuncio(
                "Recordatorio Uso de Estacionamientos",
                "Se recuerda a los residentes que los estacionamientos de visitas son de uso exclusivo por un máximo de 4 horas continuas.",
                "Conserjería",
                "20/05/2024"
        ));

        AnunciosAdapter adapter = new AnunciosAdapter(listaAnuncios);
        rvAnuncios.setAdapter(adapter);
    }
}
