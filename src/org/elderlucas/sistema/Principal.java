package org.elderlucas.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.elderlucas.bean.Horario;
import org.elderlucas.controller.AreasController;
import org.elderlucas.controller.CargosController;
import org.elderlucas.controller.ContactoUrgenciaController;
import org.elderlucas.controller.EspecialidadesController;
import org.elderlucas.controller.HorariosController;
import org.elderlucas.controller.InformacionController;
import org.elderlucas.controller.LoginController;
import org.elderlucas.controller.MedicoEspecialidadController;
import org.elderlucas.controller.MedicosController;
import org.elderlucas.controller.MenuPrincipalController;
import org.elderlucas.controller.PacientesController;
import org.elderlucas.controller.RegistroController;
import org.elderlucas.controller.ResponsableTurnoController;
import org.elderlucas.controller.TelefonosMedicoController;
import org.elderlucas.controller.TurnosController;

/**
 *
 * @author programacion
 */
public class Principal extends Application{

    private final String PAQUETE_VISTA = "/org/elderlucas/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital Healty Life");
        escenarioPrincipal.getIcons().add(new Image("/org/elderlucas/images/logo.jpg"));
        ventanaLogin();
        escenarioPrincipal.show();
 
    }
    public void ventanaLogin (){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 390, 293);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaRegistro(){
        try{
            RegistroController registro = (RegistroController)cambiarEscena("RegisterView.fxml", 958, 509);
            registro.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal (){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 715, 434);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void ventanaMedicos(){
        try{
           MedicosController medicos = (MedicosController) cambiarEscena("MedicoView.fxml", 993, 680);
           medicos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void ventanaTelefonosMedico(){
        try{
           TelefonosMedicoController telefono = (TelefonosMedicoController) cambiarEscena("TelefonoMedicoView.fxml", 662, 523);
           telefono.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void infoProgramador(){
        try{
            InformacionController info = (InformacionController)cambiarEscena("ProgramadorView.fxml", 340, 400);
            info.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
    }
    }    
   
    public void ventanaPacientes(){
        try{
           PacientesController pacientes = (PacientesController) cambiarEscena("PacientesView.fxml", 1262, 600);
           pacientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorarios(){
        try{
           HorariosController horario = (HorariosController) cambiarEscena("HorariosView.fxml", 971, 600);
           horario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTurnos(){
        try{
           TurnosController horario = (TurnosController) cambiarEscena("TurnoView.fxml", 823, 579);
           horario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaContactoUrgencia(){
        try{
           ContactoUrgenciaController urgencia = (ContactoUrgenciaController) cambiarEscena("ContactoUrgenciaView.fxml", 971, 600);
           urgencia.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidades(){
        try{
           EspecialidadesController especialidades = (EspecialidadesController) cambiarEscena("EspecialidadesView.fxml", 578, 449);
           especialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaResponsableTurno(){
        try{
           ResponsableTurnoController responsable = (ResponsableTurnoController) cambiarEscena("ResponsableTurnoView.fxml", 971, 600);
           responsable.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicoEspecialidad(){
        try{
           MedicoEspecialidadController medicoEspecialidad = (MedicoEspecialidadController) cambiarEscena("MedicosEspecialidadView.fxml", 709, 515);
           medicoEspecialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaAreas(){
        try{
           AreasController areas = (AreasController) cambiarEscena("AreasView.fxml", 555, 449);
           areas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCargos(){
        try{
           CargosController cargos = (CargosController) cambiarEscena("CargosView.fxml", 555, 449);
           cargos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Initializable cambiarEscena(String fxml, int ancho, int alto)throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
