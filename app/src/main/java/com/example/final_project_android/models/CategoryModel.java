package com.example.final_project_android.models;

import java.util.List;

public class CategoryModel {

    private String menu_id, name, image;
    private List<FurnitureModel> furniture;

    public CategoryModel() {
    }


    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<FurnitureModel> getFurniture() {
        return furniture;
    }

    public void setFurniture(List<FurnitureModel> furniture) {
        this.furniture = furniture;
    }
}
