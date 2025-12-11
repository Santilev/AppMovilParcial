package com.example.parcial2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.parcial2.modelos.User;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.example.parcial2.modelos.Persona;
import java.util.ArrayList;

public class DBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "ejemplo.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase db = getWritableDatabase();
        if (!checkDatabaseExistence(db)) {
            try {
                copyDatabase(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDatabaseExistence(SQLiteDatabase db) {
        String path = db.getPath();
        File file = new File(path);
        return file.exists();
    }

    private void copyDatabase(Context context) throws IOException {
        close();
        OutputStream output = new FileOutputStream(getDatabasePath(context));
        InputStream input = context.getAssets().open("databases/" + DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        input.close();
        output.flush();
        output.close();
    }

    private String getDatabasePath(Context context) {
        return context.getDatabasePath(DATABASE_NAME).getPath();
    }

    // ============================
    //   TABLA Y COLUMNAS
    // ============================

    private static final String TABLE_USUARIO = "tabla_usuario";
    private static final String COL_ID = "id_usuario";
    private static final String COL_NOMBRE = "nombre_usuario";
    private static final String COL_PASSWORD = "password";

    // ============================
    //        MÉTODOS CRUD
    // ============================

    public User comprobarUsuarioLocal(String nombreUsuario, String password) {
        User user = new User();
        user.setId(-1);

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USUARIO +
                " WHERE " + COL_NOMBRE + " = ? AND " + COL_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{nombreUsuario, password});

        if (cursor != null && cursor.moveToFirst()) {
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
            user.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
        }

        if (cursor != null) cursor.close();
        db.close();

        return user;
    }

    public long addUser(User user) {
        if (user == null) return -1;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, user.getNombreUsuario());
        values.put(COL_PASSWORD, user.getPassword());

        long id = db.insert(TABLE_USUARIO, null, values);
        db.close();
        return id;
    }

    public User getUserByUsername(String nombreUsuario) {
        User user = new User();
        user.setId(-1);

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIO + " WHERE " + COL_NOMBRE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nombreUsuario});

        if (cursor != null && cursor.moveToFirst()) {
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
            user.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
        }

        if (cursor != null) cursor.close();
        db.close();

        return user;
    }

    // ======= TABLA PERSONA =======
    private static final String TABLE_PERSONA       = "persona";
    private static final String COL_P_ID            = "id";
    private static final String COL_P_NOMBRE        = "nombre";
    private static final String COL_P_PROFESION     = "profesion";
    private static final String COL_P_NACIONALIDAD  = "nacionalidad";
    private static final String COL_P_ESTUDIOS      = "estudios";

    // Inserta un registro en la tabla persona
    public boolean insertPerfil(com.example.parcial2.modelos.Perfil p) {
        if (p == null) return false;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_P_NOMBRE,       p.getNombre());
        cv.put(COL_P_PROFESION,    p.getProfesion());
        cv.put(COL_P_NACIONALIDAD, p.getNacionalidad());
        cv.put(COL_P_ESTUDIOS,     p.getEstudios());

        long res = db.insert(TABLE_PERSONA, null, cv);
        db.close();
        return res != -1;
    }
    // === persona: leer el último/único perfil guardado ===
    public com.example.parcial2.modelos.Perfil getPerfilUnico() {
        com.example.parcial2.modelos.Perfil p = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, nombre, profesion, nacionalidad, estudios FROM persona ORDER BY id DESC LIMIT 1",
                null
        );
        if (c != null && c.moveToFirst()) {
            p = new com.example.parcial2.modelos.Perfil();
            p.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            p.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
            p.setProfesion(c.getString(c.getColumnIndexOrThrow("profesion")));
            p.setNacionalidad(c.getString(c.getColumnIndexOrThrow("nacionalidad")));
            p.setEstudios(c.getString(c.getColumnIndexOrThrow("estudios")));
        }
        if (c != null) c.close();
        db.close();
        return p;
    }

    // === persona: upsert (si hay registro, UPDATE; si no, INSERT) ===
    public boolean upsertPerfil(com.example.parcial2.modelos.Perfil p) {
        if (p == null) return false;

        SQLiteDatabase db = getWritableDatabase();

        // ¿ya existe algún perfil?
        Long idExistente = null;
        Cursor c = db.rawQuery("SELECT id FROM persona ORDER BY id DESC LIMIT 1", null);
        if (c != null && c.moveToFirst()) {
            idExistente = c.getLong(0);
        }
        if (c != null) c.close();

        ContentValues cv = new ContentValues();
        cv.put("nombre",       p.getNombre());
        cv.put("profesion",    p.getProfesion());
        cv.put("nacionalidad", p.getNacionalidad());
        cv.put("estudios",     p.getEstudios());

        long res;
        if (idExistente != null) {
            // UPDATE
            res = db.update("persona", cv, "id = ?", new String[]{String.valueOf(idExistente)});
            p.setId(idExistente);
            db.close();
            return res > 0;
        } else {
            // INSERT
            res = db.insert("persona", null, cv);
            if (res != -1) p.setId(res);
            db.close();
            return res != -1;
        }
    }



    // adentro de DBHelper:
    public ArrayList<Persona> getAllPersonas() {
        ArrayList<Persona> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, nombre, profesion, nacionalidad, estudios FROM persona",
                null
        );

        if (c.moveToFirst()) {
            do {
                Persona p = new Persona();
                p.setId(c.getInt(0));
                p.setNombre(c.getString(1));
                p.setProfesion(c.getString(2));
                p.setNacionalidad(c.getString(3));
                p.setEstudios(c.getString(4));
                lista.add(p);
            } while (c.moveToNext());
        }
        c.close();
        return lista;
    }

}
