package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.Token;
import ch.unil.doplab.beeaware.service.authentification.SessionUtils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

import java.io.Serializable;

@SessionScoped
@Named
public class LoginBean implements Serializable {

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    BeezzerData beezzerData;


    @PostConstruct
    public void init() {
        HttpSession session = SessionUtils.getSession(true); // Utilisation de la session centralisée
        if (theService != null) {
            theService.authenticateSession(session);
        } else {
            throw new IllegalStateException("ApplicationServiceManagement is not injected!");
        }
        reset();
    }


    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public void reset() {
        username = null;
        password = null;
    }

    public String login() {
        try {
            Token token = theService.getAuthentificationService().authenticate(username, password);
            if (token != null) {
                HttpSession session = SessionUtils.getSession(false); // Utilisation de la session globale
                session.setAttribute("bearerToken", token.getKey());
                session.setAttribute("key", token.getKey());
                session.setAttribute("beezzerId", token.getBeezzerId());
                session.setAttribute("role", token.getRole());
                session.setAttribute("expiration", token.getExpiration());
                theService.authenticateSession(session);
                beezzerData.setId(token.getBeezzerId());
                beezzerData.setToken(token);
                BeezzerDTO beezzerDTO = theService.getBeezzerService().getBeezzer(token.getBeezzerId());
                beezzerData.setNewBeezzerData(beezzerDTO);
                return "HomePage.xhtml?faces-redirect=true";
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Invalid login", null));
            reset();
            return "Login";
        } catch (RuntimeException e) {
            if (e.getMessage().contains("403")) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Bad login", "Invalid username or password."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Login error", e.getMessage()));
            }
            reset();
            return "Login";
        }
    }


    public String logout() {
        System.out.println("Logout current beezzer : " + beezzerData.getId());
        if(theService.getAuthentificationService().logout(beezzerData.getId())) {
            reset();
            invalidateSession();
            return "Login?faces-redirect=true";
        }
            return "";
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static HttpSession getSession(boolean create) {
        var facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }
        var externalContext = facesContext.getExternalContext();
        if (externalContext == null) {
            return null;
        }
        return (HttpSession) externalContext.getSession(create);
    }

    public static void invalidateSession() {
        var session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
