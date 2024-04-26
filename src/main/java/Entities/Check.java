package Entities;

import java.util.Date;

public class Check {
    String check_number; //NN PK
    String Employee_id_employee; //NN FK
    String CustomerCard_card_number; //N FK
    Date print_date; //NN
    double sum_total; //NN
    double vat; //NN

    public Check(String check_number, String employee_id_employee, String customerCard_card_number, Date print_date, double sum_total, double vat) {
        this.check_number = check_number;
        Employee_id_employee = employee_id_employee;
        CustomerCard_card_number = customerCard_card_number;
        this.print_date = print_date;
        this.sum_total = sum_total;
        this.vat = vat;
    }

    public Check(String employee_id_employee, String customerCard_card_number, Date print_date, double sum_total, double vat) {
        Employee_id_employee = employee_id_employee;
        CustomerCard_card_number = customerCard_card_number;
        this.print_date = print_date;
        this.sum_total = sum_total;
        this.vat = vat;
    }

    public Check(String check_number) {
        this.check_number = check_number;
    }

    public String getCheck_number() {
        return check_number;
    }

    public void setCheck_number(String check_number) {
        this.check_number = check_number;
    }

    public String getEmployee_id_employee() {
        return Employee_id_employee;
    }

    public void setEmployee_id_employee(String employee_id_employee) {
        Employee_id_employee = employee_id_employee;
    }

    public String getCustomerCard_card_number() {
        return CustomerCard_card_number;
    }

    public void setCustomerCard_card_number(String customerCard_card_number) {
        CustomerCard_card_number = customerCard_card_number;
    }

    public Date getPrint_date() {
        return print_date;
    }

    public void setPrint_date(Date print_date) {
        this.print_date = print_date;
    }

    public double getSum_total() {
        return sum_total;
    }

    public void setSum_total(double sum_total) {
        this.sum_total = sum_total;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
}
