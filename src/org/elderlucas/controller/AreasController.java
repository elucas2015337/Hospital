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
import org.elderlucas.bean.Area;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;


public class AreasController implements Initializable{
    private Principal escenarioPrincipal;

    
    private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    @FXML private ImageView imageAreas;
    @FXML private TableView tblAreas;
    @FXML private TextField txtNombreArea;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombreArea;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    public void image1(){
        imageAreas.setFitHeight(imageAreas.getFitHeight() + 10);
        imageAreas.setFitWidth(imageAreas.getFitWidth()+ 10);
    }
    public void image2(){
        imageAreas.setFitHeight(imageAreas.getFitHeight() - 10);
        imageAreas.setFitWidth(imageAreas.getFitWidth() - 10);
    }
    
    public void cargarDatos(){
        tblAreas.setItems(getAreas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
    }

    public ObservableList <Area> getAreas(){
        ArrayList <Area> lista = new ArrayList <Area>(); 
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"),
                                    resultado.getString("nombreArea")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaArea = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblAreas.getSelectionModel().getSelectedItem() != null){
        txtNombreArea.setText(((Area)tblAreas.getSelectionModel().getSelectedItem()).getNombreArea());
    }else{
            tblAreas.getSelectionModel().clearSelection();
        }
        }
    
    
    public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarArea(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(registro.getInt("codigoArea"),
                                registro.getString("nombreArea")
                                
                
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
                if (tblAreas.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnReporte.setOnAction(e -> cancelar());
                    tblAreas.setOnMouseClicked(null);
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
                tblAreas.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
     
     public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarArea(?,?)}");
            Area registro = (Area)tblAreas.getSelectionModel().getSelectedItem();
            
            registro.setNombreArea(txtNombreArea.getText());
            procedimiento.setInt(1, registro.getCodigoArea());
            procedimiento.setString(2, registro.getNombreArea());
            
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
                
                if (tblAreas.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarArea(?)}");
                         procedimiento.setInt(1, ((Area)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                         procedimiento.execute();
                         listaArea.remove(tblAreas.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                      tblAreas.getSelectionModel().clearSelection();
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
                btnEliminar.setOnAction(e -> cancelar());
                tblAreas.setOnMouseClicked(null);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblAreas.setOnMouseClicked(e -> seleccionarElemento());
                
                
        }
    }
    
    
  public void cancelar (){
      
             desactivarControles();
              btnNuevo.setText("Nuevo");
              btnEliminar.setText("Eliminar");
              btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
              btnNuevo.setDisable(false);
              btnEliminar.setDisable(false);
              btnEditar.setDisable(false);
              btnReporte.setDisable(false);
              tipoDeOperacion = operaciones.NINGUNO;
              limpiarControles();
              btnEliminar.setOnAction(e -> eliminar());
              tblAreas.getSelectionModel().clearSelection();
              tblAreas.setOnMouseClicked(e -> seleccionarElemento());
  }
  
  public void guardar(){
      Area registro = new Area();
      registro.setNombreArea(txtNombreArea.getText());
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarArea(?)}");
          procedimiento.setString(1, registro.getNombreArea());
          procedimiento.execute();
      }catch(Exception e){
          e.printStackTrace();
      }
  }
    
    public void desactivarControles(){
        txtNombreArea.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreArea.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreArea.setText("");
    }
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }

    
}