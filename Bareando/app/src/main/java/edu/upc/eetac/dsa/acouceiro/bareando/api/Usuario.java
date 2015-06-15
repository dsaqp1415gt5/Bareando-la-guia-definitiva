package edu.upc.eetac.dsa.acouceiro.bareando.api;


import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private Boolean loginsuccesful;
    private String nick;
    private String mail;
    private String nombre;
    private String pass;
    private Boolean Successful;
    private String existe;


    public void register (String nick, String mail, String nombre) {
        this.nick = nick;
        this.mail = mail;
        this.nombre = nombre;
    }

    public String getExiste() {
        return existe;
    }

    public void setExiste(String existe) {
        this.existe = existe;
    }

    public Boolean getSuccessful() {
        return Successful;
    }

    public void setSuccessful(Boolean successful) {
        Successful = successful;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Boolean getLoginsuccesful() {
        return loginsuccesful;
    }

    public void setLoginsuccesful(Boolean loginsuccesful) {
        this.loginsuccesful = loginsuccesful;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
