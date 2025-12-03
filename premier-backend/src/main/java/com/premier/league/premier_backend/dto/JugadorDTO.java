package com.premier.league.premier_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JugadorDTO {
    private String id;
    private String idJugadorApi;
    private String nombre;
    private String name;
    private String position;
    private String posicion;
    private Integer numero;
    private String nacionalidad;
    private String countryOfBirth;
    private String dateOfBirth;
    private String fechaNacimiento;

    public JugadorDTO() {
    }

    public JugadorDTO(String id, String nombre, String position) {
        this.id = id;
        this.idJugadorApi = id;
        this.nombre = nombre;
        this.name = nombre;
        this.position = position;
        this.posicion = position;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.idJugadorApi = id;
    }

    public String getIdJugadorApi() {
        return idJugadorApi;
    }

    public void setIdJugadorApi(String idJugadorApi) {
        this.idJugadorApi = idJugadorApi;
    }

    public String getNombre() {
        return nombre != null ? nombre : name;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (this.nombre == null) {
            this.nombre = name;
        }
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
