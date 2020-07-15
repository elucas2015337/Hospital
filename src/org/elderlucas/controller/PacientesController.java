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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
//import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.elderlucas.bean.ContactoUrgencia;
import org.elderlucas.controller.ContactoUrgenciaController;
import org.elderlucas.bean.Paciente;
import org.elderlucas.db.Conexion;
import org.elderlucas.report.GenerarReporte;
import org.elderlucas.sistema.Principal;


public class PacientesController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    @FXML private ImageView imageContacto;
    @FXML private ImageView imagePacientes;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEdad;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtOcupacion;
    @FXML private TextField txtSexo;
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private DatePicker dateNacimiento;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
//        dateNacimiento = new DatePicker(Locale.ENGLISH);
//        dateNacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//        dateNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
//        dateNacimiento.getCalendarView().setShowWeeks(false);
//        dateNacimiento.getStylesheets().add("/org.elderlucas/resource/DatePicker.css");
//        grpFecha.add(fecha, 0, 0);
        
    }
    
    public void image1(){
        imagePacientes.setFitHeight(imagePacientes.getFitHeight() + 10);
        imagePacientes.setFitWidth(imagePacientes.getFitWidth()+ 10);
    }
    public void image2(){
        imagePacientes.setFitHeight(imagePacientes.getFitHeight() - 10);
        imagePacientes.setFitWidth(imagePacientes.getFitWidth() - 10);
    }
    
    public void image3(){
        imageContacto.setFitHeight(imageContacto.getFitHeight() + 10);
        imageContacto.setFitWidth(imageContacto.getFitWidth()+ 10);
    }
    public void image4(){
        imageContacto.setFitHeight(imageContacto.getFitHeight() - 10);
        imageContacto.setFitWidth(imageContacto.getFitWidth() - 10);
    }
    
    public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        
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
    
    public void SoloNumerosEnteros(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (!Character.isDigit(key))
            keyEvent.consume();
        
        if(txtDPI.getText().length() > 13)
            keyEvent.consume();
        
        if(txtDPI.getText().length() < 13)
            btnNuevo.setDisable(true);
        else
            btnNuevo.setDisable(false);

    } catch (Exception ex){ }
}

    
    public void seleccionarElemento(){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
        txtDPI.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI());
        txtApellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
        txtNombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
        dateNacimiento.setValue(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento().toLocalDate());
        txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
        txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
        txtOcupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
        txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
        }else{
            tblPacientes.getSelectionModel().clearSelection();
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
     
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblPacientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tblPacientes.setOnMouseClicked(null);
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
                btnReporte.setOnAction(e -> generarReporte());
                btnNuevo.setDisable(false);
                tblPacientes.setOnMouseClicked(e -> seleccionarElemento());
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
     
     public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarPaciente(?,?,?,?,?,?,?,?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            
            registro.setDPI(txtDPI.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNombres(txtNombres.getText());
            registro.setFechaNacimiento(java.sql.Date.valueOf(dateNacimiento.getValue()));
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNombres());
            procedimiento.setDate(5, registro.getFechaNacimiento());
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
            actualizarPacientes();
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
                
                if (tblPacientes.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarPaciente(?)}");
                         procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                         procedimiento.execute();
                         listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }
                 tblPacientes.getSelectionModel().clearSelection();
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
                tblPacientes.setOnMouseClicked(null);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setOnAction(e -> cancelar());
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = PacientesController.operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                
                if (!txtDPI.getText().equals("") && !txtNombres.getText().equals("") && !txtApellidos.getText().equals("") && dateNacimiento.getValue()!= null && !txtSexo.getText().equals("") && !txtOcupacion.getText().equals("") && !txtDireccion.getText().equals("")){
                guardar();
                desactivarControles();
                limpiarControles();
                tblPacientes.setOnMouseClicked(e -> seleccionarElemento());
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                escenarioPrincipal.ventanaContactoUrgencia();
                ContactoUrgenciaController nuevo = new ContactoUrgenciaController();
                nuevo.guardarPaciente();
                }else{
          
          tipoDeOperacion = operaciones.GUARDAR;
          JOptionPane.showMessageDialog(null, "Debe ingresar todos los campos correspondientes");
          
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
              btnReporte.setOnAction(e -> generarReporte());
              tipoDeOperacion = operaciones.NINGUNO;
              limpiarControles();
              btnEliminar.setOnAction(e -> eliminar());
              tblPacientes.getSelectionModel().clearSelection();
              tblPacientes.setOnMouseClicked(e -> seleccionarElemento());
  }
  
  public void guardar(){
      Paciente registro = new Paciente();
      registro.setDPI(txtDPI.getText());
      registro.setApellidos(txtApellidos.getText());
      registro.setNombres(txtNombres.getText());
      registro.setFechaNacimiento(java.sql.Date.valueOf(dateNacimiento.getValue()));
      registro.setDireccion(txtDireccion.getText());
      registro.setOcupacion(txtOcupacion.getText());
      registro.setSexo(txtSexo.getText());
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?,? )}");
          procedimiento.setString(1, registro.getDPI());
          procedimiento.setString(2, registro.getApellidos());
          procedimiento.setString(3, registro.getNombres());
          procedimiento.setDate(4, registro.getFechaNacimiento());
          procedimiento.setString(5, registro.getDireccion());
          procedimiento.setString(6, registro.getOcupacion());
          procedimiento.setString(7, registro.getSexo());
          procedimiento.execute();
          //listaPaciente.add(registro);
          
          actualizarPacientes();
          
      }catch(Exception e){
          e.printStackTrace();
      }
  }
  
  public void generarReporte(){
      switch (tipoDeOperacion){
          case NINGUNO:
              imprimirReporte();
              limpiarControles();
              
              break;
          
      }
  }
  
  public void imprimirReporte(){
      Map parametros = new HashMap();
      parametros.put("codigoPaciente", null);
      GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
  }
  
  public void actualizarPacientes(){
      try{
      PreparedStatement edades = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarPacientes()}");
          edades.execute();
      }catch(Exception e){
          e.printStackTrace();
      }
  }
    
    public void desactivarControles(){
        txtDPI.setDisable (true);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false); 
        dateNacimiento.setDisable(true);
        dateNacimiento.setEditable(false);
        txtEdad.setEditable(false);
        txtDireccion.setEditable(false);
        txtOcupacion.setEditable(false);
        txtSexo.setEditable(false);
    }
    
    public void activarControles(){
        txtDPI.setDisable (false);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true); 
        dateNacimiento.setDisable(false);
        dateNacimiento.setEditable(true);
        txtEdad.setEditable(false);
        txtDireccion.setEditable(true);
        txtOcupacion.setEditable(true);
        txtSexo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtApellidos.setText("");
        txtNombres.setText("");
        dateNacimiento.setValue(null);
        txtEdad.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtSexo.setText("");
        
    }
    
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
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
