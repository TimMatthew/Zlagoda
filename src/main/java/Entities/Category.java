package Entities;

import services.CategoryService;

public class Category {
    int category_number; //NN PK
    String category_name; //NN
    int products_count; //NN
    int available_products_count;

    public Category(String category_name) {
        this.category_number = new CategoryService().getNewCategoryID();
        this.category_name = category_name;
        this.products_count = 0;
        this.available_products_count = 0;
    }
    public Category(int category_number, String category_name) {
        this.category_number = category_number;
        this.category_name = category_name;
        this.products_count = 0;
        this.available_products_count = 0;
    }

    public int getCategory_number() {
        return category_number;
    }

    public void setCategory_number(int category_number) {
        this.category_number = category_number;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getProducts_count() {
        return products_count;
    }

    public void setProducts_count(int count) {
        this.products_count = count;
    }

    public int getAvailableProducts_count() {
        return available_products_count;
    }

    public void setAvailableProducts_count(int count) {
        this.available_products_count = count;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_number=" + category_number +
                ", category_name='" + category_name + '\'' +
                ", products_count=" + products_count +
                ", available_products_count=" + available_products_count +
                '}';
    }
}
