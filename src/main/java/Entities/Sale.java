package Entities;

public class Sale {
    String Store_Product_UPC; //NN  PPK, FK1
    String Check_check_number; //NN PPK, FK2
    int product_number; //NN
    double selling_price; //NN Тип даних в RM - Decimal (13,4), що б це не означало...
    String product_name;

    public Sale(String store_Product_UPC, String check_check_number, int product_number, double selling_price) {
        Store_Product_UPC = store_Product_UPC;
        Check_check_number = check_check_number;
        this.product_number = product_number;
        this.selling_price = selling_price;
    }

    public String getStore_Product_UPC() {
        return Store_Product_UPC;
    }

    public void setStore_Product_UPC(String store_Product_UPC) {
        Store_Product_UPC = store_Product_UPC;
    }

    public String getCheck_check_number() {
        return Check_check_number;
    }

    public void setCheck_check_number(String check_check_number) {
        Check_check_number = check_check_number;
    }

    public int getProduct_number() {
        return product_number;
    }

    public void setProduct_number(int product_number) {
        this.product_number = product_number;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getTotal_price() {
        return selling_price * product_number;
    }

    @Override
    public String toString() {
        return product_name + ": " + selling_price + " * " + product_number + " = " + getTotal_price() + ";";
    }
}
