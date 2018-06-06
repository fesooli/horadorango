package br.com.fellipe.horadorango.dao.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

import br.com.fellipe.horadorango.util.DataConverter;

/**
 * Created by fellipe on 01/06/18.
 */

@Entity
public class Order implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer userId;
    private Integer deliveryAddressId;
    private Integer placeId;
    private String paymentForm;
    @TypeConverters(DataConverter.class)
    private List<String> items;
    private Double totalPrice;
    private String urlPlaceImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Integer deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(String paymentForm) {
        this.paymentForm = paymentForm;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getUrlPlaceImage() {
        return urlPlaceImage;
    }

    public void setUrlPlaceImage(String urlPlaceImage) {
        this.urlPlaceImage = urlPlaceImage;
    }
}
