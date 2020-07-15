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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.elderlucas.bean.Especialidad;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;


public class EspecialidadesController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    @FXML private ImageView imageEspecialidad;
    @FXML private TableView tblEspecialidades;
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombreEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidades());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colNombreEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("nombreEspecialidad"));
    }
    
    public void image1(){
        imageEspecialidad.setFitHeight(imageEspecialidad.getFitHeight() + 10);
        imageEspecialidad.setFitWidth(imageEspecialidad.getFitWidth()+ 10);
    }
    public void image2(){
        imageEspecialidad.setFitHeight(imageEspecialidad.getFitHeight() - 10);
        imageEspecialidad.setFitWidth(imageEspecialidad.getFitWidth() - 10);
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
    
    public void seleccionarElemento(){
        if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
        txtNombreEspecialidad.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
    }else{
            tblEspecialidades.getSelectionModel().clearSelection();
        }
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
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tblEspecialidades.setOnMouseClicked(null);
                    btnReporte.setOnAction(e -> cancelar());
                    activarControles();
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
                tblEspecialidades.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
     
     public void actualizar(){
        try{
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarEspecialidad(?,?)}");
            Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
            
            
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getNombreEspecialidad());
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
                
                if (tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarEspecialidad(?)}");
                         procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                         procedimiento.execute();
                         listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
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
                tblEspecialidades.setOnMouseClicked(null);
                btnEliminar.setOnAction(e -> cancelar());
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = EspecialidadesController.operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tblEspecialidades.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = EspecialidadesController.operaciones.NINGUNO;
                cargarDatos();
        }
    }
    
    
  public void cancelar (){
      
              desactivarControles();
              btnNuevo.setText("Nuevo");
              btnEliminar.setText("Eliminar");
              btnEditar.setDisable(false);
              btnReporte.setDisable(false);
              tipoDeOperacion = EspecialidadesController.operaciones.NINGUNO;
              limpiarControles();
              btnEliminar.setOnAction(e -> eliminar());
              tblEspecialidades.setOnMouseClicked(e -> seleccionarElemento());
  }
  
  public void guardar(){
      Especialidad registro = new Especialidad();
      registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
          procedimiento.setString(1, registro.getNombreEspecialidad());
          procedimiento.execute();
          //listaMedico.add(registro);
      }catch(Exception e){
          e.printStackTrace();
      }
  }
    
    public void desactivarControles(){
        txtNombreEspecialidad.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreEspecialidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreEspecialidad.setText("");
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
