package ch.unil.doplab.beeaware.ui;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable; // Importation nécessaire
import java.time.LocalDate;

@Named
public class HistoryFilterController implements Serializable {

    private static final long serialVersionUID = 1L; // Requis pour Serializable

    @Inject
    private BeezzerData beezzerData;

    private String selectedAllergen;
    private LocalDate startDate; // Utilisation de LocalDate pour une meilleure gestion des dates
    private LocalDate endDate;   // Utilisation de LocalDate

    public void onAllergenChange() {
        if (selectedAllergen != null) {
            System.out.println("Selected Allergen: " + selectedAllergen);
            // Mettre à jour le modèle du graphique avec les nouveaux filtres
            HistoryBean.updateChartModel(selectedAllergen, formatDate(startDate), formatDate(endDate));
        }
    }

    public void onDateChange() {
        System.out.println("Start Date: " + (startDate != null ? startDate.toString() : "null") +
                           ", End Date: " + (endDate != null ? endDate.toString() : "null"));
        // Mettre à jour le modèle du graphique avec les nouveaux filtres
        HistoryBean.updateChartModel(selectedAllergen, formatDate(startDate), formatDate(endDate));
    }

    // Méthode pour formater LocalDate en String pour l'affichage ou le traitement
    private String formatDate(LocalDate date) {
        return (date != null) ? date.toString() : null; // Ou utilise un DateTimeFormatter pour un format spécifique
    }

    // Getters et Setters
    public String getSelectedAllergen() {
        return selectedAllergen;
    }

    public void setSelectedAllergen(String selectedAllergen) {
        this.selectedAllergen = selectedAllergen;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
