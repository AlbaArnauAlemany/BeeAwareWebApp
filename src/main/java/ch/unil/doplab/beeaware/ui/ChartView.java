package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import ch.unil.doplab.beeaware.Domain.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Role;
import ch.unil.doplab.beeaware.Domain.Token;
import ch.unil.doplab.beeaware.Utilis;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Named("chartView")
@RequestScoped
public class ChartView {
    private LineChartModel lineModel;
    private List<SymptomsDTO> symptomsDTOList;
    private List<PollenInfoDTO> pollenInfoDTOList;

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    BeezzerData beezzerData;

    public static String parseDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }
    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        Date tempDate = new Date();
        String dateTo = LocalDate.now().format(formatter);
        LocalDate date = LocalDate.parse(dateTo, formatter);
        LocalDate dateMinusOneMonth = date.minusMonths(1);
        String dateFrom = dateMinusOneMonth.format(formatter);
        symptomsDTOList = theService.getSymptomService().getSymptomForRangeDate(beezzerData.getId(), dateFrom, dateTo);


        List<List<Object>> values = new ArrayList<>();
        List<Boolean> histamine = new ArrayList<>();
        values.add(new ArrayList<>());
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < symptomsDTOList.size(); ++i){
            SymptomsDTO currentSymptomsDTO = symptomsDTOList.get(i);
            System.out.println(currentSymptomsDTO);

//            labels.add(parseDate(currentSymptomsDTO.getDate()) + (currentSymptomsDTO.isAntihistamine() ? " 💊" : ""));
            labels.add(parseDate(currentSymptomsDTO.getDate()));
            values.get(0).add(currentSymptomsDTO.getReaction());
            histamine.add(currentSymptomsDTO.isAntihistamine());
        }

        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        List<LineChartDataSet> dataSet = new ArrayList<>();
        dataSet.add(new LineChartDataSet());


        dataSet.get(0).setData(values.get(0));
        dataSet.get(0).setLabel("Symptoms");
        dataSet.get(0).setYaxisID("left-y-axis");
        dataSet.get(0).setFill(true);
        dataSet.get(0).setTension(0.5);

        pollenInfoDTOList = theService.getForecastService().getForecastForRangeDate(beezzerData.getId(), dateFrom, dateTo);
        Map<String, List<PollenInfoDTO>> allergenType = new HashMap<>();
        for (int i = 0; i < pollenInfoDTOList.size(); ++i){
            PollenInfoDTO currentPollenDTO = pollenInfoDTOList.get(i);
            if (!allergenType.containsKey(currentPollenDTO.getDisplayName())){
                allergenType.put(currentPollenDTO.getDisplayName(), new ArrayList<>());
            }
            allergenType.get(currentPollenDTO.getDisplayName()).add(currentPollenDTO);
        }

        int count = 1;
        for (Map.Entry<String, List<PollenInfoDTO>> pollen  : allergenType.entrySet()){
            values.add(new ArrayList<>());
            dataSet.add(new LineChartDataSet());
            for (PollenInfoDTO pollenElem: pollen.getValue()) {
                if (!labels.contains(parseDate(pollenElem.getDate()))) {
                    labels.add(parseDate(pollenElem.getDate()));
                }
                values.get(count).add(pollenElem.getIndex());
            }
            dataSet.get(count).setData(values.get(count));
            dataSet.get(count).setLabel(pollen.getKey());
            dataSet.get(count).setYaxisID("right-y-axis");
            dataSet.get(count).setFill(true);
            dataSet.get(count).setTension(0.5);
            count++;
        }

        for (int i = 0; i < histamine.size(); ++i) {
            labels.set(i, labels.get(i) + (histamine.get(i)  ? " 💊" : ""));
        }


        for (LineChartDataSet dataSetElement: dataSet) {
            data.addChartDataSet(dataSetElement);
        }


        data.setLabels(labels);
        lineModel.setData(data);

        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");
        CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
        linearAxes2.setId("right-y-axis");
        linearAxes2.setPosition("right");

        cScales.addYAxesData(linearAxes);
        cScales.addYAxesData(linearAxes2);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Symptom vs Pollen");
        options.setTitle(title);

        lineModel.setOptions(options);
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }
}
