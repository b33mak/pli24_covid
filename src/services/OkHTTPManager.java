/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHTTPManager {
    private static final String baseUrlToCall="https://covid2019-api.herokuapp.com/timeseries/";
    private String urlToCall;
    private String responseString;

    public OkHTTPManager(String strDatatype) {
        this.urlToCall = OkHTTPManager.baseUrlToCall + strDatatype;
        responseString="";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlToCall).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseString = response.body().string();
                //System.out.println(responseString);
            }
        } catch (Exception e) {
            System.out.println("Σφάλμα σύνδεσης με το Http API");
        }

    }

    public String getUrlToCall() {
        return urlToCall;
    }

    public void setUrlToCall(String strDatatype) {
        this.urlToCall = OkHTTPManager.baseUrlToCall + strDatatype;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

}
