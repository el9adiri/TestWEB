package com.location.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.Client;
import model.Kid;
import model.User;

import com.location.ejb.ClientService;
import com.location.ejb.KidService;
import com.location.ejb.TestService;
import com.location.ejb.UsersService;
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

	@EJB
	private UsersService tservice;
	
	public ClientController() {
		// TODO Auto-generated constructor stub 
	}
	
	public String test(){
		try {
			Kid k = new Kid(1, "me", 24);
			tservice.create(k);
			tservice.create(new Kid(2, "me", 24));
			k.setId(2);
			k.setNom("new name");
			tservice.update(k); 
			tservice.update(new User(10, "newUser"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					e.getMessage(), "verifier vos données");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		tservice.findAll().forEach(new Consumer<User>() {
			@Override
			public void accept(User t) {
				// TODO Auto-generated method stub
				System.out.println("mykids : "+t);
			}
		});
		return "";
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
