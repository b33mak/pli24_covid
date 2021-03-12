/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import model.ResultOfCountry;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.Country;
import model.Coviddata;

public class DBManager {

    private static final String persistenceUnit = "pli24_covidPU";
    private EntityManager em;
    private EntityManagerFactory emf;
    private long[] stats;

    public DBManager() {
        // κάνω σύνδεση με το persistence unit (στο αρχείο METAINF\persistence.xml, πεδίο Persistance Unit Name)
        try {
            emf = Persistence.createEntityManagerFactory(DBManager.persistenceUnit);
            em = emf.createEntityManager(); //αρχικοποιώ τη μεταβλητή em
        } catch (Exception e) {
            emf = null;
            em = null;
            System.out.println("Σφάλμα σύνδεσης με τον DB Server. Δοκιμάστε να τον ξεκινήσετε!");
        }
    }

    public DBManager(String persistenceUnit) {
        // κάνω σύνδεση με το persistence unit (στο αρχείο METAINF\persistence.xml, πεδίο Persistance Unit Name)
        try {
            emf = Persistence.createEntityManagerFactory(persistenceUnit);
            em = emf.createEntityManager(); //αρχικοποιώ τη μεταβλητή em
        } catch (Exception e) {
            emf = null;
            em = null;
            System.out.println("Σφάλμα σύνδεσης με τον DB Server. Δοκιμάστε να τον ξεκινήσετε!");
        }
    }

