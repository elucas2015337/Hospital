package org.elderlucas.bean;

public class Area {
    private int codigoArea;
    private String nombreArea;

    public Area() {
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public Area(int codigoArea, String nombreArea) {
        this.codigoArea = codigoArea;
        this.nombreArea = nombreArea;
    }

   

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    public String toString(){
        return getCodigoArea() + " | " + getNombreArea();
    }
   
    
}
