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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.elderlucas.bean.Area;
import org.elderlucas.bean.Cargo;
import org.elderlucas.bean.Responsable;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;


public class ResponsableTurnoController implements Initializable{
private Principal escenarioPrincipal;

    

    
 private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Responsable> listaResponsable;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargo> listaCargo;
    @FXML private ImageView imageResponsable;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbArea;
    @FXML private ComboBox cmbCargo;
    @FXML private TableView tblResponsables;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colCodigoCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCargo.setItems(getCargos());
        cmbArea.setItems(getAreas());
    }
    
    public void image1(){
        imageResponsable.setFitHeight(imageResponsable.getFitHeight() + 10);
        imageResponsable.setFitWidth(imageResponsable.getFitWidth()+ 10);
    }
    public void image2(){
        imageResponsable.setFitHeight(imageResponsable.getFitHeight() - 10);
        imageResponsable.setFitWidth(imageResponsable.getFitWidth() - 10);
    }
    
    public void SoloNumerosEnteros(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (!Character.isDigit(key))
            keyEvent.consume();
        
        if(txtTelefono.getText().length() > 8)
            keyEvent.consume();
        
        if(txtTelefono.getText().length() < 8)
            btnNuevo.setDisable(true);
        else
            btnNuevo.setDisable(false);

    } catch (Exception ex){ }
}
    
    public void cargarDatos(){
        tblResponsables.setItems(getResponsable());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Responsable, Integer>("codigoResponsableTurno"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Responsable, String>("nombreResponsable"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Responsable, String>("apellidosResponsable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Responsable, String>("telefonoPersonal"));
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<Responsable, Integer>("codigoArea"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Responsable, Integer>("codigoCargo"));
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
    
    public ObservableList <Cargo> getCargos(){
        ArrayList <Cargo> lista = new ArrayList <Cargo>(); 
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarCargos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"),
                                    resultado.getString("nombreCargo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
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
        if(tblResponsables.getSelectionModel().getSelectedItem() != null){
        txtNombre.setText(((Responsable)tblResponsables.getSelectionModel().getSelectedItem()).getNombreResponsable());
        txtApellido.setText(((Responsable)tblResponsables.getSelectionModel().getSelectedItem()).getApellidosResponsable());
        txtTelefono.setText(((Responsable)tblResponsables.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        cmbArea.getSelectionModel().select(buscarArea(((Responsable)tblResponsables.getSelectionModel().getSelectedItem()).getCodigoArea()));
        cmbCargo.getSelectionModel().select(buscarCargo(((Responsable)tblResponsables.getSelectionModel().getSelectedItem()).getCodigoCargo()));
    }else{
            tblResponsables.getSelectionModel().clearSelection();
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
    
    public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_buscarCargo(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargo(registro.getInt("codigoCargo"),
                                registro.getString("nombreCargo")
                                
                
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
                if (tblResponsables.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnReporte.setOnAction(e -> cancelar());
                    tblResponsables.setOnMouseClicked(null);
                    activarControles();
                    cmbArea.setDisable(true);
                    cmbCargo.setDisable(true);
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
                tblResponsables.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarResponsable(?,?,?,?,?,?)}");
            Responsable registro = (Responsable)tblResponsables.getSelectionModel().getSelectedItem();
            
            registro.setNombreResponsable(txtNombre.getText());
            registro.setApellidosResponsable(txtApellido.getText());
            registro.setTelefonoPersonal(txtTelefono.getText());
            
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidosResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
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
                
                if (tblResponsables.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Responsable de Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarResponsable(?)}");
                         procedimiento.setInt(1, ((Responsable)tblResponsables.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                         procedimiento.execute();
                         listaResponsable.remove(tblResponsables.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                      tblResponsables.getSelectionModel().clearSelection();
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
                tblResponsables.setOnMouseClicked(null);
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setOnAction(e -> cancelar());
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:

                if(!txtTelefono.getText().equals("") && !txtNombre.getText().equals("") && !txtApellido.getText().equals("") && cmbArea.getSelectionModel().getSelectedItem() != null && cmbCargo.getSelectionModel().getSelectedItem() != null){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                tblResponsables.setOnMouseClicked(e -> seleccionarElemento());
                cargarDatos();
        }else{
                    tipoDeOperacion = operaciones.GUARDAR;
                    JOptionPane.showMessageDialog(null, "Es obligatorio ingresar todos los campos");
                    
                }
        }
    }
    
    public void guardar(){
      
      Responsable registro = new Responsable();
      registro.setNombreResponsable(txtNombre.getText());
      registro.setApellidosResponsable(txtApellido.getText());
      registro.setTelefonoPersonal(txtTelefono.getText());
      registro.setCodigoArea(((Area)cmbArea.getSelectionModel().getSelectedItem()).getCodigoArea());
      registro.setCodigoCargo(((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
      
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsable(?,?,?,?,?)}");
          procedimiento.setString(1, registro.getNombreResponsable());
          procedimiento.setString(2, registro.getApellidosResponsable());
          procedimiento.setString(3, registro.getTelefonoPersonal());
          procedimiento.setInt(4, registro.getCodigoArea());
          procedimiento.setInt(5, registro.getCodigoCargo());
          procedimiento.execute();
          listaResponsable.add(registro);
      }catch(Exception e){
          e.printStackTrace();
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
              tblResponsables.setOnMouseClicked(e -> seleccionarElemento());
              
  }
     
     public void desactivarControles(){
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtTelefono.setEditable(false);
        cmbArea.setDisable(true);
        cmbCargo.setDisable(true);
    }
    
    public void activarControles(){
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtTelefono.setEditable(true);
        cmbArea.setDisable(false);
        cmbCargo.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        cmbArea.getSelectionModel().clearSelection();
        cmbArea.setValue(null);
        cmbCargo.getSelectionModel().clearSelection();
        cmbCargo.setValue(null);
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
    
    public void ventanaAreas(){
        escenarioPrincipal.ventanaAreas();
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
}



