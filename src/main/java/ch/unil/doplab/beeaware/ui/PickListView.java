package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.Domain.Pollen;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@ApplicationScoped
public class PickListView {

    List<String> pollensSource = new ArrayList<>();
    List<String> pollensTarget = new ArrayList<>();
    private DualListModel<String> pollens = new DualListModel<>(pollensSource, pollensTarget);

    public void logState() {
        System.out.println("Source: " + pollens.getSource());
        System.out.println("Target: " + pollens.getTarget());
    }

    @Override
    public String toString() {
        return "PickListView{" +
                "pollens.pollensSource=" + pollens.getSource() +
                "pollens.pollensTarget=" + pollens.getTarget() +
                ", pollensSource=" + pollensSource +
                ", pollensTarget=" + pollensTarget +
                '}';
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing pickedList");
//        List<String> pollensSource = new ArrayList<>();
//        List<String> pollensTarget = new ArrayList<>();
//        if(beezzerData != null && beezzerData.getAllergensListString() != null && beezzerData.getAllergensListString().size() > 0){
//            System.out.println("With data");
//            setPickListView(beezzerData.getAllergensListString());
//        } else {
//            System.out.println("Without data");
            for (Pollen pollen : Pollen.getPredefinedPollens()) {
                pollensSource.add(pollen.getPollenNameEN());
            }

            pollens.setSource(pollensSource);
            pollens.setTarget(pollensTarget);
            System.out.println("End initializing");
//        }
    }


    public void setPickListView(List<String> pollensString){
        try {
            pollensTarget.clear();
            pollensTarget.addAll(pollensString);
            pollensSource.clear();
            for (Pollen pollen : Pollen.getPredefinedPollens()) {
                pollensSource.add(pollen.getPollenNameEN());
            }
            pollensSource.removeAll(pollensTarget);
            pollens.setSource(pollensSource);
            pollens.setTarget(pollensTarget);
            System.out.println(this);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}