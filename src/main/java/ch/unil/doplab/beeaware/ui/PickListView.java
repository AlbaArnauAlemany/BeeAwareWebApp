package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.DTO.PollenDTO;
import ch.unil.doplab.beeaware.Domain.Pollen;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.List;
@Named
@RequestScoped
public class PickListView {

    private DualListModel<String> pollens;

    @PostConstruct
    public void init() {
        List<String> pollensSource = new ArrayList<>();
        List<String> pollensTarget = new ArrayList<>();

        for (Pollen pollen:Pollen.getPredefinedPollens()) {
            pollensSource.add(pollen.getPollenNameEN());
        }

        pollens = new DualListModel<>(pollensSource, pollensTarget);

    }

    // TODO: Dans les différents fichier .xhtml, faire des styles plutot que de la redondance...

    public DualListModel<String> getPollens() {
        return pollens;
    }

    public void setPollens(DualListModel<String> pollens) {
        this.pollens = pollens;
    }
}