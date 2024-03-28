public class Store_Product {
    String Store_Product_UPC; //NN PK
    String Sale_UPC_prom; //N FK лінк або на 'Товар у маґазині', або на Sale?
    int Product_id_product; //NN FK
    double selling_price; //NN Decimal(3,14)???
    int products_number; //NN
    boolean promotional_product; //NN Чи є акція(знижка)

}