    public DBManager(EntityManager em, EntityManagerFactory emf) {
        this.em = em;
        this.emf = emf;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public long[] getStats() {
        return stats;
    }

    public void setStats(long[] stats) {
        this.stats = stats;
    }

    public int insertCountriesIntoDB(List<ResultOfCountry> countriesResults) {
        if (em == null) {
            System.out.println("Δεν υπάρχει σύνδεση με τον DB Server");
            return -1;
        }

        int added_countries = 0;

        em.getTransaction().begin(); //ξεκινάω μια καινούργια συναλλαγή για να αποθηκεύσω στη βάση δεδομένων αντικείμενα
        // Σαρώνω τις χώρες του countriesResult
        for (ResultOfCountry c : countriesResults) {
            // Αν η χώρα c δεν υπάρχει στη ΒΔ, τότε την προσθέτω
            TypedQuery<Country> query = em.createNamedQuery("Country.findByName", Country.class);
            query.setParameter("name", c.getCountry_region());

            // Αν δεν υπάρχει
            if (query.getResultList().isEmpty()) {
                Country country_to_add = new Country();
                country_to_add.setName(c.getCountry_region());
                country_to_add.setLat(c.getLatitude());
                country_to_add.setLong1(c.getLongtitude());
                em.persist(country_to_add); // δημιουργώ τo query εισαγωγής για το c           
                added_countries++;
            }
        }
        em.getTransaction().commit();// κάνω commit το query

        System.out.println("Προστέθηκαν " + added_countries + " νέες χώρες στη ΒΔ");
        return added_countries;
    }

    public int insertCovidDataIntoDB(List<ResultOfCountry> countriesResults) {
        // Εξασφαλίζω ότι οι χώρες του countriesResults υπάρχουν στη ΒΔ, αλλιώς τις προσθέτω
        if (em == null) {
            System.out.println("Δεν υπάρχει σύνδεση με τον DB Server");
            return -1;
        }

        // Εξασφαλίζω ότι οι χώρες του countriesResults υπάρχουν στη ΒΔ, αλλιώς τις προσθέτω
        int added_countries = insertCountriesIntoDB(countriesResults);
        //System.out.println("Προστέθηκαν " + added_countries + " νέες χώρες στη ΒΔ");

        int added_data = 0; // Μετρητής εγγραφών που έγιναν insert στη ΒΔ
        int counter = 0; // Μετρητής τρέχουσας χώρας για εμφάνιση % ολοκλήρωσης

        short kind = 0;
        if (!countriesResults.isEmpty()) {
            if (countriesResults.get(0).getType_of_data().equals("deaths")) {
                kind = 1;
            } else if (countriesResults.get(0).getType_of_data().equals("recovered")) {
                kind = 2;
            } else {
                kind = 3;
            }
        }

        em.getTransaction().begin();
        for (ResultOfCountry c : countriesResults) {
            TypedQuery<Country> query_country = em.createNamedQuery("Country.findByName", Country.class);
            query_country.setParameter("name", c.getCountry_region());
            Country countryInDB = query_country.getResultList().get(0);
            List<Coviddata> countryInDBcoviddataList = countryInDB.getCoviddataList(); //Όλες οι εγγραφές της χώρας c στη ΒΔ

            for (Entry<String, Integer> dt_val : c.getDaily_records().descendingMap().entrySet()) {
                boolean found = false; // Για έλεγχο αν υπάρχει ήδη η εγγραφή 
                for (int i = 0; i < countryInDBcoviddataList.size(); i++) {
                    short c_kind = countryInDBcoviddataList.get(i).getDatakind();
                    String c_dt = new java.sql.Date(countryInDBcoviddataList.get(i).getTrndate().getTime()).toString();
                    if ((c_kind == kind) && (c_dt.equals(dt_val.getKey()))) {
                        found = true; // Η εγγραφή υπάρχει ήδη, άρα την παραλείπω
                        break;
                    }
                }
                if (found) { // Δεν χρειάζεται να σαρώσει τις υπόλοιπες ημερομηνίες, αφού είναι σε φθίνουσα χρονολογική σειρά
                    break;
                } else {
                    // Αν τελικά η εγγραφή δεν βρέθηκε πρέπει να προστεθεί στη ΒΔ       
                    Coviddata coviddata_to_add = new Coviddata();
                    coviddata_to_add.setTrndate(java.sql.Date.valueOf(dt_val.getKey()));
                    coviddata_to_add.setDatakind(kind);
                    coviddata_to_add.setProodqty(dt_val.getValue());
                    coviddata_to_add.setQty(c.getDaily_qty_records().get(dt_val.getKey()));
                    countryInDB.addRecordInCoviddataList(coviddata_to_add);
                    //em.persist(countryInDB);
                    added_data++;
                }
            } // Τέλος του FOR για έλεγχο ημερομηνιών χώρας και πάει στην επόμενη χώρα
            em.persist(countryInDB);
            counter++;
            System.out.println("Ολοκλήρωση: " + String.format("%.2f", counter * 100.0 / countriesResults.size()) + "%, Νέες εγγραφές: " + added_data);
        }
        em.getTransaction().commit();// κάνω commit το query

        System.out.println("Σύνολο νέων εγγραφών: " + (added_data + added_countries) + ", Coviddata: " + added_data + ", Χώρες: " + added_countries);
        return (added_data + added_countries);
    }

    public int deleteCoviddataFromDB(String strkind) {
        if (em == null) {
            System.out.println("Δεν υπάρχει σύνδεση με τον DB Server");
            return -1;
        }

        short kind = 0;
        if (strkind.equals("deaths")) {
            kind = 1;
        } else if (strkind.equals("recovered")) {
            kind = 2;
        } else {
            kind = 3;
        }

        TypedQuery<Country> query_country = em.createNamedQuery("Country.findAll", Country.class);
        List<Country> countries = query_country.getResultList();
        int deleted_data = 0;

        em.getTransaction().begin();
        int counter = 0; // Για ποσοστό ολοκλήρωσης
        for (Country c : countries) {
            Iterator<Coviddata> iter = c.getCoviddataList().iterator();
            while (iter.hasNext()) {
                Coviddata cd = iter.next();
                if (cd.getDatakind() == kind) {
                    iter.remove();
                    deleted_data++;
                }
            }
            em.persist(c);
            counter++;
            System.out.println("Ολοκλήρωση: " + String.format("%.2f", counter * 100.0 / countries.size()) + "%, Διαγραφές: " + deleted_data);
        }
        System.out.println("Γίνεται εκκαθάριση. Παρακαλώ περιμένετε...");
        em.getTransaction().commit();

        System.out.println("Διεγραμμένες εγγραφές Coviddata τύπου \"" + strkind + "\" : " + deleted_data);
        return deleted_data;
    }

    public int deleteAllTablesDataFromDB() {
        if (em == null) {
            System.out.println("Δεν υπάρχει σύνδεση με τον DB Server");
            return -1;
        }

        TypedQuery<Country> query_country = em.createNamedQuery("Country.findAll", Country.class);
        List<Country> countries = query_country.getResultList();

        int counter = 0; // Για ποσοστό ολοκλήρωσης
        int total_countries = countries.size();
        int deleted_countries = 0;
        int deleted_data = 0;

        em.getTransaction().begin();
        Iterator<Country> iter = countries.iterator();
        while (iter.hasNext()) {
            Country c = iter.next();
            deleted_data = deleted_data + c.getCoviddataList().size();
            deleted_countries++;
            iter.remove();
            em.remove(c);
            counter++;
            System.out.println("Ολοκλήρωση: " + String.format("%.2f", counter * 100.0 / total_countries) + "%, Σύνολο διαγραφών: " + (deleted_countries + deleted_data));
        }
        System.out.println("Γίνεται εκκαθάριση. Παρακαλώ περιμένετε...");
        em.getTransaction().commit();

        System.out.println("Σύνολο διαγραφών: " + (deleted_data + deleted_countries) + ", Coviddata: " + deleted_data + ", Χώρες: " + deleted_countries);
        return (deleted_data + deleted_countries);
    }

    public long[] dbRecordsInfo() {
        long[] stats = new long[5];   //stats[0]=χώρες, stats[1]=deaths, stats[2]=recovered, stats[3]=confirmed, stats[4]=totals
        if (em == null) {
            System.out.println("Δεν υπάρχει σύνδεση με τον DB Server");
            for (int i = 0; i < stats.length; i++) {
                stats[i] = 0L;
            }
            return stats;
        }

        em.getTransaction().begin();
        Query query = em.createQuery("SELECT COUNT(c) FROM Country c");
        stats[0] = (long) query.getSingleResult();
        stats[4] = stats[0]; // Προσθέτω τις χώρες
        for (int i = 1; i < stats.length - 1; i++) {
            query = em.createQuery("SELECT COUNT(c) FROM Coviddata c WHERE c.datakind=" + i);
            stats[i] = (long) query.getSingleResult();
            stats[4] = stats[4] + stats[i]; // Προσθέτω coviddata
        }
        em.getTransaction().commit();

        return stats;
    }

}
