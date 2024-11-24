package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.Domain.Pollen;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.List;
@Named
@RequestScoped
@Setter
@Getter
public class PickListView {

    private DualListModel<String> pollens;
    List<String> pollensSource;
    List<String> pollensTarget;
    @Inject
    BeezzerData beezzerData;

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
        List<String> pollensSource = new ArrayList<>();
        List<String> pollensTarget = new ArrayList<>();
        if(beezzerData != null && beezzerData.getAllergensListString() != null && beezzerData.getAllergensListString().size() > 0){
            setPickListView(beezzerData.getAllergensListString());
        } else {
            for (Pollen pollen : Pollen.getPredefinedPollens()) {
                pollensSource.add(pollen.getPollenNameEN());
            }

            pollens = new DualListModel<>(pollensSource, pollensTarget);
        }
    }


    public void setPickListView(List<String> pollensString){
        pollensTarget = pollensString;
        pollensSource = new ArrayList<>();
        for (Pollen pollen:Pollen.getPredefinedPollens()) {
            pollensSource.add(pollen.getPollenNameEN());
        }
        pollensSource.removeAll(pollensTarget);
        pollens = new DualListModel<>(pollensSource, pollensTarget);
        System.out.println(this);
    }
}