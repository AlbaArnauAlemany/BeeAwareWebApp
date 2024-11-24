package ch.unil.doplab.beeaware;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Getter;
import java.util.ResourceBundle;

@Getter
@ApplicationScoped
@Named
public class Utilis {
    private String apikey;

    @PostConstruct
    public void init() {
        apikey = ResourceBundle.getBundle("application").getString("API_KEY");
        System.out.println("apikey : " + apikey);
    }
}
