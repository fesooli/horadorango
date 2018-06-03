package br.com.fellipe.horadorango.model;

import java.io.Serializable;

/**
 * Created by fellipe on 28/05/18.
 */

public class Menu implements Serializable {

    private Integer id;
    private String item;
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
