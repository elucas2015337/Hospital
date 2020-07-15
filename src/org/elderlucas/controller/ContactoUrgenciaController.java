package org.elderlucas.controller;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
import org.elderlucas.bean.ContactoUrgencia;
import org.elderlucas.bean.Paciente;
import org.elderlucas.db.Conexion;
import org.elderlucas.sistema.Principal;

public class ContactoUrgenciaController implements Initializable{
    
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, CANCELAR, REPORTE, ACTUALIZAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <ContactoUrgencia> listaContactoUrgencia;
    private ObservableList <Paciente> listaPaciente;
    
    @FXML private TextField txtNombres;
    @FXML private TextField txtContacto;
    @FXML private TextField txtApellidos;
    @FXML private TableView tblContactos;
    @FXML private TableColumn colNumero;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colApellidos;
    @FXML private ImageView imageContactoUrgencia;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void cargarDatos(){
        tblContactos.setItems(getContactos());
        colCodigo.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer> ("codigoContactourgencia"));
        colApellidos.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("apellidos"));
        colNombres.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("nombres"));
        colNumero.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer>("codigoPaciente"));
    }
    
    
    
    public ObservableList <ContactoUrgencia> getContactos(){
        ArrayList <ContactoUrgencia> lista = new ArrayList <ContactoUrgencia>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarContactos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                                               resultado.getString("apellidos"),
                                               resultado.getString("nombres"),
                                               resultado.getString("numeroContacto"),
                                               resultado.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaContactoUrgencia = FXCollections.observableList(lista);
    }
    
    public ObservableList <Paciente> getPacientes(){
        ArrayList <Paciente> lista = new ArrayList <Paciente>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Paciente (resultado.getInt("codigoPaciente"),
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
    
    public void activarControles(){
        txtApellidos.setDisable(false);
        txtApellidos.setEditable(true);
        txtNombres.setDisable(false);
        txtNombres.setEditable(true);
        txtContacto.setDisable(false);
        txtContacto.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
        
    }
    
    public void desactivarControles(){
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtContacto.setDisable(true);
        cmbCodigoPaciente.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}