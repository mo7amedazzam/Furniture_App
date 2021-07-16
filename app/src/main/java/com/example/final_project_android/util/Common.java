package com.example.final_project_android.util;


import com.example.final_project_android.models.CategoryModel;
import com.example.final_project_android.models.FurnitureModel;
import com.example.final_project_android.models.UserModel;

import java.util.Random;

public class Common {
    public static final String USER_REFERENCES = "Users";
    public static final String CATEGORY_REF = "Category";
    public static UserModel currentUser;
    public static CategoryModel categorySelected;
    public static FurnitureModel selectFurniture;
    public static final String ORDER_REF ="Order" ;


    public static String createOrderNumber() {
        return new StringBuilder()
                .append(System.currentTimeMillis())// Get current time
                .append(Math.abs(new Random().nextInt())) // Add random number to block same order at same time
                .toString();
    }

}
