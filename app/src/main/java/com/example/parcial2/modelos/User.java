package com.example.parcial2.modelos;

public class User {

    private long id;
    private String nombreUsuario;
    private String password;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
