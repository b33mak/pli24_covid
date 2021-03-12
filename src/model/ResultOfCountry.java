/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class ResultOfCountry {
    private String type_of_data;
    private String province_state;
    private String country_region;
    private double latitude;
    private double longtitude;
    private TreeMap<String, Integer> daily_qty_records;
    private TreeMap<String, Integer> daily_records;
    

    public ResultOfCountry() {
        this.daily_qty_records = new TreeMap<>();
        this.daily_records = new TreeMap<>();
    }

    public ResultOfCountry(String type_of_data, String province_state, String country_region, double latitude, double longtitude) {
        this.type_of_data = type_of_data;
        this.daily_qty_records = new TreeMap<>();
        this.daily_records = new TreeMap<>();
        this.province_state = province_state;
        this.country_region = country_region;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getType_of_data() {
        return type_of_data;
    }

    public void setType_of_data(String type_of_data) {
        this.type_of_data = type_of_data;
    }
    
        
    public String getProvince_state() {
        return province_state;
    }

    public void setProvince_state(String province_state) {
        this.province_state = province_state;
    }

    public String getCountry_region() {
        return country_region;
    }

    public void setCountry_region(String country_region) {
        this.country_region = country_region;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public TreeMap<String, Integer> getDaily_qty_records() {
        return daily_qty_records;
    }

    public void setDaily_qty_records(TreeMap<String, Integer> daily_qty_records) {
        this.daily_qty_records = daily_qty_records;
    }
    
    

    public TreeMap<String, Integer> getDaily_records() {
        return daily_records;
    }

    public void setDaily_records(TreeMap<String, Integer> daily_records) {
        this.daily_records = daily_records;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.type_of_data);
        hash = 29 * hash + Objects.hashCode(this.country_region);
        return hash;
    }

    // Ελέγχω την ισότητα με άλλο ResultCountry με βάση μόνο τον τύπο δεδομένων (deaths/recovered/confirmed)
    // και το όνομα της χώρας
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResultOfCountry other = (ResultOfCountry) obj;
        if (!Objects.equals(this.type_of_data, other.type_of_data)) {
            return false;
        }
        if (!Objects.equals(this.country_region, other.country_region)) {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString() {
        return "ResultOfCountry{" + "province_state=" + province_state + ", country_region=" + country_region + ", latitude=" + latitude + ", longtitude=" + longtitude + ", daily_qty_records=" + daily_qty_records.size() +", daily_total_records=" + daily_records.size() + '}';
    }
 
}
