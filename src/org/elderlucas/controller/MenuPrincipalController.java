package org.elderlucas.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;


public class MenuPrincipalController implements Initializable{
    
    @FXML
    private Principal escenarioPrincipal;
    private LoginController sesion;
    @FXML private ImageView fondoImage;
    @FXML private ImageView userImage;
    @FXML private Label lblCerrar;
    
    public void image1(){
        userImage.setFitHeight(userImage.getFitHeight() + 10);
        userImage.setFitWidth(userImage.getFitWidth() + 10);
        lblCerrar.setText("Cerrar Cesion");
    }
    
    public void image2(){
        userImage.setFitHeight(userImage.getFitHeight() - 10);
        userImage.setFitWidth(userImage.getFitWidth() - 10);
        lblCerrar.setText("");
    }
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void infoProgramador(){
       escenarioPrincipal.infoProgramador();
    }
    public void ventanaMedicos(){
       escenarioPrincipal.ventanaMedicos();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    
    public void ventanaEspecialidades(){
        escenarioPrincipal.ventanaEspecialidades();
    }
    
    public void cerrarCesion(){
        int codigo = LoginController.codUsuario();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarEstado(?)");
            procedimiento.setInt(1, codigo);
            procedimiento.execute();
            escenarioPrincipal.ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorarios();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }
    
    public void ventanaMedicoEspecialidad(){
        escenarioPrincipal.ventanaMedicoEspecialidad();
    }
    
    public void fondoBlur(){
        fondoImage.setEffect(new GaussianBlur());
    }
    
    public void fondoBlurSalida(){
        fondoImage.setEffect(null);
    }
    
    public void ventanaTurnos(){
        escenarioPrincipal.ventanaTurnos();
    }
    
  

    
    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
