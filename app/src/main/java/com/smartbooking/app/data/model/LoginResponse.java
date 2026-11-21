package com.smartbooking.app.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("id_usuario")
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String access;
    private String refresh;
    private String mensaje;

    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getAccess() { return access; }
    public String getRefresh() { return refresh; }
    public String getMensaje() { return mensaje; }
}