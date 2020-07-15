package org.elderlucas.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.elderlucas.bean.TipoUsuario;
import org.elderlucas.bean.Usuario;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;

public class RegistroController implements Initializable{
private Principal escenarioPrincipal;
private enum operaciones{NUEVO, ELIMINAR, NINGUNO, CANCELAR, ACTUALIZAR, GUARDAR};
operaciones tipoDeOperacion = operaciones.NINGUNO;
private ObservableList <Usuario> listaUsuarios;
private ObservableList <TipoUsuario> listaTipoUsuario;

@FXML private TextField txtUserName;
@FXML private TextField txtPassword;
@FXML private TableColumn colUsuario;
@FXML private TableColumn colHora;
@FXML private TableColumn colFecha;
@FXML private TableColumn colEstado;
@FXML private TableColumn colContrasena;
@FXML private TableColumn colCodigo;
@FXML private TableColumn colTipoUsuario;
@FXML private ComboBox cmbTipoUsuario;
@FXML private Button btnNuevo;
@FXML private Button btnLogin;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;
@FXML private TableView tblUsuarios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     cmbTipoUsuario.setItems(getTiposDeUsuario());
     cargarDatos();
    }
    
    public void cargarDatos(){
        tblUsuarios.setItems(getUsuarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory <Usuario, Integer>("codigoUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory <Usuario, String>("usuarioLogin"));
        colContrasena.setCellValueFactory(new PropertyValueFactory <Usuario, String>("usuarioContrasena"));
        colEstado.setCellValueFactory(new PropertyValueFactory <Usuario, Boolean> ("usuarioEstado"));
        colFecha.setCellValueFactory(new PropertyValueFactory <Usuario, Date>("usuarioFecha"));
        colHora.setCellValueFactory(new PropertyValueFactory <Usuario, Time>("usuarioHora"));
        colTipoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoTipoUsuario"));
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
    
    public void seleccionarElemento(){
        if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
            txtUserName.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioLogin());
            txtPassword.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioContrasena());
            cmbTipoUsuario.getSelectionModel().select(buscarTipoUsuario(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()));
        }else{
            tblUsuarios.getSelectionModel().clearSelection();
        }
    }
    
    public TipoUsuario buscarTipoUsuario(int codUsuario){
        TipoUsuario resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_buscarTipoUsuario(?)");
            procedimiento.setInt(1, codUsuario);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new TipoUsuario(registro.getInt("codigoTipousuario"),
                                            registro.getString("descripcion"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setOnAction(e -> cancelar());
                btnEditar.setDisable(true);
                tblUsuarios.setOnMouseClicked(null);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(!txtUserName.getText().equals("") && !txtPassword.getText().equals("") && cmbTipoUsuario.getValue() != null){
                guardar();
                cancelar();
                cargarDatos();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe ingresar todos los datos para crear el usuario");
                }
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblUsuarios.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Eliminar");
                    btnNuevo.setDisable(true);
                    tblUsuarios.setOnMouseClicked(null);
                    btnEliminar.setOnAction(e -> cancelar());
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                cancelar();
                cargarDatos();
                break;
        }
    }
     
     public void actualizar(){
        try{
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarUsuario(?,?,?,?)}");
            Usuario registro = (Usuario)tblUsuarios.getSelectionModel().getSelectedItem();
            registro.setUsuarioLogin(txtUserName.getText());
            registro.setUsuarioContrasena(txtPassword.getText());
            registro.setCodigoTipoUsuario(((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
            
            
            procedimiento.setInt(1, registro.getCodigoUsuario());
            procedimiento.setString(2, registro.getUsuarioLogin());
            procedimiento.setString(3, registro.getUsuarioContrasena());
            procedimiento.setInt(4, registro.getCodigoTipoUsuario());
            
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void guardar(){
      
      Usuario registro = new Usuario();
      registro.setUsuarioLogin(txtUserName.getText());
      registro.setUsuarioContrasena(txtPassword.getText());
      registro.setCodigoTipoUsuario(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
      
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?)}");
          procedimiento.setString(1, registro.getUsuarioLogin());
          procedimiento.setString(2, registro.getUsuarioContrasena());
          procedimiento.setInt(3, registro.getCodigoTipoUsuario());
          procedimiento.execute();
          listaUsuarios.add(registro);
      }catch(Exception e){
          e.printStackTrace();
      }
  
  }
    
    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break;
                
            default:
                
                if (tblUsuarios.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el Usuario?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarContacto(?)}");
                         procedimiento.setInt(1, ((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                         procedimiento.execute();
                         listaUsuarios.remove(tblUsuarios.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                      tblUsuarios.getSelectionModel().clearSelection();
                     limpiarControles();
                 }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Item");
                 }
            
        }
    }
    
    
    
    
    
    public void cancelar(){
    limpiarControles();
    desactivarControles();
    btnNuevo.setText("Nuevo");
    btnEditar.setText("Editar");
    btnEliminar.setText("Eliminar");
    btnNuevo.setDisable(false);
    btnEditar.setDisable(false);
    btnEliminar.setDisable(false);
    btnEliminar.setOnAction(e -> eliminar());
    tblUsuarios.getSelectionModel().clearSelection();
    tblUsuarios.setOnMouseClicked(e -> seleccionarElemento());
    tipoDeOperacion = operaciones.NINGUNO;
    }
    
    public void limpiarControles(){
        txtUserName.setText("");
        txtPassword.setText("");
        cmbTipoUsuario.setValue(null);
        cmbTipoUsuario.getSelectionModel().clearSelection();
    }
    
    public void activarControles(){
        txtUserName.setEditable(true);
        txtPassword.setEditable(true);
        cmbTipoUsuario.setDisable(false);
    }
    
    public void desactivarControles(){
        txtUserName.setEditable(false);
        txtPassword.setEditable(false);
        cmbTipoUsuario.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void regresarLogin(){
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
    
}
