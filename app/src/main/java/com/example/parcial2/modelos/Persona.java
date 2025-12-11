
package com.example.parcial2.modelos;

public class Persona {
    private int id;
    private String nombre;
    private String profesion;
    private String nacionalidad;
    private String estudios;

    public Persona() {}

    public Persona(int id, String nombre, String profesion, String nacionalidad, String estudios) {
        this.id = id;
        this.nombre = nombre;
        this.profesion = profesion;
        this.nacionalidad = nacionalidad;
        this.estudios = estudios;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getEstudios() { return estudios; }
    public void setEstudios(String estudios) { this.estudios = estudios; }
}
