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

public class AnunciosActivity extends AppCompatActivity {

    private final List<Anuncio> listaAnuncios = new ArrayList<>();
    private AnunciosAdapter adapter;
    private DatabaseReference mAnunciosRef;
    private ValueEventListener anunciosListener;

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
        adapter = new AnunciosAdapter(listaAnuncios);
        rvAnuncios.setAdapter(adapter);

        mAnunciosRef = FirebaseDatabase.getInstance().getReference("anuncios");
        cargarAnunciosDesdeFirebase();
    }

    private void cargarAnunciosDesdeFirebase() {
        anunciosListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAnuncios.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Anuncio anuncio = item.getValue(Anuncio.class);
                    if (anuncio != null) {
                        listaAnuncios.add(anuncio);
                    }
                }
                if (listaAnuncios.isEmpty()) {
                    cargarAnunciosDemo();
                } else {
                    Collections.reverse(listaAnuncios);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listaAnuncios.isEmpty()) {
                    cargarAnunciosDemo();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        mAnunciosRef.addValueEventListener(anunciosListener);
    }

    private void cargarAnunciosDemo() {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnunciosRef != null && anunciosListener != null) {
            mAnunciosRef.removeEventListener(anunciosListener);
        }
    }
}
