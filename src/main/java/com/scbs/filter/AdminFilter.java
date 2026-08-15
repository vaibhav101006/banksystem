package com.scbs.filter;

import com.scbs.model.Student;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Student user = (session != null) ? (Student) session.getAttribute("currentUser") : null;

        if (user == null || !user.isAdmin()) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard?error=Access+denied.+Admin+only.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
