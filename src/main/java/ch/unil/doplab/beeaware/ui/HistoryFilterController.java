package ch.unil.doplab.beeaware.ui;

import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class HistoryFilterController {

    @Inject
    private BeezzerData beezzerData;

    private String selectedAllergen;
    private String startDate;
    private String endDate;

    public void onAllergenChange() {
        // Logique pour mettre à jour le modèle du graphique
        System.out.println("Selected Allergen: " + selectedAllergen);
        beezzerData.updateChartModel(selectedAllergen, startDate, endDate);
    }

    public void onDateChange() {
        // Logique pour mettre à jour le modèle du graphique en fonction des dates
        System.out.println("Start Date: " + startDate + ", End Date: " + endDate);
        beezzerData.updateChartModel(selectedAllergen, startDate, endDate);
    }

    // Getters et Setters pour selectedAllergen, startDate et endDate
}