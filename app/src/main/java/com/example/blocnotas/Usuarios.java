package com.example.blocnotas;

import java.io.Serializable;

public class Usuarios implements Serializable {

    private String url;
    private String name1;
    private  String telefono;
    private  String longitud;
    private  String latitud;
    private  String key;


    public Usuarios(String name1, String telefono, String longitud, String latitud,String url, String key ) {
        this.name1 = name1;
        this.telefono = telefono;
        this.longitud = longitud;
        this.latitud = latitud;
        this.url = url;
        this.key = key;
    }
    Usuarios() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getUrl() {
        return url;
    }

    public String getName1() {
        return name1;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }
}
