package org.elderlucas.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.elderlucas.bean.TipoUsuario;
import org.elderlucas.bean.Usuario;
import org.elderlucas.db.Conexion;
import org.elderlucas.report.GenerarReporte;
import org.elderlucas.sistema.Principal;

public class LoginController implements Initializable{
    private Principal escenarioPrincipal;

    private ObservableList <TipoUsuario> listaTipoUsuario;
    private ObservableList <Usuario> listaUsuarios;
    private static int codigoUsuario;
    
    @FXML private TextField txtUserName;
    @FXML private PasswordField pswPassword;
    @FXML private ComboBox cmbTipoUsuario;
    @FXML private Button btnReporte;
    @FXML private Button btnRegistro;
    @FXML private Button btnLogin;
    @FXML private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipoUsuario.setItems(getTiposDeUsuario());
        getUsuarios();
    }
    
    public ObservableList<TipoUsuario> getTiposDeUsuario(){
        ArrayList <TipoUsuario> lista = new ArrayList<TipoUsuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_listarTipoUsuario()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoUsuario(resultado.getInt("codigoTipoUsuario"),
                                          resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoUsuario = FXCollections.observableList(lista);
    }
    
    
    public ObservableList <Usuario> getUsuarios(){
        ArrayList <Usuario> lista = new ArrayList<Usuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_listarUsuarios()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                                      resultado.getString("usuarioLogin"),
                                      resultado.getString("usuarioContrasena"),
                                      resultado.getBoolean("usuarioEstado"),
                                      resultado.getDate("usuarioFecha"),
                                      resultado.getTime("usuarioHora"),
                                      resultado.getInt("codigoTipoUsuario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
      return listaUsuarios = FXCollections.observableList(lista);
    }
    
    public void login(){
        
        if(!txtUserName.getText().equals("") && !pswPassword.getText().equals("") && cmbTipoUsuario.getValue() != null){
            
        for(int i = 1; i<listaUsuarios.size() ; i++){
            if(txtUserName.getText().equals(listaUsuarios.get(i).getUsuarioLogin()) && 
                    pswPassword.getText().equals(listaUsuarios.get(i).getUsuarioContrasena()) 
                    && (((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()) == 
                    listaUsuarios.get(i).getCodigoTipoUsuario()){
                
                escenarioPrincipal.menuPrincipal();
                codigoUsuario = listaUsuarios.get(i).getCodigoUsuario();
                actualizarHora_Estado(codigoUsuario);
                
                break;
              
            }else if(i == listaUsuarios.size() -1){
                JOptionPane.showMessageDialog(null, "Datos no Validos");
                
            }
            
        }
            
        }else{
            JOptionPane.showMessageDialog(null, "ingrese todos los campos");
        }
    }
   
    public static int codUsuario (){
        return codigoUsuario;
    }
    
    public void registro(){
        if(!txtUserName.getText().equals("") && !pswPassword.getText().equals("") && cmbTipoUsuario.getValue() != null){
            if(((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario() < 3){
            
        for(int i = 1; i < listaUsuarios.size(); i++){
            if(txtUserName.getText().equals(listaUsuarios.get(i).getUsuarioLogin()) && 
                    pswPassword.getText().equals(listaUsuarios.get(i).getUsuarioContrasena()) 
                    && (((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()) == 
                    listaUsuarios.get(i).getCodigoTipoUsuario()){
                
                escenarioPrincipal.ventanaRegistro();
                codigoUsuario = listaUsuarios.get(i).getCodigoUsuario();
                actualizarHora_Estado(codigoUsuario);
                
           
            }else if(i == listaUsuarios.size() -1){
                JOptionPane.showMessageDialog(null, "Datos no Validos");
                break;
            }
        }
        }else{
            JOptionPane.showMessageDialog(null, "Usuario de Invitado no pueden registrar usuarios");
        }
        }else{
            JOptionPane.showMessageDialog(null, "ingrese todos los campos");
        }
        
        
    }
    
   public void reporte (){
       if(!txtUserName.getText().equals("") && !pswPassword.getText().equals("") && cmbTipoUsuario.getValue() != null){
            if(((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario() < 3){
            
        for(int i = 1; i < listaUsuarios.size(); i++){
            if(txtUserName.getText().equals(listaUsuarios.get(i).getUsuarioLogin()) && 
                    pswPassword.getText().equals(listaUsuarios.get(i).getUsuarioContrasena()) 
                    && (((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()) == 
                    listaUsuarios.get(i).getCodigoTipoUsuario()){
                
                imprimirReporte();
                codigoUsuario = listaUsuarios.get(i).getCodigoUsuario();
                actualizarHora_Estado(codigoUsuario);
                
           
            }else if(i == listaUsuarios.size() -1){
                JOptionPane.showMessageDialog(null, "Datos no Validos");
                break;
            }
        }
        }else{
            JOptionPane.showMessageDialog(null, "Usuario de Invitado no pueden Imprimir reportes");
        }
        }else{
            JOptionPane.showMessageDialog(null, "ingrese todos los campos para validarse");
        }
   }
  
  public void imprimirReporte(){
      Map parametros = new HashMap();
      parametros.put("codigoUsuario", null);
      GenerarReporte.mostrarReporte("ReporteUsuarios.jasper", "Reporte de Pacientes", parametros);
  }
    
    public void actualizarHora_Estado(int codUsuario){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarHoraEstado(?)");
            procedimiento.setInt(1, codUsuario);
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cancelar(){
        txtUserName.setText("");
        pswPassword.setText("");
        cmbTipoUsuario.setValue(null);
        cmbTipoUsuario.getSelectionModel().clearSelection();
    }
    
   public Principal getEscenarioPrincipal(){
       return escenarioPrincipal;
   }
   
   public void setEscenarioPrincipal (Principal escenarioPrincipal){
       this.escenarioPrincipal = escenarioPrincipal;
   }
   
   public void ventanaRegistro(){
       escenarioPrincipal.ventanaRegistro();
   }
    
}
