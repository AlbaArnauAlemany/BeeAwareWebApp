//package ch.unil.doplab.beeaware.ui;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.RequestScoped;
//import jakarta.faces.application.FacesMessage;
//import jakarta.faces.context.FacesContext;
//import jakarta.inject.Named;
//import org.primefaces.component.linechart.LineChart;
//import org.primefaces.component.piechart.PieChart;
//import org.primefaces.event.ItemSelectEvent;
//
//import java.awt.*;
//import java.io.Serializable;
//import java.math.BigDecimal;
//
//@Named
//@RequestScoped
//public class ChartView implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private String cartesianLinerModel;
//    private String lineModel;
//
//
//    @PostConstruct
//    public void init() {
//        createCartesianLinerModel();
//        createLineModel();
//
//    }
//
//
//    public void createLineModel() {
//        lineModel = new LineChart()
//                .setId(new LineData()
//                .addDataset(new LineDataset()
//                        .setId(65, 59, 80, 81, 56, 55, 40)
//                        .setLabel("My First Dataset")
//                        .setBorderColor(new Color(75, 192, 192))
//                        .setLineTension(0.1f)
//                        .setFill(new Fill<Boolean>(false)))
//                .setLabels("January", "February", "March", "April", "May", "June", "July"))
//                .setOptions(new LineOptions()
//                        .setResponsive(true)
//                        .setMaintainAspectRatio(false)
//                        .setPlugins(new Plugins()
//                                .setTitle(new Title()
//                                        .setDisplay(true)
//                                        .setText("Line Chart Subtitle")))
//                ).toJson();
//    }
//
//    public void createCartesianLinerModel() {
//        cartesianLinerModel = new LineChart()
//                .setId(new LineData()
//                .addDataset(new LineDataset()
//                        .setId(20, 50, 100, 75, 25, 0)
//                        .setLabel("Left Dataset")
//                        .setLineTension(0.5f)
//                        .setYAxisID("left-y-axis")
//                        .setFill(new Fill<Boolean>(true)))
//                .addDataset(new LineDataset()
//                        .setId(0.1, 0.5, 1.0, 2.0, 1.5, 0)
//                        .setLabel("Right Dataset")
//                        .setLineTension(0.5f)
//                        .setYAxisID("right-y-axis")
//                        .setFill(new Fill<Boolean>(true)))
//                .setLabels("Jan", "Feb", "Mar", "Apr", "May", "Jun"))
//                .setOptions(new LineOptions()
//                        .setResponsive(true)
//                        .setMaintainAspectRatio(false)
//                        .setScales(new Scales()
//                                .addScale("left-y-axis", new LinearScale().setPosition(ScalesPosition.LEFT))
//                                .addScale("right-y-axis", new LinearScale().setPosition(ScalesPosition.RIGHT)))
//                        .setPlugins(new Plugins()
//                                .setTitle(new Title()
//                                        .setDisplay(true)
//                                        .setText("Cartesian Linear Chart")))
//                ).toJson();
//    }
//
//
//    public void itemSelect(ItemSelectEvent event) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
//                "Value: " + event.getData()
//                + ", Item Index: " + event.getItemIndex()
//                + ", DataSet Index:" + event.getDataSetIndex());
//
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
//
//    public String getLineModel() {
//        return lineModel;
//    }
//
//    public void setLineModel(String lineModel) {
//        this.lineModel = lineModel;
//    }
//
//    public String getCartesianLinerModel() {
//        return cartesianLinerModel;
//    }
//
//    public void setCartesianLinerModel(String cartesianLinerModel) {
//        this.cartesianLinerModel = cartesianLinerModel;
//    }
