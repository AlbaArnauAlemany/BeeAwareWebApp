package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.*;
import ch.unil.doplab.beeaware.Domain.DTO.AllergenDTO;
import ch.unil.doplab.beeaware.Domain.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.DTO.LocationDTO;
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
    private String newEmail;
    private String username;
    private String newUsername;
    private String newPassword;
    private String password;
    private int npa;
    private String country;
    private int newNpa;
    private String newCountry;
    private Location location;
    private Location newLocation;
    private String cityName;
    private Token token;
    private List<String> allergensListString;

    private String dialogMessage;

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    CoordinateBean coordinateBean;

    @Inject
    PickListView pickListView;

    private List<BeezzerDTO> beezzerDTOs;

    private List<String> pollen;
    private boolean changed;
    private boolean changedPassword;
    private boolean changedLocationValid;


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
        this.newUsername = beezzerDTO.getUsername();
        this.email = beezzerDTO.getEmail();
        this.newEmail = beezzerDTO.getEmail();
        this.location = new Location(beezzerDTO.getLocation().getNPA(), beezzerDTO.getLocation().getCountry(), beezzerDTO.getLocation().getCoordinate());
        this.newLocation = new Location(beezzerDTO.getLocation().getNPA(), beezzerDTO.getLocation().getCountry(), beezzerDTO.getLocation().getCoordinate());
        this.npa = beezzerDTO.getLocation().getNPA();
        this.country = beezzerDTO.getLocation().getCountry();
        this.newNpa = beezzerDTO.getLocation().getNPA();
        this.newCountry = beezzerDTO.getLocation().getCountry();
        this.allergensListString = new ArrayList<>();
        for (PollenDTO pollenAllergen : beezzerDTO.getAllergens().getPollenList()){
            this.allergensListString.add(pollenAllergen.getPollenNameEN());
        }
        pickListView.setPickListView(this.allergensListString);

        System.out.println("Beezzer Setted" + this);
    }
    //TODO: Ne pas oublier de gérer le retours de nos calls API
    public void updateBeezzer() {
        try {
            if (this.id != null) {
                Beezzer beezzerElement = new Beezzer(newUsername, newEmail);
                System.out.println(theService.getBeezzerService().setBeezzer(this.id, beezzerElement));
                setNewBeezzerData(theService.getBeezzerService().getBeezzer(this.id));
                changed = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile updated successfully."));
            }
        } catch (Exception e) {
            dialogMessage = e.getMessage();
            PrimeFaces.current().executeScript("PF('updateErrorDialog').show();");
            e.printStackTrace();

        }
    }
    public void resetBeezzer() {
        this.newNpa = this.npa;
        this.newCountry = this.country;
    }

    public void saveLocation() {
        System.out.println(newLocation);
        if(theService.getBeezzerService().setBeezzerLocation(id, new LocationDTO(newLocation))) {
            this.changedLocationValid = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Location updated successfully."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to set the new Location."));
        }
    }

    public void saveAllergen() {
        System.out.println(pickListView);
        this.allergensListString = pickListView.getPollens().getTarget();
        List<Pollen> pollens = new ArrayList<>();
        for (String pollenAllergenElement : this.allergensListString){
            pollens.add(getPollenByName(pollenAllergenElement));
        }

        if(theService.getBeezzerService().setAllergenSet(id, pollens)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Allergen updated successfully."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Allergen updated successfully."));
        }
    }

    public void checkLocation() {
        System.out.println("newLocation : " + newLocation);
        Location newElementLocation = coordinateBean.getCoordinate(newLocation.getNPA(), newLocation.getCountry());
        if (newElementLocation != null) {
            newLocation = newElementLocation;
            changedLocationValid = true;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Location verified successfully."));
        } else {
            changedLocationValid = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to fetch location. Check NPA and Country."));
        }
        System.out.println("newLocation : " + newLocation);
    }
    public void resetLocation(){
        this.newLocation = this.location;
        this.newNpa = this.npa;
        this.newCountry = this.country;
        this.changedLocationValid = false;
    }


    public void resetAllergenList(){
        pickListView.setPickListView(this.allergensListString);
    }


    public void checkIfChanged() {
        boolean emailChanged = !email.equals(newEmail);
        boolean usernameChanged = !username.equals(newUsername);
        changed = emailChanged || usernameChanged;
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
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful", null));
                return "Login.xhtml?faces-redirect=true";
            } else {
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

    public void resetPasswordChange() {
        this.newPassword = null;
        this.changedPassword = false;
    }

    public void savePasswordChange(){
        if (PasswordUtilis.checkPasswordConstraints(newPassword)) {
            if(theService.getBeezzerService().changePassword(this.id, newPassword)) {
                this.setPassword(newPassword);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password updated successfully."));
                resetPasswordChange();
            }
        } else {
            dialogMessage = "Your password could not be changed because the current password you entered is incorrect.";
            PrimeFaces.current().executeScript("PF('passwordChangeDialog').show();");
        }
    }

    public String goToSummary() {
        pollen = pickListView.getPollens().getTarget();
        System.out.println("npa : " + npa);
        System.out.println("country : " + country);
        if(theService.getBeezzerService().checkBeezzerUsername(username)){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username already exists"));
            return null;
        }
        Location locationTemp = coordinateBean.getCoordinate(npa, country);
        if(locationTemp != null){
            location = coordinateBean.getCoordinate(npa, country);
            return "Summary.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Location information is incomplete or false. Please verify the NPA and country."));
                return null;
        }
    }

}
