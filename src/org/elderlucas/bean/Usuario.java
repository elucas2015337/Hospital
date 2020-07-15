package org.elderlucas.bean;

import java.sql.Date;
import java.sql.Time;

public class Usuario {
    private int codigoUsuario;
    private String usuarioLogin;
    private String usuarioContrasena;
    private boolean usuarioEstado;
    private Date usuarioFecha;
    private Time usuarioHora;
    private int codigoTipoUsuario;

    public Usuario() {
    }

    public Usuario(int codigoUsuario, String usuarioLogin, String usuarioContrasena, boolean usuarioEstado, Date usuarioFecha, Time usuarioHora, int codigoTipoUsuario) {
        this.codigoUsuario = codigoUsuario;
        this.usuarioLogin = usuarioLogin;
        this.usuarioContrasena = usuarioContrasena;
        this.usuarioEstado = usuarioEstado;
        this.usuarioFecha = usuarioFecha;
        this.usuarioHora = usuarioHora;
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getUsuarioContrasena() {
        return usuarioContrasena;
    }

    public void setUsuarioContrasena(String usuarioContrasena) {
        this.usuarioContrasena = usuarioContrasena;
    }

    public boolean isUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public Date getUsuarioFecha() {
        return usuarioFecha;
    }

    public void setUsuarioFecha(Date usuarioFecha) {
        this.usuarioFecha = usuarioFecha;
    }

    public Time getUsuarioHora() {
        return usuarioHora;
    }

    public void setUsuarioHora(Time usuarioHora) {
        this.usuarioHora = usuarioHora;
    }

    public int getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(int codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }
    
    

}
