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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.elderlucas.bean.Horario;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;

public class HorariosController implements Initializable{
private Principal escenarioPrincipal;
    
    
    
    private enum operaciones {NUEVO, ELIMINAR, GUARDAR, CANCELAR, NINGUNO, REPORTE, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horario> listaHorarios;
    @FXML private ImageView imageHorarios;
    @FXML private TextField txtEntrada;
    @FXML private TextField txtSalida;
    @FXML private CheckBox cbxLunes;
    @FXML private CheckBox cbxMartes;
    @FXML private CheckBox cbxMiercoles;
    @FXML private CheckBox cbxJueves;
    @FXML private CheckBox cbxViernes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void image1(){
        imageHorarios.setFitHeight(imageHorarios.getFitHeight() + 10);
        imageHorarios.setFitWidth(imageHorarios.getFitWidth()+ 10);
    }
    public void image2(){
        imageHorarios.setFitHeight(imageHorarios.getFitHeight() - 10);
        imageHorarios.setFitWidth(imageHorarios.getFitWidth() - 10);
    }
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory((new PropertyValueFactory<Horario, Boolean>("lunes")));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
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
   
   public void seleccionarElemento(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
        txtEntrada.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada());
        txtSalida.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
        cbxLunes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getLunes());
        cbxMartes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMartes());
        cbxMiercoles.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles());
        cbxJueves.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getJueves());
        cbxViernes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getViernes());
        
        }else{
            tblHorarios.getSelectionModel().clearSelection();
        }
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
   
     
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblHorarios.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tblHorarios.setOnMouseClicked(null);
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
                tblHorarios.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
     
     
     public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarHorario(?,?,?,?,?,?,?,?)}");
            Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();
            
            registro.setHorarioEntrada(txtEntrada.getText());
            registro.setHorarioSalida(txtSalida.getText());
            registro.setLunes(cbxLunes.isSelected());
            registro.setMartes(cbxMartes.isSelected());
            registro.setMiercoles(cbxMiercoles.isSelected());
            registro.setJueves(cbxJueves.isSelected());
            registro.setViernes(cbxViernes.isSelected());
           
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioEntrada());
            procedimiento.setString(3, registro.getHorarioSalida());
            procedimiento.setBoolean(4, registro.getLunes());
            procedimiento.setBoolean(5, registro.getMartes());
            procedimiento.setBoolean(6, registro.getMiercoles());
            procedimiento.setBoolean(7, registro.getJueves());
            procedimiento.setBoolean(8, registro.getViernes());
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
                
                if (tblHorarios.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarHorario(?)}");
                         procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                         procedimiento.execute();
                         listaHorarios.remove(tblHorarios.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }
                 tblHorarios.getSelectionModel().clearSelection();
                     limpiarControles();
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
                tblHorarios.setOnMouseClicked(null);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setOnAction(e -> cancelar());
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                if (!txtEntrada.getText().isEmpty() && !txtSalida.getText().isEmpty()){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tblHorarios.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                }else{
          
          tipoDeOperacion = operaciones.GUARDAR;
          JOptionPane.showMessageDialog(null, "Debe ingresar todos los campos de Horario de Entrada y Salida");
          
      }
                
        }
    }
    
    
 
  
  public void guardar(){
      Horario registro = new Horario();
      registro.setHorarioEntrada(txtEntrada.getText());
      registro.setHorarioSalida(txtSalida.getText());
      registro.setLunes(cbxLunes.isSelected());
      registro.setMartes(cbxMartes.isSelected());
      registro.setMiercoles(cbxMiercoles.isSelected());
      registro.setJueves(cbxJueves.isSelected());
      registro.setViernes(cbxViernes.isSelected());
      
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?,?,?,?,?,?,? )}");
          procedimiento.setString(1, registro.getHorarioEntrada());
          procedimiento.setString(2, registro.getHorarioSalida());
          procedimiento.setBoolean(3, registro.getLunes());
          procedimiento.setBoolean(4, registro.getMartes());
          procedimiento.setBoolean(5, registro.getMiercoles());
          procedimiento.setBoolean(6, registro.getJueves());
          procedimiento.setBoolean(7, registro.getViernes());
          procedimiento.execute();
          //listaPaciente.add(registro);
          
          
          
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
               tblHorarios.setOnMouseClicked(e -> seleccionarElemento());
              
  }
    
    public void desactivarControles(){
        txtEntrada.setEditable(false);
        txtSalida.setEditable(false);
        cbxLunes.setDisable(true);
        cbxMartes.setDisable(true);
        cbxMiercoles.setDisable(true);
        cbxJueves.setDisable(true);
        cbxViernes.setDisable(true);
    }
    
    public void activarControles(){
        txtEntrada.setEditable(true);
        txtSalida.setEditable(true);
        cbxLunes.setDisable(false);
        cbxMartes.setDisable(false);
        cbxMiercoles.setDisable(false);
        cbxJueves.setDisable(false);
        cbxViernes.setDisable(false);
    }
    
    public void limpiarControles(){
        txtEntrada.setText("");
        txtSalida.setText("");
        cbxLunes.setSelected(false);
        cbxMartes.setSelected(false);
        cbxMiercoles.setSelected(false);
        cbxJueves.setSelected(false);
        cbxViernes.setSelected(false);
        
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
