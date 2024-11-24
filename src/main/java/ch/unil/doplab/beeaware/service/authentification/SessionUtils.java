package ch.unil.doplab.beeaware.service.authentification;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static HttpSession getSession(boolean create) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            return (HttpSession) facesContext.getExternalContext().getSession(create);
        }
        throw new IllegalStateException("FacesContext is not available.");
    }
}
