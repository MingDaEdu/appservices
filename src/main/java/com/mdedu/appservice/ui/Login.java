package com.mdedu.appservice.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.mdedu.appservice.domainobject.User;
import com.mdedu.appservice.repository.UserRepository;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name=Login.NAME)
public class Login extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String NAME="";
	private Navigator navigator;

	private TextField username = new TextField("用户名：");

	private PasswordField password = new PasswordField("密码：");

	/* Action buttons */
	private Button loginBtn = new Button("登录", FontAwesome.SIGN_IN);

	@Autowired
	private UserRepository userRepository;
	
	public Login() {
		FormLayout  layout = new FormLayout ();
		layout.setWidthUndefined();
		layout.addComponents(username, password, loginBtn);

		username.setRequired(true);
		password.setRequired(true);
		addComponents(layout);
		layout.setSpacing(true);
		layout.setComponentAlignment(loginBtn, Alignment.BOTTOM_CENTER);
		this.setSizeFull();
		this.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

		loginBtn.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -9084809776566261915L;

			@Override
			public void buttonClick(ClickEvent event) {
				User user=userRepository.findByUsernameAndPassword(username.getValue(), password.getValue());
				if(user!=null){
					VaadinSession.getCurrent().setAttribute("user",user);
					navigator.navigateTo("main");
				}
				else
				{
					Notification.show("Invalid Username or password");
					username.focus();
				}
			}
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {

		navigator = event.getNavigator();
	}
}
