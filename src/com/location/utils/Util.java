package com.location.utils;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Util {

	public Util() {
		// TODO Auto-generated constructor stub
	}

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	public static void clearSession() throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.invalidateSession();
		context.redirect(context.getRequestContextPath() + "/faces/index.xhtml");
	}

}
