package Entities;

import java.util.UUID;

public class Customer_Card {
    String card_number; //NN PK
    String cust_surname; //NN
    String cust_name; //NN
    String cust_patronymic; //N
    String phone_number; //NN
    String city; //N
    String street; //N
    String zip_code; //N
    int percent; //NN

    public Customer_Card(String cust_surname, String cust_name, String cust_patronymic, String phone_number, String city, String street, String zip_code, int percent) {
        this.card_number = UUID.randomUUID().toString();
        this.cust_surname = cust_surname;
        this.cust_name = cust_name;
        this.cust_patronymic = cust_patronymic;
        this.phone_number = phone_number;
        this.city = city;
        this.street = street;
        this.zip_code = zip_code;
        this.percent = percent;
    }

    public Customer_Card(String card_number, String cust_surname, String cust_name, String cust_patronymic, String phone_number, String city, String street, String zip_code, int percent) {
        this(cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent);

        this.card_number = card_number;
    }

    public String getCard_number() {
        return card_number;
    }

    public String getCust_surname() {
        return cust_surname;
    }

    public void setCust_surname(String cust_surname) {
        this.cust_surname = cust_surname;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_patronymic() {
        return cust_patronymic;
    }

    public void setCust_patronymic(String cust_patronymic) {
        this.cust_patronymic = cust_patronymic;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getFullName(){
        return cust_patronymic == null ? cust_surname + " " + cust_name : cust_surname + " " + cust_name + " " + cust_patronymic;
    }

    @Override
    public String toString() {
        return "Customer_Card{" +
                "card_number='" + card_number + '\'' +
                ", cust_surname='" + cust_surname + '\'' +
                ", cust_name='" + cust_name + '\'' +
                ", cust_patronymic='" + cust_patronymic + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zip_code='" + zip_code + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }
}
