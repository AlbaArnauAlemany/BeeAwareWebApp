package ch.unil.doplab.beeaware.ui;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"*.xhtml"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession(true); // Utilisation de HttpServletRequest pour la session

        String contextPath = req.getContextPath();
        String loginURI = contextPath + "/Login.xhtml";
        String registerURI = contextPath + "/Register.xhtml";
        String indexURI = contextPath + "/index.xhtml";
        String summaryURI = contextPath + "/Summary.xhtml";

        boolean loginRequest = requestURI.equals(loginURI);
        boolean registerRequest = requestURI.equals(registerURI);
        boolean indexRequest = requestURI.equals(indexURI);
        boolean summaryRequest = requestURI.equals(summaryURI);
        boolean resourceRequest = requestURI.contains("javax.faces.resource");

        boolean hasValidToken = session.getAttribute("bearerToken") != null;

        if (hasValidToken || loginRequest || registerRequest || summaryRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }

}
