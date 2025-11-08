
package com.example.parcial2.modelos;

public class Perfil {
    private long id;       // autoincrement
    private long userId;   // opcional: id del usuario de tabla_usuario, puede quedar -1
    private String nombre;
    private String profesion;
    private String nacionalidad;
    private String estudios;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getEstudios() { return estudios; }
    public void setEstudios(String estudios) { this.estudios = estudios; }
}
