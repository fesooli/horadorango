package br.com.fellipe.horadorango.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.fellipe.horadorango.dao.model.Address;

/**
 * Created by fellipe on 01/06/18.
 */

@Dao
public interface AddressDAO {

    @Query("SELECT * FROM address")
    List<Address> getAllAddress();

    @Query("SELECT * FROM address WHERE street LIKE :street LIMIT 1")
    Address findByAddressStreet(String street);

    @Query("SELECT * FROM address WHERE id LIKE :addressId LIMIT 1")
    Address findByAddressId(Integer addressId);

    @Insert
    Long insert(Address addresses);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);
}
