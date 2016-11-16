package com.mdedu.appservice.ui;

import com.mdedu.appservice.domainobject.User;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class Header extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5203133049465017509L;
	private Label label= new Label();
	private Button logoutBtn = new Button("退出", FontAwesome.SIGN_OUT);
	public Header( Navigator navigator){
		User user=(User)VaadinSession.getCurrent().getAttribute("user");
		label.setCaption("Welcome "+user.getUsername());
		this.addComponent(label);
		this.addComponent(logoutBtn);
		this.setComponentAlignment(label,Alignment.BOTTOM_RIGHT);

		this.setComponentAlignment(logoutBtn,Alignment.BOTTOM_RIGHT);
		this.setSpacing(true);
		this.setHeight(80, Unit.PIXELS);
		this.setWidth("100%");
		logoutBtn.addClickListener(new ClickListener(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -4667566312850524000L;

			@Override
			public void buttonClick(ClickEvent event) {
				VaadinSession.getCurrent().getSession().invalidate();
				navigator.navigateTo("");
			}
			
		});
	}

}
