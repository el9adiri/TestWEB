package com.location.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chaine) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = (HttpSession) req.getSession(false);

		boolean UserLogged = (session != null && session.getAttribute("logged") != null);
		String role = null;
		if (session != null)
			role = (String) session.getAttribute("role");

		String requestedURL = req.getRequestURI();

		if ((requestedURL.contains("/signup.xhtml") || requestedURL
				.contains("/login.xhtml")) && UserLogged) {			
			res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
		}

		if (UserLogged) {
			if (requestedURL.contains("/user/") && role.equals("ADMIN"))
				res.sendRedirect(req.getContextPath()
						+ "/faces/admin/admin_index.xhtml");
			if (requestedURL.contains("/admin/") && role.equals("CLIENT"))
				res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
		}

		if ((requestedURL.contains("/user/") || requestedURL
				.contains("/admin/")) && !UserLogged) {
			res.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
		} else {
			chaine.doFilter(req, res);
		}

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}