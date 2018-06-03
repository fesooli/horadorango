package br.com.fellipe.horadorango.dao.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fellipe on 31/05/18.
 */

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @SerializedName("cep")
    private String cep;
    @SerializedName("logradouro")
    private String street;
    private Integer number;
    @SerializedName("bairro")
    private String neighborhood;
    @SerializedName("localidade")
    private String city;
    @SerializedName("uf")
    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
