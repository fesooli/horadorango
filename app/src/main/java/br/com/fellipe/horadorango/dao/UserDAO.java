package br.com.fellipe.horadorango.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.fellipe.horadorango.dao.model.User;

/**
 * Created by fellipe on 01/06/18.
 */

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    User findByUsername(String username);

    @Insert
    Long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
