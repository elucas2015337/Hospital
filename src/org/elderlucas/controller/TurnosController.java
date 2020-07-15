package org.elderlucas.controller;

import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.elderlucas.bean.MedicoEspecialidad;
import org.elderlucas.bean.Paciente;
import org.elderlucas.bean.Responsable;
import org.elderlucas.bean.Turno;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;

public class TurnosController implements Initializable{
private Principal escenarioPrincipal;
private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
 private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Turno> listaTurno;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Responsable> listaResponsable;
@FXML private ImageView imageTurno;
@FXML private Button btnEditar;
@FXML private Button btnEliminar;
@FXML private Button btnNuevo;
@FXML private Button btnReporte;
@FXML private ComboBox cmbMedicoEspecialidad;
@FXML private ComboBox cmbPaciente;
@FXML private ComboBox cmbResponsableTurno;
@FXML private DatePicker dpFechaCita;
@FXML private DatePicker dpFechaTurno;
@FXML private TableColumn colCodigo;
@FXML private TableColumn colCodigoPaciente;
@FXML private TableColumn colFechaCita;
@FXML private TableColumn colFechaTurno;
@FXML private TableColumn colMedicoEspecialidad;
@FXML private TableColumn colResponsableTurno;
@FXML private TableColumn colValor;
@FXML private TableView tblTurnos;
@FXML private TextField txtValor;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbPaciente.setItems(getPacientes());
        cmbResponsableTurno.setItems(getResponsable());
        cmbMedicoEspecialidad.setItems(getMedicoEspecialidad());
    }
    
    public void image1(){
        imageTurno.setFitHeight(imageTurno.getFitHeight() + 10);
        imageTurno.setFitWidth(imageTurno.getFitWidth()+ 10);
    }
    public void image2(){
        imageTurno.setFitHeight(imageTurno.getFitHeight() - 10);
        imageTurno.setFitWidth(imageTurno.getFitWidth() - 10);
    }
    
    public void cargarDatos(){
        tblTurnos.setItems(getTurnos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurno"));
        colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaCita"));
        colValor.setCellValueFactory(new PropertyValueFactory<Turno, Double>("valorCita"));
        colMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoMedicoEspecialidad"));
        colResponsableTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoResponsableTurno"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoPaciente"));
        
    }
    
    public ObservableList <Turno> getTurnos(){
        ArrayList <Turno> lista = new ArrayList <Turno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarTurnos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Turno(resultado.getInt("codigoTurno"),
                                    resultado.getDate("fechaTurno"),
                                    resultado.getDate("fechaCita"),
                                    resultado.getDouble("valorCita"),
                                    resultado.getInt("codigoMedicoEspecialidad"),
                                    resultado.getInt("codigoResponsableTurno"),
                                    resultado.getInt("codigoPaciente")));
                        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTurno = FXCollections.observableList(lista);
    }
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList <MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarMedicoEspecialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                                                resultado.getInt("codigoMedico"),
                                                resultado.getInt("codigoEspecialidad"),
                                                resultado.getInt("codigoHorario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicoEspecialidad = FXCollections.observableList(lista);
    }
    
    public ObservableList <Paciente> getPacientes(){
        ArrayList <Paciente> lista = new ArrayList <Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                    resultado.getString("DPI"),
                                    resultado.getString("apellidos"),
                                    resultado.getString("nombres"),
                                    resultado.getDate("fechaNacimiento"),
                                    resultado.getInt("edad"),
                                    resultado.getString("direccion"),
                                    resultado.getString("ocupacion"),
                                    resultado.getString("sexo")));
                        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableList(lista);
    }
    
    public ObservableList <Responsable> getResponsable(){
        ArrayList <Responsable> lista = new ArrayList <Responsable>(); 
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarResponsables()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Responsable(resultado.getInt("codigoResponsableTurno"),
                                    resultado.getString("nombreResponsable"),
                                    resultado.getString("apellidosResponsable"),
                                    resultado.getString("telefonoPersonal"),
                                    resultado.getInt("codigoArea"),
                                    resultado.getInt("codigoCargo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaResponsable = FXCollections.observableList(lista);
    }
    
    
     public void seleccionarElemento(){
        if(tblTurnos.getSelectionModel().getSelectedItem() != null){
        dpFechaTurno.setValue(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getFechaTurno().toLocalDate());
        dpFechaCita.setValue(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getFechaCita().toLocalDate());
        txtValor.setText(String.valueOf(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getValorCita()));
        cmbPaciente.getSelectionModel().select(buscarPaciente(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        cmbResponsableTurno.getSelectionModel().select(buscarResponsable(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
        cmbMedicoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
        }else{
            tblTurnos.getSelectionModel().clearSelection();
        }
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                registro.getString("DPI"),
                                registro.getString("apellidos"),
                                registro.getString("nombres"),
                                registro.getDate("fechaNacimiento"),
                                registro.getInt("edad"),
                                registro.getString("direccion"),
                                registro.getString("ocupacion"),
                                registro.getString("sexo")
                
                                    );
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
        MedicoEspecialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarMedicoEspecialidad(?)}");
            procedimiento.setInt(1, codigoMedicoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),
                                registro.getInt("codigoMedico"),
                                registro.getInt("codigoEspecialidad"),
                                registro.getInt("codigoHorario")
                
                                    );
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Responsable buscarResponsable(int codigoResponsableTurno){
        Responsable resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarResponsable(?)}");
            procedimiento.setInt(1, codigoResponsableTurno);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Responsable(registro.getInt("codigoResponsableTurno"),
                                registro.getString("nombreResponsable"),
                                registro.getString("apellidosResponsable"),
                                registro.getString("telefonoPersonal"),
                                registro.getInt("codigoArea"),
                                registro.getInt("codigoCargo")
                
                                    );
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                tblTurnos.setOnMouseClicked(null);
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setOnAction(e -> cancelar());
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:

                if(cmbResponsableTurno.getSelectionModel().getSelectedItem() != null && cmbPaciente.getSelectionModel().getSelectedItem() != null && cmbMedicoEspecialidad.getSelectionModel().getSelectedItem() != null && dpFechaCita.getValue() != null && dpFechaTurno.getValue() != null && !txtValor.getText().equals("")){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                tblTurnos.setOnMouseClicked(e -> seleccionarElemento());
                cargarDatos();
        }else{
                    tipoDeOperacion = operaciones.GUARDAR;
                    JOptionPane.showMessageDialog(null, "Es obligatorio ingresar todos los campos");
                    
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblTurnos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tblTurnos.setOnMouseClicked(null);
                    btnReporte.setOnAction(e -> cancelar());
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    cmbPaciente.setDisable(true);
                    cmbResponsableTurno.setDisable(true);
                    cmbMedicoEspecialidad.setDisable(true);
                    
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
                tblTurnos.setOnMouseClicked(e -> seleccionarElemento());
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarTurno(?,?,?,?,?,?,?)}");
            Turno registro = (Turno)tblTurnos.getSelectionModel().getSelectedItem();
            
            registro.setValorCita(Double.valueOf(txtValor.getText()));
            registro.setFechaTurno(java.sql.Date.valueOf(dpFechaTurno.getValue()));
            registro.setFechaCita(java.sql.Date.valueOf(dpFechaCita.getValue()));
            
            procedimiento.setInt(1, registro.getCodigoTurno());
            procedimiento.setDate(2, registro.getFechaTurno());
            procedimiento.setDate(3, registro.getFechaCita());
            procedimiento.setDouble(4, registro.getValorCita());
            procedimiento.setInt(5, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(6, registro.getCodigoResponsableTurno());
            procedimiento.setInt(7, registro.getCodigoPaciente());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void guardar(){
      
      Turno registro = new Turno();
      registro.setFechaTurno(java.sql.Date.valueOf(dpFechaTurno.getValue()));
      registro.setFechaCita(java.sql.Date.valueOf(dpFechaCita.getValue()));
      registro.setValorCita(Double.valueOf(txtValor.getText()));
      registro.setCodigoPaciente(((Paciente)cmbPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
      registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
      registro.setCodigoResponsableTurno(((Responsable)cmbResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
      
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?,?,?,?)}");
          procedimiento.setDate(1, registro.getFechaTurno());
          procedimiento.setDate(2, registro.getFechaCita());
          procedimiento.setDouble(3, registro.getValorCita());
          procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
          procedimiento.setInt(5, registro.getCodigoResponsableTurno());
          procedimiento.setInt(6, registro.getCodigoPaciente());
          procedimiento.execute();
          listaTurno.add(registro);
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
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break;
                
            default:
                
                if (tblTurnos.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar MedicoEspecialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarTurno(?)}");
                         procedimiento.setInt(1, ((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoTurno());
                         procedimiento.execute();
                         listaMedicoEspecialidad.remove(tblTurnos.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                      tblTurnos.getSelectionModel().clearSelection();
                     limpiarControles();
                 }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
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
              btnReporte.setDisable(false);
              tipoDeOperacion = operaciones.NINGUNO;
              limpiarControles();
              btnEliminar.setOnAction(e -> eliminar());
              tblTurnos.setOnMouseClicked(e -> seleccionarElemento());
              
  }
    
    public void desactivarControles(){
        dpFechaTurno.setDisable(true);
        dpFechaCita.setDisable(true);
        cmbPaciente.setDisable(true);
        cmbMedicoEspecialidad.setDisable(true);
        cmbResponsableTurno.setDisable(true);
        txtValor.setDisable(true);
    }
    
    public void activarControles(){
        dpFechaTurno.setDisable(false);
        dpFechaCita.setDisable(false);
        cmbPaciente.setDisable(false);
        cmbMedicoEspecialidad.setDisable(false);
        cmbResponsableTurno.setDisable(false);
        txtValor.setDisable(false);
    }
    
    public void limpiarControles(){
        dpFechaTurno.setValue(null);
        dpFechaCita.setValue(null);
        txtValor.setText("");
        cmbPaciente.getSelectionModel().clearSelection();
        cmbPaciente.setValue(null);
        cmbResponsableTurno.getSelectionModel().clearSelection();
        cmbResponsableTurno.setValue(null);
        cmbMedicoEspecialidad.getSelectionModel().clearSelection();
        cmbMedicoEspecialidad.setValue(null);
        
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
