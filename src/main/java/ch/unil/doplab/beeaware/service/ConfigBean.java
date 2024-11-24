package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Utilis;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("configBean")
@ApplicationScoped
public class ConfigBean {

    public String getGoogleMapsApiKey() {
        return Utilis.getProperty("google.maps.api.key");
    }
}
