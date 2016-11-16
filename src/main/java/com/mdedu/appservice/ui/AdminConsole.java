package com.mdedu.appservice.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@SpringUI(path="/")
@Theme("valo")
@UIScope
public class AdminConsole extends UI{
	
	private Navigator navigator;
	
	 @Autowired
	private  SpringViewProvider viewProvider;

	 public AdminConsole(){
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3027108499291279970L;

	@Override
	protected void init(VaadinRequest request) {
		this.navigator =new Navigator(this,this);
		navigator.addProvider(viewProvider);
	
//		this.setContent(login);
	}

}
