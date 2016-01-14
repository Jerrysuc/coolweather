package com.igodiva.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by su11 on 2016/1/13.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * Province 省份
     */
    public static final String CREATE_PROVINCE = "create table cw_province(\n" +
            "\tid int primary key autoincrement,\n" +
            "\tprovince_name varchar(50),\n" +
            "\tprovince_code varchar(50)\n" +
            ");";

    /**
     * City 城市
     */
    public static final String CREATE_CITY = "create table cw_city(\n" +
            "\tid int primary key autoincrement,\n" +
            "\tcity_name varchar(100),\n" +
            "\tcity_code varchar(50),\n" +
            "\tprovince_id int\n" +
            ");";

    /**
     * County 县
     */
    public static final String CREATE_COUNTY = "create table cw_county(\n" +
            "\tid int primary key autoincrement,\n" +
            "\tcountry_name varchar(100),\n" +
            "\tcountry_code varchar(50),\n" +
            "\tcity_id int\n" +
            ");";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
