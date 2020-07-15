package org.elderlucas.controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.elderlucas.bean.Especialidad;
import org.elderlucas.bean.Horario;
import org.elderlucas.bean.Medico;
import org.elderlucas.bean.MedicoEspecialidad;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;

public class MedicoEspecialidadController implements Initializable {
  private Principal escenarioPrincipal;
  
  private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Especialidad> listaEspecialidad;
    private ObservableList<Horario> listaHorarios;
    @FXML private ImageView imageMedicoEspecialidad;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private ComboBox cmbMedico;
    @FXML private ComboBox cmbHorario;
    @FXML private ComboBox cmbEspecialidad;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colCodigo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbMedico.setItems(getMedicos());
        cmbEspecialidad.setItems(getEspecialidades());
        cmbHorario.setItems(getHorarios());
    }
    
    public void image1(){
        imageMedicoEspecialidad.setFitHeight(imageMedicoEspecialidad.getFitHeight() + 10);
        imageMedicoEspecialidad.setFitWidth(imageMedicoEspecialidad.getFitWidth()+ 10);
    }
    public void image2(){
        imageMedicoEspecialidad.setFitHeight(imageMedicoEspecialidad.getFitHeight() - 10);
        imageMedicoEspecialidad.setFitWidth(imageMedicoEspecialidad.getFitWidth() - 10);
    }
    
    public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidad());
        colCodigo.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedicoEspecialidad"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedico"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoEspecialidad"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoHorario"));
    }
    public void seleccionarElemento(){
        if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
        cmbMedico.getSelectionModel().select(buscarMedico(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbEspecialidad.getSelectionModel().select(buscarEspecialidad(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        cmbHorario.getSelectionModel().select(buscarHorario(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
    }else{
            tblMedicoEspecialidad.getSelectionModel().clearSelection();
        }
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
    
      public ObservableList <Horario> getHorarios(){
        ArrayList <Horario> lista = new ArrayList <Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"),
                                    resultado.getString("horarioEntrada"),
                                    resultado.getString("horarioSalida"),
                                    resultado.getBoolean("lunes"),
                                    resultado.getBoolean("martes"),
                                    resultado.getBoolean("miercoles"),
                                    resultado.getBoolean("jueves"),
                                    resultado.getBoolean("viernes")));
                        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaHorarios = FXCollections.observableList(lista);
   }
    
     public ObservableList <Especialidad> getEspecialidades(){
        ArrayList <Especialidad> lista = new ArrayList <Especialidad>(); 
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                    resultado.getString("nombreEspecialidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableList(lista);
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
    
    public Medico buscarMedico(int codigoMedico){
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
                                registro.getString("sexo")
                
                                    );
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidad(registro.getInt("codigoEspecialidad"),
                                registro.getString("nombreEspecialidad")
                                
                
                                    );
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
     public Horario buscarHorario(int codigoHorario){
        Horario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarHorario(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horario(registro.getInt("codigoHorario"),
                                registro.getString("horarioEntrada"),
                                registro.getString("horarioSalida"),
                                registro.getBoolean("lunes"),
                                registro.getBoolean("martes"),
                                registro.getBoolean("miercoles"),
                                registro.getBoolean("jueves"),
                                registro.getBoolean("viernes")
                                    );
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
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
                
                if (tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar MedicoEspecialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarMedicoEspecialidad(?)}");
                         procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                         procedimiento.execute();
                         listaMedicoEspecialidad.remove(tblMedicoEspecialidad.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                      tblMedicoEspecialidad.getSelectionModel().clearSelection();
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
                tblMedicoEspecialidad.setOnMouseClicked(null);
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setOnAction(e -> cancelar());
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:

                if(cmbMedico.getSelectionModel().getSelectedItem() != null && cmbEspecialidad.getSelectionModel().getSelectedItem() != null && cmbHorario.getSelectionModel().getSelectedItem() != null){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                tblMedicoEspecialidad.setOnMouseClicked(e -> seleccionarElemento());
                cargarDatos();
        }else{
                    tipoDeOperacion = operaciones.GUARDAR;
                    JOptionPane.showMessageDialog(null, "Es obligatorio ingresar todos los campos");
                    
                }
        }
    }
     
     public void guardar(){
      
      MedicoEspecialidad registro = new MedicoEspecialidad();
      registro.setCodigoMedico(((Medico)cmbMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
      registro.setCodigoEspecialidad(((Especialidad)cmbEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
      registro.setCodigoHorario(((Horario)cmbHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
      
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidad(?,?,?)}");
          procedimiento.setInt(1, registro.getCodigoMedico());
          procedimiento.setInt(2, registro.getCodigoEspecialidad());
          procedimiento.setInt(3, registro.getCodigoHorario());
          procedimiento.execute();
          listaMedicoEspecialidad.add(registro);
      }catch(Exception e){
          e.printStackTrace();
      }
  
  }
     
     public void cancelar (){
      
              desactivarControles();
              btnNuevo.setText("Nuevo");
              btnEliminar.setText("Eliminar");
              btnReporte.setText("Reporte");
              btnNuevo.setDisable(false);
              btnEliminar.setDisable(false);
              btnReporte.setDisable(false);
              tipoDeOperacion = operaciones.NINGUNO;
              limpiarControles();
              btnEliminar.setOnAction(e -> eliminar());
              tblMedicoEspecialidad.setOnMouseClicked(e -> seleccionarElemento());
              
  }
     
     public void desactivarControles(){
        cmbMedico.setDisable(true);
        cmbEspecialidad.setDisable(true);
        cmbHorario.setDisable(true);
    }
    
    public void activarControles(){
        cmbMedico.setDisable(false);
        cmbEspecialidad.setDisable(false);
        cmbHorario.setDisable(false);
    }
    
    public void limpiarControles(){
        cmbMedico.getSelectionModel().clearSelection();
        cmbMedico.setValue(null);
        cmbEspecialidad.getSelectionModel().clearSelection();
        cmbEspecialidad.setValue(null);
        cmbHorario.getSelectionModel().clearSelection();
        cmbHorario.setValue(null);
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
