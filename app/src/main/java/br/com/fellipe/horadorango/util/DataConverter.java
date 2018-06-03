package br.com.fellipe.horadorango.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by fellipe on 02/06/18.
 */

public class DataConverter {

    @TypeConverter
    public String fromStringList(List<String> items) {
        if (items == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        String json = gson.toJson(items, type);
        return json;
    }

    @TypeConverter
    public List<String> toStringList(String item) {
        if (item == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> countryLangList = gson.fromJson(item, type);
        return countryLangList;
    }
}
