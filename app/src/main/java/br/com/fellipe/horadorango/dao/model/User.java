package br.com.fellipe.horadorango.dao.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by fellipe on 01/06/18.
 */

@Entity(foreignKeys = @ForeignKey(entity = Address.class,
        parentColumns = "id",
        childColumns = "addressId",
        onDelete = CASCADE))
public class User {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String username;
    private Integer addressId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
