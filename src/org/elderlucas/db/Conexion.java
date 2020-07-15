package org.elderlucas.db;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class Conexion {
    private Connection conexion;
  //  private Statement sentencia;
    private static Conexion instancia;
    


public Conexion(){
    try{
        Class.forName ("com.mysql.jdbc.Driver").newInstance();
        conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBHOspitalInfectologia2015337?useSSL=false", "root", "admin"); //Quitar de comentario cuando sea necesario
        //conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBHOspitalInfectologia2015337?useSSL=false", "r00t", "admin");
}catch (ClassNotFoundException e){
    e.printStackTrace();
}catch (InstantiationException e){
    e.printStackTrace();
}catch (IllegalAccessException e){
    e.printStackTrace();
}catch (SQLException e){
    e.printStackTrace();
}
}

public static Conexion getInstancia(){
    if (instancia == null){
        instancia = new Conexion();
    }
    return instancia;
}

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }



}