package com.example.final_project_android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.example.final_project_android.models.FurnitureModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Utils.NAME_DATABASE, null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utils.SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utils.NAME_TABLE);
        onCreate(db);
    }

    public long createItem(FurnitureModel furnitureModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(Utils.COLUMN_NAME, furnitureModel.getName());
        initialValues.put(Utils.COLUMN_PRICE, furnitureModel.getPrice());
        initialValues.put(Utils.COLUMN_IMAGE, furnitureModel.getImage());
        initialValues.put(String.valueOf(Utils.COLUMN_QUANTITY), furnitureModel.getQuantity());
        return db.insert(Utils.NAME_TABLE, null, initialValues);
    }

    public boolean deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        int doneDelete = 0;
        doneDelete = db.delete(Utils.NAME_TABLE, null, null);
        Log.w("doneDelete", Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public long getFurnitureCount() {
        SQLiteDatabase db = this.getWritableDatabase();

        return DatabaseUtils.queryNumEntries(db, Utils.NAME_TABLE);
    }

    public int getTotalItemsCount() {
        String countQuery = "SELECT  * FROM " + Utils.NAME_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public double getAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + Utils.COLUMN_PRICE + ") FROM " + Utils.NAME_TABLE, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            Log.d("total1", String.valueOf(total));
        }
        Log.d("total", String.valueOf(total));
        return total;
    }

    public int getQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + Utils.COLUMN_QUANTITY + ") FROM " + Utils.NAME_TABLE ,null );
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
            Log.d("total1", String.valueOf(total));
        }
        Log.d("total", String.valueOf(total));
        return total;
    }

    public double getAmountFull(){
        double total =getAmount()*getQuantity();
        Log.d("total2", String.valueOf(total));
        return total;

    }

    public ArrayList<FurnitureModel> getAllFurniture() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<FurnitureModel> furnitureList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utils.NAME_TABLE, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(Utils.COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(Utils.COLUMN_PRICE));
                String image = cursor.getString(cursor.getColumnIndex(Utils.COLUMN_IMAGE));
                int quantity = cursor.getInt(cursor.getColumnIndex(String.valueOf(Utils.COLUMN_QUANTITY)));

                FurnitureModel furnitureModel = new FurnitureModel(name, image, price, quantity);
                furnitureList.add(furnitureModel);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return furnitureList;
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Utils.NAME_TABLE);
    }
}
