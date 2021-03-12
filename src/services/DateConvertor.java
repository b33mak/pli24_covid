/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.sql.Date;

public class DateConvertor {

    String dateToConvert;   //Μορφής: 28/2/21
    String dateConvertedToSQLFormat;  // Μορφής: 2021-02-28
    
    public DateConvertor(){
        
    }

    public DateConvertor(String dateToConvert) {
        this.dateToConvert = dateToConvert;

        String[] date_elements = dateToConvert.split("/"); //[0]=day, [1]=month, [2]=yy
        try {
            date_elements[2] = "20" + date_elements[2]; //21-->2021
            dateConvertedToSQLFormat = Date.valueOf(date_elements[2] + "-" + date_elements[0] + "-" + date_elements[1]).toString();
        } catch (Exception e) {
            for (String s : date_elements) {
                System.out.println(s);
            }
        }
    }
    
    java.sql.Date convertUtilDate2SQLDate(java.util.Date date){
        return new java.sql.Date(date.getTime());
    }

    public String getDateToConvert() {
        return dateToConvert;
    }

    public void setDateToConvert(String dateToConvert) {
        this.dateToConvert = dateToConvert;
    }

    public String getDateConvertedToSQLFormat() {
        return dateConvertedToSQLFormat;
    }

    public void setDateConvertedToSQLFormat(String dateConvertedToSQLFormat) {
        this.dateConvertedToSQLFormat = dateConvertedToSQLFormat;
    }

}
