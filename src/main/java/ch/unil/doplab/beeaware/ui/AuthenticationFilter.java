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

        if (hasValidToken || loginRequest || registerRequest || indexRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }
}
