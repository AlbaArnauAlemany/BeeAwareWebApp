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
    private String country = "CH";
    private String cityName; // Résultat retourné par l'API
    private Coordinate locality; // Résultat retourné par l'API

    @PostConstruct
    public void init() {
        locality = new Coordinate(46.5197, 6.6328); // Lausanne, Suisse
    }

    public Location getCoordinate(int npa, String country) {
        System.out.println(npa);
        System.out.println(country);
        locality = theService.getCoordinateService().getCoordinates(npa, country);
        return new Location(npa, country, locality);
    }
}
