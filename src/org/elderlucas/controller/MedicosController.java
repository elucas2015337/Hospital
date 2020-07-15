package org.elderlucas.controller;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.elderlucas.bean.Medico;
import org.elderlucas.db.Conexion;
import org.elderlucas.report.GenerarReporte;
import org.elderlucas.sistema.Principal;


public class MedicosController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    @FXML private ImageView imageMedico;
    @FXML private ImageView imageTelefonoMedico;
    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtTurno;
    @FXML private TextField txtSexo;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    public void image1(){
        imageMedico.setFitHeight(imageMedico.getFitHeight() + 10);
        imageMedico.setFitWidth(imageMedico.getFitWidth()+ 10);
    }
    public void image2(){
        imageMedico.setFitHeight(imageMedico.getFitHeight() - 10);
        imageMedico.setFitWidth(imageMedico.getFitWidth() - 10);
    }
    
    public void image3(){
        imageTelefonoMedico.setFitHeight(imageTelefonoMedico.getFitHeight() + 10);
        imageTelefonoMedico.setFitWidth(imageTelefonoMedico.getFitWidth()+ 10);
    }
    public void image4(){
        imageTelefonoMedico.setFitHeight(imageTelefonoMedico.getFitHeight() - 10);
        imageTelefonoMedico.setFitWidth(imageTelefonoMedico.getFitWidth() - 10);
    }
    
    public void SoloNumerosEnteros(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (!Character.isDigit(key))
            keyEvent.consume();
        
        if(txtLicenciaMedica.getText().length() > 8)
            keyEvent.consume();
        
        if(txtLicenciaMedica.getText().length() < 8)
            btnNuevo.setDisable(true);
        else
            btnNuevo.setDisable(false);

    } catch (Exception ex){ }
}
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("CodigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("licenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaSalida"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
        
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

    
    public void seleccionarElemento(){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
        txtLicenciaMedica.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombres.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtHoraEntrada.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        txtHoraSalida.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
        txtTurno.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
        txtSexo.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
        }else{
            tblMedicos.getSelectionModel().clearSelection();
        }
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
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblMedicos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tblMedicos.setOnMouseClicked(null);
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
                btnEliminar.setDisable(false);
                tblMedicos.setOnMouseClicked(e -> seleccionarElemento());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_editarMedico(?,?,?,?,?,?,?)}");
            Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
            registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setHoraEntrada(txtHoraEntrada.getText());
            registro.setHoraSalida(txtHoraSalida.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getLicenciaMedica());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getHoraEntrada());
            procedimiento.setString(6, registro.getHoraSalida());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
            desactivarControles();
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
                
                if (tblMedicos.getSelectionModel().getSelectedItem() != null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminar el registro?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento =Conexion.getInstancia().getConexion().prepareCall("{call sp_eliminarMedico(?)}");
                         procedimiento.setInt(1, ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                         procedimiento.execute();
                         listaMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                 }else{
                     tblMedicos.getSelectionModel().clearSelection();
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
                tblMedicos.setOnMouseClicked(null);
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
                if (!txtLicenciaMedica.getText().equals("") && !txtNombres.getText().equals("") && !txtApellidos.getText().equals("") && !txtHoraEntrada.getText().equals("") && !txtHoraSalida.getText().equals("") && !txtSexo.getText().equals("")){
                guardar();
                desactivarControles();
                limpiarControles();
                tblMedicos.setOnMouseClicked(e -> seleccionarElemento());
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                JOptionPane.showMessageDialog(null, "A continuacion asigne un teléfono, un horario y una especialidad para el último médico agregado");
                escenarioPrincipal.ventanaTelefonosMedico();
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
              tblMedicos.getSelectionModel().clearSelection();
              tblMedicos.setOnMouseClicked(e -> seleccionarElemento());
  }
  
  public void guardar(){
      
          Medico registro = new Medico();
          registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
          registro.setNombres(txtNombres.getText());
          registro.setApellidos(txtApellidos.getText());
          registro.setHoraEntrada(txtHoraEntrada.getText());
          registro.setHoraSalida(txtHoraSalida.getText());
          registro.setSexo(txtSexo.getText());
          try{
              PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico(?,?,?,?,?,?)}");
              procedimiento.setInt(1, registro.getLicenciaMedica());
              procedimiento.setString(2, registro.getNombres());
              procedimiento.setString(3, registro.getApellidos());
              procedimiento.setString(4, registro.getHoraEntrada());
              procedimiento.setString(5, registro.getHoraSalida());
              procedimiento.setString(6, registro.getSexo());
              procedimiento.execute();
              //listaMedico.add(registro);
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
      parametros.put("codigoMedico", null);
      GenerarReporte.mostrarReporte("ReporteMedicos.jasper", "Reporte de Medicos", parametros);
  }
  
  
    
    public void desactivarControles(){
        txtLicenciaMedica.setDisable (true);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtTurno.setEditable(false);
        txtSexo.setEditable(false);
    }
    
    public void activarControles(){
        txtLicenciaMedica.setDisable(false);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
         txtTurno.setEditable(false);
        txtSexo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        txtTurno.setText("");
        txtSexo.setText("");
        
    }
    
    public void ventanaTelefonosMedico(){
        escenarioPrincipal.ventanaTelefonosMedico();
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
