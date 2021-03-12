/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import model.CountryDataForDiagram;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;


public class jFrameViewDataDiagram extends javax.swing.JFrame {

    private CountryDataForDiagram countryDataForDiagram;
    private ChartPanel chartPanel;
    private XYDataset dataset;
    private TimeSeriesCollection tsTotal;

    private JPanel panel;
    private JCheckBox chkDeaths;
    private JCheckBox chkRecovered;
    private JCheckBox chkConfirmed;
    private JCheckBox chkProodqtys;

    /**
     * Creates new form jFrameViewDataDiagram
     */
    public jFrameViewDataDiagram(CountryDataForDiagram countryDataForDiagram) {
        this.setLayout(new BorderLayout(0, 5));
        this.setTitle("Διάγραμμα δεδομένων χώρας");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.countryDataForDiagram = countryDataForDiagram;

        dataset = createDataset();
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Δεδομένα " + countryDataForDiagram.getCountryName(),
                "Ημ/νία",
                "Πλήθος",
                dataset);
        chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Επιλογές δεδομένων", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", 1, 14)));

        chkDeaths = new JCheckBox("Θάνατοι_Ημερ", false);
        chkDeaths.setFont(new Font("Tahoma", 1, 14));
        chkDeaths.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JCheckBox cb = (JCheckBox) event.getSource();
                if (cb.isSelected()) { // Αν επιλεγεί το πλήκτρο Θάνατοι_Ημερ
                    // Είναι cb.getText() --> "Θάνατοι_Ημερ"
                    // Σε περίπτωση που είναι επιλεγμένο το πλήκτρο Σωρρευτικά_Πλήθη, τότε α) το απενεργοποιώ και αφαιρώ τα data του
                    if (chkProodqtys.isSelected() == true) {
                        chkProodqtys.setSelected(false);
                        tsTotal.removeAllSeries();
                    }
                    // Προσθέτω την timeSeries του "Θάνατοι_Ημερ" (αν δεν υπάρχει ήδη)
                    if (tsTotal.getSeries("Θάνατοι_Ημερ") == null) { // Δηλαδή αν δεν υπάρχει, την προσθέτω
                        tsTotal.addSeries(createSeriesQTY("Θάνατοι_Ημερ"));
                        dataset = tsTotal;
                    }
                } else { // Αν αποεπιλεγεί το πλήκτρο Θάνατοι_Ημερ, τότε αν υπάρχει η TimeSeries στα data, την αφαιρώ 
                    //System.out.println(cb.getText() + " unselected");
                    if (tsTotal.getSeries("Θάνατοι_Ημερ") != null) { // Δηλαδή αν υπάρχει ήδη στα data, την αφαιρώ
                        tsTotal.removeSeries(tsTotal.getSeriesIndex("Θάνατοι_Ημερ"));
                        dataset = tsTotal;
                    }
                }
            }
        });

        chkRecovered = new JCheckBox("Ανάρρωσαν_Ημερ", false);
        chkRecovered.setFont(new Font("Tahoma", 1, 14));
        chkRecovered.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JCheckBox cb = (JCheckBox) event.getSource();
                if (cb.isSelected()) { // Αν επιλεγεί το πλήκτρο Ανάρρωσαν_Ημερ
                    // Είναι cb.getText() --> "Ανάρρωσαν_Ημερ"
                    // Σε περίπτωση που είναι επιλεγμένο το πλήκτρο Σωρρευτικά_Πλήθη, τότε α) το απενεργοποιώ και αφαιρώ τα data του
                    if (chkProodqtys.isSelected() == true) {
                        chkProodqtys.setSelected(false);
                        tsTotal.removeAllSeries();
                    }
                    // Προσθέτω την timeSeries του "Ανάρρωσαν_Ημερ" (αν δεν υπάρχει ήδη)
                    if (tsTotal.getSeries("Ανάρρωσαν_Ημερ") == null) { // Δηλαδή αν δεν υπάρχει, την προσθέτω
                        tsTotal.addSeries(createSeriesQTY("Ανάρρωσαν_Ημερ"));
                        dataset = tsTotal;
                    }
                } else { // Αν αποεπιλεγεί το πλήκτρο Ανάρρωσαν_Ημερ, τότε αν υπάρχει η TimeSeries στα data, την αφαιρώ 
                    //System.out.println(cb.getText() + " unselected");
                    if (tsTotal.getSeries("Ανάρρωσαν_Ημερ") != null) { // Δηλαδή αν υπάρχει ήδη στα data, την αφαιρώ
                        tsTotal.removeSeries(tsTotal.getSeriesIndex("Ανάρρωσαν_Ημερ"));
                        dataset = tsTotal;
                    }
                }
            }
        });

        chkConfirmed = new JCheckBox("Κρούσματα_Ημερ", false);
        chkConfirmed.setFont(new Font("Tahoma", 1, 14));
        chkConfirmed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JCheckBox cb = (JCheckBox) event.getSource();
                if (cb.isSelected()) { // Αν επιλεγεί το πλήκτρο Κρούσματα_Ημερ
                    // Είναι cb.getText() --> "Κρούσματα_Ημερ"
                    // Σε περίπτωση που είναι επιλεγμένο το πλήκτρο Σωρρευτικά_Πλήθη, τότε α) το απενεργοποιώ και αφαιρώ τα data του
                    if (chkProodqtys.isSelected() == true) {
                        chkProodqtys.setSelected(false);
                        tsTotal.removeAllSeries();
                    }
                    // Προσθέτω την timeSeries του "Κρούσματα_Ημερ" (αν δεν υπάρχει ήδη)
                    if (tsTotal.getSeries("Κρούσματα_Ημερ") == null) { // Δηλαδή αν δεν υπάρχει, την προσθέτω
                        tsTotal.addSeries(createSeriesQTY("Κρούσματα_Ημερ"));
                        dataset = tsTotal;
                    }
                } else { // Αν αποεπιλεγεί το πλήκτρο Κρούσματα_Ημερ, τότε αν υπάρχει η TimeSeries στα data, την αφαιρώ 
                    //System.out.println(cb.getText() + " unselected");
                    if (tsTotal.getSeries("Κρούσματα_Ημερ") != null) { // Δηλαδή αν υπάρχει ήδη στα data, την αφαιρώ
                        tsTotal.removeSeries(tsTotal.getSeriesIndex("Κρούσματα_Ημερ"));
                        dataset = tsTotal;
                    }
                }
            }
        });

        chkProodqtys = new JCheckBox("Σωρρευτικά_Πλήθη", true);
        chkProodqtys.setFont(new Font("Tahoma", 1, 14));
        chkProodqtys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JCheckBox cb = (JCheckBox) event.getSource();
                if (cb.isSelected()) { // Αν επιλεγεί το πλήκτρο Σωρρευτικά_Πλήθη
                    System.out.println(cb.getText() + " selected");
                    // Είναι cb.getText() --> "Σωρρευτικά_Πλήθη"
                    // Αποεπιλέγω όλα τα υπόλοιπα πλήκτρα (Θάνατοι_Ημερ, Ανάρρωσαν_Ημερ, Κρούσματα_Ημερ και αφαιρώ τα data τους
                    chkDeaths.setSelected(false);
                    chkRecovered.setSelected(false);
                    chkConfirmed.setSelected(false);
                    tsTotal.removeAllSeries();

                    // Προσθέτω τις 3 timeSeries των "Σωρρευτικά_Πλήθη" (αν δεν υπάρχει ήδη)
                    tsTotal.addSeries(createSeriesProodQTY("Θάνατοι_Σωρρ"));
                    tsTotal.addSeries(createSeriesProodQTY("Ανάρρωσαν_Σωρρ"));
                    tsTotal.addSeries(createSeriesProodQTY("Κρούσματα_Σωρρ"));
                    dataset = tsTotal;
                } else { // Αν αποεπιλεγεί το πλήκτρο Σωρρευτικά_Πλήθη, τότε αφαιρώ όλα τα data
                    System.out.println(cb.getText() + " unselected");
                    tsTotal.removeAllSeries();
                    dataset = tsTotal;
                }
            }
        });

        panel.add(chkDeaths);
        panel.add(chkRecovered);
        panel.add(chkConfirmed);
        panel.add(chkProodqtys);

        this.add(panel, BorderLayout.SOUTH);
        this.pack();
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private XYDataset createDataset() {
        tsTotal = new TimeSeriesCollection();

        // Για τα σωρρετικά
        TimeSeriesCollection ts = this.createSeriesProodQTY();
        for (int i = 0; i < ts.getSeriesCount(); i++) {
            tsTotal.addSeries(ts.getSeries(i));
        }

        // Για τα ημερίσια
//        ts = this.createSeriesQTY();
//        for (int i = 0; i < ts.getSeriesCount(); i++) {
//            tsTotal.addSeries(ts.getSeries(i));
//        }
        return tsTotal;
    }

    private TimeSeriesCollection createSeriesProodQTY() {
        TimeSeriesCollection ts = new TimeSeriesCollection();

        // Deaths
        TimeSeries seriesDeathsPROODQTY = new TimeSeries("Θάνατοι_Σωρρ");
        for (int i = (countryDataForDiagram.getCountry_deaths_dates().size() - 1); i >= 0; i--) {
            LocalDate dt = countryDataForDiagram.getCountry_deaths_dates().get(i).toLocalDate();
            int proodqty = countryDataForDiagram.getCountry_deaths_proodqty().get(i);
            seriesDeathsPROODQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), proodqty);
        }
        ts.addSeries(seriesDeathsPROODQTY);

        // Recovered
        TimeSeries seriesRecoveredPROODQTY = new TimeSeries("Ανάρρωσαν_Σωρρ");
        for (int i = (countryDataForDiagram.getCountry_recovered_dates().size() - 1); i >= 0; i--) {
            LocalDate dt = countryDataForDiagram.getCountry_recovered_dates().get(i).toLocalDate();
            int proodqty = countryDataForDiagram.getCountry_recovered_proodqty().get(i);
            seriesRecoveredPROODQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), proodqty);
        }
        ts.addSeries(seriesRecoveredPROODQTY);

        // Confirmed
        TimeSeries seriesConfirmedPROODQTY = new TimeSeries("Κρούσματα_Σωρρ");
        for (int i = (countryDataForDiagram.getCountry_confirmed_dates().size() - 1); i >= 0; i--) {
            LocalDate dt = countryDataForDiagram.getCountry_confirmed_dates().get(i).toLocalDate();
            int proodqty = countryDataForDiagram.getCountry_confirmed_proodqty().get(i);
            seriesConfirmedPROODQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), proodqty);
        }
        ts.addSeries(seriesConfirmedPROODQTY);

        return ts;
    }

    private TimeSeries createSeriesProodQTY(String timeSeriesKeyName) {

        // TimeSeries με όνομα-κλειδί: timeSeriesKeyName (λχ "Θάνατοι_Σωρρ", "Ανάρρωσαν_Σωρρ", "Κρούσματα_Σωρρ"
        TimeSeries seriesQTY = new TimeSeries(timeSeriesKeyName);
        if (timeSeriesKeyName.equals("Θάνατοι_Σωρρ")) { // Deaths
            for (int i = (countryDataForDiagram.getCountry_deaths_dates().size() - 1); i >= 0; i--) {
                LocalDate dt = countryDataForDiagram.getCountry_deaths_dates().get(i).toLocalDate();
                int qty = countryDataForDiagram.getCountry_deaths_proodqty().get(i);
                seriesQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
            }
        } else if (timeSeriesKeyName.equals("Ανάρρωσαν_Σωρρ")) {  // Recovered
            for (int i = (countryDataForDiagram.getCountry_recovered_dates().size() - 1); i >= 0; i--) {
                LocalDate dt = countryDataForDiagram.getCountry_recovered_dates().get(i).toLocalDate();
                int qty = countryDataForDiagram.getCountry_recovered_proodqty().get(i);
                seriesQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
            }
        } else if (timeSeriesKeyName.equals("Κρούσματα_Σωρρ")) {  // Confirmed
            for (int i = (countryDataForDiagram.getCountry_confirmed_dates().size() - 1); i >= 0; i--) {
                LocalDate dt = countryDataForDiagram.getCountry_confirmed_dates().get(i).toLocalDate();
                int qty = countryDataForDiagram.getCountry_confirmed_proodqty().get(i);
                seriesQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
            }
        }
        //ts.addSeries(seriesQTY);

        return seriesQTY;
    }

    private TimeSeriesCollection createSeriesQTY() {
        TimeSeriesCollection ts = new TimeSeriesCollection();

        // Deaths
        TimeSeries seriesDeathsQTY = new TimeSeries("Θάνατοι_Ημερ");
        for (int i = (countryDataForDiagram.getCountry_deaths_dates().size() - 1); i >= 0; i--) {
            LocalDate dt = countryDataForDiagram.getCountry_deaths_dates().get(i).toLocalDate();
            int qty = countryDataForDiagram.getCountry_deaths_qty().get(i);
            seriesDeathsQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
        }
        ts.addSeries(seriesDeathsQTY);

        // Recovered
        TimeSeries seriesRecoveredQTY = new TimeSeries("Ανάρρωσαν_Ημερ");
        for (int i = (countryDataForDiagram.getCountry_recovered_dates().size() - 1); i >= 0; i--) {
            LocalDate dt = countryDataForDiagram.getCountry_recovered_dates().get(i).toLocalDate();
            int qty = countryDataForDiagram.getCountry_recovered_qty().get(i);
            seriesRecoveredQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
        }
        ts.addSeries(seriesRecoveredQTY);

        // Confirmed
        TimeSeries seriesConfirmedQTY = new TimeSeries("Κρούσματα_Ημερ");
        for (int i = (countryDataForDiagram.getCountry_confirmed_dates().size() - 1); i >= 0; i--) {
            LocalDate dt = countryDataForDiagram.getCountry_confirmed_dates().get(i).toLocalDate();
            int qty = countryDataForDiagram.getCountry_confirmed_qty().get(i);
            seriesConfirmedQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
        }
        ts.addSeries(seriesConfirmedQTY);

        return ts;
    }

    private TimeSeries createSeriesQTY(String timeSeriesKeyName) {

        // TimeSeries με όνομα-κλειδί: timeSeriesKeyName (λχ "Θάνατοι_Ημερ"
        TimeSeries seriesQTY = new TimeSeries(timeSeriesKeyName);
        if (timeSeriesKeyName.equals("Θάνατοι_Ημερ")) { // Deaths
            for (int i = (countryDataForDiagram.getCountry_deaths_dates().size() - 1); i >= 0; i--) {
                LocalDate dt = countryDataForDiagram.getCountry_deaths_dates().get(i).toLocalDate();
                int qty = countryDataForDiagram.getCountry_deaths_qty().get(i);
                seriesQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
            }
        } else if (timeSeriesKeyName.equals("Ανάρρωσαν_Ημερ")) {  // Recovered
            for (int i = (countryDataForDiagram.getCountry_recovered_dates().size() - 1); i >= 0; i--) {
                LocalDate dt = countryDataForDiagram.getCountry_recovered_dates().get(i).toLocalDate();
                int qty = countryDataForDiagram.getCountry_recovered_qty().get(i);
                seriesQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
            }
        } else if (timeSeriesKeyName.equals("Κρούσματα_Ημερ")) {  // Confirmed
            for (int i = (countryDataForDiagram.getCountry_confirmed_dates().size() - 1); i >= 0; i--) {
                LocalDate dt = countryDataForDiagram.getCountry_confirmed_dates().get(i).toLocalDate();
                int qty = countryDataForDiagram.getCountry_confirmed_qty().get(i);
                seriesQTY.add(new Day(dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), qty);
            }
        }
        //ts.addSeries(seriesQTY);

        return seriesQTY;
    }

}
