package br.com.fellipe.horadorango.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Created by fellipe on 26/05/18.
 */

@Builder
@Data
public class Place implements Serializable{

    private Integer id;
    private String name;
    private String kindOfFood;
    private String image;
    private String phone;
    private String address;
    private List<Menu> menu;

    public String getImage() {
        return image;
    }

    public String getKindOfFood() {
        return kindOfFood;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKindOfFood(String kindOfFood) {
        this.kindOfFood = kindOfFood;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
