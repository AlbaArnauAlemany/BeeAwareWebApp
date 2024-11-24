package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.*;
import ch.unil.doplab.beeaware.Domain.DTO.AllergenDTO;
import ch.unil.doplab.beeaware.Domain.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.DTO.PollenDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.unil.doplab.beeaware.Domain.Pollen.getPollenByName;

@ApplicationScoped
@Named
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeezzerData extends Beezzer{

    private Long id;
    private String email;
    private String username;
    private String password;
    private int npa;
    private String country;
    private String cityName;
    private Token token;
    private List<String> allergensListString;

    private String dialogMessage;

    private String newPassword;

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    CoordinateBean coordinateBean;

    @Inject
    PickListView pickListView;

    private List<BeezzerDTO> beezzerDTOs;

    private List<String> pollen;
    private boolean changed;


    public void loadBeezzers() {
        beezzerDTOs = theService.getBeezzerService().getAllBeezzers();
    }

    public boolean isButtonDisabled() {
        return !changed;
    }

    public void setNewBeezzerData(BeezzerDTO beezzerDTO){
        System.out.println(beezzerDTO);
        this.id = beezzerDTO.getId();
        this.username = beezzerDTO.getUsername();
        this.email = beezzerDTO.getEmail();
        this.npa = beezzerDTO.getLocation().getNPA();
        this.country = beezzerDTO.getLocation().getCountry();
        this.allergensListString = new ArrayList<>();
        for (PollenDTO pollenAllergen : beezzerDTO.getAllergens().getPollenList()){
            this.allergensListString.add(pollenAllergen.getPollenNameEN());
        }
        pickListView.setPickListView(this.allergensListString);

        System.out.println("Beezzer Setted");
    }
    public void updateBeezzer() {
        try {
            if (this.id != null) {
                theService.getBeezzerService().setBeezzer(this);
                setNewBeezzerData(theService.getBeezzerService().getBeezzer(this.getId()));
                changed = false;
            }
        } catch (Exception e) {
            dialogMessage = e.getMessage();
            PrimeFaces.current().executeScript("PF('updateErrorDialog').show();");
        }
    }

    public void checkIfChanged() {
        boolean emailChanged = !email.equals(this.getEmail());
        boolean usernameChanged = !username.equals(this.getUsername());
        boolean passwordChanged = !password.equals(this.getPassword());
        changed = emailChanged || usernameChanged || passwordChanged;
    }

    public String saveBeezzer() {
        try {
            Map<Long, Pollen> pollens = new HashMap<>();
            for (String allergen: pollen) {
                Pollen pol = getPollenByName(allergen);
                pollens.put(pol.getId(), pol);
            }

            Beezzer beezzer = new Beezzer(0L, username, email, password, new Location(npa, country), Role.BEEZZER);
            beezzer.setAllergens(pollens);
            BeezzerDTO createdBeezzer = theService.getBeezzerService().addBeezzer(beezzer);
            if (createdBeezzer != null) {
                // Succès : ajouter un message de confirmation et rediriger vers Login
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful", null));
                return "Login.xhtml?faces-redirect=true";
            } else {
                // Échec : ajouter un message d'erreur
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration Failed", null));
                return "Register.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred: " + e.getMessage(), null));
            return "Register.xhtml?faces-redirect=true";
        }
    }

    public void reset() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.pollen = null;
    }

    public void resetPasswordChange() {
        this.newPassword = null;
    }

    public void savePasswordChange() throws Exception {
        //TODO: Changer la fonction, c'est celle de studdybuddy actuellemnt...
//        if (Utils.checkPassword(currentPassword, theStudent.getPassword())) {
//            this.setPassword(Utils.hashPassword(newPassword));
//            updateStudent();
//            dialogMessage = "Your password was successfully changed.";
//            PrimeFaces.current().executeScript("PF('passwordChangeDialog').show();");
//            resetPasswordChange();
//        } else {
//            dialogMessage = "Your password could not be changed because the current password you entered is incorrect.";
//            PrimeFaces.current().executeScript("PF('passwordChangeDialog').show();");
//        }
    }

    public String goToSummary() {
        pollen = pickListView.getPollens().getTarget();
        npa = coordinateBean.getNpa();
        country = coordinateBean.getCountry();
        cityName = coordinateBean.getCityName();
        System.out.println("PERFECT!!");
        return "Summary.xhtml?faces-redirect=true";
    }
}
