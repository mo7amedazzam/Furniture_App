package com.example.final_project_android.database;

public class Utils {
    public static final int DATABASE_VERSION = 5;
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String NAME_DATABASE = "cart_db";
    public static final String NAME_TABLE = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";

    public static final String SQL ="CREATE TABLE IF NOT EXISTS "
            + Utils.NAME_TABLE + " (" +
            Utils.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Utils.COLUMN_NAME + " TEXT," +
            Utils.COLUMN_PRICE + " REAL," +
            Utils.COLUMN_QUANTITY + " INTEGER," +
            Utils.COLUMN_IMAGE + " TEXT" + ");";
}
