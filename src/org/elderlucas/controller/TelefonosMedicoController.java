package org.elderlucas.controller;

import java.awt.event.KeyAdapter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.elderlucas.bean.Area;
import org.elderlucas.bean.Medico;
import org.elderlucas.bean.TelefonoMedico;
import org.elderlucas.bean.TelefonoMedico;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;


public class TelefonosMedicoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoMedico> listaTelefonoMedico;
    private ObservableList<Medico> listaMedico;
    @FXML private ImageView imageTelefonoMedico;
    @FXML private TableView tblTelefonosMedicos;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private TableColumn colCodigoMedico;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedicos());
      
    }
    public void image1(){
        imageTelefonoMedico.setFitHeight(imageTelefonoMedico.getFitHeight() + 10);
        imageTelefonoMedico.setFitWidth(imageTelefonoMedico.getFitWidth()+ 10);
    }
    public void image2(){
        imageTelefonoMedico.setFitHeight(imageTelefonoMedico.getFitHeight() - 10);
        imageTelefonoMedico.setFitWidth(imageTelefonoMedico.getFitWidth() - 10);
    }
    
    public void SoloNumerosEnteros(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (!Character.isDigit(key))
            keyEvent.consume();
        
        if(txtTelefonoTrabajo.getText().length() > 8)
            keyEvent.consume();
        
        if(txtTelefonoTrabajo.getText().length() < 8 && txtTelefonoTrabajo.getText().length() > 1)
            btnNuevo.setDisable(true);
        else
            btnNuevo.setDisable(false);
        

    } catch (Exception ex){ }
}
    
     public void SoloNumerosEnteros2(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (!Character.isDigit(key))
            keyEvent.consume();
        
        if(txtTelefonoPersonal.getText().length() > 8)
            keyEvent.consume();
        
        if(txtTelefonoPersonal.getText().length() < 8)
            btnNuevo.setDisable(true);
        else
            btnNuevo.setDisable(false);
        

    } catch (Exception ex){ }
}
    
    
    
    public void cargarDatos(){
        tblTelefonosMedicos.setItems(getTelefonoMedico());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoTelefonoMedico"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
    }

   public ObservableList <Medico> getMedicos(){
        ArrayList <Medico> lista = new ArrayList <Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),
                                    resultado.getInt("licenciaMedica"),
                                    resultado.getString("nombres"),
                                    resultado.getString("apellidos"),
                                    resultado.getString("horaEntrada"),
                                    resultado.getString("horaSalida"),
                                    resultado.getInt("turnoMaximo"),
                                    resultado.getString("sexo")));
                        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedico = FXCollections.observableList(lista);
   }
    
    public ObservableList <TelefonoMedico> getTelefonoMedico(){
        ArrayList <TelefonoMedico> lista = new ArrayList <TelefonoMedico>(); 
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarTelefonos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                                    resultado.getString("telefonoPersonal"),
                                    resultado.getString("telefonoTrabajo"),
                                    resultado.getInt("codigoMedico")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTelefonoMedico = FXCollections.observableList(lista);
    }
    
    
    
    public void seleccionarElemento(){
        if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
        txtTelefonoPersonal.setText(((TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txtTelefonoTrabajo.setText(((TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
        cmbCodigoMedico.getSelectionModel().select(bucarMedico(((TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
    }else{
            tblTelefonosMedicos.getSelectionModel().clearSelection();
        }
        }
    
    public TelefonoMedico bucarContacto(int codigoTelefonoMedico){
        TelefonoMedico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarTelefono(?)}");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonoMedico(registro.getInt("codigoTelefonoMedico"),
                                registro.getString("telefonoPersonal"),
                                registro.getString("telefonoTrabajo"),
                                registro.getInt("codigoMedico"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public Medico bucarMedico(int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico(registro.getInt("codigoMedico"),
                                registro.getInt("licenciaMedica"),
                                registro.getString("nombres"),
                                registro.getString("apellidos"),
                                registro.getString("horaEntrada"),
                                registro.getString("horaSalida"),
                                registro.getInt("turnoMaximo"),
                                registro.getString("sexo"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tblTelefonosMedicos.setOnMouseClicked(null);
                    btnReporte.setOnAction(e -> cancelar());
                    activarControles();
                    cmbCodigoMedico.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnReporte.setOnAction(null);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tblTelefonosMedicos.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarTelefono(?,?,?,?)}");
            TelefonoMedico registro = (TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            
            procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
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
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break;
                
            default:
                
                if (tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Telefono de Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarTelefono(?)}");
                         procedimiento.setInt(1, ((TelefonoMedico)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                         procedimiento.execute();
                         listaTelefonoMedico.remove(tblTelefonosMedicos.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                      tblTelefonosMedicos.getSelectionModel().clearSelection();
                     limpiarControles();
                 }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                 }
            
        }
    }
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                tblTelefonosMedicos.setOnMouseClicked(null);
                btnEliminar.setOnAction(e -> cancelar());
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:

                if(!txtTelefonoPersonal.getText().equals("") && cmbCodigoMedico.getSelectionModel().getSelectedItem() != null){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                tblTelefonosMedicos.setOnMouseClicked(e -> seleccionarElemento());
                cargarDatos();
        }else{
                    JOptionPane.showMessageDialog(null, "Es obligatorio ingresar el numero de telefono Personal");
                    
                }
        }
    }
    
    
    public void cancelar (){
      
              desactivarControles();
              btnNuevo.setText("Nuevo");
              btnEliminar.setText("Eliminar");
              btnReporte.setText("Reporte");
              btnEditar.setText("Editar");
              btnNuevo.setDisable(false);
              btnEliminar.setDisable(false);
              btnEditar.setDisable(false);
              cmbCodigoMedico.setValue(null);
              btnReporte.setDisable(false);
              tipoDeOperacion = operaciones.NINGUNO;
              limpiarControles();
              btnEliminar.setOnAction(e -> eliminar());
              tblTelefonosMedicos.setOnMouseClicked(e -> seleccionarElemento());
              
  }
  
  public void guardar(){
      
      TelefonoMedico registro = new TelefonoMedico();
      registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
      registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
      registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
      
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefono(?,?,?)}");
          procedimiento.setString(1, registro.getTelefonoPersonal());
          procedimiento.setString(2, registro.getTelefonoTrabajo());
          procedimiento.setInt(3, registro.getCodigoMedico());
          procedimiento.execute();
          listaTelefonoMedico.add(registro);
      }catch(Exception e){
          e.printStackTrace();
      }
  
  }
    
    public void desactivarControles(){
        txtTelefonoPersonal.setDisable(true);
        txtTelefonoTrabajo.setDisable(true);
        cmbCodigoMedico.setDisable(true);
    }
    
    public void activarControles(){
        txtTelefonoPersonal.setDisable(false);
        txtTelefonoTrabajo.setDisable(false);
        cmbCodigoMedico.setDisable(false);
    }
    
    public void limpiarControles(){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        cmbCodigoMedico.getSelectionModel().clearSelection();
        cmbCodigoMedico.setValue(null);
    }
    public void ventanaMedicos(){
       escenarioPrincipal.ventanaMedicos();
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void regresarMenu(){
       escenarioPrincipal.menuPrincipal();
    }

    
}
