package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.Coordinate;
import ch.unil.doplab.beeaware.Domain.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@ApplicationScoped
@Named
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeezzerData {
    private String email;
    private String username;
    private String password;
    private int npa;
    private String country;
    private String cityName;
    private Location location;

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    CoordinateBean coordinateBean;

    @Inject
    private PickListView pickListView;
    private List<BeezzerDTO> beezzerDTOs;

    private List<String> pollen;

    public void loadBeezzers() {
        beezzerDTOs = theService.getBeezzerService().getAllBeezzers();
    }

    public void reset() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.pollen = null;
    }

    public String goToSummary() {
        pollen = pickListView.getPollens().getTarget();
        npa = coordinateBean.getNpa();
        country = coordinateBean.getCountry();
        location = coordinateBean.getCoordinate(coordinateBean.getNpa(), coordinateBean.getCountry());
        cityName = coordinateBean.getCityName();
        return "Summary.xhtml?faces-redirect=true";
    }
}
