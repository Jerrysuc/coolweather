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
    public static final String CREATE_PROVINCE = "create table Province(\n" +
            "\t id integer primary key autoincrement,\n" +
            "\t province_name varchar(50),\n" +
            "\t province_code varchar(50),\n" +
            "\t pyName varchar(50)\n" +
            ")";

    /**
     * City 城市
     */
    public static final String CREATE_CITY = "create table City(\n" +
            "\t id integer primary key autoincrement,\n" +
            "\t city_name varchar(100),\n" +
            "\t city_code varchar(50),\n" +
            "\t pyName varchar(50),\n" +
            "\t province_id integer\n" +
            ")";

    /**
     * County 县
     */
    public static final String CREATE_COUNTY = "create table County(\n" +
            "\t id integer primary key autoincrement,\n" +
            "\t country_name varchar(100),\n" +
            "\t country_code varchar(50),\n" +
            "\t pyName varchar(50),\n" +
            "\t city_id integer\n" +
            ")";

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
