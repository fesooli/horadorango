package br.com.fellipe.horadorango.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.fellipe.horadorango.dao.model.Address;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.dao.model.User;

/**
 * Created by fellipe on 01/06/18.
 */

@Database(entities = {User.class, Address.class, Order.class}, version = 2)
public abstract class HoraDoRangoDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "HoraDoRangoDatabase";

    public abstract AddressDAO addressDAO();

    public abstract UserDAO userDAO();

    public abstract OrderDAO orderDAO();
}
