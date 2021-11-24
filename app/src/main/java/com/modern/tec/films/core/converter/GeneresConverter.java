package com.modern.tec.films.core.converter;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GeneresConverter {

    @TypeConverter
    public static int[] fromString(String value) {
        Type arrayType = new TypeToken<int[]>() {
        }.getType();
        return new Gson().fromJson(value, arrayType);
    }

    @TypeConverter
    public static String fromIntArray(int[] list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
