package ch.unil.doplab.beeaware;

import ch.unil.doplab.beeaware.service.*;
import ch.unil.doplab.beeaware.service.authentification.AuthentificationService;
import ch.unil.doplab.beeaware.service.authentification.BearerTokenFilter;
import ch.unil.doplab.beeaware.service.authentification.SessionUtils;
import ch.unil.doplab.beeaware.ui.PickListView;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ResourceBundle;

@ApplicationScoped
@NoArgsConstructor
@Getter
@Setter
public class ApplicationServiceManagement {
    private final String BASE_URL = "http://localhost:8080/BeeAwareService-1.0-SNAPSHOT/api";
    private String APIKEY = ResourceBundle.getBundle("application").getString("API_KEY");

    private AuthentificationService authentificationService;
    private BeezzerService beezzerService;
    private ServiceResource serviceResource;
    private CoordinateService coordinateService;
    private IndexPollenForBeezzerService indexPollenForBeezzerService;
    private SymptomService symptomService;
    private ForecastService forecastService;
    private ExcelService excelService;
    private Client client;

    private PickListView pickListView;
    @Inject
    private Utilis utilis;

    @PostConstruct
    public void init() {
        System.out.println("BeezzerService" + this.hashCode());
        client = ClientBuilder.newClient();
        client.register(new BearerTokenFilter(SessionUtils.getSession(false)));
        authentificationService = new AuthentificationService(client.target(BASE_URL).path("authentication"));
        beezzerService = new BeezzerService(client.target(BASE_URL).path("beezzers"));
        serviceResource = new ServiceResource(client.target(BASE_URL).path("service"));
        coordinateService = new CoordinateService(client.target(BASE_URL).path("geo"));
        indexPollenForBeezzerService = new IndexPollenForBeezzerService(client.target(BASE_URL).path("forecast"));
        symptomService = new SymptomService(client.target(BASE_URL).path("symptom"));
        forecastService = new ForecastService(client.target(BASE_URL).path("forecast"));
        excelService = new ExcelService(client.target(BASE_URL).path("excel"));
        System.out.println("Utilis API Key length: " + utilis.getApikey().length());
    }

    public void authenticateSession(HttpSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        client.register(new BearerTokenFilter());
        System.out.println("BearerTokenFilter registered with session ID: " + session.getId());
    }
}
