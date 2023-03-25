package com.example.costcalculator30;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters
{
    @TypeConverter
    public static ArrayList<Tower> fromString(String value)
    {
        Type arrayListType = new TypeToken<ArrayList<Tower>>() {}.getType();
        return new Gson().fromJson(value, arrayListType);
    }

    @TypeConverter
    public static String arrayListToString(ArrayList<Tower> towers)
    {
        Gson gson = new Gson();
        return gson.toJson(towers);
    }
}
