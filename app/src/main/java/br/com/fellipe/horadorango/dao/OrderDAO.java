package br.com.fellipe.horadorango.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.fellipe.horadorango.dao.model.Order;

/**
 * Created by fellipe on 02/06/18.
 */

@Dao
public interface OrderDAO {

    @Query("SELECT * FROM 'order' WHERE userId = :userId")
    List<Order> getOrdersByUser(Integer userId);

    @Insert
    void insert(Order order);

    @Delete
    void delete(Order order);
}
