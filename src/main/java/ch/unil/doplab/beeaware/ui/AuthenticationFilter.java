package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.Domain.Role;
import ch.unil.doplab.beeaware.Domain.Token;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"*.xhtml"})
public class AuthenticationFilter implements Filter {

    //TODO : Delete before send to prod
    @Inject
    BeezzerData beezzerData;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession(false);

        String contextPath = req.getContextPath();
        String loginURI = contextPath + "/Login.xhtml";
        String registerURI = contextPath + "/Register.xhtml";
        String indexURI = contextPath + "/index.xhtml";

        boolean loginRequest = requestURI.equals(loginURI);
        boolean registerRequest = requestURI.equals(registerURI);
        boolean indexRequest = requestURI.equals(indexURI);
        boolean resourceRequest = requestURI.contains("javax.faces.resource");

        boolean hasValidToken = session != null && session.getAttribute("bearerToken") != null;

        // TODO : A commenter pour la prod car on donne un token admin ou si on veut utiliser un autre compte
        if (session != null && session.getAttribute("bearerToken") == null) {
            session.setAttribute("bearerToken", "u7u6m0f9rhvvtml0ibssscagoe");
            session.setAttribute("key", "u7u6m0f9rhvvtml0ibssscagoe"); // Stockage du token
            session.setAttribute("beezzerId", 1); // Stockage du token
            session.setAttribute("role", Role.ADMIN); // Stockage du token
            session.setAttribute("expiration", new Date()); // TODO : +2 ne pas oublier
        }

        if (hasValidToken || loginRequest || registerRequest || indexRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }
}
