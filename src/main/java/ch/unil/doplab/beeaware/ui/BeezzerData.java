package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.DTO.BeezzerDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApplicationScoped
@Named
@Getter
@Setter
public class BeezzerData {
    private static final long serialVersionUID = 1L;

    @Inject
    ApplicationServiceManagement theService;
    private List<BeezzerDTO> beezzerDTOs;

    public void loadBeezzers() {
        beezzerDTOs = theService.getBeezzerService().getAllBeezzers();
    }
}
