package com.location.controllers;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.Client;

import com.location.ejb.ClientService;
import com.location.utils.Util;

@SessionScoped
@ManagedBean
public class ClientController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private ClientService service;
	private Client client = new Client();

	public ClientController() {
		// TODO Auto-generated constructor stub
	}

	public static void sayHi() {
		System.out.println("hiiiiiiiiiii");
	}

	public static void sayHello() {
		System.out.println("hiiiiiiiiiii");
		System.out.println("hello");
	}
	
	public String login() {
		Client c = service.findByLoginPass(client.getEmail(),
				client.getPassword());
		HttpSession session = Util.getSession();
		if (c != null) {
			session.setAttribute("logged", client.getCin());
			session.setAttribute("role", client.getRole());
			switch (client.getRole()) {
			case "admin":
				return "admin/admin_index";
			default:
				return "index";
			}
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"email ou mot de pass incorrect", "verifier vos données");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "login";
	}

	public String inscrire() {
		if (service.findByCIN(client.getCin()) == null) {
			client.setRole("client");
			service.save(client);
			return "login";
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"compte exist déjà", "verifier vos données");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "signup";
	}

	public String logout() throws IOException {
		Util.clearSession();
		return "";
	}

	public boolean isLogged() {
		try {
			return Util.getSession().getAttribute("logged") == null ? false : true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;		
	}

}
