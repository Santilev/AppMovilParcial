package com.example.parcial2.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial2.R;
import com.example.parcial2.adapters.PersonaAdapter;
import com.example.parcial2.databases.DBHelper;
import com.example.parcial2.modelos.Persona;

import java.util.ArrayList;

public class Activity3 extends AppCompatActivity {

    private ImageView btnPerfil;
    private SearchView searchView;
    private RecyclerView rvResultados;
    private PersonaAdapter adapter;
    private DBHelper db;
    private ArrayList<Persona> listaPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);

        btnPerfil = findViewById(R.id.btnPerfil);
        searchView = findViewById(R.id.searchView);
        rvResultados = findViewById(R.id.rvResultados);

        db = new DBHelper(this);
        listaPersonas = db.getAllPersonas();

        adapter = new PersonaAdapter(listaPersonas);
        rvResultados.setLayoutManager(new LinearLayoutManager(this));
        rvResultados.setAdapter(adapter);

        // Botón perfil → Activity2
        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Activity3.this, MainActivity2.class);
            startActivity(intent);
        });

        // Listener del buscador
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
