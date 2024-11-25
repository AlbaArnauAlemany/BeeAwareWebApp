package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.Coordinate;
import ch.unil.doplab.beeaware.Domain.Location;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

@ApplicationScoped
@Named
@Getter
@Setter
public class CoordinateBean {
    @Inject
    ApplicationServiceManagement theService;

    private Integer npa;
    private String country;
    private String cityName;
    private Coordinate coordinate;

    @PostConstruct
    public void init() {
        coordinate = new Coordinate(46.5197, 6.6328); // Lausanne, Suisse
    }

    public Location getCoordinate(int npa, String country) {
        try {
            Coordinate tempLocation = theService.getCoordinateService().getCoordinates(npa, country);
            if (tempLocation != null) {
                coordinate = tempLocation;
                return new Location(npa, country, tempLocation);
            } else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }
}
