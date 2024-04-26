package Entities;

import main.MainMenu;
import services.ProductService;
import utils.UPC;

public class Store_Product {
    String Store_Product_UPC; //NN PK
    String Sale_UPC_prom; //N FK лінк або на 'Товар у маґазині', або на Sale?
    int Product_id_product; //NN FK
    String productName;
    double selling_price; //NN Decimal(3,14)???
    int products_number; //NN
    boolean promotional_product; //NN Чи є акція(знижка)

    public Store_Product(String store_Product_UPC, String sale_UPC_prom, int product_id_product, double selling_price, int products_number, boolean promotional_product) {
        Store_Product_UPC = store_Product_UPC;
        Sale_UPC_prom = sale_UPC_prom;
        Product_id_product = product_id_product;
        this.selling_price = selling_price;
        this.products_number = products_number;
        this.promotional_product = promotional_product;
    }

    public Store_Product(String sale_UPC_prom, int product_id_product, double selling_price, int products_number, boolean promotional_product) {
        this(UPC.generateRandomUPC(), sale_UPC_prom, product_id_product, selling_price, products_number, promotional_product);
    }

    public Store_Product(int product_id_product, String store_Product_UPC, double selling_price, int products_number, int promotional_product) {
        this.Product_id_product = product_id_product;
        this.productName = getProductName();
        this.Store_Product_UPC = store_Product_UPC;
        this.selling_price = selling_price;
        this.products_number = products_number;
        this.promotional_product = promotional_product == 1;
    }

    public String getStore_Product_UPC() {
        return Store_Product_UPC;
    }

    public void setStore_Product_UPC(String store_Product_UPC) {
        Store_Product_UPC = store_Product_UPC;
    }

    public String getSale_UPC_prom() {
        return Sale_UPC_prom;
    }

    public void setSale_UPC_prom(String sale_UPC_prom) {
        Sale_UPC_prom = sale_UPC_prom;
    }

    public int getProduct_id_product() {
        return Product_id_product;
    }

    public void setProduct_id_product(int product_id_product) {
        Product_id_product = product_id_product;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public int getProducts_number() {
        return products_number;
    }

    public void setProducts_number(int products_number) {
        this.products_number = products_number;
    }

    public boolean isPromotional_product() {
        return promotional_product;
    }

    public void setPromotional_product(boolean promotional_product) {
        this.promotional_product = promotional_product;
    }

    public String getProductName() {
        productName = MainMenu.productMapGetName.get(Product_id_product);
        return productName;
    }

    @Override
    public String toString() {
        return "Store_Product{" +
                "Store_Product_UPC='" + Store_Product_UPC + '\'' +
                ", Sale_UPC_prom='" + Sale_UPC_prom + '\'' +
                ", Product_id_product=" + Product_id_product +
                ", productName='" + productName + '\'' +
                ", selling_price=" + selling_price +
                ", products_number=" + products_number +
                ", promotional_product=" + promotional_product +
                '}';
    }
}
