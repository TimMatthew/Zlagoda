package Entities;

import java.util.Date;

public class Check {
    String check_number; //NN PK
    String Employee_id_employee; //NN FK
    String CustomerCard_card_number; //N FK
    Date print_date; //NN
    double sum_total; //NN
    double vat; //NN

}
