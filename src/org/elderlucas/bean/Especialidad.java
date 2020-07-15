package org.elderlucas.bean;

public class Especialidad{
    private int CodigoEspecialidad;
    private String NombreEspecialidad;

    public Especialidad() {
    }

    public Especialidad(int CodigoEspecialidad, String NombreEspecialidad) {
        this.CodigoEspecialidad = CodigoEspecialidad;
        this.NombreEspecialidad = NombreEspecialidad;
    }

    public int getCodigoEspecialidad() {
        return CodigoEspecialidad;
    }

    public void setCodigoEspecialidad(int CodigoEspecialidad) {
        this.CodigoEspecialidad = CodigoEspecialidad;
    }

    public String getNombreEspecialidad() {
        return NombreEspecialidad;
    }

    public void setNombreEspecialidad(String NombreEspecialidad) {
        this.NombreEspecialidad = NombreEspecialidad;
    }
    
    public String toString(){
        return getCodigoEspecialidad() + " | " + getNombreEspecialidad();
    }
}
