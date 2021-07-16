package com.example.final_project_android.models;

public class FurnitureModel {
    private String Key;
    private String name, image, id, description;
    private Double price;



    private int Quantity =1;

    public FurnitureModel(String name, String image, Double price, int Quantity) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.Quantity = Quantity;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public FurnitureModel() {
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
