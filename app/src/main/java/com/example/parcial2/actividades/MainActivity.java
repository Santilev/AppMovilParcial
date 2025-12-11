package com.example.parcial2.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2.R;
import com.example.parcial2.databases.DBHelper;
import com.example.parcial2.modelos.User;

public class MainActivity extends AppCompatActivity {


    private EditText etUser, etPass;
    private Button btnContinuar;
    private DBHelper db;

    @Override
    protected void

    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.Usuario);
        etPass = findViewById(R.id.Password);
        btnContinuar = findViewById(R.id.btnContinuar);

        db = new DBHelper(this);

        // seed admin/admin
        try {
            User probe = db.getUserByUsername("admin");
            if (probe == null || probe.getId() == -1) {
                User u = new User();
                u.setNombreUsuario("admin");
                u.setPassword("admin");
                db.addUser(u);
            }
        } catch (Exception ignored) {}

        btnContinuar.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String u = etUser.getText().toString().trim();
        String p = etPass.getText().toString().trim();

        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Completá usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        User encontrado = db.comprobarUsuarioLocal(u, p);
        if (encontrado.getId() != -1) {
            Intent i = new Intent(this, Activity3.class);
            i.putExtra("username", encontrado.getNombreUsuario());
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
        }
    }
}
