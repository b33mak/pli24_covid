/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import model.ResultOfCountry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class JsonManager {

    private List<ResultOfCountry> countriesResults; // Για να κρατάω τα αποτελέσματα του HTTP query

    public JsonManager() {
        this.countriesResults = new ArrayList<>();
    }

    public JsonManager(String jsonString, String strDataType) {
        this.countriesResults = new ArrayList<>();
        //this.countriesResults.clear(); // Εκκαθάριση λίστας αποτελεσμάτων

        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(jsonString);

        // Πάντα στην ρίζα είναι ένα object
        JsonObject jsonObject = jsonTree.getAsJsonObject();
        JsonArray jsonArrayOfCountries = jsonObject.get(strDataType).getAsJsonArray();
        
        // Για κάθε χώρα - object του πίνακα αντικειμένων του json tree model
        for (int i = 0; i < jsonArrayOfCountries.size(); i++) {

            ResultOfCountry c = new ResultOfCountry(); // Αντικείμενο που θα συμπληρωθεί και προστεθεί στη λίστα χωρών countriesResults
            c.setType_of_data(strDataType); // Για deaths/recovered/confirmed
            TreeMap<String, Integer> map = new TreeMap<>(); // Για dates/values
            TreeMap<String, Integer> map_qty = new TreeMap<>(); // Για ημερήσιες περιπτώσεις
            int previous_proodqty = 0; // Εδώ θα κρατάω το προηγούμενο σωρρευτικό άθροισμα περιπτώσεων

            JsonObject jObj = jsonArrayOfCountries.get(i).getAsJsonObject(); // Παίρνω ένα αντικείμενο της λίστας json  
            Set<Map.Entry<String, JsonElement>> es = jObj.entrySet(); // Κρατάω όλα τα key-value pairs

            for (Map.Entry<String, JsonElement> en : es) {
                String key = en.getKey();
                switch (key) {
                    case "Province/State":
                        c.setProvince_state(en.getValue().getAsString());
                        break;
                    case "Country/Region":
                        c.setCountry_region(en.getValue().getAsString());
                        break;
                    case "Lat":
                        if (en.getValue().getAsString().equals("")) {
                            c.setLatitude(0.0);
                        } else {
                            c.setLatitude(en.getValue().getAsDouble());
                        }
                        break;
                    case "Long":
                        if (en.getValue().getAsString().equals("")) {
                            c.setLongtitude(0.0);
                        } else {
                            c.setLongtitude(en.getValue().getAsDouble());
                        }
                        break;
                    default: // Αλλιώς θα είναι μορφής ημερομηνίας
                        DateConvertor dt_fixed = new DateConvertor(key);
                        map.put(dt_fixed.getDateConvertedToSQLFormat(), en.getValue().getAsInt());
                        int cur_qty_calculated = en.getValue().getAsInt() - previous_proodqty;
                        if (cur_qty_calculated<0){
                            cur_qty_calculated=0;
                        }
                        map_qty.put(dt_fixed.getDateConvertedToSQLFormat(), cur_qty_calculated);
                        previous_proodqty = en.getValue().getAsInt();
                        
                }

            } // Ολοκλήρωση σάρωσης χωρών και των στοιχείων τους από το json Tree Model του HttpRequest
            
            c.setDaily_records(map);    // Προσθήκη των date/values στο αντικείμενο χώρας
            c.setDaily_qty_records(map_qty);
            
            // Ρύθμιση του ονόματος της χώρας
            if (c.getProvince_state().length()>0){
                c.setCountry_region(c.getCountry_region()+"-"+c.getProvince_state());
            }
            
            // Ως εδώ ολοκληρώθηκε η προετοιμασία του αντικειμένου που πρόκειται να προστεθεί στη λίστα χωρών countriesResults
            // TEST για δεδομένα που έχουν εξαχθεί:
//            for (String dt: c.getDaily_records().keySet()){
//                System.out.println("Kind: " + c.getType_of_data() + "," + c.getCountry_region() + ", " + dt + ", QTY:" + c.getDaily_qty_records().get(dt) + ", PRODQTY:" + c.getDaily_records().get(dt));
//            }
            
            
            // TEST dates:
//            for (String dt: c.getDaily_records().keySet()){
//                System.out.println(c.getCountry_region() + " " + dt + " QTY:" + c.getDaily_qty_records().get(dt) + " PRODQTY:" + c.getDaily_records().get(dt));
//            }
//            System.exit(0);

            // Πρέπει να ελέγξω αν η χώρα c υπάρχει ήδη στο countiesResults
//            boolean found = false;
//            for (ResultOfCountry rc : this.countriesResults) {
//                // Αν η χώρα υπάρχει ήδη, τότε αυτό σημαίνει ότι έχουμε δεδομένα από άλλο region της ίδιας χώρας
//                // τα οποία θα πρέπει να αθροίσουμε με τα ήδη υπάρχοντα
//                if (c.getCountry_region().equals(rc.getCountry_region()) && (c.getType_of_data().equals(rc.getType_of_data()))) {
//                    
//                    System.out.println("Η χώρα " + c.getCountry_region() + " υπάρχει ήδη. Θα γίνει συγχώνευση για data: " + c.getType_of_data() + "...");
//
//                    // Συγχώνευση των δεδομένων date/values του c με του rc
//                    // Σαρώνω όλα τα κλειδιά του Map με τα date/values του c
//                    for (String dt : c.getDaily_records().keySet()) {
//                        Integer dt_proodqty_value = c.getDaily_records().get(dt); // το prodqty_value του dt από το αντικείμενο c
//                        Integer dt_qty_value = c.getDaily_qty_records().get(dt); // το qty_value του dt από το αντικείμενο c
//                        // Αν το κλειδί dt δεν υπάρχει(-1) στα κλειδιά του rc.daily_records, τότε προστίθεται το dt_value του c
//                        Integer rc_prodqty_value = rc.getDaily_records().getOrDefault(dt, -1);
//                        Integer rc_qty_value = rc.getDaily_qty_records().getOrDefault(dt, -1);
//                        if (rc_prodqty_value == -1) {
//                            rc.getDaily_records().put(dt, dt_proodqty_value);
//                            rc.getDaily_qty_records().put(dt, dt_qty_value);                            
//                        } else {
//                            // Αλλιώς, δηλαδή αν υπάρχει, τότε πρέπει να προσθέσω τo value σε αυτό του rc
//                            rc.getDaily_records().put(dt, rc_prodqty_value + dt_proodqty_value);
//                            rc.getDaily_qty_records().put(dt, rc_qty_value + dt_proodqty_value);                            
//                        }
//                    }
//                    found = true;
//                    break; // Βγαίνει έξω από το for loop συγχώνευσης περιοχών της ίδιας χώρας
//                }
//            } // End for συγχωνεύσεων δεδομένων χωρών με πολλαπλά regions σε 1 ενιαία χώρα
//
//            // Αν εν τέλει η χώρα c δεν υπήρχε στις ήδη καταχωρημένες χώρες, τότε την προσθέτω
//            if (found == false) {
                countriesResults.add(c);    // Προσθήκη της χώρας στη λίστα χωρών
//            }
            System.out.println("Ανάλυση json data: " + String.format("%.2f", i*100.0/jsonArrayOfCountries.size()) + "%");
        }
        System.out.println("Η ανάλυση των json data ολοκληρώθηκε");
        //System.exit(1);
    }

    public List<ResultOfCountry> getCountriesResults() {
        return countriesResults;
    }

    public void setCountriesResults(List<ResultOfCountry> countriesResults) {
        this.countriesResults = countriesResults;
    }

}
