package com.example.parcial2.actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.R;
import com.example.parcial2.databases.DBHelper;
import com.example.parcial2.modelos.Perfil;

public class MainActivity2 extends AppCompatActivity {

    private EditText inputNombre, inputProfesion, inputNacionalidad, inputEstudios;
    private Button btnGuardar;
    private DBHelper db;

    private long userId = -1L;      // opcional: si querés vincular el perfil al usuario logueado
    private String userName = null; // opcional

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        inputNombre       = findViewById(R.id.inputNombre);
        inputProfesion    = findViewById(R.id.inputProfesion);
        inputNacionalidad = findViewById(R.id.inputNacionalidad);
        inputEstudios     = findViewById(R.id.inputEstudios);
        btnGuardar        = findViewById(R.id.btnContinuar);

        db = new DBHelper(this);

        // (opcional) traer user_id y user_name del Intent
        if (getIntent() != null) {
            userId   = getIntent().getLongExtra("user_id", -1L);
            userName = getIntent().getStringExtra("user_name");
        }

        // CARGAR si ya hay algo guardado
        cargarPerfil();

        btnGuardar.setOnClickListener(v -> guardarPerfil());
    }

    private void cargarPerfil() {
        com.example.parcial2.modelos.Perfil p = db.getPerfilUnico();
        if (p != null) {
            // si querés, guardá el id para futuras actualizaciones

            inputNombre.setText(p.getNombre());
            inputProfesion.setText(p.getProfesion());
            inputNacionalidad.setText(p.getNacionalidad());
            inputEstudios.setText(p.getEstudios());
        } else if (userName != null && inputNombre.getText().toString().trim().isEmpty()) {
            inputNombre.setText(userName);
        }
    }

    private void guardarPerfil() {
        String nombre = inputNombre.getText().toString().trim();
        String profesion = inputProfesion.getText().toString().trim();
        String nacionalidad = inputNacionalidad.getText().toString().trim();
        String estudios = inputEstudios.getText().toString().trim();

        if (nombre.isEmpty() || profesion.isEmpty() || nacionalidad.isEmpty() || estudios.isEmpty()) {
            Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        com.example.parcial2.modelos.Perfil p = new com.example.parcial2.modelos.Perfil();
        p.setUserId(userId); // opcional
        p.setNombre(nombre);
        p.setProfesion(profesion);
        p.setNacionalidad(nacionalidad);
        p.setEstudios(estudios);

        boolean ok = db.upsertPerfil(p);   // <- CAMBIO: upsert
        if (ok) {
            Toast.makeText(this, "Perfil guardado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}