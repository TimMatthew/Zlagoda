package Entities;


import java.util.UUID;

public class Category {
    int category_number; //NN PK
    String category_name; //NN
    int products_count; //NN

    public Category(String category_name) {
        this.category_number = 0;
        this.category_name = category_name;
    }
    public Category(int category_number, String category_name) {
        this.category_number = category_number;
        this.category_name = category_name;
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
        return 0;
    }
}
