package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.Token;
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
    private HttpSession session;

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    BeezzerData beezzerData;


    @PostConstruct
    public void init() {
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
            Token token = theService.getAuthentificationService().authenticate(username, password); // Récupération du token
            var session = getSession(true);
            if (token != null) {
                assert session != null;
                session.setAttribute("bearerToken", token.getKey()); // Stockage du token
                session.setAttribute("key", token.getKey()); // Stockage du token
                session.setAttribute("beezzerId", token.getBeezzerId()); // Stockage du token
                session.setAttribute("role", token.getRole()); // Stockage du token
                session.setAttribute("expiration", token.getExpiration()); // Stockage du token
                beezzerData.setId(token.getBeezzerId());
                beezzerData.setToken(token);
                PrimeFaces.current().executeScript("console.log('Token received: " + token + "');");
                return "GetAllBeezzers";
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
        invalidateSession();
        reset();
        return "Login?faces-redirect=true";
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
