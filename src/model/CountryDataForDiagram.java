/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class CountryDataForDiagram {
    
    // Για τα δεδομένα της επιλεγμένης χώρας προς εμφάνιση σε διάγραμμα
    private String countryName;
    private List<java.sql.Date> country_deaths_dates = new ArrayList();
    private List<Integer> country_deaths_qty = new ArrayList();
    private List<Integer> country_deaths_proodqty = new ArrayList();

    private List<java.sql.Date> country_recovered_dates = new ArrayList();
    private List<Integer> country_recovered_qty = new ArrayList();
    private List<Integer> country_recovered_proodqty = new ArrayList();

    private List<java.sql.Date> country_confirmed_dates = new ArrayList();
    private List<Integer> country_confirmed_qty = new ArrayList();
    private List<Integer> country_confirmed_proodqty = new ArrayList();

    public CountryDataForDiagram(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<Date> getCountry_deaths_dates() {
        return country_deaths_dates;
    }

    public void setCountry_deaths_dates(List<Date> country_deaths_dates) {
        this.country_deaths_dates = country_deaths_dates;
    }

    public List<Integer> getCountry_deaths_qty() {
        return country_deaths_qty;
    }

    public void setCountry_deaths_qty(List<Integer> country_deaths_qty) {
        this.country_deaths_qty = country_deaths_qty;
    }

    public List<Integer> getCountry_deaths_proodqty() {
        return country_deaths_proodqty;
    }

    public void setCountry_deaths_proodqty(List<Integer> country_deaths_proodqty) {
        this.country_deaths_proodqty = country_deaths_proodqty;
    }

    public List<Date> getCountry_recovered_dates() {
        return country_recovered_dates;
    }

    public void setCountry_recovered_dates(List<Date> country_recovered_dates) {
        this.country_recovered_dates = country_recovered_dates;
    }

    public List<Integer> getCountry_recovered_qty() {
        return country_recovered_qty;
    }

    public void setCountry_recovered_qty(List<Integer> country_recovered_qty) {
        this.country_recovered_qty = country_recovered_qty;
    }

    public List<Integer> getCountry_recovered_proodqty() {
        return country_recovered_proodqty;
    }

    public void setCountry_recovered_proodqty(List<Integer> country_recovered_proodqty) {
        this.country_recovered_proodqty = country_recovered_proodqty;
    }

    public List<Date> getCountry_confirmed_dates() {
        return country_confirmed_dates;
    }

    public void setCountry_confirmed_dates(List<Date> country_confirmed_dates) {
        this.country_confirmed_dates = country_confirmed_dates;
    }

    public List<Integer> getCountry_confirmed_qty() {
        return country_confirmed_qty;
    }

    public void setCountry_confirmed_qty(List<Integer> country_confirmed_qty) {
        this.country_confirmed_qty = country_confirmed_qty;
    }

    public List<Integer> getCountry_confirmed_proodqty() {
        return country_confirmed_proodqty;
    }

    public void setCountry_confirmed_proodqty(List<Integer> country_confirmed_proodqty) {
        this.country_confirmed_proodqty = country_confirmed_proodqty;
    }
    
    
}
