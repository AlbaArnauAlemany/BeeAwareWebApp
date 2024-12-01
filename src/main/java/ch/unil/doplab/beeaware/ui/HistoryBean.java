package ch.unil.doplab.beeaware.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.component.linechart.LineChart;
import org.primefaces.component.piechart.PieChart;
import org.primefaces.event.ItemSelectEvent;
import java.awt.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@RequestScoped
public class HistoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LineChart lineChart;
    private PieChart pieChart;

    private String lineChartModel;
    private String pieChartModel;

    private List<String> pollenData;    // Données des pollens
    private List<BigDecimal> allergenData; // Données des allergènes

    // Méthode pour mettre à jour le modèle du graphique
    public static void updateChartModel(String selectedAllergen, String startDate, String endDate) {
        // Implémentation logique ici pour filtrer ou modifier les données
        System.out.println("Updating chart model for Allergen: " + selectedAllergen);
        System.out.println("Start Date: " + startDate + ", End Date: " + endDate);
        // Vous pouvez ajouter ici une logique pour récupérer de nouvelles données ou filtrer selon les dates.
    }

    @PostConstruct
    public void init() {
        createLineChartModel();
    }

    // Créer le modèle pour le graphique en ligne avec les données existantes
    public void createLineChartModel() {
        if (pollenData != null && !pollenData.isEmpty()) {
            // Créer un modèle de graphique en ligne à partir des données
            lineChart = new LineChart();
            lineChart.setId("lineChart");
            lineChart.setAriaLabel("Pollen Levels Over Time");

            // Ajouter les données des pollens dans le graphique en ligne
            int[] pollenLevels = new int[pollenData.size()]; // Taille égale à la liste de pollenData
            for (int i = 0; i < pollenData.size(); i++) {
                pollenLevels[i] = Integer.parseInt(pollenData.get(i)); // Conversion des données en entiers
            }

            // Alternative pour ajouter des séries dans le graphique en ligne
            Map<String, Object> seriesData = new HashMap<>();
            seriesData.put("Pollen Intensity", pollenLevels);
//            lineChart.setSeries(seriesData); // Définir la série de données

//            lineChartModel = lineChart.toJson(); // Récupérer le modèle du graphique
        }
    }

    // Getter pour récupérer le modèle du graphique en ligne
    public String getLineChartModel() {
        return lineChartModel;
    }

    // Setter pour définir le modèle du graphique en ligne
    public void setLineChartModel(String lineChartModel) {
        this.lineChartModel = lineChartModel;
    }

    // Getter pour récupérer le modèle du graphique en camembert
    public String getPieChartModel() {
        return pieChartModel;
    }

    // Setter pour définir le modèle du graphique en camembert
    public void setPieChartModel(String pieChartModel) {
        this.pieChartModel = pieChartModel;
    }

    // Méthode d'événement de sélection d'un élément dans le graphique
    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Value: " + event.getData() + ", Item Index: " + event.getItemIndex()
                + ", DataSet Index:" + event.getDataSetIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
